package com.sanyue.donghua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private OneView oneView;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oneView =findViewById(R.id.one);
        stop = findViewById(R.id.stop);
        oneView.setOnClickFillIn(new OneView.OnClickFillIn() {
            @Override
            public void click() {
                Log.e("TAGSSSSF","开始");

            }

            @Override
            public void complete() {
                Log.e("TAGSSSSF","打勾完成");

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneView.complete();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}