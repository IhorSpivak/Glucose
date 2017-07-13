package com.ixbiopharma.glucose.model;

import android.graphics.Point;

import com.ixbiopharma.glucose.utils.AndroidUtils;

/**
 * Dot
 * <p>
 * Created by ivan on 01.05.17.
 */

public class Dot {
    public int x;
    public float y;
    public float data;
    private int targetX;
    private float targetY;
    public int linenumber;
    private int velocity = AndroidUtils.dpToPx(18);

    public Dot(int x, float y, int targetX, float targetY, float data, int linenumber) {
        this.x = x;
        this.y = y;
        this.linenumber = linenumber;
        setTargetData(targetX, targetY, data, linenumber);
    }

    public Point setupPoint(Point point) {
        point.set(x, (int) y);
        return point;
    }

    public Dot setTargetData(int targetX, float targetY, float data, int linenumber) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.data = data;
        this.linenumber = linenumber;
        return this;
    }

    public boolean isAtRest() {
        return (x == targetX) && (y == targetY);
    }

    public void update() {
        x = (int) updateSelf(x, targetX, velocity);
        y = updateSelf(y, targetY, velocity);
    }

    private float updateSelf(float origin, float target, int velocity) {
        if (origin < target) {
            origin += velocity;
        } else if (origin > target) {
            origin -= velocity;
        }
        if (Math.abs(target - origin) < velocity) {
            origin = target;
        }
        return origin;
    }
}