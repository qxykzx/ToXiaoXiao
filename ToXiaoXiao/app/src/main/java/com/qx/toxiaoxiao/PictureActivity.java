package com.qx.toxiaoxiao;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by QX on 2018/12/1.
 */

public class PictureActivity extends Activity {
    public static final  int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private  Button openCemara;
    private  Button choosePicture;
    private ImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_picture);

        imageView = findViewById(R.id.picture_ImageView);

        //打开照相机
        openCemara = findViewById(R.id.picture_openCamera);
        openCemara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建File对象，用于存储拍照后的照片
                File outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                    return;
                }
                startActionCapture(PictureActivity.this, outputImage, TAKE_PHOTO);
            }
        });

        //选择图片
        choosePicture = findViewById(R.id.picture_choosePicture);
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePicture();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                Intent intent = new Intent("com.android.camera.cation.CROP");
                intent.setDataAndType(imageUri,"image/*");
                intent.putExtra("scale",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CROP_PHOTO); //启动剪裁程序
            }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                    }catch(FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开文件
     * 当手机中没有一个app可以打开file时会抛ActivityNotFoundException
     * @param context     activity
     * @param file        File
     * @param contentType 文件类型如：文本（text/html）
     */
    public static void startActionFile(Context context, File file, String contentType) throws ActivityNotFoundException {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(getUriForFile(context, file), contentType);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 打开相机
     *
     * @param activity    Activity
     * @param file        File
     * @param requestCode result requestCode
     */
    public static void startActionCapture(Activity activity, File file, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, file));
        activity.startActivityForResult(intent, requestCode);
    }

    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.qx.toxiaoxiao.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    //从图库中获取图片
    private void ChoosePicture(){

    }
}
