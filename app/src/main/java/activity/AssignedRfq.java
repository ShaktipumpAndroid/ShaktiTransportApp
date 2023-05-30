package activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import adapter.RfqCustomList;
import bean.RfqBean;
import database.DatabaseHelper;

public class AssignedRfq extends AppCompatActivity {
    Context context;
    DatabaseHelper db;
    TextView create_rfq;
    ListView inst_list;
    TextView textview_rfq_id;
    String caseid_text = "";

    RfqCustomList adapter;
    EditText editsearch;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_rfq);

        context = this;

        db = new DatabaseHelper(context);


        mToolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.Quotation));

        create_rfq =  findViewById(R.id.create_rfq);

        ArrayList<RfqBean> arraylist_rfq;
        arraylist_rfq = db.getRfqList();

        adapter = new RfqCustomList(context, arraylist_rfq);
        inst_list =  findViewById(R.id.rfq_list);
        inst_list.setAdapter(adapter);


        inst_list.setOnItemClickListener((parent, view, position, id) -> {

            textview_rfq_id =  view.findViewById(R.id.rfq_id_value);
            caseid_text = textview_rfq_id.getText().toString();

            Intent i = new Intent(context, NewRfq.class);
            Bundle extras = new Bundle();
            extras.putString("rfq_docno", caseid_text);
            extras.putString("flag_create_rfq", "Y");
            i.putExtras(extras);
            startActivity(i);
        });


        create_rfq.setOnClickListener(v -> {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle("Confirmation");
            // Setting Dialog Message
            alertDialog.setMessage(" Do you want to create new Request For Quotation?");
            // On pressing Settings button
            alertDialog.setPositiveButton("Yes", (dialog, which) -> createEnquiry());
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            // Showing Alert Message
            alertDialog.show();

        });

        // Locate the EditText in listview_main.xml
        editsearch =  findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void createEnquiry() {

        Intent i = new Intent(context, NewRfq.class);
        Bundle extras = new Bundle();
        extras.putString("flag_create_rfq", "X");
        i.putExtras(extras);
        startActivity(i);
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

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {

        AssignedRfq.this.finish();
        Intent intent = new Intent(AssignedRfq.this, AssignedRfq.class);
        startActivity(intent);

        super.onRestart();
    }
}
