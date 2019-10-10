package com.groth.android.videotoserver.touchfield;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.groth.android.videotoserver.connection.ConnectionHandler;
import com.groth.android.videotoserver.connection.MouseClicks;

public class TouchfieldView extends View implements ServiceConnection
{
    private final Paint backgroundPaint;

    private final TouchState touchState;
    private Rect backgroundRect;
    private boolean debugMode = true;
    private String debugText = "";
    private Paint debugPaint;
    private ConnectionHandler serverConnectionService;


    public TouchfieldView(Context c, AttributeSet attrs) {
        super(c);

        connectService();
        touchState = new TouchState();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.DKGRAY);
        backgroundPaint.setShadowLayer(10,5,5,Color.BLACK);

        debugPaint = new Paint();
        debugPaint.setTextSize(15);
        debugPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        this.backgroundRect = new Rect(0,0,width-15,height-15);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(backgroundRect, backgroundPaint);
        if (debugMode) {
            canvas.drawText(debugText, 0, backgroundRect.bottom - 15, debugPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchState.setCurrentFingerPosition(new TouchPoint(event.getX(), event.getY()));
        switch (event.getPointerCount()) {
            case 1:
                return handleSingleFingerTouchEvent(event);
            case 2:
                return handleTwoFingerTouchEvent(event);
            case 3:
                return handleThreeFingerTouchEvent(event);
            default:
                return false;
        }
    }

    public boolean handleSingleFingerTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchState.touch_start();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                // In case this is not already a registered dragging check weather this is a real
                // movement or start of dragging.
                if ( !TouchType.DRAG.equals(touchState.getTouchType() ) )
                {
                    if (touchState.computeIsDragStart()) {
                        handleMouseClick(MouseClicks.DRAG_CLICK_HOLD);
                        touchState.setTouchType(TouchType.DRAG);

                    } else {
                        touchState.setTouchType(TouchType.MOVE);
                    }
                }

                String mode = TouchType.DRAG.equals(touchState.getTouchType()) ? "in drag mode to " : "";
                debugText = "Move "
                        + mode
                        + touchState.getCurrentFingerPosition() +
                        " startpoint: "+touchState.getStartFingerPosition();
                handleMouseMove(touchState.getMoveVector() );
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // TODO Check if multitouch is needed here too
                if (TouchType.DRAG.equals(touchState.getTouchType()) )
                {
                    debugText = "End of drag.";
                    handleMouseClick(MouseClicks.DRAG_CLICK_RELEASE);
                }
                if (touchState.computeIsSimpleClick() )
                {
                    debugText = "Simple Click.";
                    handleMouseClick(MouseClicks.LEFT_CLICK);
                }
                // normal movement already done.
                // reset the touch state
                touchState.touch_up();
                invalidate();
                break;
        }
        return true;
    }


    public boolean handleTwoFingerTouchEvent(MotionEvent event) {
        return false;
    /*    float x = event.getX();
        float y = event.getY();
        currentFingerPosition = new PointF(x, y);
        double newDist = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startFingerPosition.set(new PointF(x, y));
                downStartTime = System.currentTimeMillis();
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                int xDiff1 = (int) Math.abs(currentFingerPosition.x - startFingerPosition.x);
                int yDiff1 = (int) Math.abs(currentFingerPosition.y - startFingerPosition.y);
                // Multitouch
                if (event.getPointerCount() > 1) {

                    //more than 1 finger
                    yX = event.getX(1);
                    yY = event.getY(1);

                    newDist = Math.sqrt(Math.pow(yX - x, 2) + Math.pow(yY - y, 2));
                    if(firstTouch)
                    {
                        dist=newDist;
                        firstTouch=false;
                    }

                    scaleFactor = (newDist-dist)/dist;

                    if(Math.abs(scaleFactor)>0.08){
                        zooming=true;
                        zoomCounter++;
                        if(zoomCounter>zoomOverFlow) {

                            if (scaleFactor > 0) {
                                OnXMouseClicked(ClickType.Zoom_in);
                            } else {
                                OnXMouseClicked(ClickType.Zoom_out);
                            }
                            zoomCounter=0;
                        }

                    }else if(!zooming){
                        twoFingerScroll=true;
                        scrolling=true;

                    }
                }

                long thisTime = System.currentTimeMillis()- downStartTime;
                if (xDiff1 < CLICK*6 && yDiff1 < CLICK*6){

                    if(draggable && dragging==false && thisTime>350){

                        OnXMouseClicked(ClickType.Drag_Down);
                        dragging=true;
                    }



                }else{

                    draggable=false;
                }
                touch_move(x, y);

                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                int xDiff = (int) Math.abs(currentFingerPosition.x - startFingerPosition.x);
                int yDiff = (int) Math.abs(currentFingerPosition.y - startFingerPosition.y);
                if (xDiff < CLICK && yDiff < CLICK){
                    Log.d("onTouchEvent", "up click");
                    if(scrolling){
                        OnXMouseClicked(ClickType.Right_click);
                    }else{
                        OnXMouseClicked(ClickType.Left_click);
                    }

                }else{
                    Log.d("onTouchEvent","up outside click");
                }
                if(dragging){
                    dragging=false;
                    OnXMouseClicked(ClickType.Drag_up);
                }


                firstTouch=true;
                zooming=false;
                draggable=true;
                twoFingerScroll=false;
                touch_up();
                invalidate();

                break;
        }
        return true; */
    }

    public boolean handleThreeFingerTouchEvent(MotionEvent event) {
        // not implemented yet
        return false;
    }


    private void handleMouseMove(TouchPoint moveVector) {
        if (validateServerConnection() )
        {
            serverConnectionService.getConnection().mouseMove(moveVector.x, moveVector.y);
        }
    }


    private void handleMouseClick(MouseClicks type) {
        if (validateServerConnection() )
        {
            serverConnectionService.getConnection().mouseClick(type);
        }
    }

    private boolean validateServerConnection()
    {
        return serverConnectionService != null && serverConnectionService.getConnection()!= null;
    }


    private void connectService()
    {
        Intent service = new Intent(getContext(), ConnectionHandler.class);
        getContext().bindService(service, this, Context.BIND_IMPORTANT);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (! (iBinder instanceof  ConnectionHandler.ConnectionHandlerBinder))
        {
            //TODO Error !
            return;
        }
        serverConnectionService = ((ConnectionHandler.ConnectionHandlerBinder) iBinder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        serverConnectionService = null;
    }

}


