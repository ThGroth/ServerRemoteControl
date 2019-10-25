package com.groth.android.videotoserver.buttonBar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.groth.android.videotoserver.R;
import com.groth.android.videotoserver.connection.ssh.ServerCommand;
import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;


public class ButtonChangeDialog extends DialogFragment implements IconDialog.Callback {

    private final FragmentManager fragmentManager;
    private Context context;
    private ServerCommandButton serverCommandButton;
    private Button save;
    private Button cancel;
    private IconDialog iconDialog;
    private Button getIcon;

    private Icon[] selectedIcons;
    private ImageView currentIcon;
    private EditText command;
    private CheckBox withGui;

    public ButtonChangeDialog(Context context, FragmentManager fragmentManager, ServerCommandButton serverCommandButton) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.serverCommandButton = serverCommandButton;

        iconDialog = new IconDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_button_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        save = view.findViewById(R.id.buttonSaveChanges);
        cancel = view.findViewById(R.id.buttonCancel);
        command = view.findViewById(R.id.buttonCommandEditText);
        withGui = view.findViewById(R.id.buttonGuiCheckBox);
        getIcon = view.findViewById(R.id.buttonChangeImageButton);
        currentIcon = view.findViewById(R.id.buttonCurrentImage);


        cancel.setOnClickListener((View v) -> dismiss());
        getIcon.setOnClickListener((View v) -> {
            iconDialog.setSelectedIcons(selectedIcons);
            iconDialog.setTargetFragment(this, 0);
            iconDialog.show(fragmentManager, "icon_dialog");
        });
        save.setOnClickListener((View v) -> saveCommandButtonChanges());


    }

    private void saveCommandButtonChanges() {
        String newCommand = command.getText() == null ? "" : command.getText().toString();
        boolean useGui = withGui.isChecked();
        serverCommandButton.setCommand(new ServerCommand(newCommand, useGui));
    }


    @Override
    public void onIconDialogIconsSelected(IconDialog dialog, @NonNull Icon[] icons) {
        selectedIcons = icons;
        if (selectedIcons.length > 0) {
            currentIcon.setImageDrawable(icons[0].getDrawable(getContext()));
        }

    }
}
