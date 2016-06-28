package io.github.touyou.shooting_unkown_enemy;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by touyou on 2016/01/02.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private static final String TAG = "CameraFaceDetect";

    public CameraView(Context context) {
        super(context);
        // サーフェスホルダーの取得とコールバック通知先の指定
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        // SurfaceViewの種別をプッシュバッファーに
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int w, int h) {
        camera.stopPreview();
        Camera.Parameters params = camera.getParameters();
        params.setRotation(90);
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        final Camera.Size optimalSize = getOptimalPreviewSize(sizes,w,h);
        params.setPreviewSize(optimalSize.width, optimalSize.height);
        if (params.isVideoStabilizationSupported()) {
            params.setVideoStabilization(true);
        }
        if (params.getMaxNumDetectedFaces() > 0) {
            Log.d(TAG, "can detect faces");
        } else {
            Log.d(TAG, "cannot detect faces");
        }
        params.setFocusMode(params.FOCUS_MODE_CONTINUOUS_VIDEO);
        camera.setParameters(params);

        camera.startPreview();

            /* 顔認識したかった丸
            camera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
                @Override
                public void onFaceDetection(Camera.Face[] faces, Camera camera) {
                    Log.d(TAG, "faces count: " + faces.length);
                    actionView = new ActionView(SUEMainActivity.this);
                    actionView.faces = faces;
                    actionView.optSize = optimalSize;
                    myRelativeLayout.addView(actionView);
                    for (Face face : faces) {
                        Log.d(TAG, "face id: " + face.id);
                        Log.d(TAG, "face score: " + face.score);    // 信頼度
                        Log.d(TAG, "face rect: " + face.rect.left
                                + "," + face.rect.top + "-" + face.rect.right
                                + "," + face.rect.bottom); // 位置
                    }
                }
            });
            try {
                camera.startFaceDetection();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } */
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;
        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio-targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
