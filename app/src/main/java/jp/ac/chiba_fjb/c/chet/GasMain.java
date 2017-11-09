package jp.ac.chiba_fjb.c.chet;

import android.support.v4.app.FragmentActivity;
import com.google.api.services.script.model.Operation;
import java.util.ArrayList;
import java.util.List;
import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;

/**
 * Created by x15g009 on 2017/11/09.
 */

public class GasMain {
    public static String[] s;
    public void main(FragmentActivity activity){
        String chettext = new MainActivity().text;
        MainFragment mf = new MainFragment();
        List<Object> params = new ArrayList<>();

        params.add("userid");
        params.add("username");
        System.out.println(mf.p.my);
        params.add(mf.p.my);
        params.add(mf.p.my);
        params.add(mf.latitude);
        params.add(mf.longitude);
        params.add("imageurl");
        params.add("parentid");
        params.add(chettext);

        //MainActivityで作成したGASを共有して利用する
        GoogleScript gas = ((MainActivity) activity).getGas();
        //ID,ファンクション名,結果コールバック　後ろのは受け取った管理者APIキー
        gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null ,"Main",
                params, new GoogleScript.ScriptListener() {
                    @Override
                    public void onExecuted(GoogleScript script, Operation op) {
                        if(op == null || op.getError() != null) {
//                            userid.append("Script結果:エラー\n");
                            System.out.println("Script結果:エラー\n");
                            if(op != null) {
                                System.out.println(op.getError());
                            }
                           }else {
                            //戻ってくる型は、スクリプト側の記述によって変わる
                            s = (String[] )op.getResponse().get("result");
//                            userid.append("Script結果:" + s + "\n");
                            System.out.println("Script結果:成功\n");
                            System.out.println(s);
                        }
                    }
                });
    }
}
