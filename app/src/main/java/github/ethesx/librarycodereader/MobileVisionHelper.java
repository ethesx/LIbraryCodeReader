package github.ethesx.librarycodereader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MobileVisionHelper {

    private static BarcodeDetector barcodeDetector;
    private static CameraSource cameraSource;

    public static void init(final Context context, final SurfaceView cameraView, final CodeListener codeListener) {
        barcodeDetector =
                new BarcodeDetector.Builder(context)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)//Barcode.ISBN)
                        .build();

        cameraSource = new CameraSource
                .Builder(context, barcodeDetector)
                .setRequestedPreviewSize(800, 800)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder()
                .addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        startCameraView(context, cameraSource, cameraView);
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                        startCameraView(context, cameraSource, cameraView);
                    }
                });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    System.out.println("Value : " + barcodes.valueAt(0).displayValue);
                    codeListener.onDetected(barcodes.valueAt(0).displayValue);
                }
            }
        });

    }

    private static void startCameraView(Context context, CameraSource cameraSource, SurfaceView
            cameraView) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(cameraView.getHolder());
            } else {
                System.out.println("Permission not granted!");
            }
        } catch (IOException ie) {
            Log.e("CAMERA SOURCE", ie.getMessage());
        }

    }

    public static void stopCameraView(Activity activity) {

        if (cameraSource != null){
            cameraSource.takePicture(new CameraSource.ShutterCallback() {
                @Override
                public void onShutter() {
                    //Do nothing - don't care about the shutter timing
                }
            }, new CameraSource.PictureCallback() {
                //When the jpeg byte array is returned (async) populate the imageView and show,
                //then hide the surfaceView

                @Override
                public void onPictureTaken(final byte[] bytes) {
                    final Drawable jpeg = Drawable.createFromStream(new ByteArrayInputStream(bytes), null);

                    activity.findViewById(R.id.code_info).post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView barcodeImage = (ImageView)activity.findViewById(R.id.imageView);
                            SurfaceView surfaceView = (SurfaceView)activity.findViewById(R.id.camera_view);

                            barcodeImage.setImageDrawable(jpeg);
                            barcodeImage.setVisibility(View.VISIBLE);
                            surfaceView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });

            cameraSource.stop();
        }

    }

    /*public static void releaseCameraView(){
        if(cameraSource != null)
            cameraSource.release();
    }*/
}

