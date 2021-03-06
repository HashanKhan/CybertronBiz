package com.example.asus.nfc_qr_app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.nfc_qr_app.Prediction.Main2Activity_Prediction;
import com.example.asus.nfc_qr_app.Prediction.MainActivity_Prediction;
import com.example.asus.nfc_qr_app.db.DBContract;
import com.example.asus.nfc_qr_app.db.DbHelper;

import net.sqlcipher.database.SQLiteDatabase;

import static com.example.asus.nfc_qr_app.db.DbHelper.PASS_PHRASE;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    String usernameET;
    String photo;
    String Num;

    //DB connect
    DbHelper dbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        SQLiteDatabase.loadLibs(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Database Section
        SQLiteDatabase db = dbHelper.getReadableDatabase(PASS_PHRASE);
        Cursor cursor = dbHelper.readFromLocaleDataBase(db);




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.navName);
        TextView nav_ID = (TextView)hView.findViewById(R.id.navID);
        //ImageView imageView = hView.findViewById(R.id.userImage1);

        while (cursor.moveToNext()){
            usernameET = cursor.getString(cursor.getColumnIndex(DBContract.NAME));
            //photo = cursor.getString(cursor.getColumnIndex(DBContract.PHOTO));
            Num = cursor.getString(cursor.getColumnIndex(DBContract.REGNO));
        }

        if(usernameET != null && Num != null){
            nav_user.setText(usernameET);
            nav_ID.setText(Num);
            //Bitmap bitmap = StringToBitMap(photo);
            //imageView.setImageBitmap(bitmap);
        }
    }

    //Converting the byte array to bitmap.
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.nav_User:
                Intent intent1= new Intent(Navigation.this,UserActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_Gen:
                Intent intent2= new Intent(Navigation.this,MainActivity2.class);
                startActivity(intent2);
                break;
            case R.id.nav_Scan:
                Intent intent3= new Intent(Navigation.this,MainActivity3.class);
                startActivity(intent3);
                break;
            case R.id.nav_Prediction:
                Intent intent5= new Intent(Navigation.this, Main2Activity_Prediction.class);
                startActivity(intent5);
                break;
            case R.id.nav_Authentication:
                Intent intent6= new Intent(Navigation.this, com.example.asus.nfc_qr_app.Authentication.MainActivity.class);
                startActivity(intent6);
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Authentication(View view) {
        Intent intent = new Intent(this, com.example.asus.nfc_qr_app.Authentication.MainActivity.class);
        startActivity(intent);
    }

    public void ID(View view) {
        Intent intent = new Intent(this, com.example.asus.nfc_qr_app.MainActivity2.class);
        startActivity(intent);
    }

    public void Prediction(View view) {
        Intent intent = new Intent(this, com.example.asus.nfc_qr_app.Prediction.Main2Activity_Prediction.class);
        startActivity(intent);
    }
}
