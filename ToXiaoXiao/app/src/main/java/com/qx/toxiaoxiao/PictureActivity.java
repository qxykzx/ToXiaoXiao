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
    private static Uri imageUri;
    private static Uri imageOutputUri;

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

                //创建File对象，用于存储拍照后的照片
                File cropOutputImage = new File(Environment.getExternalStorageDirectory(), "tempCropImage.jpg");
                try{
                    if(cropOutputImage.exists()){
                        cropOutputImage.delete();
                    }
                    cropOutputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                    return;
                }
                imageOutputUri = Uri.fromFile(cropOutputImage);
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
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//重要的，，，
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    }
                    intent.setAction("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");// mUri是已经选择的图片Uri
                    // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
                    //intent.putExtra("crop", "true");
                    // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
                    //intent.putExtra("aspectX", 1);// 裁剪框比例
                    //intent.putExtra("aspectY", 1);
                    // outputX outputY 是裁剪图片宽高
                    intent.putExtra("outputX", 150);// 输出图片大小
                    intent.putExtra("outputY", 150);
                    //裁剪时是否保留图片的比例，这里的比例是1:1
                    intent.putExtra("scale", true);
                    //是否是圆形裁剪区域，设置了也不一定有效
                    //intent.putExtra("circleCrop", true);
                    //设置输出的格式
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    //是否将数据保留在Bitmap中返回
                    intent.putExtra("return-data", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageOutputUri);
                    startActivityForResult(intent, CROP_PHOTO);

            }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        imageView.setBackgroundResource(0);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageOutputUri));
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
        imageUri = uri;
        return uri;
    }

    //从图库中获取图片
    private void ChoosePicture(){

    }
}
