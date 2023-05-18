package activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.administrator.shaktiTransportApp.BuildConfig;
import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;

import adapter.RouteCustomList;
import bean.RouteBean;
import database.DatabaseHelper;


public class RouteList extends AppCompatActivity {


    Context context;
    ListView route_list;
    RouteCustomList adapter_route;
    TextView cr_new_route;
    TextView textview_from_loc;
    TextView textview_to_loc;
    String from_loc_value = "";
    String to_loc_value = "";
    DatabaseHelper db;
    EditText editsearch;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        context = this;

        db = new DatabaseHelper(context);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Route List");

        cr_new_route = (TextView) findViewById(R.id.cr_route);


        ArrayList<RouteBean> arraylist_route = new ArrayList<RouteBean>();

        arraylist_route = db.getRouteList(db.KEY_APPROVED_ROUTE, "null", "null");

        adapter_route = new RouteCustomList(RouteList.this, arraylist_route);
        route_list = (ListView) findViewById(R.id.route_list);
        route_list.setAdapter(adapter_route);

        cr_new_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                // Setting Dialog Title
                alertDialog.setTitle("Confirmation");
                // Setting Dialog Message
                alertDialog.setMessage(" Do you want to create a new route?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(RouteList.this, NewRoute.class);
                        Bundle extras = new Bundle();
                        extras.putString("flag_display", "Y");
                        i.putExtras(extras);
                        startActivity(i);
                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }
        });


        route_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                textview_from_loc = (TextView) view.findViewById(R.id.fr_loc_value);
                textview_to_loc = (TextView) view.findViewById(R.id.to_loc_value);
                from_loc_value = textview_from_loc.getText().toString();
                to_loc_value = textview_to_loc.getText().toString();


                // Launching new Activity on selecting single List Item
                Intent i = new Intent(RouteList.this, NewRoute.class);//
                // sending data to new activity
                i.putExtra("flag_display", "X");
                i.putExtra("from_loc", from_loc_value);
                i.putExtra("to_loc", to_loc_value);
                startActivity(i);

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_signout:
                return true;
            case R.id.action_sync_offline:
//                Intent i = new Intent(AssignedSurvey.this, RejectedSurvey.class);
//                Bundle extras = new Bundle();
//                i.putExtras(extras);
//                startActivity(i);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_offline, menu);
//        return true;
//    }

    @Override
    protected void onRestart() {

        RouteList.this.finish();
        Intent intent = new Intent(RouteList.this, RouteList.class);
        startActivity(intent);
        super.onRestart();
    }

}

