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


    public void main(final FragmentActivity activity, final Context context,String method){
        String chettext = new MainActivity().text;
        List<Object> params = new ArrayList<>();

        handler = new Handler();
        setRunnable(context);

        params.add("userid3");
        params.add("username");
        params.add(this.mf.p.my);
        params.add(this.mf.p.mx);
        params.add(this.mf.latitude);
        params.add(this.mf.longitude);
        params.add("imageurl");
        params.add("parentid");
        params.add(chettext);

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
                                        ArrayList<String> subuser = new ArrayList<>();
                                        new InvitationFragment().user = new HashMap<String, String>();
                                        for (int i = 0; i < s.size(); i++) {
                                            subuser.add(s.get(i).get(1).toString());
                                        }
                                        for (int i = 0; i < subuser.size(); i++) {
                                            //追加先のインスタンスの取得
                                            LinearLayout layout;
                                            //fragment_invitation_userのレイアウトを読み込んで追加
                                            layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.fragment_invitation_user, null);   //レイアウトをその場で生成
                                            layout.setOrientation(LinearLayout.HORIZONTAL);

                                            ImageView image = new ImageView(context);
                                            TextView username = new TextView(context);

                                            //取得したレイアウトにidを設定する
                                            username.setId(i);
                                            //setImageResource(ID);
                                            username.setText(subuser.get(i));

                                            username.setTextSize(24);

                                            username.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    TextView tv = view.findViewById(view.getId());
                                                    tv.setBackgroundColor(101010);
                                                    tv.setAlpha(0.5f);

                                                    new InvitationFragment().user.put(String.valueOf(view.getId()), tv.getText().toString());
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
        }else{
            gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null ,"Return",
                    null, new GoogleScript.ScriptListener() {
                        @Override
                        public void onExecuted(GoogleScript script, Operation op) {
                            if(op == null || op.getError() != null) {
                                System.out.println("Script結果:エラー\n");
                                if(op != null) {
                                    System.out.println(op.getError());
                                }
                            }else {
                                //戻ってくる型は、スクリプト側の記述によって変わる
                                s = (ArrayList<ArrayList<Object>>) op.getResponse().get("result");
                                System.out.println("Script結果:成功\n");
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
                for (int i = 0; i < 3; i++) {
                    cnt--;
                    if(cnt < 0){
                        break;
                    }else if(s.get(cnt).get(8).equals("")){
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
        };
    }
}
