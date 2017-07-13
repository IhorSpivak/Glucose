package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ixbiopharma.glucose.utils.AndroidUtils;

import java.util.ArrayList;


/**
 * Created by Dacer on 11/4/13.
 * Edited by Lee youngchan 21/1/14
 * Edited by dector 30-Jun-2014
 */
public class LineViewTest extends View {
    private int mViewHeight;
    private int dataOfAGird = 10;
    private int bottomTextHeight = 0;
    private ArrayList<String> bottomTextList = new ArrayList<>();

    private ArrayList<ArrayList<Float>> dataLists;

    private ArrayList<Integer> xCoordinateList = new ArrayList<>();
    private ArrayList<Integer> yCoordinateList = new ArrayList<>();

    private ArrayList<ArrayList<Dot>> drawDotLists = new ArrayList<>();

    private Paint bottomTextPaint = new Paint();
    private int bottomTextDescent;

    //popup
    private Paint popupTextPaint = new Paint();
    public boolean showPopup = true;

    private Dot pointToSelect;
    private int popupBottomPadding = AndroidUtils.dpToPx(getContext(), 2);

    private int topLineLength = AndroidUtils.dpToPx(getContext(), 12);
    // | | ←this
    //-+-+-
    private int sideLineLength = AndroidUtils.dpToPx(getContext(), 45) / 3 * 2;// --+--+--+--+--+--+--
    //  ↑this
    private int backgroundGridWidth = AndroidUtils.dpToPx(getContext(), 45);

    //Constants
    private final int popupTopPadding = AndroidUtils.dpToPx(getContext(), 2);
    private final int popupBottomMargin = AndroidUtils.dpToPx(getContext(), 5);
    private final int bottomTextTopMargin = AndroidUtils.sp2px(getContext(), 5);
    private final int bottomLineLength = AndroidUtils.sp2px(getContext(), 22);
    private final int DOT_INNER_CIR_RADIUS = AndroidUtils.dpToPx(getContext(), 2);
    private final int DOT_OUTER_CIR_RADIUS = AndroidUtils.dpToPx(getContext(), 5);
    private final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");

    private Boolean drawDotLine = false;
    private int[] colorArray = {Color.parseColor("#e74c3c"), Color.parseColor("#2980b9"), Color.parseColor("#1abc9c")};

    public void setDrawDotLine(Boolean drawDotLine) {
        this.drawDotLine = drawDotLine;
    }

    public LineViewTest(Context context) {
        this(context, null);
    }

