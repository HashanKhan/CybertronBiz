package com.example.asus.nfc_qr_app.Authentication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.asus.nfc_qr_app.R;
import com.example.asus.nfc_qr_app.db.DBContract;
import com.example.asus.nfc_qr_app.db.DbHelper;
import com.google.zxing.Result;

import net.sqlcipher.database.SQLiteDatabase;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.asus.nfc_qr_app.db.DbHelper.PASS_PHRASE;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    //private EditText usernameET ;
    private String usernameET ;
    private String userNameWeb;
    private String passwordWeb;
    private Button signInButton;
    private String photo;

    private LinearLayout qrScannerLayout;

    //DB connect
    DbHelper dbHelper = new DbHelper(this);
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String userurl = "http://192.168.8.102:8080/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_authenticate);
        SQLiteDatabase.loadLibs(this);

        //Database Section
        SQLiteDatabase db = dbHelper.getReadableDatabase(PASS_PHRASE);
        Cursor cursor = dbHelper.readFromLocaleDataBase(db);

        //usernameET = (EditText) findViewById(R.id.usernameET);

        while (cursor.moveToNext()){
            usernameET = cursor.getString(cursor.getColumnIndex(DBContract.NAME));
            userNameWeb = cursor.getString(cursor.getColumnIndex(DBContract.USERNAME));
            passwordWeb = cursor.getString(cursor.getColumnIndex(DBContract.PASSWORD));
            //photo = cursor.getString(cursor.getColumnIndex(DBContract.PHOTO));
        }
        /*usernameET = "Manula";
        userNameWeb = "IT15060686";
        passwordWeb = "IT15060686";*/


        qrScannerLayout = (LinearLayout) findViewById(R.id.qrScannerLayout);
        performQrScanner();

    }

    /*public void signIn(View view2) {
        if (usernameET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Must have username defined", Toast.LENGTH_SHORT).show();
            return ;
        }

        // If a username is defined, then lock interface and update
        //usernameET.setEnabled(false);
        //signInButton.setEnabled(false);

    }*/

    public void performQrScanner() {

        qrScannerLayout.setVisibility(View.VISIBLE);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view<br />

        qrScannerLayout.addView(
                mScannerView
        );

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();   // Stop camera on pause
        }
    }



    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box.

        mScannerView.stopCamera();

        try {
            /*ServerFacade.sendRegisterUserToQRCode(
                    this,
                    usernameET.getText().toString(),
                    rawResult.getText()
            );*/

            qrScannerLayout.removeView(
                    mScannerView
            );
            qrScannerLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Main Activity", e.getMessage(), e);
            qrScannerLayout.removeView(
                    mScannerView
            );
            qrScannerLayout.setVisibility(View.GONE);
        }
        Intent intent = new Intent(this, FaceMicrosoftSample.class);
        Bundle b = new Bundle();
        intent.putExtra("qrCode",rawResult.toString());
        intent.putExtra("userName",usernameET.toString());
        intent.putExtra("userNameWeb",userNameWeb.toString());
        intent.putExtra("passwordWeb",passwordWeb.toString());
        //intent.putExtra("userPhoto",photo);
        startActivity(intent);

    }

    public void FaceRecognition(View view){
        Intent intent = new Intent(this, FaceMicrosoftSample.class);
        startActivity(intent);
    }

}
