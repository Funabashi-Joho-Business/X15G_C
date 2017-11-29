package jp.ac.chiba_fjb.c.chet;

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
import java.util.List;
import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;

/**
 * Created by x15g009 on 2017/11/09.
 */

public class GasMain {

    private static ArrayList<ArrayList<Object>> s;
    private final MainFragment mf = new MainFragment();
    private Handler handler;
    private Runnable runnable;
    private LinearLayout layout;


    public void main(final FragmentActivity activity, final Context context,String method){
        String chettext = new MainActivity().text;
        final List<Object> params = new ArrayList<>();

        handler = new Handler();

        if(method.equals("Main")){
            if(mf.getSheetid() != null) {
                params.add(mf.getSheetid());
            }else{
                params.add("null");
            }
            params.add("userid3");
            params.add("username");
            params.add(this.mf.p.my);
            params.add(this.mf.p.mx);
            params.add(this.mf.latitude);
            params.add(this.mf.longitude);
            params.add("imageurl");
            params.add("parentid");
            params.add(chettext);
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
                                setRunnable(context);
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
                                            //追加先のインスタンスの取得

                                            //fragment_invitation_userのレイアウトを読み込んで追加
                                            layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.fragment_invitation_user, null);   //レイアウトをその場で生成
                                            layout.setOrientation(LinearLayout.HORIZONTAL);

                                            ImageView image = new ImageView(context);
                                            TextView username = new TextView(context);

                                            //取得したレイアウトにidを設定する
                                            username.setId(i);
                                            //setImageResource(ID);
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
                                setRunnable(context);
                                handler.post(runnable);
                            }
                        }
                    });
        }
    }

    public ArrayList<ArrayList<Object>> getArray(){
        return s;
    }
    public TextView setTO(TextView tv,int i){
        tv.setText(s.get(i).get(8).toString());
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
        ll.addView(iv);
        return ll;
    }
    private void setRunnable(final Context context){
        runnable = new Runnable() {
            @Override
            public void run() {
                mf.chatbox.removeAllViews();
                int cnt = s.size();
                if(s.get(0).size() == 10) {
                    for (int i = 0; i < 3; i++) {
                        cnt--;
                        if (cnt < 0) {
                            break;
                        } else if (s.get(cnt).get(8).equals("")) {
                            i--;
                            continue;
                        }
                        LinearLayout ll = new LinearLayout(context);
                        TextView tv = new TextView(context);
                        setMyLO(ll);
                        setTO(tv, cnt);
                        ll.addView(tv);
                        mf.chatbox.addView(ll);
                    }
                }
            }
        };
    }
}
