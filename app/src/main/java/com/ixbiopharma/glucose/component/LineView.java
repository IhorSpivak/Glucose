package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.Dot;
import com.ixbiopharma.glucose.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * LineView
 * <p>
 * Created by ivan on 17.04.17.
 */

public class LineView extends View {
    private int mViewHeight;
    //drawBackground
    private boolean autoSetDataOfGird = true;
    private boolean autoSetGridWidth = true;
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
    private final int bottomTriangleHeight = 12;
    public boolean showPopup = true;
    private boolean showFloatNumInPopup;

    private Dot pointToSelect;
    private Dot selectedDot;
    private int popupBottomPadding = AndroidUtils.dpToPx(getContext(), 2);

    private int topLineLength = AndroidUtils.dpToPx(getContext(), 16);
    // | | â†this
    //-+-+-
    private int sideLineLength = AndroidUtils.dpToPx(getContext(), 45) / 3 * 2;// --+--+--+--+--+--+--
    //  â†‘this
    private int backgroundGridWidth = AndroidUtils.dpToPx(getContext(), 45);

    //Constants
    private final int popupTopPadding = AndroidUtils.dpToPx(getContext(), 2);
    private final int popupBottomMargin = AndroidUtils.dpToPx(getContext(), 5);
    private final int bottomTextTopMargin = AndroidUtils.sp2px(getContext(), 5);
    private final int bottomLineLength = AndroidUtils.sp2px(getContext(), 22);
    //    private final int DOT_INNER_CIR_RADIUS = AndroidUtils.dpToPx(getContext(), 2);
    private final int DOT_OUTER_CIR_RADIUS = AndroidUtils.dpToPx(getContext(), 4);
    private final int MIN_TOP_LINE_LENGTH = AndroidUtils.dpToPx(getContext(), 12);
    private final int MIN_VERTICAL_GRID_NUM = 4;
    private final int MIN_HORIZONTAL_GRID_NUM = 1;
    private final int BACKGROUND_LINE_COLOR = Color.parseColor("#EEEEEE");
    private final int BOTTOM_TEXT_COLOR = Color.parseColor("#9B9A9B");

    public static final int SHOW_POPUPS_All = 1;
    public static final int SHOW_POPUPS_MAXMIN_ONLY = 2;
    public static final int SHOW_POPUPS_NONE = 3;
    private List<Integer> yList = new ArrayList<>();

    private int showPopupType = SHOW_POPUPS_NONE;

    public void setShowPopup(int popupType) {
        this.showPopupType = popupType;
    }

    private Boolean drawDotLine = false;
    private int[] colorArray = {Color.parseColor("#e74c3c"), Color.parseColor("#2980b9"), Color.parseColor("#1abc9c")};

    // onDraw optimisations
    private final Point tmpPoint = new Point();

