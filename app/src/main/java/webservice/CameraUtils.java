package webservice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import com.administrator.shaktiTransportApp.BuildConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraUtils {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";
    public static final String GALLERY_DIRECTORY_NAME = "Solar Photo";
    public static final String DELIVERED_DIRECTORY_NAME = "Delivered_photo";
    protected static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public static boolean checkPermissions(Context context) {
        int permissionCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionStorageRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionStorageRead != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */
    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();
        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Checks whether device has camera or not. This method not necessary if
     * android:required="true" is used in manifest file
     */
    public static boolean isDeviceSupportCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Open device app settings to allow user to enable permissions
     */
    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * Creates and returns the image or video file before opening the camera
     */
    public static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(GALLERY_DIRECTORY_NAME, "Oops! Failed create "
                        + GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + "." + IMAGE_EXTENSION);
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + "." + VIDEO_EXTENSION);
        } else {
            return null;
        }
        return mediaFile;
    }

    public static File getDeliveredOutputMediaFile(int type, String rfqNo, String keyimage) {
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),DELIVERED_DIRECTORY_NAME);
//        File dir = new File(root.getAbsolutePath() + "/SHAKTI/DELIVERED/" + rfqNo); //it is my root directory
        File billno = new File(root.getAbsolutePath() + "/SHAKTI/DELIVERED/" + rfqNo); // it is my sub folder directory .. it can vary..
        try {
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
            if (!billno.exists()) {
                billno.mkdirs();
                if (!billno.mkdirs()) {
                    Log.e(rfqNo, "Oops! Failed create " + rfqNo + " directory");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File mediaFile;
        Bitmap bitmap = null;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(billno.getPath() + File.separator + "IMG_" + keyimage + "." + IMAGE_EXTENSION);
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(mediaFile);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(billno.getPath() + File.separator
                    + "VID_" /*+ timeStamp + "_"*/ + keyimage + "." + VIDEO_EXTENSION);
        } else {
            return null;
        }
        return mediaFile;
    }

    public static File getOutputMediaFile(int type, String enq_docno, String keyimage) {
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);
        File dir = new File(root.getAbsolutePath() + "/SKTR/BLT/" + enq_docno); //it is my root directory
        File billno = new File(root.getAbsolutePath() + "/SKTR/BLT/" + enq_docno); // it is my sub folder directory .. it can vary..
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
                if (!billno.mkdirs()) {
                    Log.e(enq_docno, "Oops! Failed create "
                            + enq_docno + " directory");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // adds timestamp
        //String timeStamp = newworkorder SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(newworkorder Date());
        File mediaFile;
        Bitmap bitmap = null;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(billno.getPath() + File.separator + "IMG_" /*+ timeStamp + "_"*/ + keyimage + "." + IMAGE_EXTENSION);
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(mediaFile);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(billno.getPath() + File.separator
                    + "VID_" /*+ timeStamp + "_"*/ + keyimage + "." + VIDEO_EXTENSION);
        } else {
            return null;
        }
        return mediaFile;
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }
}
