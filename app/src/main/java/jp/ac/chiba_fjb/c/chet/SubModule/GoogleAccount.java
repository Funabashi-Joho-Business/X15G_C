package jp.ac.chiba_fjb.c.chet.SubModule;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.ExponentialBackOff;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by oikawa on 2016/10/11.
 */

public class GoogleAccount {
    private static final int REQUEST_ACCOUNT_PICKER = 1234;
    private static final int REQUEST_AUTHORIZATION = 1235;
    private static final String EXTRA_NAME = "SCRIPT_INFO";
    private static final String PREF_ACCOUNT_NAME = "ScriptUser";

    private Context mContext;
    private GoogleAccountCredential mCredential;
    private String mAccountName;
    private static final String[] SCOPES = {
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/drive.file",
            "https://www.googleapis.com/auth/spreadsheets",
            "https://www.googleapis.com/auth/script.send_mail"};

    public GoogleAccount(Context con, String[] scope) {
        //Activityの保存
        mContext = con;
        //認証用クラスの生成
        mCredential = GoogleAccountCredential.usingOAuth2(
                con, Arrays.asList(scope==null?SCOPES:scope))
                .setBackOff(new ExponentialBackOff());
        //登録済みアカウント名を取得
        mAccountName = mContext.getSharedPreferences("GOOGLE",Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
        if(mAccountName!=null){
            Account account = new Account(mAccountName,"com.google");
            mCredential.setSelectedAccount(account);
        }
        System.out.println(mAccountName);
    }
    public GoogleAccountCredential getCredential(){
        return mCredential;
    }
    public String getAccount(){
        return mAccountName;
    }
    public void resetAccount(){
        //登録アカウントの解除
        SharedPreferences settings =
                mContext.getSharedPreferences("GOOGLE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, null);
        editor.apply();
        mAccountName = null;
        mCredential.setSelectedAccount(null);
    }
    public void requestAccount(){
        //ユーザ選択
        if(mCredential.getSelectedAccountName()==null && mContext instanceof Activity)
            ((Activity)mContext).startActivityForResult(mCredential.newChooseAccountIntent(),REQUEST_ACCOUNT_PICKER);
        else
            call();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_ACCOUNT_PICKER) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                mAccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                if (mAccountName != null) {
                    Account account = new Account(mAccountName,"com.google");
                    //アカウント選択確定
                    mCredential.setSelectedAccount(account);
                    SharedPreferences settings =
                            mContext.getSharedPreferences("GOOGLE",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(PREF_ACCOUNT_NAME, mAccountName);
                    editor.apply();
                }else{
                    requestAccount();
                }

                call();		//実行要求
            }
            else

                onError();	//実行不能時の
            // 処理
        }
        else if(requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode == Activity.RESULT_OK)
                call();			//実行要求
            else
                onError();	//実行不能時の処理
        }
    }
    public void call(){
        new Thread() {
            @Override
            public void run() {
                try {
                    onExec();
                }
                catch (Exception e) {
                    if (e instanceof UserRecoverableAuthIOException) {
                        //権限要求の呼び出し
                        if(mContext instanceof Activity) {
                            Intent intent = ((UserRecoverableAuthIOException) e).getIntent();
                            ((Activity) mContext).startActivityForResult(intent, REQUEST_AUTHORIZATION);
                        }

                    } else if (e instanceof IllegalArgumentException) {
                        //アカウント要求
                        requestAccount();
                    } else if (e instanceof GoogleJsonResponseException) {
                        onError();
                        e.printStackTrace();

                    }else if (e instanceof GoogleAuthIOException) {
                        //登録系エラー
                        onError();
                        e.printStackTrace();
                    }else {

                    }
                }
            }
        }.start();
    }
    protected void onExec() throws IOException {

    }
    protected void onError(){

    }
}