    public LineViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("LineViewTest", "LineViewTest");

        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(AndroidUtils.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);

        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(AndroidUtils.sp2px(getContext(), 12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
        int BOTTOM_TEXT_COLOR = Color.parseColor("#9B9A9B");
        bottomTextPaint.setColor(BOTTOM_TEXT_COLOR);
        refreshTopLineLength();
    }

    public void setColorArray(int[] colors) {
        this.colorArray = colors;
    }

    /**
     * dataList will be reset when called is method.
     *
     * @param bottomTextList The String ArrayList in the bottom.
     */
    public void setBottomTextList(ArrayList<String> bottomTextList) {
        this.bottomTextList = bottomTextList;

        Rect r = new Rect();
        int longestWidth = 0;
        String longestStr = "";
        bottomTextDescent = 0;
        for (String s : bottomTextList) {
            bottomTextPaint.getTextBounds(s, 0, s.length(), r);
            if (bottomTextHeight < r.height()) {
                bottomTextHeight = r.height();
            }
            if (longestWidth < r.width()) {
                longestWidth = r.width();
                longestStr = s;
            }
            if (bottomTextDescent < (Math.abs(r.bottom))) {
                bottomTextDescent = Math.abs(r.bottom);
            }
        }

        if (backgroundGridWidth < longestWidth) {
            backgroundGridWidth = longestWidth + (int) bottomTextPaint.measureText(longestStr, 0, 1);
        }
        if (sideLineLength < longestWidth / 2) {
            sideLineLength = longestWidth / 2;
        }

        refreshXCoordinateList(getHorizontalGridNum());
    }

    public void setFloatDataList(ArrayList<ArrayList<Float>> dataLists) {
        this.dataLists = dataLists;
        for (ArrayList<Float> list : dataLists) {
            if (list.size() > bottomTextList.size()) {
                throw new RuntimeException("dacer.LineView error:" +
                        " dataList.size() > bottomTextList.size() !!!");
            }
        }
        float biggestData = 0;
        for (ArrayList<Float> list : dataLists) {
            for (Float i : list) {
                if (biggestData < i) {
                    biggestData = i;
                }
            }
            dataOfAGird = 1;
            while (biggestData / 10 > dataOfAGird) {
                dataOfAGird *= 10;
            }
        }

        refreshAfterDataChanged();
        showPopup = true;
        setMinimumWidth(0); // It can help the LineView reset the Width,
        // I don't know the better way..
        postInvalidate();
    }

    private void refreshAfterDataChanged() {
        int verticalGridNum = getVerticalGridlNum();
        refreshYCoordinateList(verticalGridNum);
        refreshDrawDotList(verticalGridNum);
    }

    private int getVerticalGridlNum() {
        int verticalGridNum = 4;
        if (dataLists != null && !dataLists.isEmpty()) {
            for (ArrayList<Float> list : dataLists) {
                for (Float f : list) {
                    if (verticalGridNum < (f + 1)) {
                        verticalGridNum = (int) Math.floor(f + 1);
                    }
                }
            }
        }
        return verticalGridNum;
    }

    private int getHorizontalGridNum() {
        int horizontalGridNum = bottomTextList.size() - 1;
        int MIN_HORIZONTAL_GRID_NUM = 1;
        if (horizontalGridNum < MIN_HORIZONTAL_GRID_NUM) {
            horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
        }
        return horizontalGridNum;
    }

    private void refreshXCoordinateList(int horizontalGridNum) {
        xCoordinateList.clear();
        for (int i = 0; i < (horizontalGridNum + 1); i++) {
            xCoordinateList.add(sideLineLength + backgroundGridWidth * i);
        }

    }

    private void refreshYCoordinateList(int verticalGridNum) {
        yCoordinateList.clear();
        for (int i = 0; i < (verticalGridNum + 1); i++) {
            yCoordinateList.add(topLineLength +
                    ((mViewHeight - topLineLength - bottomTextHeight - bottomTextTopMargin -
                            bottomLineLength - bottomTextDescent) * i / (verticalGridNum)));
        }
    }

    private void refreshDrawDotList(int verticalGridNum) {
        if (dataLists != null && !dataLists.isEmpty()) {
            if (drawDotLists.size() == 0) {
                for (int k = 0; k < dataLists.size(); k++) {
                    drawDotLists.add(new ArrayList<>());
                }
            }
            for (int k = 0; k < dataLists.size(); k++) {
                int drawDotSize = drawDotLists.get(k).isEmpty() ? 0 : drawDotLists.get(k).size();

                for (int i = 0; i < dataLists.get(k).size(); i++) {
                    int x = xCoordinateList.get(i);
                    float y = getYAxesOf(dataLists.get(k).get(i), verticalGridNum);
                    if (i > drawDotSize - 1) {
                        drawDotLists.get(k).add(new Dot(x, y, dataLists.get(k).get(i), k));
                    } else {
                        drawDotLists.get(k).set(i, drawDotLists.get(k).get(i).setTargetData(x, y, dataLists.get(k).get(i), k));
                    }
                }

                int temp = drawDotLists.get(k).size() - dataLists.get(k).size();
                for (int i = 0; i < temp; i++) {
                    drawDotLists.get(k).remove(drawDotLists.get(k).size() - 1);
                }
            }
        }
    }

    private float getYAxesOf(float value, int verticalGridNum) {
        return topLineLength +
                ((mViewHeight - topLineLength - bottomTextHeight - bottomTextTopMargin -
                        bottomLineLength - bottomTextDescent) * (verticalGridNum - value) / (getVerticalGridlNum()));
    }

    private void refreshTopLineLength() {
        // For prevent popup can't be completely showed when backgroundGridHeight is too small.
        topLineLength = getPopupHeight() + DOT_OUTER_CIR_RADIUS + DOT_INNER_CIR_RADIUS + 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("LineViewTest", "onDraw");
        drawBackgroundLines(canvas);
        drawLines(canvas);
        drawDots(canvas);
    }

    private int getPopupHeight() {
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9", 0, 1, popupTextRect);
        int bottomTriangleHeight = 12;
        Rect r = new Rect(-popupTextRect.width() / 2,
                -popupTextRect.height() - bottomTriangleHeight - popupTopPadding * 2 - popupBottomMargin,
                +popupTextRect.width() / 2,
                +popupTopPadding - popupBottomMargin + popupBottomPadding);
        return r.height();
    }

    private void drawDots(Canvas canvas) {
        Paint bigCirPaint = new Paint();
        bigCirPaint.setAntiAlias(true);
        Paint smallCirPaint = new Paint(bigCirPaint);
        smallCirPaint.setColor(Color.parseColor("#FFFFFF"));
        if (drawDotLists != null && !drawDotLists.isEmpty()) {
            for (int k = 0; k < drawDotLists.size(); k++) {
                bigCirPaint.setColor(colorArray[k % colorArray.length]);
                for (Dot dot : drawDotLists.get(k)) {
                    canvas.drawCircle(dot.targetX, dot.targetY, DOT_OUTER_CIR_RADIUS, bigCirPaint);
                    canvas.drawCircle(dot.targetX, dot.targetY, DOT_INNER_CIR_RADIUS, smallCirPaint);
                }
            }
        }
    }

    private void drawLines(Canvas canvas) {
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(AndroidUtils.dpToPx(getContext(), 2));
        for (int k = 0; k < drawDotLists.size(); k++) {
            linePaint.setColor(colorArray[k % colorArray.length]);
            for (int i = 0; i < drawDotLists.get(k).size() - 1; i++) {
                canvas.drawLine(drawDotLists.get(k).get(i).targetX,
                        drawDotLists.get(k).get(i).targetY,
                        drawDotLists.get(k).get(i + 1).targetX,
                        drawDotLists.get(k).get(i + 1).targetY,
                        linePaint);
            }
        }
    }

    private void drawBackgroundLines(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AndroidUtils.dpToPx(getContext(), 1));
        paint.setColor(BACKGROUND_LINE_COLOR);
        PathEffect effects = new DashPathEffect(
                new float[]{10, 5, 10, 5}, 1);

        //draw vertical lines
        for (int i = 0; i < xCoordinateList.size(); i++) {
            canvas.drawLine(xCoordinateList.get(i),
                    0,
                    xCoordinateList.get(i),
                    mViewHeight - bottomTextTopMargin - bottomTextHeight - bottomTextDescent,
                    paint);
        }

        //draw dotted lines
        paint.setPathEffect(effects);
        Path dottedPath = new Path();
        for (int i = 0; i < yCoordinateList.size(); i++) {
            if ((yCoordinateList.size() - 1 - i) % dataOfAGird == 0) {
                dottedPath.moveTo(0, yCoordinateList.get(i));
                dottedPath.lineTo(getWidth(), yCoordinateList.get(i));
                canvas.drawPath(dottedPath, paint);
            }
        }
        //draw bottom text
        if (bottomTextList != null) {
            for (int i = 0; i < bottomTextList.size(); i++) {
                canvas.drawText(bottomTextList.get(i), sideLineLength + backgroundGridWidth * i, mViewHeight - bottomTextDescent, bottomTextPaint);
            }
        }

        if (!drawDotLine) {
            //draw solid lines
            for (int i = 0; i < yCoordinateList.size(); i++) {
                if ((yCoordinateList.size() - 1 - i) % dataOfAGird == 0) {
                    canvas.drawLine(0, yCoordinateList.get(i), getWidth(), yCoordinateList.get(i), paint);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("LineViewTest", "onMeasure");

        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        refreshAfterDataChanged();

        Log.e("LineViewTest", mViewHeight+"");

        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int measureWidth(int measureSpec) {
        int horizontalGridNum = getHorizontalGridNum();
        int preferred = backgroundGridWidth * horizontalGridNum + sideLineLength * 2;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pointToSelect = findPointAt((int) event.getX(), (int) event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (pointToSelect != null) {
                pointToSelect = null;
                postInvalidate();
            }
        }

        return true;
    }

    private Dot findPointAt(int x, int y) {
        if (drawDotLists.isEmpty()) {
            return null;
        }

        final int width = backgroundGridWidth / 2;
        final Region r = new Region();

        for (ArrayList<Dot> data : drawDotLists) {
            for (Dot dot : data) {
                final int pointX = dot.targetX;
                final int pointY = (int) dot.targetY;

                r.set(pointX - width, pointY - width, pointX + width, pointY + width);
                if (r.contains(x, y)) {
                    return dot;
                }
            }
        }

        return null;
    }


    private class Dot {
        float data;
        int targetX;
        float targetY;
        int linenumber;

        Dot(int targetX, float targetY, float data, int linenumber) {
            this.linenumber = linenumber;
            setTargetData(targetX, targetY, data, linenumber);
        }

        Dot setTargetData(int targetX, float targetY, float data, int linenumber) {
            this.targetX = targetX;
            this.targetY = targetY;
            this.data = data;
            this.linenumber = linenumber;
            return this;
        }
    }
}