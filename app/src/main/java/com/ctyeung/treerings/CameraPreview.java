package com.ctyeung.treerings;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera camera = null;

    public CameraPreview(Context context){
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        //mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); - don automatically
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
        int bestfit = 0;
        Camera.Size pickedSize = sizes.get(bestfit);
        if(pickedSize!=null){
            camera.getParameters().setPreviewSize(pickedSize.width, pickedSize.height);
            //camera.setParameters(params);
        }
        camera.startPreview();
    }

    public void surfaceCreated(SurfaceHolder holder){

        try {
            camera = Camera.open();
            camera.setPreviewDisplay(mHolder);
            camera.setDisplayOrientation(90);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public boolean capture(Camera.PictureCallback jpegHandler){
        if(camera!=null){
            camera.takePicture(null, null, jpegHandler);
            return true;
        }
        else
            return false;
    }
}
