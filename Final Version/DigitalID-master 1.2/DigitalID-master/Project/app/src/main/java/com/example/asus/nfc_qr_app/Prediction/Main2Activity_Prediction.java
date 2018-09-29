package com.example.asus.nfc_qr_app.Prediction;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asus.nfc_qr_app.R;
import com.example.asus.nfc_qr_app.db.DBContract;
import com.example.asus.nfc_qr_app.db.DbHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.asus.nfc_qr_app.R.*;
import static com.example.asus.nfc_qr_app.db.DbHelper.PASS_PHRASE;

public class Main2Activity_Prediction extends AppCompatActivity {

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Button btnPerformance;
    private String getMyperformanceUrl="http://192.168.8.101:8081/ResearchProject/performance/getperformancevalues?action=2";
    private EditText performanceText;
    private BarChart barChart;

    String itnumber;

    //DB connect
    DbHelper dbHelper = new DbHelper(this);
    private RequestQueue requestQueue1;
    private StringRequest stringRequest1;
    private String userurl = "http://192.168.8.101:8080/user/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main2_prediction);

        //SQL DB
        SQLiteDatabase.loadLibs(this);

        //Database Section
        SQLiteDatabase db = dbHelper.getReadableDatabase(PASS_PHRASE);
        Cursor cursor = dbHelper.readFromLocaleDataBase(db);

        while (cursor.moveToNext()){
            itnumber = cursor.getString(cursor.getColumnIndex(DBContract.REGNO));
        }

        btnPerformance = (Button)findViewById(id.btn_performance);
        btnPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //itnumber = "IT18507270";
                getMyPerformancelevel(itnumber);
                //generateGraph("1");
                //performanceText = (EditText)findViewById(id.performanceText);
                //performanceText.setText("Your current performance level is 4");
            }
        });

    }

    private void getMyPerformancelevel(final String itnumber) {
        requestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.POST, getMyperformanceUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                performanceText = (EditText)findViewById(id.performanceText);
                try {
                    JSONObject data = new JSONObject(response);
                    String performance = data.getString("data");
                    String attendance = data.getString("attendance");
                    //String per []=performance.split(":");
                    //String per1 = per[0];
                    //String per2 = per[1];
                    int perform = Integer.parseInt(performance);

                    if(perform == 3){
                        performanceText.setText("Your current performance level is risk \n" +"Your current attendance percentage to lectures for 5 subjects : "+attendance+"%");
                    }else if(perform == 2){
                        performanceText.setText("Your current performance level is good \n"+"Your current attendance percentage to lectures for 5 subjects : "+attendance+"%");
                    }else if(perform == 1){
                        performanceText.setText("Your current performance level is excelent \n" +"Your current attendance percentage to lectures for 5 subjects : "+attendance+"%");
                    }else{
                        performanceText.setText("Your current performance level is at risk \n" +"Your current attendance percentage to lectures for 5 subjects : "+attendance+"%");
                    }

                    generateGraph(performance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main2Activity_Prediction.this,"The Error is : "+error.toString(),Toast.LENGTH_SHORT).show();
            }
        }) {
            protected HashMap<String, String> getParams() {
                HashMap<String, String> performance_info = new HashMap<String, String>();
                performance_info.put("performance_info",itnumber); //Add the data you'd like to send to the server.
                return performance_info;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void generateGraph(String performance) {
       // String per []=performance.split(":");
        //String per1 = per[0];
        //String per2 = per[1];
        int perform = Integer.parseInt(performance);
        barChart = (BarChart)findViewById(id.bargraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        if(perform==3){
            barEntries.add(new BarEntry(0,0));
        }else if(perform==2){
            barEntries.add(new BarEntry(2,0));
        }else if(perform==0){
            barEntries.add(new BarEntry(1,0));
        }else if(perform==1){
            barEntries.add(new BarEntry(3,0));
        }


        BarDataSet barDataSet = new BarDataSet(barEntries,"Levels");

        ArrayList<String> weekArrayList = new ArrayList<>();
        weekArrayList.add("Semester 1");
        weekArrayList.add("Semester 2");


        BarData barData = new BarData(weekArrayList,barDataSet);
        barChart.setData(barData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }


}