    public void setDrawDotLine(Boolean drawDotLine) {
        this.drawDotLine = drawDotLine;
    }

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            for (ArrayList<Dot> data : drawDotLists) {
                for (Dot dot : data) {
                    dot.update();
                    if (!dot.isAtRest()) {
                        needNewFrame = true;
                    }
                }
            }
            if (needNewFrame) {
                postDelayed(this, 25);
            }
            invalidate();
        }
    };

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        popupTextPaint.setAntiAlias(true);
        popupTextPaint.setColor(Color.WHITE);
        popupTextPaint.setTextSize(AndroidUtils.sp2px(getContext(), 13));
        popupTextPaint.setStrokeWidth(5);
        popupTextPaint.setTextAlign(Paint.Align.CENTER);

        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setTextSize(AndroidUtils.sp2px(getContext(), 12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
        bottomTextPaint.setStyle(Paint.Style.FILL);
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
            if (autoSetGridWidth && (longestWidth < r.width())) {
                longestWidth = r.width();
                longestStr = s;
            }
            if (bottomTextDescent < (Math.abs(r.bottom))) {
                bottomTextDescent = Math.abs(r.bottom);
            }
        }

        if (autoSetGridWidth) {
            if (backgroundGridWidth < longestWidth) {
                backgroundGridWidth = longestWidth + (int) bottomTextPaint.measureText(longestStr, 0, 1);
            }
            if (sideLineLength < longestWidth / 2) {
                sideLineLength = longestWidth / 2;
            }
        }

        refreshXCoordinateList(getHorizontalGridNum());
//        invalidate();
    }

    /**
     * @param dataLists The Float ArrayLists for showing,
     *                  dataList.size() must be smaller than bottomTextList.size()
     */
    public void setFloatDataList(ArrayList<ArrayList<Float>> dataLists) {
        setFloatDataList(dataLists, true);
    }

    public void setFloatDataList(ArrayList<ArrayList<Float>> dataLists, boolean showFloatNumInPopup) {
        selectedDot = null;
        this.showFloatNumInPopup = showFloatNumInPopup;
        this.dataLists = dataLists;
        for (ArrayList<Float> list : dataLists) {
//            if (list.size() > bottomTextList.size()) {
//                throw new RuntimeException("dacer.LineView error:" +
//                        " dataList.size() > bottomTextList.size() !!!");
//            }
        }
        float biggestData = 0;
        for (ArrayList<Float> list : dataLists) {
            if (autoSetDataOfGird) {
                for (Float i : list) {
                    if (biggestData < i) {
                        biggestData = i;
                    }
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
        int verticalGridNum = getVerticalGridNum();
        refreshYCoordinateList(verticalGridNum);
        refreshDrawDotList(verticalGridNum);
    }

    private int getVerticalGridNum() {
        int verticalGridNum = MIN_VERTICAL_GRID_NUM;
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
        if (horizontalGridNum < MIN_HORIZONTAL_GRID_NUM) {
            horizontalGridNum = MIN_HORIZONTAL_GRID_NUM;
        }
        return horizontalGridNum;
    }

    private void refreshXCoordinateList(int horizontalGridNum) {
        xCoordinateList.clear();
        for (int i = 0; i < (horizontalGridNum + 2); i++) {
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
        yList.clear();

        if (dataLists != null && !dataLists.isEmpty()) {
            if (drawDotLists.size() == 0) {
                for (int k = 0; k < dataLists.size(); k++) {
                    drawDotLists.add(new ArrayList<>());
                }
            }
            for (int k = 0; k < dataLists.size(); k++) {
                int drawDotSize = drawDotLists.get(k).isEmpty() ? 0 : drawDotLists.get(k).size();

                for (int i = 0; i < dataLists.get(k).size(); i++) {
                    int x = backgroundGridWidth * (i + 1);
                    float y = getYAxesOf(dataLists.get(k).get(i), verticalGridNum);
                    yList.add((int) y);
                    if (i > drawDotSize - 1) {
                        drawDotLists.get(k).add(new Dot(x, 0, x, y, dataLists.get(k).get(i), k));
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
        removeCallbacks(animator);
        post(animator);
    }

    private float getYAxesOf(float value, int verticalGridNum) {
        return topLineLength +
                ((mViewHeight - topLineLength - bottomTextHeight - bottomTextTopMargin -
                        bottomLineLength - bottomTextDescent) * (verticalGridNum - value) / (getVerticalGridNum()));
    }

    private void refreshTopLineLength() {
        // For prevent popup can't be completely showed when backgroundGridHeight is too small.
        topLineLength = getPopupHeight() + DOT_OUTER_CIR_RADIUS + 2; //+ DOT_INNER_CIR_RADIUS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackgroundLines(canvas);
//        drawLines(canvas);
        drawDots(canvas);


        for (int k = 0; k < drawDotLists.size(); k++) {
            float maxValue = 0;
            float minValue = 0;

            try {
                maxValue = Collections.max(dataLists.get(k));
                minValue = Collections.min(dataLists.get(k));
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }

            for (Dot d : drawDotLists.get(k)) {
                if (showPopupType == SHOW_POPUPS_All)
                    drawPopup(canvas, d.data, d.setupPoint(tmpPoint),
                            colorArray[k % colorArray.length]);
                else if (showPopupType == SHOW_POPUPS_MAXMIN_ONLY) {
                    if (d.data == maxValue)
                        drawPopup(canvas, d.data, d.setupPoint(tmpPoint),
                                colorArray[k % colorArray.length]);
                    if (d.data == minValue)
                        drawPopup(canvas, d.data, d.setupPoint(tmpPoint),
                                colorArray[k % colorArray.length]);
                }
            }
        }

        if (showPopup && selectedDot != null) {
            drawPopup(canvas,
                    selectedDot.data,
                    selectedDot.setupPoint(tmpPoint), colorArray[selectedDot.linenumber % colorArray.length]);
        }
    }

    /**
     * @param canvas The canvas you need to draw on.
     * @param point  The Point consists of the x y coordinates from left bottom to right top.
     *               Like is
     *               <p>
     *               3
     *               2
     *               1
     *               0 1 2 3 4 5
     */
    private void drawPopup(Canvas canvas, float num, Point point, int PopupColor) {
        String numStr = showFloatNumInPopup ? String.valueOf(num) : String.valueOf(Math.round(num));
        boolean singularNum = (numStr.length() == 1);
        int sidePadding = AndroidUtils.dpToPx(getContext(), singularNum ? 8 : 5);
        int x = point.x;
        int y = point.y - AndroidUtils.dpToPx(getContext(), 5);
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds(numStr, 0, numStr.length(), popupTextRect);
        Rect r = new Rect(x - popupTextRect.width() / 2 - sidePadding,
                y - popupTextRect.height() - bottomTriangleHeight - popupTopPadding * 2 - popupBottomMargin,
                x + popupTextRect.width() / 2 + sidePadding,
                y + popupTopPadding - popupBottomMargin + popupBottomPadding);

        NinePatchDrawable popup = (NinePatchDrawable) getResources().getDrawable(R.drawable.popup_white);
        popup.setColorFilter(new PorterDuffColorFilter(PopupColor, PorterDuff.Mode.MULTIPLY));
        popup.setBounds(r);
        popup.draw(canvas);
        canvas.drawText(numStr, x, y - bottomTriangleHeight - popupBottomMargin, popupTextPaint);
    }

    private int getPopupHeight() {
        Rect popupTextRect = new Rect();
        popupTextPaint.getTextBounds("9", 0, 1, popupTextRect);
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
                    canvas.drawCircle(dot.x, dot.y, DOT_OUTER_CIR_RADIUS, bigCirPaint);
//                    canvas.drawCircle(dot.x, dot.y, DOT_INNER_CIR_RADIUS, smallCirPaint);
                }
            }
        }
    }

    public int minTitle = 0;
    public int maxTitle = 10;
    private int current = 0;

    private void drawBackgroundLines(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AndroidUtils.dpToPx(getContext(), 1));
        paint.setColor(BACKGROUND_LINE_COLOR);
        PathEffect effects = new DashPathEffect(
                new float[]{10, 5, 10, 5}, 1);

        //draw vertical lines
        for (int i = 0; i < xCoordinateList.size(); i++) {
            canvas.drawLine(backgroundGridWidth * i,
                    0,
                    backgroundGridWidth * i,
                    mViewHeight - bottomTextTopMargin - bottomTextHeight - bottomTextDescent,
                    paint);
        }

        //draw dotted lines
        paint.setPathEffect(effects);
        Path dottedPath = new Path();

        int k = (maxTitle) / (yCoordinateList.size() - 2);
        for (int i = 0; i < yCoordinateList.size(); i++) {


            if ((yCoordinateList.size() - 1 - i) % dataOfAGird == 0) {

                canvas.drawText(String.valueOf(maxTitle - k * i), topLineLength / 2, yCoordinateList.get(i), bottomTextPaint);

                dottedPath.moveTo(0, yCoordinateList.get(i));
                dottedPath.lineTo(getWidth(), yCoordinateList.get(i));
                canvas.drawPath(dottedPath, paint);
            }
        }

        //draw bottom text
        if (bottomTextList != null) {
            for (int i = 0; i < bottomTextList.size(); i++) {
                canvas.drawText(bottomTextList.get(i), backgroundGridWidth * (i + 1), mViewHeight - bottomTextDescent, bottomTextPaint);
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
        int mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
//        mViewHeight = MeasureSpec.getSize(measureSpec);
        refreshAfterDataChanged();
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private int measureWidth(int measureSpec) {
        int horizontalGridNum = getHorizontalGridNum();
        int preferred = backgroundGridWidth * horizontalGridNum + sideLineLength * 2;
        int w = getMeasurement(measureSpec, measureSpec);

        backgroundGridWidth = w / xCoordinateList.size();

        return w;
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
                selectedDot = pointToSelect;
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
                final int pointX = dot.x;
                final int pointY = (int) dot.y;

                r.set(pointX - width, pointY - width, pointX + width, pointY + width);
                if (r.contains(x, y)) {
                    return dot;
                }
            }
        }

        return null;
    }
}