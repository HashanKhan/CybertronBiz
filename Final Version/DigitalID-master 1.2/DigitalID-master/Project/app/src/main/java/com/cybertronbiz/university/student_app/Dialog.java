package com.cybertronbiz.university.student_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Dialog extends AppCompatDialogFragment {
    private EditText PIN;
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("PIN value required")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Can put anything if needed.
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pin = PIN.getText().toString();
                        if (pin.equals("123")){
                            Intent intent = new Intent(getActivity().getApplicationContext(),UserActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getActivity(),"Sorry, wrong PIN value",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        PIN = view.findViewById(R.id.editTextpin);

        return builder.create();
    }
}
