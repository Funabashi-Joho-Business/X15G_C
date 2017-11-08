package jp.ac.chiba_fjb.c.chet1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity{
    Permission mPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermission = new Permission();
        mPermission.setOnResultListener(new Permission.ResultListener() {
            @Override
            public void onResult() {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new MainFragment());
                ft.commit();
            }
        });
        mPermission.requestPermissions(this);
        Log.d("フィンガーコード",AppFinger.getSha1(this));


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }
}
