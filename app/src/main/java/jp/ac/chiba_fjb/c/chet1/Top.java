package jp.ac.chiba_fjb.c.chet1;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Top extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent inte = new Intent(Top.this,MainActivity.class);
                startActivity(inte);
                Top.this.finish();
            }
        },2000);
    }
}
