package jocelyn_test04.com.camera_test.Camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import jocelyn_test04.com.camera_test.BuildConfig;


/**
 * Created by Jocelyn on 7/11/2016.
 */

/*public class CameraPicture {

    private Context c;
    private String imgFileLocaltion;
    public View view;


    public String getImgFileLocaltion() {
        return imgFileLocaltion;
    }

    public void setImgFileLocaltion(String imgFileLocaltion) {
        this.imgFileLocaltion = imgFileLocaltion;
    }

    public CameraPicture(Context c,View view) {
        this.c = c;
        this.view = view;
    }

    private void callTakePhotoAct(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(c.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.getStackTrace();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(c,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
               //ActivityCompat.startActivityForResult(intent,REQUEST);
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = "IMAGE_" + timeStamp+".jpg";
    *//*    File directory = new File(Environment
              .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"camera");
        File file = File.createTempFile(imgName,".jpg",directory);*//*
        File file = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + imgName
        );
        this.setImgFileLocaltion(file.getAbsolutePath());
        return file;
    }



    private Bitmap setReducedImgSize(View view){

        int targetW = view.getWidth();
        int targetH = view.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(this.imgFileLocaltion, bmOptions);

        int imgW = bmOptions.outWidth;
        int imgH = bmOptions.outHeight;

        int scaleFactor = Math.min(imgW/targetW, imgH/targetH);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap smallImage = BitmapFactory.decodeFile(this.imgFileLocaltion, bmOptions);
        //imgView.setImageBitmap(smallImage);
        return smallImage;
    }

    private Bitmap rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(this.imgFileLocaltion);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert exifInterface != null;
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotatedImg = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
                bitmap.getHeight(),matrix,true);

        return rotatedImg;
    }


   public void addToGallery() {
       Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
       File f = new File(this.imgFileLocaltion);
       Uri contentUri = Uri.fromFile(f);
       mediaScanIntent.setData(contentUri);
       c.sendBroadcast(mediaScanIntent);
   }
}*/
