package jocelyn_test04.com.camera_test;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jocelyn_test04.com.camera_test.Camera.ImageUpload;


public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST =1;
    public static final int REQUEST_CODE = 2;
    public static final int GALLERY_REQUEST_CODE = 10;
    private final String url = "http://192.*.*.*/myphp/androidApp/uploadimage.php";

    private String imgFileLocaltion;
    private Uri photoUri;
    private ImageView ivCamera,ivGallery,ivUpload,imgView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivCamera = (ImageView) findViewById(R.id.ivCam);
        ivGallery = (ImageView) findViewById(R.id.ivGallery);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
        imgView = (ImageView) findViewById(R.id.imgView);


        ivCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                doTakePhoto();
                //cp.addToGallery();
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgView.getDrawable() == null) {
                    Toast.makeText(MainActivity.this,
                            "No image,Add image first", Toast.LENGTH_SHORT).show();

                } else {
                    Bitmap image = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                    new ImageUpload(MainActivity.this,
                            image, "_Img.jpg", url).execute();

                }
            }
        });

    }

    private void selectImage(){
        Intent selectImg = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectImg, GALLERY_REQUEST_CODE);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doTakePhoto(){
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            callTakePhotoAct();
        }else {
            if(shouldShowRequestPermissionRationale(android.Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,
                        "External storage permission saving image",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                callTakePhotoAct();
            }else{
                Toast.makeText(this,
                        "External write permission has been granted,Can not saved image",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void callTakePhotoAct(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                imgFileLocaltion = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                ex.getStackTrace();
                return;
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(MainActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
               // intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = "IMAGE_" + timeStamp+".jpg";
    /*    File directory = new File(Environment
              .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"camera");
        File file = File.createTempFile(imgName,".jpg",directory);*/
        File file = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + imgName
        );

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            rotateImage(setReducedImgSize());
        }
        else if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Uri selectImg = data.getData();
            imgView.setImageURI(selectImg);
        }
    }

    private Bitmap setReducedImgSize(){

        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFileLocaltion, bmOptions);

        int imgW = bmOptions.outWidth;
        int imgH = bmOptions.outHeight;

        int scaleFactor = Math.min(imgW/targetW, imgH/targetH);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap smallImage = BitmapFactory.decodeFile(imgFileLocaltion, bmOptions);
        //imgView.setImageBitmap(smallImage);
        return smallImage;
    }

    private void rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(imgFileLocaltion);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                break;
        }
        Bitmap rotatedImg = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
                bitmap.getHeight(),matrix,true);
        imgView.setImageBitmap(rotatedImg);

    }

}
