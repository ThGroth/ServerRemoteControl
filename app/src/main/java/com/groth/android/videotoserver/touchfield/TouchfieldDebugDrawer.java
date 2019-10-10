package com.groth.android.videotoserver.touchfield;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;


public class TouchfieldDebugDrawer {

    private Paint paintStyle;
    private Bitmap bitmap;
    private Path path;
    private Paint bitmapPaint;
    private TouchState touchState;


    public TouchfieldDebugDrawer(TouchState touchState) {

        this.touchState = touchState;
        paintStyle = new Paint();
        paintStyle.setAntiAlias(true);
        paintStyle.setDither(true);

        paintStyle.setStyle(Paint.Style.STROKE);
        paintStyle.setStrokeJoin(Paint.Join.ROUND);
        paintStyle.setStrokeCap(Paint.Cap.ROUND);

        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        bitmapPaint.setColor(Color.BLUE);
    }

    public Paint getPaintStyle() {
        return paintStyle;
    }

    public void setPaintStyle(Paint paintStyle) {
        this.paintStyle = paintStyle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getBitmapPaint() {
        return bitmapPaint;
    }

    public void setBitmapPaint(Paint bitmapPaint) {
        this.bitmapPaint = bitmapPaint;
    }

    public void updateBitmapSize(Object o) {

    }

    public void updateBitmapSize(int width, int height)
    {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public void drawBackground(Canvas canvas) {
        canvas.drawBitmap(getBitmap(), 0, 0, getBitmapPaint());
        canvas.drawRect(new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),bitmapPaint);
    }



    public void drawPath(Canvas canvas) {
        paintStyle.setColor(Color.RED);
        paintStyle.setStrokeWidth(3);
        TouchPoint curr = touchState.getCurrentFingerPosition();
        switch (touchState.getTouchType())
        {
            case MOVE:
                break;
            case CLICK:
                canvas.drawCircle(curr.x, curr.y, 80, paintStyle);
            case DRAG:
                paintStyle.setColor(Color.YELLOW);
                paintStyle.setStrokeWidth(10);
                break;
            case SCROLL:
                paintStyle.setColor(Color.GREEN);
            case ZOOM:
                canvas.drawCircle(curr.x, curr.y, 100, paintStyle);
                break;
            case NONE:
                return;
        }
        canvas.drawPath(path, paintStyle);
    }

    public void touch_up() {
        TouchPoint curr = touchState.getCurrentFingerPosition();
        path.lineTo(curr.x, curr.y);
        path.reset(); // end of path
    }
    }
