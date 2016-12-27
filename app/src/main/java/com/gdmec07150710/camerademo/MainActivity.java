package com.gdmec07150710.camerademo;

import android.graphics.Camera;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private ImageView imageView;
    private File file;
    private Camera camera;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView mSurfaceView=(SurfaceView)this.findViewById(R.id.surfaceView1);
        SurfaceHolder mSurfaceHolder=mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
    }
    public  void takePhoto(View v){
        camera.takePicture(null,null,pictureCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera=Camera.open();
        android.hardware.Camera.Parameters params=camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    try{
        camera.setPreviewDisplay(holder);
    }catch (IOException exception){
        camera.relase();
        camera=null;
    }
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    Camera.PictureCallback pictureCallback=new Camera.PictureCallback(){
        public void onPictureTaken(byte[] date,Camera camera){
            if (date!=null){
                savePicture(date);
            }
        }
    };
    public void savePicture(byte[] date){
        try{
            String imageId=System.currentTimeMillis()+"";
            String pathName=android.os.Environment.getExternalStorageDirectory().getPath()+"/";
            File file=new File(pathName);
            if (!file.exists()){
                file.mkdirs();

            }
            pathName+=imageId+".jpeg";
            if (!file.exists()){
                file.createNewFile();

            }
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(date);
            fos.close();
            Toast.makeText(this,"已存在路径:"+pathName,Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
