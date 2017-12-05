package jp.ac.chiba_fjb.c.chet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;
import jp.ac.chiba_fjb.c.chet.SubModule.Permission;


public class MainActivity extends AppCompatActivity{
    private GoogleScript mGoogleScript;
    private Activity activity;
    private Context context;
    Permission mPermission;
    public static String text = "";
    public static Matcher m;
    public static Intent intent;
    public GoogleScript getGas(){return mGoogleScript;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);

        activity = this.activity;
        context = this.context;

        intent = getIntent();
        if(intent != null && intent.getDataString() != null){
            Pattern p = Pattern.compile("id=(.*)");
            m = p.matcher(intent.getDataString());
            if(m.find())
                new MainFragment().setSheetid(m.group(1));
        }

        //Android6.0以降のパーミッション処理
        mPermission = new Permission();
        mPermission.setOnResultListener(new Permission.ResultListener() {
            @Override
            public void onResult() {
                //GoogleAppsScript初期化処理
                //Scriptで必要な権限を記述する
                final String[] SCOPES = {
                        "https://www.googleapis.com/auth/drive",
                        "https://www.googleapis.com/auth/drive.file",
                        "https://www.googleapis.com/auth/spreadsheets",
                        "https://www.googleapis.com/auth/script.send_mail"};
                mGoogleScript = new GoogleScript(MainActivity.this,SCOPES);
//                mGoogleScript.resetAccount();
                //ランタイムパーミッションの許可が下りた後の処理
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.activity_main);

                        boolean flg = false;
                        File file = new File("/data/data/jp.ac.chiba_fjb.c.chet/files");
                        String s[] = file.list();
                        if(s != null) {
                            for (String a : s) {
                                if (a.equals("Chet.txt")) {
                                    flg = true;
                                }
                            }
                        }
                        System.out.println(flg);
                        if(flg) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.maindisplay, new MainFragment());
                            ft.commit();
                        }else{
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.maindisplay, new SignupFragment());
                            ft.commit();
                        }
                    }
                },2000);
            }
        });
        mPermission.requestPermissions(this);

//        Log.d("フィンガーコード", AppFinger.getSha1(this));
        //強制的にアカウントを切り替える場合
//        mGoogleScript.resetAccount();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //必要に応じてアカウントや権限ダイアログの表示
        mGoogleScript.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }
    public void onBackPressed() {
        //フラグメントをさかのぼる処理
        int backStackCnt = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCnt != 0) {
            getSupportFragmentManager().popBackStack();
        }
        else
            super.onBackPressed();
    }
}
