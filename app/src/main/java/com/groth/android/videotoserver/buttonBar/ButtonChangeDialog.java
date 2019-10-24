package com.groth.android.videotoserver.buttonBar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.groth.android.videotoserver.R;


public class ButtonChangeDialog extends Dialog {

    private Context context;
    private ServerCommandButton serverCommandButton;
    private Button save;
    private Button cancel;

    public ButtonChangeDialog(Context context, ServerCommandButton serverCommandButton) {
        super(context);
        this.context = context;
        this.serverCommandButton = serverCommandButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_server_dialog);
        save = (Button) findViewById(R.id.buttonSaveChanges);
        cancel = (Button) findViewById(R.id.buttonCancel);

    }

}
