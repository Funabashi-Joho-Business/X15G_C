package jp.ac.chiba_fjb.c.chet;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.google.api.services.script.model.Operation;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;

/**
 * Created by x15g009 on 2017/11/15.
 */

public class SignupMain implements Serializable{
    private String userid;
    private String username;

    public SignupMain(String username) {
        this.userid = makeID();
        this.username = username;
    }

    public void main(final FragmentActivity activity){
        List<Object> params = new ArrayList<>();

        params.add(userid);
        params.add(username);

        GoogleScript gas = ((MainActivity) activity).getGas();
        gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null ,"User",
                params, new GoogleScript.ScriptListener() {
                @Override
                public void onExecuted(GoogleScript script, Operation op) {
                if(op == null || op.getError() != null) {
                    System.out.println("Script結果:エラー\n");
                    if(op != null) {
                       System.out.println(op.getError());
                    }
                }else {
                 //戻ってくる型は、スクリプト側の記述によって変わる
//                     s = (ArrayList<ArrayList<Object>>) op.getResponse().get("result");
                    System.out.println("Script結果:成功\n");
                 }
            }
        });
    }
    private String makeID(){
        int ran = (int)Math.random()*1000;
        return String.valueOf(ran);
    }
}
