package activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.administrator.shaktiTransportApp.R;

import java.util.ArrayList;
import java.util.Locale;

import activity.languagechange.LocaleHelper;
import adapter.QuotationCustomList;
import adapter.RfqCustomList;
import bean.LoginBean;
import bean.RfqBean;
import database.DatabaseHelper;
import utility.CustomUtility;

public class AssignedRfqVendor extends AppCompatActivity {
    Context context;
    DatabaseHelper db;
    TextView create_rfq;
    ListView inst_list;
    TextView textview_rfq_id;
    String caseid_text = "";
    QuotationCustomList quotationCustomList;
    CustomUtility customUtility = new CustomUtility();
    LoginBean loginBean;
    RfqCustomList adapter;
    EditText editsearch;
    private Toolbar mToolbar;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_rfq_vendor);
        context = this;
        db = new DatabaseHelper(context);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.RFQ_List));

        ArrayList<RfqBean> quotationBeanArrayList = new ArrayList<RfqBean>();
        quotationBeanArrayList = db.getRfqList();

        adapter = new RfqCustomList(context, quotationBeanArrayList);
        inst_list = (ListView) findViewById(R.id.rfq_list);
        inst_list.setAdapter(adapter);

        inst_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textview_rfq_id = (TextView) view.findViewById(R.id.rfq_id_value);
                caseid_text = textview_rfq_id.getText().toString();
                Intent i = new Intent(context, NewQuotation.class);
                Bundle extras = new Bundle();
                extras.putString("rfq_docno", caseid_text);
                extras.putString("flag_create_rfq", "Y");
                i.putExtras(extras);
                startActivity(i);
            }
        });

        // Locate the EditText in listview_main.xml
        editsearch = (EditText) findViewById(R.id.search);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        AssignedRfqVendor.this.finish();
        Intent intent = new Intent(AssignedRfqVendor.this, AssignedRfqVendor.class);
        startActivity(intent);
        super.onRestart();
    }
}
