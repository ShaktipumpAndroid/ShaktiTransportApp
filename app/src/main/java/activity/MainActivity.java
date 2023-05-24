package activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.shaktiTransportApp.R;

import java.io.File;

import activity.Pod.ActivityPODSearchInfo;
import activity.languagechange.LocaleHelper;
import bean.LoginBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.SAPWebService;
import webservice.WebURL;

@SuppressWarnings("StatementWithEmptyBody")
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    ProgressDialog progressBar;
    String usertype;
    SAPWebService con = null;
    Context context;
    DatabaseHelper db;
    String login_flag = null;
    String versionName = "0.0";
    String newVersion = "0.0";
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
        }
    };
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        con = new SAPWebService();
        Bundle bundle = getIntent().getExtras();
        login_flag = bundle.getString("login_flag");
        LoginBean loginBean = new LoginBean();
        usertype = LoginBean.getUsertype();

        db = new DatabaseHelper(context);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        versionName = WebURL.ANDROID_APP_VERSION;

        TextView tv = (TextView) findViewById(R.id.ename);
        tv.setText( getResources().getString(R.string.Welcome)+" " + LoginBean.getUsername() + "   V " + versionName);

        // display the first navigation drawer view on app launch
        displayView(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if ("Driver".equals(usertype)) {
            if (id == R.id.action_signout) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                // Setting Dialog Title
                alertDialog.setTitle(getResources().getString(R.string.Confirmation));
                // Setting Dialog Message
                alertDialog.setMessage(getResources().getString(R.string.signout));
                // On pressing Settings button
                alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

                return true;
            }
        } else {
            switch (id) {
                case R.id.action_sync_state:
                    // sync all data
                    download();
                    return true;

                case R.id.action_pod_state:
                    // sync all data
                    Intent mIntent = new Intent(MainActivity.this, ActivityPODSearchInfo.class);
                    startActivity(mIntent);
                    return true;

                case R.id.action_signout:

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    // Setting Dialog Title
                    alertDialog.setTitle(getResources().getString(R.string.Confirmation));
                    // Setting Dialog Message
                    alertDialog.setMessage(getResources().getString(R.string.signout));
                    // On pressing Settings button
                    alertDialog.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> logout());
                    // on pressing cancel button
                    alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                Intent intent = new Intent(MainActivity.this, OfflineDataReport.class);
                startActivity(intent);
                break;
//            case 2:
////                Intent intentNewDeliveryRequestActivity = new Intent(MainActivity.this, AssignedDeliveryListActivity.class);
//                Intent intentNewDeliveryRequestActivity = new Intent(MainActivity.this, AddDeliveryBoyFormActivity.class);
//                startActivity(intentNewDeliveryRequestActivity);
//                break;
//            case 3:
//
//                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setIcon(R.drawable.new_logo);
        }
    }

    public void OnBackPressed() {
        System.exit(0);
    }

    public void download() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.Downloading));
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        fileSize = 0;

        new Thread(new Runnable() {
            public void run() {

                if (CustomUtility.isInternetOn()) {
                    while (progressBarStatus < 100) {
                        // performing operation

                        try {
                            db.deleteTableData(DatabaseHelper.TABLE_RFQ);
                            progressBarStatus = con.getRfqData(MainActivity.this);

                            progressBarStatus = 60;

                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });

                            if ("Vendor".equals(usertype)) {
                                progressBarStatus = con.getQuotationData(MainActivity.this);

                                progressBarStatus = 90;
                                // Updating the progress bar
                                progressBarHandler.post(new Runnable() {
                                    public void run() {
                                        progressBar.setProgress(progressBarStatus);
                                    }
                                });
                            }
                            progressBarStatus = 100;
                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (progressBarStatus >= 100) {
                        // sleeping for 1 second after operation completed
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // close the progress bar dialog
                        progressBar.dismiss();
                    }
                } else {
                    progressBar.dismiss();
                    Message msg = new Message();
                    msg.obj = getResources().getString(R.string.No_Internet);
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void syncState() {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage(getResources().getString(R.string.State_Data));
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        fileSize = 0;

        new Thread(new Runnable() {
            public void run() {
                if (CustomUtility.isInternetOn()) {
                    while (progressBarStatus < 100) {
                        // performing operation
                        try {
                            progressBarStatus = 30;
                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });

                            // Get State search help Data
                            progressBarStatus = con.getStateData(MainActivity.this);
                            progressBarStatus = 100;

                            // Updating the progress bar
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (progressBarStatus >= 100) {
                        // sleeping for 1 second after operation completed
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // close the progress bar dialog
                        progressBar.dismiss();
                    }
                } else {
                    progressBar.dismiss();
                    Message msg = new Message();
                    msg.obj = "No Internet Connection";
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void logout() {
        db.deleteTableData(DatabaseHelper.TABLE_LOGIN);
//      db.deleteTableData(DatabaseHelper.TABLE_SYNC_DAILY);
        if (usertype.equalsIgnoreCase("Vendor")) {
            db.deleteTableData(DatabaseHelper.TABLE_QUOTATION);
        } else {
            db.deleteTableData(DatabaseHelper.TABLE_RFQ);
        }
        CustomUtility.setSharedPreference(context, "capture", "0");
        String selectedFilePath = "/storage/emulated/0/signdemo/signature.jpg";
        File file = new File(selectedFilePath);
        boolean deleted = file.delete();
        OnBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CustomUtility.isInternetOn()) {
            if ("Driver".equals(usertype)) {

            } else {
                download();
            }

        } else {
            Toast.makeText(getApplicationContext(),  getResources().getString(R.string.No_Internet), Toast.LENGTH_SHORT).show();
        }
    }
}
