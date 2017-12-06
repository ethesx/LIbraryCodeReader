package github.ethesx.librarycodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraView;
    TextView barcodeInfo;
    ImageView barcodeImage;

    @Override
    protected void onPause() {
        super.onPause();
        MobileVisionHelper.releaseCameraView();
    }
    @Override
    protected void onStop() {
        super.onStop();
        MobileVisionHelper.releaseCameraView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeInfo = (TextView) findViewById(R.id.code_info);
        barcodeImage = (ImageView) findViewById(R.id.imageView);

        barcodeInfo.setText(this.getString(R.string.inital_display));

        MobileVisionHelper.init(this, cameraView, new CodeListener() {
            @Override
            public void onDetected(final String data) {
                barcodeInfo.post(new Runnable() {
                    @Override
                    public void run() {
                        //Update the display to show code being looked up
                        barcodeInfo.setText(getApplicationContext().getString(R.string.check_display, data));
                        MobileVisionHelper.stopCameraView(MainActivity.this);
                        startActivity(createResultsIntent(data));
                    }
                });


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobileVisionHelper.releaseCameraView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private Intent createResultsIntent(String data){
        Intent intent = new Intent(MainActivity.this, ViewResultsActivity.class);
        intent.putExtra("isbn", data);
        return intent;
    }
}

