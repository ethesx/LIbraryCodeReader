package github.ethesx.librarycodereader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraView;
    TextView barcodeInfo;
    ImageView barcodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeInfo = (TextView) findViewById(R.id.code_info);
        barcodeImage = (ImageView) findViewById(R.id.imageView);

        MobileVisionHelper.init(this, cameraView, new CodeListener() {
            @Override
            public void onDetected(final String data) {
                barcodeInfo.post(new Runnable() {
                    @Override
                    public void run() {
                        barcodeInfo.setText(data);
                        MobileVisionHelper.stopCameraView(barcodeImage, cameraView);
                        try {
                            NetworkService.lookupISBN(data, getApplicationContext(), barcodeInfo);
                        } catch(IOException e) {
                            barcodeInfo.setText(e.toString());
                        }
                    }
                });


            }
        });

    }

    /*@Override
    protected void onPause() {
        super.onPause();
        MobileVisionHelper.releaseCameraView();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        MobileVisionHelper.releaseCameraView();
    }*/
}

