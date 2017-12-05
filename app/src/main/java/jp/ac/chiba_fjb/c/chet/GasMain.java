package jp.ac.chiba_fjb.c.chet;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.api.services.script.model.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jp.ac.chiba_fjb.c.chet.SubModule.DataStorage;
import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;

/**
 * Created by x15g009 on 2017/11/09.
 */

public class GasMain {

    private static ArrayList<ArrayList<Object>> s;
    private static SignupMain sm;
    private static MainFragment mf;
    private Handler handler;
    private Runnable runnable;
    private LinearLayout layout;


    public void main(final FragmentActivity activity, final Context context,String method){
        String chettext = new MainActivity().text;
        final List<Object> params = new ArrayList<>();
        mf = new MainFragment();
        sm = new SignupMain();

        handler = new Handler();

        if(method.equals("Main")){
            sm.loadData(activity,context);
            if(mf.getSheetid() != null) {
                params.add(mf.getSheetid());
            }else{
                params.add("null");
            }
            params.add(sm.getUsername());
            params.add(mf.p.my);
            params.add(mf.p.mx);
            params.add(mf.latitude);
            params.add(mf.longitude);
            params.add("aaaa");
            params.add(sm.getMeil());
            params.add(chettext);
            new MainActivity().text = "";
        }else if(method.equals("MailUser")){
                params.add(new InvitationFragment().mailuser);
        }else if(method.equals("Return")){
            if(mf.getSheetid() != null) {
                params.add(mf.getSheetid());
            }else{
                params.add("null");
            }
        }

        //MainActivityで作成したGASを共有して利用する
        GoogleScript gas = ((MainActivity) activity).getGas();
        //ID,ファンクション名,結果コールバック　後ろのは受け取った管理者APIキー
        if(method.equals("Main")) {
            gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null, "Main",
                        params, new GoogleScript.ScriptListener() {
                        @Override
                        public void onExecuted(GoogleScript script, Operation op) {
                            if (op == null || op.getError() != null) {
                                System.out.println("Main Script結果:エラー\n");
                                if (op != null) {
                                    System.out.println(op.getError());
                                }
                            } else {
                                //戻ってくる型は、スクリプト側の記述によって変わる
                                s = (ArrayList<ArrayList<Object>>) op.getResponse().get("result");
                                System.out.println("Main Script結果:成功\n");
                                setRunnable(activity,context);
                                handler.post(runnable);
                            }
                        }
                    });
        }else if(method.equals("GetUser")){
            gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null, "GetUser",
                    null, new GoogleScript.ScriptListener() {
                        @Override
                        public void onExecuted(GoogleScript script, Operation op) {
                            if (op == null || op.getError() != null) {
                                System.out.println("GetUser Script結果:エラー\n");
                                if (op != null) {
                                    System.out.println(op.getError());
                                }
                            } else {
                                //戻ってくる型は、スクリプト側の記述によって変わる
                                s = (ArrayList<ArrayList<Object>>) op.getResponse().get("result");
                                System.out.println("Script結果:GetUser成功\n");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<String> usertext = new ArrayList<>();
                                        new InvitationFragment().user = new HashMap<String, String>();
                                        for (int i = 0; i < s.size(); i++) {
                                            usertext.add(s.get(i).get(1).toString());
                                            new InvitationFragment().user.put(s.get(i).get(1).toString(),s.get(i).get(2).toString());
                                        }
                                        for (int i = 0; i < usertext.size(); i++) {
                                            layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.fragment_invitation_user, null);   //レイアウトをその場で生成
                                            layout.setOrientation(LinearLayout.HORIZONTAL);

                                            ImageView image = new ImageView(context);
                                            TextView username = new TextView(context);

                                            username.setId(i);
                                            username.setText(usertext.get(i));
                                            username.setTextSize(24);
                                            username.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    TextView tv = view.findViewById(view.getId());
                                                    int index = new InvitationFragment().mailuser.indexOf(new InvitationFragment().user.get(tv.getText().toString()).toString());
                                                    if(index == -1) {
                                                        tv.setAlpha(0.5f);
                                                        System.out.println(tv.getText().toString()+"in");
                                                        new InvitationFragment().mailuser.add(new InvitationFragment().user.get(tv.getText().toString()).toString());
                                                    }else{
                                                        tv.setAlpha(1f);
                                                        System.out.println(tv.getText().toString()+"out");
                                                        new InvitationFragment().mailuser.remove(new InvitationFragment().mailuser.indexOf(new InvitationFragment().user.get(tv.getText().toString()).toString()));
                                                    }
                                                }
                                            });

                                            layout.addView(image);
                                            layout.addView(username);
                                            new InvitationFragment().Veritcal.addView(layout);                                                         //Viewの追加
                                        }
                                    }
                                });
                            }
                        }
                    });
        }else if(method.equals("MailUser")) {
            gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null ,"MailUser",
                    params, new GoogleScript.ScriptListener() {
                        @Override
                        public void onExecuted(GoogleScript script, Operation op) {
                            if(op == null || op.getError() != null) {
                                System.out.println("MailUserScript結果:エラー\n");
                                if(op != null) {
                                    System.out.println(op.getError());
                                }
                            }else {
                                System.out.println("MailUserScript結果:成功\n");
                            }
                        }
                    });
        } else {
            gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null ,"Return",
                    params, new GoogleScript.ScriptListener() {
                        @Override
                        public void onExecuted(GoogleScript script, Operation op) {
                            if(op == null || op.getError() != null) {
                                System.out.println("ReturnScript結果:エラー\n");
                                if(op != null) {
                                    System.out.println(op.getError());
                                }
                            }else {
                                //戻ってくる型は、スクリプト側の記述によって変わる
                                s = (ArrayList<ArrayList<Object>>) op.getResponse().get("result");
                                System.out.println("ReturnScript結果:成功"+params.get(0)+"\n");
                                setRunnable(activity,context);
                                handler.post(runnable);
                            }
                        }
                    });
        }
    }
    public ArrayList<ArrayList<Object>> getArray(){
        if(s == null){
            return null;
        }else if(s.get(0).get(0).toString().isEmpty()) {
            return null;
        }else{
            return s;
        }
    }
    public  ArrayList<String> getAllUser(Context context){
        if(getArray() != null) {
            ArrayList<ArrayList<Object>> aao = getArray();
            ArrayList<String> array = new ArrayList<>();
            for (int index = 0; index < aao.size(); index++) {
                if (!(aao.get(index).get(0).equals(sm.getUsername()))) {
                    array.add(aao.get(index).get(0).toString());
                }
            }

            array = new ArrayList<String>(new HashSet<>(array));
            for (int i = 0; i < array.size(); i++) {
                System.out.println("array.get(" + i + ")=" + array.get(i));
            }

            return array;
        }
        return null;
    }
    public TextView setTO(TextView tv,int i){
        tv.setText(s.get(i).get(7).toString());
        tv.setBackgroundResource(R.drawable.frame_style_roundness_blue);
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        return tv;
    }
    public LinearLayout setMyLO(LinearLayout ll){
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.RIGHT);
        ll.setPadding(10,10,10,10);
        return ll;
    }
    public LinearLayout setYourLO(LinearLayout ll,Context context){
        ImageView iv = new ImageView(context);
        iv.setImageURI(null); //((URL)s.get(index).get(6)
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(10,10,10,10);
        ll.addView(iv);
        return ll;
    }
    private void setRunnable(final Activity activity,final Context context){
        runnable = new Runnable() {
            @Override
            public void run() {
                MainFragment mf = new MainFragment();
                if(!(s.get(0).get(0).toString().isEmpty())) {
                    LinearLayout[] llbox = new LinearLayout[3];
                    ArrayList<String> user = getAllUser(context);
                    mf.chatbox.removeAllViews();
                    int cnt = s.size();
                    if (s.get(0).size() == 9) {
                        for (int i = 0; i < 3; i++) {
                            cnt--;
                            if (cnt < 0) {
                                break;
                            } else if (s.get(cnt).get(7).equals("")) {
                                i--;
                                continue;
                            } else if (user.contains(s.get(cnt).get(0))) {
                                LinearLayout ll = new LinearLayout(context);
                                TextView tv = new TextView(context);
                                setYourLO(ll, context);
                                setTO(tv, cnt);
                                ll.addView(tv);
                                llbox[i] = ll;
                            } else {
                                LinearLayout ll = new LinearLayout(context);
                                TextView tv = new TextView(context);
                                setMyLO(ll);
                                setTO(tv, cnt);
                                ll.addView(tv);
                                llbox[i] = ll;
                            }
                        }

                        for (int i = 2; i >= 0; i--) {
                            if(llbox[i] != null) {
                                mf.chatbox.addView(llbox[i]);
                            }
                        }
                    }
                }
            }
        };

    }
}
