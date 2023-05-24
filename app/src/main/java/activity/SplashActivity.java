package activity;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.administrator.shaktiTransportApp.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

import activity.Pod.ActivityInvoiceGetInfo;
import activity.Pod.NavigateOptionActivity;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class SplashActivity extends Activity  {

    // Splash screen timer
    private final static int SPLASH_TIME_OUT = 3000;
    DatabaseHelper databaseHelper;
    Intent i;
    private static final int REQUEST_CODE_PERMISSION = 2;
    ImageView imageView;
    Context context;
    String versionName = "0.0";
    String newVersion = "0.0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;

        imageView =  findViewById(R.id.imageSplash);


        databaseHelper = new DatabaseHelper(SplashActivity.this);

        //versionName = BuildConfig.VERSION_NAME;
        WebURL.ANDROID_APP_VERSION = "3.0";

        versionName = WebURL.ANDROID_APP_VERSION;

        new Worker1().execute();


        if(checkPermission()){
            openNextActivity();
        }
        else {
            requestPermission();
        }


    }

    private void openNextActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Log.d("newVersion", newVersion + "--" + versionName);
                if (Float.parseFloat(newVersion) > Float.parseFloat(versionName)) {

                    Intent i = new Intent(SplashActivity.this, UpdateActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Log.d("newVersion1", newVersion + "--" + versionName);
                    Intent ii;
                    if (databaseHelper.getLogin()) {

                        int mUserCheckType = Integer.parseInt(CustomUtility.getSharedPreferences(context, "UserTypeSNF"));

                        if(mUserCheckType == 1) {
                            //CustomUtility.setSharedPreference(context, "UserTypeSNF", "1");/// CNF= 1, Transport = 2;
                            // ii = new Intent(SplashActivity.this, ActivityUpdateSandFData.class);
                            ii = new Intent(SplashActivity.this, ActivityInvoiceGetInfo.class);
                            ii.putExtra("login_flag", "Y");
                            startActivity(ii);
                        }
                        else if(mUserCheckType == 2) {
                            // CustomUtility.setSharedPreference(context, "UserTypeSNF", "2");/// CNF= 1, Transport = 2;
                            ii = new Intent(SplashActivity.this, MainActivity.class);
                            ii.putExtra("login_flag", "Y");
                            startActivity(ii);
                        }
                        else
                        {
                            CustomUtility.setSharedPreference(context, "UserTypeSNF", "1");/// CNF= 1, Transport = 2;
                            ii = new Intent(SplashActivity.this, MainActivity.class);
                            ii.putExtra("login_flag", "Y");
                            startActivity(ii);
                        }

                    } else {
                        ii = new Intent(SplashActivity.this, NavigateOptionActivity.class);
                        startActivity(ii);
                        // rlvMainOptionPopupID.setVisibility(View.VISIBLE);
                        // i = new Intent(SplashActivity.this, Login.class);
                    }

                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }


    public String getVersion() {

        String newversion = "0.0";

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        String app_version_response = null;

        try {

            app_version_response = CustomHttpClient.executeHttpPost1(WebURL.VERSION_PAGE, param);

            if (!app_version_response.equalsIgnoreCase("")) {

                JSONArray ja = new JSONArray(app_version_response);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    newversion = jo.getString("app_version");
                    Log.e("VERSION", "&&&&&" + newversion);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return newversion;
    }

    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {

                if (CustomUtility.isInternetOn()) {


                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);
                    newVersion = Jsoup.connect(
                                    "https://play.google.com/store/apps/details?id=com.example.administrator.shaktiTransportApp&hl=en")
                            .timeout(30000)
                            .referrer("http://www.google.com").get()
                            .select("div[itemprop=softwareVersion]").first()
                            .ownText();
                }


                Log.d("newVersion", newVersion + "--" + versionName);

                if (Float.parseFloat(newVersion) > Float.parseFloat(versionName)) {

                    SplashActivity.this.finish();
                    Intent i = new Intent(SplashActivity.this, UpdateActivity.class);
                    startActivity(i);

                } else {
                    Log.d("newVersion1", newVersion + "--" + versionName);

                    if (databaseHelper.getLogin()) {
                        i = new Intent(SplashActivity.this, MainActivity.class);
                        i.putExtra("login_flag", "Y");

                        int mUserCheckType = Integer.parseInt(CustomUtility.getSharedPreferences(context, "UserTypeSNF"));

                        if(mUserCheckType == 1) {
                            //CustomUtility.setSharedPreference(context, "UserTypeSNF", "1");/// CNF= 1, Transport = 2;
                            // i = new Intent(SplashActivity.this, ActivityUpdateSandFData.class);
                            i = new Intent(SplashActivity.this, ActivityInvoiceGetInfo.class);
                            i.putExtra("login_flag", "Y");

                        }
                        else if(mUserCheckType == 2) {
                            // CustomUtility.setSharedPreference(context, "UserTypeSNF", "2");/// CNF= 1, Transport = 2;
                            i = new Intent(SplashActivity.this, MainActivity.class);
                            i.putExtra("login_flag", "Y");

                        }
                        else
                        {
                            CustomUtility.setSharedPreference(context, "UserTypeSNF", "1");/// CNF= 1, Transport = 2;
                            i = new Intent(SplashActivity.this, MainActivity.class);
                            i.putExtra("login_flag", "Y");

                        }

                    } else {
                        i = new Intent(SplashActivity.this, Login.class);
                    }


                    startActivity(i);
                    SplashActivity.this.finish();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }

    }

    private class Worker1 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {

                if (CustomUtility.isInternetOn()) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    newVersion = getVersion();

//                    newVersion = Jsoup.connect(
//                            "https://play.google.com/store/apps/details?id=com.administrator.shaktisolarappnew&hl=en")
//                            //.timeout(30000)
//                            .timeout(6000)    // 10 second
//
//                            .referrer("http://www.google.com").get()
//                            .select("div[itemprop=softwareVersion]").first()
//                            .ownText();

                }

            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.i("SomeTag", System.currentTimeMillis() / 1000L
//                    + " post execute \n" + result);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkPermission() {
        int FineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int CoarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int Camera = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ReadExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int WriteExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ReadMediaImages = ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES);

        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED  && ReadMediaImages == PackageManager.PERMISSION_GRANTED;

        }else {
            return FineLocation == PackageManager.PERMISSION_GRANTED && CoarseLocation == PackageManager.PERMISSION_GRANTED
                    && Camera == PackageManager.PERMISSION_GRANTED && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && WriteExternalStorage == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,  Manifest.permission.ACCESS_COARSE_LOCATION,
                            READ_MEDIA_IMAGES},
                    REQUEST_CODE_PERMISSION);
        }  else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0) {

                    if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        boolean CoarseLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  ReadMediaImages = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                        if (CoarseLocationAccepted  && Camera && ReadMediaImages ) {
                            // perform action when allow permission success
                            openNextActivity();
                        }else {
                            requestPermission();
                        }
                    } else   {
                        boolean  FineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        boolean CoarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean  Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                        boolean ReadPhoneStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                        boolean WritePhoneStorage = grantResults[4] == PackageManager.PERMISSION_GRANTED;


                        if(FineLocationAccepted && CoarseLocationAccepted && Camera && ReadPhoneStorage && WritePhoneStorage ){
                            openNextActivity();
                        }else {
                            requestPermission();
                        }
                    }
                }
                break;
        }
    }




}