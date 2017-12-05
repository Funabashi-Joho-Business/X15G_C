package jp.ac.chiba_fjb.c.chet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.google.api.services.script.model.Operation;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jp.ac.chiba_fjb.c.chet.SubModule.BitmapUtil;
import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;

/**
 * Created by x15g009 on 2017/11/15.
 */

public class SignupMain implements Serializable{
    private static String username = "";
    private static String meil = "";
    private static String icon = "";

    public SignupMain() {
    }

    public void main(final FragmentActivity activity){
        List<Object> params = new ArrayList<>();

        params.add(this.username);
        params.add(this.meil);
        params.add(this.icon);

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
    public String getUsername(){
        return this.username;
    }
    public String getMeil(){
        return this.meil;
    }
    public String getIcon(){
        if(this.icon != null) {
            return this.icon;
        }else{
            return null;
        }
    }
    public void loadData(Activity activity, Context context){
        try {
            FileInputStream fileInputStream = activity.openFileInput("Chet.txt");
            byte[] readBytes = new byte[fileInputStream.available()];
            fileInputStream.read(readBytes);
            String readString = new String(readBytes);
            setData(readString);
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setData(String s){
        String[] a = s.split(",");
        this.username = a[0];
        this.meil = a[1];
        this.icon = a[2].toString();
    }
}
