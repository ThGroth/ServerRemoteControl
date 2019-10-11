package com.groth.android.videotoserver.views.touchfield;


import android.graphics.PointF;


public class TouchPoint extends PointF
{
    private long touchTime;

    public TouchPoint(float x, float y, long touchTime)
    {
        super(x,y);
        this.touchTime = touchTime;
    }

    public TouchPoint(float x, float y)
    {
        this(x,y,System.currentTimeMillis());
    }

    public long getTouchTime() {
        return touchTime;
    }

    public void setTouchTime(long touchTime) {
        this.touchTime = touchTime;
    }

    public double spaceDistanceTo(PointF other)
    {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
    }

    public double timeDistanceTo(TouchPoint other)
    {
        return Math.abs(other.touchTime - this.touchTime);
    }


    @Override
    public String toString() {
        return String.format("(%.1f, %.1f)",x,y);
    }

    public TouchPoint getVectorFrom(PointF other)
    {
        return new TouchPoint(x-other.x, y-other.y);
    }

}
