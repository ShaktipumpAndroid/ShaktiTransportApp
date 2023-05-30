package activity;

import static utility.FileUtils.getPath;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.shaktiTransportApp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import activity.PodBean.ImageModel;
import adapter.ImageSelectionAdapter;
import database.DatabaseHelper;
import utility.CustomUtility;

public class AssignedDeliveryImages extends AppCompatActivity implements ImageSelectionAdapter.ImageSelectionListener{
    
    Toolbar mToolbar;
    RecyclerView recyclerview;
    Context mContext;
    List<ImageModel> imageArrayList = new ArrayList<>();
    List<String> itemNameList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();
    ImageSelectionAdapter LrImageAdapter;
    int selectedIndex;
    boolean isBackPressed = false, isUpdate = false;
    AlertDialog alertDialog;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PICK_FROM_FILE = 102;
    String lrno,sapbillno;
    Uri imageUri;
    Bitmap UserBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_delivery_images);


        mContext = this;
        Init();

    }

    private void Init() {

        recyclerview = findViewById(R.id.recyclerview);
        mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.AssignedDeliveryImages));
        CustomUtility.setSharedPreference(mContext, "lrImages", "");

        Bundle bundle = getIntent().getExtras();
        lrno =  bundle.getString("lrNo");
        sapbillno = bundle.getString("sapBillNo");

        SetAdapter();
        listner();
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(view -> {
            if (Save()) {
                onBackPressed();
            }
        });
    }

    private boolean Save() {

        if (!imageArrayList.get(0).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.please_lr_photo), Toast.LENGTH_SHORT).show();
        } else  if (!imageArrayList.get(1).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.please_pod_photo), Toast.LENGTH_SHORT).show();
        }else if (!imageArrayList.get(2).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.please_material_photo_front), Toast.LENGTH_SHORT).show();
        }  else if (!imageArrayList.get(3).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.please_material_photo_side), Toast.LENGTH_SHORT).show();
        }  else if (!imageArrayList.get(4).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.please_material_photo_top), Toast.LENGTH_SHORT).show();
        }   else if (!imageArrayList.get(5).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.please_material_photo_bottom), Toast.LENGTH_SHORT).show();
        }   else if (!imageArrayList.get(6).isImageSelected()) {
            Toast.makeText(this, getResources().getString(R.string.please_material_with_customer), Toast.LENGTH_SHORT).show();
        }  else {
            CustomUtility.setSharedPreference(mContext, "lrImages", "1");
            isBackPressed = true;
        }

        return isBackPressed;
    }

    private void SetAdapter() {
        imageArrayList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemNameList.add(getResources().getString(R.string.lr_photo));
        itemNameList.add(getResources().getString(R.string.material_photo_front));
        itemNameList.add(getResources().getString(R.string.material_photo_side));
        itemNameList.add(getResources().getString(R.string.material_photo_top));
        itemNameList.add(getResources().getString(R.string.material_photo_bottom));

        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setBillNo(sapbillno);
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageArrayList.add(imageModel);
        }

        DatabaseHelper db = new DatabaseHelper(this);

        imageList = db.getAssignedDeliveryImages();

        if (itemNameList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int i = 0; i < imageList.size(); i++) {
                for (int j = 0; j < itemNameList.size(); j++) {

                    if (imageList.get(i).getName().equals(itemNameList.get(j))) {
                        ImageModel imageModel = new ImageModel();
                        imageModel.setName(imageList.get(i).getName());
                        imageModel.setBillNo(imageList.get(i).getBillNo());
                        imageModel.setImagePath(imageList.get(i).getImagePath());
                        imageModel.setImageSelected(true);
                        imageArrayList.set(j, imageModel);
                    }
                }
            }
        }

        Log.e("imageList.size()","1==="+imageList.size());
        LrImageAdapter = new ImageSelectionAdapter(AssignedDeliveryImages.this, imageArrayList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(LrImageAdapter);
        LrImageAdapter.ImageSelection(this);
    }

    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (imageModelList.isImageSelected()) {
            isUpdate = true;
            selectImage("1");
        } else {
            isUpdate = false;
            selectImage("0");
        }

    }

    private void selectImage(String value) {

        Log.e("select==>", "status"+value);

        LayoutInflater inflater = (LayoutInflater)  AssignedDeliveryImages.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder( AssignedDeliveryImages.this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView camera = layout.findViewById(R.id.camera);
        TextView cancel = layout.findViewById(R.id.cancel);

        if (value.equals("0")) {
            title.setText(getResources().getString(R.string.select_image));
            gallery.setText(getResources().getString(R.string.gallery));
            camera.setText(getResources().getString(R.string.camera));

        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            camera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                openGallery();
            } else {

                Log.e("image_path==>", imageArrayList.get(selectedIndex).getImagePath());

                Intent i_display_image = new Intent( AssignedDeliveryImages.this, PhotoViewerActivity.class);
                i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
                startActivity(i_display_image);
            }
        });

        camera.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (value.equals("0")) {
                cameraIntent();
            } else {
                selectImage("0");
            }
        });

        cancel.setOnClickListener(v -> alertDialog.dismiss());
    }
    private void cameraIntent() {

        camraLauncher.launch(new Intent(AssignedDeliveryImages.this, CameraActivity2.class)
                .putExtra("cust_name", sapbillno));

    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Log.e("bundle====>", bundle.get("data").toString());
                            UpdateArrayList(bundle.get("data").toString());

                        }

                    }
                }
            });


    @SuppressWarnings("deprecation")
    public void openGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_FILE);
    }

    @Override
    public void onBackPressed() {
        Save();
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_FROM_FILE) {
            try {
                Uri mImageCaptureUri = data.getData();
                String path = getPath( AssignedDeliveryImages.this, mImageCaptureUri); // From Gallery
                Log.e("Activity", "PathHolder22= " + path);
                updatefileName(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if( requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE){

            try {
                UserBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(),
                        imageUri);

                String path = getPath(mContext, imageUri);


                if (path == null) {
                    path = data.getData().getPath(); // From File Manager
                }

                Log.e("Activity", "PathHolder22= " + path);

                updatefileName(path);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void updatefileName(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);
        String file;
        if (filename.indexOf(".") > 0) {
            file = filename.substring(0, filename.lastIndexOf("."));
        } else {
            file = "";
        }
        if (TextUtils.isEmpty(file)) {
            Toast.makeText( AssignedDeliveryImages.this, "File not valid!", Toast.LENGTH_LONG).show();
        } else {
            UpdateArrayList(path);

        }
    }

    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setBillNo(sapbillno);
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageArrayList.set(selectedIndex, imageModel);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if( isUpdate){
            db.updateLrImagesRecord(imageArrayList.get(selectedIndex).getName(), path,true, sapbillno);
        }else {
            db.insertLrImage(imageArrayList.get(selectedIndex).getName(), path,true, sapbillno);
        }
        LrImageAdapter.notifyDataSetChanged();

    }
}