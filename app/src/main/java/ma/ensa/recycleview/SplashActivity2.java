package ma.ensa.recycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                    Intent intent = new Intent(SplashActivity2.this, ListActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}