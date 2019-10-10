package com.groth.android.videotoserver.settings;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.groth.android.videotoserver.R;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

public class GenerateKeyFragment extends Fragment implements View.OnClickListener
{

    private File publicKeyFile = null;
    private Button copyKeyButton;
    private Button sendKeyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generate_key, container, false);
        view.findViewById(R.id.GenerateKey).setOnClickListener(this);
        copyKeyButton = view.findViewById(R.id.CopyPublicKeyButton);
        copyKeyButton.setEnabled(false);
        copyKeyButton.setOnClickListener(this);
        sendKeyButton = view.findViewById(R.id.SendPublicKeyButton);
        sendKeyButton.setEnabled(false);
        sendKeyButton.setOnClickListener(this);
        return view;
    }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.GenerateKey:
                    generateNewKey();
                    break;
                case R.id.CopyPublicKeyButton:
                    copyPublicContentToClipboard();
                    break;
                case R.id.SendPublicKeyButton:
                    sendPublicKeyByMail();
                    break;
            }
        }

        private void copyPublicContentToClipboard() {
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (publicKeyFile == null) {
                toastErrorMessage(getString(R.string.ErrorNoKeyGeneratedYet));
                return;
            }
            StringBuilder text = new StringBuilder();
            try( BufferedReader br = new BufferedReader(new FileReader(publicKeyFile)) ) {

                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
            } catch (FileNotFoundException e) {
                toastErrorMessage(getString(R.string.ErrorNoKeyGeneratedYet));
                return;
            } catch (IOException e) {
                toastErrorMessage(getString(R.string.ErrorReadingKeyFile));
                return;
            }
            ClipData clip = ClipData.newPlainText(getString(R.string.PublicKey), text.toString());
            clipboard.setPrimaryClip(clip);
        }

        private void sendPublicKeyByMail(){
            Uri path = Uri.fromFile( publicKeyFile );
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            // set the type to 'email'
            emailIntent .setType("vnd.android.cursor.dir/email");
            // the attachment
            emailIntent .putExtra(Intent.EXTRA_STREAM, path);
            // the mail subject
            emailIntent .putExtra(Intent.EXTRA_SUBJECT, "PublicKey");
            startActivity(Intent.createChooser(emailIntent , "Send email..."));
        }

        private void generateNewKey() {
            Spinner typeSelect = getView().findViewById(R.id.KeyTypesListView);
            EditText passphrase = getView().findViewById(R.id.GenKeyPassphrase);
            Switch externalInternal = getView().findViewById(R.id.ExternalInternalSwitch);
            JSch jsch=new JSch();
            int type = -1;
            switch (typeSelect.getSelectedItem().toString() )
            {
                case "RSA": type = KeyPair.RSA; break;
                case "DSA": type = KeyPair.DSA; break;
                case "ECDSA": type = KeyPair.ECDSA; break;
                default: type = KeyPair.ERROR;
            }
            String filename = android.os.Build.MODEL + "_key";
            String filenamePub = filename+".pub";
            File privateFile = null;
            File publicFile = null;
            if (externalInternal.isChecked()) {
                privateFile = new File(getContext().getExternalFilesDir(null), filename);
                publicFile = new File(getContext().getExternalFilesDir(null), filenamePub);
            }
            else {
                privateFile = new File(getContext().getFilesDir(), filename);
                publicFile = new File(getContext().getFilesDir(), filenamePub);
            }


            KeyPair keyPair= null;
            try {
                OutputStream privateOS = new FileOutputStream(privateFile);
                OutputStream publicOS = new FileOutputStream(publicFile);

                keyPair = KeyPair.genKeyPair(jsch, type);
                keyPair.writePrivateKey(privateOS , passphrase.getText().toString().getBytes());
                keyPair.writePublicKey(publicOS, android.os.Build.MODEL);
                publicKeyFile = publicFile;

                System.out.println("Finger print: "+keyPair.getFingerPrint());
                successNoteAndEnableButton(keyPair.getFingerPrint());
                keyPair.dispose();

            } catch (JSchException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    private void successNoteAndEnableButton(String fingerPrint) {
        toastErrorMessage(getActivity().getString(R.string.KeyGenerated)+fingerPrint);
        copyKeyButton.setEnabled( true );
        sendKeyButton.setEnabled( true );
    }

    private void toastErrorMessage(String msg ) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG);
    }



}
