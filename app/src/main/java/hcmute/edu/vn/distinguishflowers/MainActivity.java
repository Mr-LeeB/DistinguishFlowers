package hcmute.edu.vn.distinguishflowers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import hcmute.edu.vn.distinguishflowers.helpers.MLImageHelperActivity;


public class MainActivity extends MLImageHelperActivity {

    Button gal, cam;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gal = findViewById(R.id.b_select_from_gal);
        cam = findViewById(R.id.b_select_from_cam);

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlowerIdentificationActivity.class);
                onStartActive = 1;
                startActivity(intent);
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlowerIdentificationActivity.class);
                startActivity(intent);
                onStartActive = 2;
            }
        });
    }

    @Override
    protected void runDetection(Bitmap bitmap) {

    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }
}