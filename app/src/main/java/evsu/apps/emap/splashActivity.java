package evsu.apps.emap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashActivity extends AppCompatActivity {
    private ImageView iv;
    private TextView tv3,tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = (ImageView) findViewById(R.id.iv);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.transition);
        iv.startAnimation(animation);
        tv3.startAnimation (animation);
        tv1.startAnimation (animation);
        tv2.startAnimation (animation);
        final Intent i = new Intent(this,MainActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}

