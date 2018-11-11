package com.cybertronbiz.university.reader_app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ReaderActivity extends AppCompatActivity {
    private static final String TAG = ReaderActivity.class.getName();
    NfcAdapter nfcAdapter;
    private Button button;
    private TextView textView,textView1,id;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String userurl = DBContract.SERVER_URL;
    private String idnum;
    private ProgressBar progressBar;
    private Thread thread;
    private ImageView photo,draft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        textView = (TextView) findViewById(R.id.textViewValidity);
        textView1 = (TextView) findViewById(R.id.textViewValidity1);
        photo = (ImageView) findViewById(R.id.imageViewUser);
        draft = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.buttonQRscanner);
        progressBar = (ProgressBar) findViewById(R.id.progbar);
        final Activity activity = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });

        //NFC Part
        id = (TextView) findViewById(R.id.textviewID);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter != null && nfcAdapter.isEnabled()){
            Toast.makeText(this,"NFC is Here!!!",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Sorry No NFC!!!",Toast.LENGTH_LONG).show();
        }

        thread = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(10000);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setVisibility(View.INVISIBLE);
                                textView1.setVisibility(View.INVISIBLE);
                                id.setVisibility(View.INVISIBLE);
                                photo.setVisibility(View.INVISIBLE);
                                draft.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    //NFC Reader Part
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)){
            Toast.makeText(this,"NFC Intent Recieved!",Toast.LENGTH_LONG).show();
            draft.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (parcelables != null && parcelables.length > 0){
                readTextFromMessage((NdefMessage) parcelables[0]);
            }else {
                Toast.makeText(this,"No NDEF Message Found!!!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    private void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this,ReaderActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilters = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,intentFilters,null);
    }

    private void disableForegroundDispatchSystem(){
        nfcAdapter.disableForegroundDispatch(this);
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord){
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        }catch (UnsupportedEncodingException e){
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

    private void readTextFromMessage(NdefMessage ndefMessage){
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0){
            NdefRecord ndefRecord = ndefRecords[0];
            String tagcontent = getTextFromNdefRecord(ndefRecord);
            id.setText(tagcontent);

            String regno = id.getText().toString();
            String url = userurl + regno;
            sendIdAndCheckValidity(url);
        }else {
            Toast.makeText(this,"No NDEF records found!!!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this,"You cancelled the scan!!!",Toast.LENGTH_LONG).show();
            }else {
                draft.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                idnum = result.getContents();
                String url = userurl + idnum;
                sendIdAndCheckValidity(url);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void sendIdAndCheckValidity(final String url) {
        requestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (url != null){
                    try {
                        JSONObject data = new JSONObject(response);
                        String pic = data.getString("photo");
                        String idno = data.getString("regno");

                        id.setText(idno);
                        id.setVisibility(View.VISIBLE);

                        Bitmap bm = StringToBitMap(pic);
                        photo.setImageBitmap(bm);
                        photo.setVisibility(View.VISIBLE);

                        Log.i(TAG,"Response :" + response.toString());
                        textView.setVisibility(View.VISIBLE);
                        textView1.setVisibility(View.VISIBLE);
                        textView1.setTextColor(Color.parseColor("#41DA30"));
                        textView.setText("Student");
                        textView1.setText("Verified");
                        requestQueue.stop();
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.i(TAG,"Inavalid User!!!");
                    textView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    textView1.setTextColor(Color.parseColor("#E32B2B"));
                    textView.setText("Student");
                    textView1.setText("Not - Verified");
                    requestQueue.stop();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
                textView.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView1.setTextColor(Color.parseColor("#E32B2B"));
                textView.setText("Student");
                textView1.setText("Not - Verified");
                requestQueue.stop();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }
}
