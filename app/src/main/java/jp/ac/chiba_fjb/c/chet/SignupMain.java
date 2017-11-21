package jp.ac.chiba_fjb.c.chet;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.google.api.services.script.model.Operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;

/**
 * Created by x15g009 on 2017/11/15.
 */

public class SignupMain implements Serializable{
    private static String userid;
    private static String username;
    private static String meil;

    public SignupMain(String username,String meil) {
        this.userid = makeID();
        this.username = username;
        this.meil = meil;
        System.out.println(this.userid);
        System.out.println(this.username);
    }

    public void main(final FragmentActivity activity){
        List<Object> params = new ArrayList<>();

        params.add(this.userid);
        params.add(this.username);
        params.add(this.meil);

        System.out.println(this.userid);
        System.out.println(this.username);

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
//                    ArrayList<ArrayList<Object>> s = (ArrayList<ArrayList<Object>>) op.getResponse().get("result");
//                    System.out.println(s.get(0).get(1));
                 }
            }
        });
    }
    private String makeID(){
        int ran = (int)(Math.random()*1000);
        return String.valueOf(ran);
    }
}
