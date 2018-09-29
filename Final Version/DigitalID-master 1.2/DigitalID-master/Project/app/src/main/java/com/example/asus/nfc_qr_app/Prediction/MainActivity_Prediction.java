package com.example.asus.nfc_qr_app.Prediction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.example.asus.nfc_qr_app.R;

public class MainActivity_Prediction extends AppCompatActivity {
    Button btnPerformance;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prediction);

        btnPerformance = (Button)findViewById(R.id.performance);
        btnPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPerformance = new Intent(view.getContext(),Main2Activity_Prediction.class);
                startActivity(intentPerformance);
            }
        });

    }

}
