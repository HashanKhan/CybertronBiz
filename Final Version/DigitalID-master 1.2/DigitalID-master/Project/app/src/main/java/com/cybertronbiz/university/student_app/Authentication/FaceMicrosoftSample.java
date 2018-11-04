package com.cybertronbiz.university.student_app.Authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.cybertronbiz.university.student_app.db.DBContract;
import com.cybertronbiz.university.student_app.db.DbHelper;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.cybertronbiz.university.student_app.db.DbHelper.PASS_PHRASE;

public class FaceMicrosoftSample extends Activity {
    private final int PICK_IMAGE_0 = 0;
    private final int PICK_IMAGE_1 = 1;

    private static boolean s = false;

    String usernameET;
    String qrCode;
    String userNameWeb;
    String passwordWeb;
    String photo;

    //DB connect
    DbHelper dbHelper = new DbHelper(this);
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String userurl = "http://192.168.8.102:8080/user/";

    private ProgressDialog detectionProgressDialog;

    private final String apiEndpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0";
    private final String subscriptionKey = "6b38142a188f4ba9a5d392f05197a453";

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    public class VerificationTask extends AsyncTask<Void, String, VerifyResult> {
        // The IDs of two face to verify.
        private UUID mFaceId0;
        private UUID mFaceId1;

        VerificationTask (UUID faceId0, UUID faceId1) {
            mFaceId0 = faceId0;
            mFaceId1 = faceId1;
        }

        @Override
        protected VerifyResult doInBackground(Void... params) {
            try{
                publishProgress("Verifying...");
                // Start verification.
                return faceServiceClient.verify(
                        mFaceId0,      /* The first face ID to verify */
                        mFaceId1);     /* The second face ID to verify */
            }  catch (Exception e) {
                publishProgress(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            detectionProgressDialog.show();
            //addLog("Request: Verifying face " + mFaceId0 + " and face " + mFaceId1);
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            detectionProgressDialog.setMessage(progress[0]);
        }

        @Override
        protected void onPostExecute(VerifyResult result) {
            detectionProgressDialog.dismiss();
            if (result != null) {
                if(result.isIdentical){
                    s = true;
                    Context context = getApplicationContext();
                    CharSequence text = "Verify success";
                    int duration = Toast.LENGTH_SHORT;

                    ImageView imageView = findViewById(com.cybertronbiz.university.student_app.R.id.imageViewSuccess);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),com.cybertronbiz.university.student_app.R.drawable.success);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 380, 380, false));
                    TextView textView = findViewById(com.cybertronbiz.university.student_app.R.id.textView2);
                    textView.setText("Verification Success");

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    s = false;
                    Context context = getApplicationContext();
                    CharSequence text = "Not same persone";
                    int duration = Toast.LENGTH_SHORT;

                    ImageView imageView = findViewById(com.cybertronbiz.university.student_app.R.id.imageViewSuccess);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),com.cybertronbiz.university.student_app.R.drawable.fail);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 380, 380, false));
                    TextView textView = findViewById(com.cybertronbiz.university.student_app.R.id.textView2);
                    textView.setText("Verification Failed");

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                /*addLog("Response: Success. Face " + mFaceId0 + " and face "
                        + mFaceId1 + (result.isIdentical ? " " : " don't ")
                        + "belong to the same person");*/
            }

            // Show the result on screen when verification is done.
            sendResultToServer();
        }
    }

    private void sendResultToServer() {
        if(s == false){
          userNameWeb = "Undefind";
          passwordWeb = "Undefind";
        }
        try {
            ServerFacade.sendVerificationToServer(
                    this,
                    usernameET.toString(),
                    qrCode.toString(),s,userNameWeb,passwordWeb
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cybertronbiz.university.student_app.R.layout.activity_face_recognition);
        Button button1 = (Button)findViewById(com.cybertronbiz.university.student_app.R.id.button1);

        usernameET = getIntent().getStringExtra("userName");
        qrCode = getIntent().getStringExtra("qrCode");
        userNameWeb = getIntent().getStringExtra("userNameWeb");
        passwordWeb = getIntent().getStringExtra("passwordWeb");
        //photo = getIntent().getStringExtra("userPhoto");

        SQLiteDatabase.loadLibs(this);

        //Database Section
        SQLiteDatabase db = dbHelper.getReadableDatabase(PASS_PHRASE);
        Cursor cursor = dbHelper.readFromLocaleDataBase(db);

        while (cursor.moveToNext()){
            photo = cursor.getString(cursor.getColumnIndex(DBContract.PHOTO));
        }


        detectionProgressDialog = new ProgressDialog(this);

        selectImage0();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index;
        if (requestCode == PICK_IMAGE_0) {
            index = 0;
        } else if (requestCode == PICK_IMAGE_1) {
            index = 1;
        } else {
            return;
        }

        if(resultCode == RESULT_OK) {
            // If image is selected successfully, set the image URI and bitmap.
            Bitmap bitmap = null;
            // For image capture by camera need to access it from files
            if(index == 1){
                File file = new File(mCurrentPhotoPath);
                try {
                    bitmap = MediaStore.Images.Media
                            .getBitmap(getContentResolver(), Uri.fromFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Image pass by image selector
            else {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //bitmap = (Bitmap) getIntent().getParcelableExtra("image");



                //byte[] byteArray = data.getByteArrayExtra("image");
                //bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
            if (bitmap != null) {
                // Image is select but not detected, disable verification button.
                //setVerifyButtonEnabledStatus(false);
                //clearDetectedFaces(index);

                // Set the image to detect.
                if (index == 0) {
                    //mBitmap0 = bitmap;
                    //mFaceId0 = null;
                    ImageView imageView = (ImageView) findViewById(com.cybertronbiz.university.student_app.R.id.imageView1);
                    imageView.setImageBitmap(bitmap);
                } else {
                    //mBitmap1 = bitmap;
                    //mFaceId1 = null;
                    ImageView imageView = (ImageView) findViewById(com.cybertronbiz.university.student_app.R.id.imageView2);
                    imageView.setImageBitmap(bitmap);
                }


                // Start detecting in image.
                detectAndFrame(bitmap,index);
            }
        }
    }

    // Detect faces by uploading a face image.
// Frame faces after detection.
    private void detectAndFrame(final Bitmap imageBitmap, final int index) {
        int mIndex;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());
        mIndex=index;

        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    String exceptionMessage = "";

                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    null          // returnFaceAttributes:
                                /* new FaceServiceClient.FaceAttributeType[] {
                                    FaceServiceClient.FaceAttributeType.Age,
                                    FaceServiceClient.FaceAttributeType.Gender }
                                */
                            );
                            if (result == null){
                                publishProgress(
                                        "Detection Finished. Nothing detected");
                                return null;
                            }
                            else {
                                publishProgress(String.format(
                                        "Detection Finished. %d face(s) detected",
                                        result.length));
                                return result;
                            }
                        } catch (Exception e) {
                            exceptionMessage = String.format(
                                    "Detection failed: %s", e.getMessage());
                            return null;
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        detectionProgressDialog.show();
                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        detectionProgressDialog.setMessage(progress[0]);
                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        //detectionProgressDialog.dismiss();
                        if(!exceptionMessage.equals("")){
                            showError(exceptionMessage);
                        }
                        if (result == null) return;
                        if(index == 0) {
                            ImageView imageView = findViewById(com.cybertronbiz.university.student_app.R.id.imageView1);
                            imageView.setImageBitmap(
                                    drawFaceRectanglesOnBitmap(imageBitmap, result));
                            imageBitmap.recycle();
                        }
                        if(index == 1) {
                            ImageView imageView = findViewById(com.cybertronbiz.university.student_app.R.id.imageView2);
                            imageView.setImageBitmap(
                                    drawFaceRectanglesOnBitmap(imageBitmap, result));
                            imageBitmap.recycle();
                        }
                        setFaceID(result,index);

                        if(mFaceId0 != null && mFaceId1 != null){
                            verify();
                            detectionProgressDialog.dismiss();
                        }
                    }
                };

        detectTask.execute(inputStream);
    }

    private UUID mFaceId0;
    private UUID mFaceId1;

    private void setFaceID(Face[] result, int index) {

        // Set the default face ID to the ID of first face, if one or more faces are detected.
        if (index == 0) {
            mFaceId0 = result[0].faceId;
        } else {
            mFaceId1 = result[0].faceId;
        }
    }

    public void verify() {
        new VerificationTask(mFaceId0, mFaceId1).execute();
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }})
                .create().show();
    }

    private static Bitmap drawFaceRectanglesOnBitmap(
            Bitmap originalBitmap, Face[] faces) {
        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        if (faces != null) {
            for (Face face : faces) {
                FaceRectangle faceRectangle = face.faceRectangle;
                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);
            }
        }
        return bitmap;
    }

    public void selectImage0() {
        selectImage(0);
    }

    public void selectImage1(View view) {
        selectImage(1);
    }

    private void selectImage(int index) {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(
                intent, "Select Picture"), index == 0 ? PICK_IMAGE_0: PICK_IMAGE_1);*/

        //final Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),com.cybertronbiz.university.student_app.R.drawable.test);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        //byte[] byteArray = stream.toByteArray();

        //String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), myBitmap, "Title", null);

        //Intent intent = new Intent();
        //intent.putExtra("image",Uri.parse(path));
        //intent.putExtra("image",myBitmap);
        //startActivityForResult(intent, PICK_IMAGE_0);

        //Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/image.jpg");
        //myBitmap = BitmapFactory.decodeResource(getResources(),com.cybertronbiz.university.student_app.R.drawable.test);
        onResult();
    }




    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }





    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cybertronbiz.university.student_app.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,PICK_IMAGE_1);
            }
        }
    }

    public void onResult(){

        ImageView imageView;

        //final Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),com.cybertronbiz.university.student_app.R.drawable.test);
        final Bitmap myBitmap = StringToBitMap(photo);
        imageView = (ImageView)findViewById(com.cybertronbiz.university.student_app.R.id.imageView1);
        imageView.setImageBitmap(myBitmap);

        int index = 0;
        detectAndFrame(myBitmap,index);
        dispatchTakePictureIntent();

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


}
