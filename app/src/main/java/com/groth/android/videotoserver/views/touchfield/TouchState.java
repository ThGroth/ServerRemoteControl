package com.groth.android.videotoserver.views.touchfield;

public class TouchState {
    private TouchType touchType;
    private TouchPoint currentFingerPosition;
    private TouchPoint startFingerPosition;

    // number of pixels that you may move without stopping drag time
    private static final double DRAG_TOLERANCE = 10;
    // milliseconds how long since when a click becomes a drag
    private static final long DRAG_TIME = 500;

    public TouchState() {
        startFingerPosition = new TouchPoint(0,0);
        currentFingerPosition = new TouchPoint(0,0);
        touchType = TouchType.NONE;
    }

    public TouchType getTouchType() {
        return touchType;
    }

    public void setTouchType(TouchType touchType) {
        this.touchType = touchType;
    }

    public TouchPoint getCurrentFingerPosition() {
        return currentFingerPosition;
    }

    public void setCurrentFingerPosition(TouchPoint currentFingerPosition) {
        this.currentFingerPosition = currentFingerPosition;
    }

    public TouchPoint getStartFingerPosition() {
        return startFingerPosition;
    }

    public void setStartFingerPosition(TouchPoint startFingerPosition) {
        this.startFingerPosition = startFingerPosition;
    }

    public void setStartToCurrentFingerPosition() {
        startFingerPosition.set(
                new TouchPoint(currentFingerPosition.x, currentFingerPosition.y));
        startFingerPosition.setTouchTime(currentFingerPosition.getTouchTime());
    }

    public void touch_start() {
        /* we will assume it is start of a movement, we can still switch to click if
        it's too short in distance and time later
        setTouchType(TouchType.MOVE);  better without*/
        setStartToCurrentFingerPosition();
    }

    //determine if this is a real movement or just startFingerPosition of dragging.
    public boolean computeIsDragStart()
    {
        return currentFingerPosition.spaceDistanceTo(startFingerPosition) < DRAG_TOLERANCE
                &&
                currentFingerPosition.timeDistanceTo(startFingerPosition ) > DRAG_TIME ;
    }

    //determine if time and movement too long for a simple click
    public boolean computeIsSimpleClick()
    {
        return currentFingerPosition.spaceDistanceTo(startFingerPosition) < DRAG_TOLERANCE
                &&
                currentFingerPosition.timeDistanceTo(startFingerPosition) < DRAG_TIME ;
    }

    public TouchPoint getMoveVector() {
        return currentFingerPosition.getVectorFrom(startFingerPosition);
    }

    public void touch_up() {
        setTouchType(TouchType.NONE);
    }
}
