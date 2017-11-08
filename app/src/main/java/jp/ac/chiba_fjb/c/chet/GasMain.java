package jp.ac.chiba_fjb.c.chet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;


import com.google.api.services.script.model.Operation;

import java.util.ArrayList;
import java.util.List;

import jp.ac.chiba_fjb.c.chet.SubModule.AppFinger;
import jp.ac.chiba_fjb.c.chet.SubModule.GoogleDrive;
import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;
//import jp.ac.chiba_fjb.example.googlescript.R;

/*
//GASのソース作成後、公開から実行可能API(全員)を選ぶ
//リソース Cloud Platformプロジェクトから以下を設定
// APIとサービス
//    Google Apps Script Execution APIを有効
//    認証でAPIキーを作成

//スクリプト側(指定したフォルダを作成する)
function Main(name) {
   DriveApp.getRootFolder().createFolder(name);
   return name+"作成完了";
}
 */

public class GasMain extends AppCompatActivity {

    private GoogleScript mGoogleScript;
   private GoogleDrive mDrive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //キー登録用SHA1の出力(いらなければ消す)
        Log.d("フィンガーコード", AppFinger.getSha1(this));

//

        //Scriptで必要な権限を記述する
        final String[] SCOPES = {
                "https://www.googleapis.com/auth/drive",
                "https://www.googleapis.com/auth/script.storage",
                "https://www.googleapis.com/auth/spreadsheets"};

        mGoogleScript = new GoogleScript(this,SCOPES);
        //強制的にアカウントを切り替える場合
        mGoogleScript.resetAccount();

//        EditText userid = (EditText) findViewById(R.id.userid);
//        EditText username = (EditText) findViewById(R.id.username);
//        EditText positionN = (EditText) findViewById(R.id.positionN);
//        EditText positionE = (EditText) findViewById(R.id.positionE);
//        EditText pinN = (EditText) findViewById(R.id.pinN);
//        EditText pinE = (EditText) findViewById(R.id.pinE);
//        EditText imageurl = (EditText) findViewById(R.id.imageurl);
//        EditText parentid = (EditText) findViewById(R.id.parentid);
        EditText chettext = (EditText) findViewById(R.id.chettext);

        //送信パラメータ
        List<Object> params = new ArrayList<>();
//        params.add(userid);
//        params.add(username);
//        params.add(positionN);
//        params.add(positionE);
//        params.add(pinN);
//        params.add(pinE);
//        params.add(imageurl);
//        params.add(parentid);

        params.add("userid");
        params.add("username");
        params.add("positionN");
        params.add("positionE");
        params.add("pinN");
        params.add("pinE");
        params.add("imageurl");
        params.add("parentid");
        params.add(chettext);

        //ID,ファンクション名,結果コールバック　後ろのは受け取った管理者APIキー
        mGoogleScript.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null ,"main",
                params, new GoogleScript.ScriptListener() {
                    @Override
                    public void onExecuted(GoogleScript script, Operation op) {
                        if(op == null || op.getError() != null) {
//                            userid.append("Script結果:エラー\n");
                            System.out.println("Script結果:エラー\n");
                        }else {
                            //戻ってくる型は、スクリプト側の記述によって変わる
                            String s = (String) op.getResponse().get("result");
//                            userid.append("Script結果:" + s + "\n");
                            System.out.println("Script結果:成功\n");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //必要に応じてアカウントや権限ダイアログの表示
        mGoogleScript.onActivityResult(requestCode,resultCode,data);
    }
}
