package com.groth.android.videotoserver.buttonBar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.groth.android.videotoserver.R;
import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;


public class ButtonChangeDialog extends Dialog implements IconDialog.Callback {

    private final FragmentManager fragmentManager;
    private Context context;
    private ServerCommandButton serverCommandButton;
    private Button save;
    private Button cancel;
    private IconDialog iconDialog;
    private Button getIcon;

    private Icon[] selectedIcons;
    private ImageView currentIcon;

    public ButtonChangeDialog(Context context, FragmentManager fragmentManager, ServerCommandButton serverCommandButton) {
        super(context);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.serverCommandButton = serverCommandButton;

        iconDialog = new IconDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_button_dialog);
        save = findViewById(R.id.buttonSaveChanges);
        cancel = findViewById(R.id.buttonCancel);
        getIcon = findViewById(R.id.buttonChangeImageButton);
        currentIcon = findViewById(R.id.buttonCurrentImage);

        getIcon.setOnClickListener((View v) -> {
            iconDialog.setSelectedIcons(selectedIcons);
            iconDialog.show(fragmentManager, "icon_dialog");
        });

    }


    @Override
    public void onIconDialogIconsSelected(IconDialog dialog, @NonNull Icon[] icons) {
        selectedIcons = icons;
        if (selectedIcons.length > 0) {
            currentIcon.setImageDrawable(icons[0].getDrawable(getContext()));
            currentIcon.invalidateDrawable(icons[0].getDrawable(getContext()));
        }

    }
}
