package activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.administrator.shaktiTransportApp.BuildConfig;
import com.administrator.shaktiTransportApp.R;

import database.DatabaseHelper;

public class ShowDocument extends AppCompatActivity {

    Context context;
    String string_image = "";
    String key_field = "";
    String key_ind = "";
    String key_where1 = "";
    String key_table = "";
    String key_where = "";
    String docno = "";
    byte[] encodeByte;
    Bitmap bitmap;
    DatabaseHelper db;
    ImageView imageView;
    String string_title = "";
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_document);

        context = this;

        db = new DatabaseHelper(context);
        Bundle bundle = getIntent().getExtras();
//        string_image = bundle.getString("string_image");

        key_field = bundle.getString("key_field");
        key_table = bundle.getString("key_table");
        key_where = bundle.getString("key_where");
        key_where1 = bundle.getString("key_where1");
        key_ind = bundle.getString("key_ind");
        docno = bundle.getString("docno");

        switch (key_field) {


            case DatabaseHelper.KEY_PHOTO_1:
                string_title = "Photo 1";
                break;
            case DatabaseHelper.KEY_PHOTO_2:
                string_title = "Photo 2";
                break;
            case DatabaseHelper.KEY_PHOTO_3:
                string_title = "Photo 3";
                break;
            case DatabaseHelper.KEY_PHOTO_4:
                string_title = "Photo 4";
                break;
            case DatabaseHelper.KEY_PHOTO_5:
                string_title = "Photo 5";
                break;

        }


        //Toolbar code
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(string_title);


        imageView = (ImageView) findViewById(R.id.imageView);


        string_image = db.getImage(key_table, key_where, docno, key_field, key_where1, key_ind);

        if (string_image != null && !string_image.isEmpty()) {
            encodeByte = Base64.decode(string_image, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(bitmap);
        }

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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
