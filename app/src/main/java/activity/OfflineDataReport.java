package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.administrator.shaktiTransportApp.BuildConfig;
import com.administrator.shaktiTransportApp.R;

import bean.LoginBean;
import database.DatabaseHelper;
import utility.CustomUtility;
import webservice.SAPWebService;

public class OfflineDataReport extends AppCompatActivity {

    Boolean flag_offline_data = false;
    Boolean flag_back_press = false;
    ProgressDialog progressBar;
    Context context;
    Cursor cursor = null;
    SAPWebService con = null;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
    private Toolbar mToolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_report);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        con = new SAPWebService();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Offline Data");

        context = this;

        selectData();
    }

    public void selectData() {

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        // Add header row

        TableRow rowHeader = new TableRow(context);
        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"Document Type", "Document No", "Fr Location"};


        for (String c : headerText) {
            TextView tv = new TextView(context);

            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.CENTER);

            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setPadding(50, 5, 5, 5);
            tv.setTextSize(14);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        tableLayout.addView(rowHeader);

        // Get data from sqlite database and add them to the table
        // Open the database for reading
        // Create DatabaseHelper instance

        DatabaseHelper dataHelper = new DatabaseHelper(context);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        LoginBean loginBean = new LoginBean();

        String usertype = loginBean.getUsertype().trim();

        if ("Vendor".equals(usertype)) {

            try {
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

                String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_QUOTATION;

                cursor = db.rawQuery(selectQuery, null);


                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {

                        // Read columns data

                        if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_QUOTE_COMPLETED)))
                                && cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_QUOTE_COMPLETED)).equals("X")
                                && TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_SYNC)))) {

                            flag_offline_data = true;
                            String rfq_docno = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_RFQ_DOC));
//                            String from_place = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FR_LOCATION));
                            String from_place = "";
                            String type = "QUOTATION";


                            // dara rows
                            TableRow row = new TableRow(context);
                            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                            String[] colText = {type + " ", rfq_docno + " ", from_place};


                            for (String text : colText) {
                                TextView tv = new TextView(context);
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                // tv.setPadding(5, 5, 5, 5);
                                tv.setPadding(40, 30, 10, 5);
                                tv.setText(text);
                                row.addView(tv);

                            }
                            tableLayout.addView(row);


                            // draw seprator between rows
                            View line = new View(context);
                            line.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
                            line.setBackgroundColor(Color.parseColor("#8b8b8c"));
                            tableLayout.addView(line);

                        }
                    }
                }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////


                db.setTransactionSuccessful();

            } catch (SQLiteException e) {
                e.printStackTrace();

            } finally {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }
        }
        /*else if("Driver".equals(usertype)) {

        try
        {

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

            String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_VERIFICATION;

            cursor = db.rawQuery(selectQuery, null);


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    // Read columns data

                    if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_QUOTE_COMPLETED)))
                            && cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_QUOTE_COMPLETED)).equals("X")
                            && TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_SYNC)))) {

                        flag_offline_data = true;
                        String rfq_docno = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_RFQ_DOC));
//                            String from_place = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FR_LOCATION));
                        String from_place = "";
                        String type = "CUSTOMER DATA";


                        // dara rows
                        TableRow row = new TableRow(context);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

                        String[] colText = {type + " ", rfq_docno + " ", from_place};


                        for (String text : colText) {
                            TextView tv = new TextView(context);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(14);
                            // tv.setPadding(5, 5, 5, 5);
                            tv.setPadding(40, 30, 10, 5);
                            tv.setText(text);
                            row.addView(tv);

                        }
                        tableLayout.addView(row);


                        // draw seprator between rows
                        View line = new View(context);
                        line.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
                        line.setBackgroundColor(Color.parseColor("#8b8b8c"));
                        tableLayout.addView(line);

                    }
                }
            }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////



            db.setTransactionSuccessful();

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }
        finally
        {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }*/
        else {
            try {


/////////////////////////////////////////////////////////////////////////////////////////////////////////////

                String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_RFQ;

                cursor = db.rawQuery(selectQuery, null);


                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {

                        // Read columns data

                        if (!TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_RFQ_COMPLETED)))
                                && cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_RFQ_COMPLETED)).equals("X")
                                && TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_SYNC)))) {

                            flag_offline_data = true;
                            String rfq_docno = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_RFQ_DOC));
                            String from_place = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FR_LOCATION));
                            String type = "RFQ";


                            // dara rows
                            TableRow row = new TableRow(context);
                            row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));

                            String[] colText = {type + " ", rfq_docno + " ", from_place};


                            for (String text : colText) {
                                TextView tv = new TextView(context);
                                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));
                                tv.setGravity(Gravity.CENTER);
                                tv.setTextSize(14);
                                // tv.setPadding(5, 5, 5, 5);
                                tv.setPadding(40, 30, 10, 5);
                                tv.setText(text);
                                row.addView(tv);

                            }
                            tableLayout.addView(row);


                            // draw seprator between rows
                            View line = new View(context);
                            line.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
                            line.setBackgroundColor(Color.parseColor("#8b8b8c"));
                            tableLayout.addView(line);

                        }
                    }
                }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////


                db.setTransactionSuccessful();

            } catch (SQLiteException e) {
                e.printStackTrace();

            } finally {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id_temp = item.getItemId();

        switch (id_temp) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_sync_offline:

                if (flag_offline_data) {
                    sync_offline_data();
                } else {
                    Toast.makeText(getApplicationContext(), "No Offline Data available  ", Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offline, menu);
        return true;
    }

    public void sync_offline_data() {

        progressDialog = ProgressDialog.show(context, "", "Sync Offline Data......!");

        new Thread() {

            public void run() {

                try {

                    if (CustomUtility.isInternetOn()) {


                        // insert attendance data
                        SAPWebService con = new SAPWebService();

                        con.SyncOfflineDataToSap(context);
                        // select attendance data
                        progressDialog.dismiss();

                        OfflineDataReport.this.finish();

                        Intent intent = new Intent(OfflineDataReport.this, OfflineDataReport.class);
                        startActivity(intent);
                        selectData();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No internet Connection  ", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();

                }

            }

        }.start();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
