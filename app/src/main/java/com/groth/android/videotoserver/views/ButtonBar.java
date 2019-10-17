package com.groth.android.videotoserver.views;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageButton;

import com.groth.android.videotoserver.R;

import java.util.List;

public class ButtonBar {
    private List<ImageButton> buttons;

    public ImageButton getMonitorDownButton(Context context)
    {
        ImageButton button = new ImageButton(context);
        button.setImageResource(R.drawable.ic_launch_black_24dp);
        return button;
    }

}
