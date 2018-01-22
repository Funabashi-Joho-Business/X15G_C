package jp.ac.chiba_fjb.c.chet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
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

import jp.ac.chiba_fjb.c.chet.SubModule.BitmapUtil;
import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;

import static jp.ac.chiba_fjb.c.chet.Pin.mx;
import static jp.ac.chiba_fjb.c.chet.Pin.my;

/**
 * Created by x15g009 on 2017/11/09.
 */

public class GasMain {

    private static ArrayList<ArrayList<Object>> s;
    private static SignupMain sm;
    private static MainFragment mf;
    private static ChatFragment cf = new ChatFragment();
    private static InvitationFragment inf;
    private Handler handler;
    private Runnable runnable;
    private LinearLayout layout;


    public void main(final Activity activity, final Context context, String method) {
        if(activity == null || context == null || method == null){
            return;
        }
        final List<Object> params = new ArrayList<>();
        mf = new MainFragment();
        sm = new SignupMain();
        inf = new InvitationFragment();
        MainActivity ma = new MainActivity();
        String chettext = ma.text;
        ma.text = "";

        handler = new Handler();

        if (method.equals("Main")) {
            sm.loadData(activity);
            if (mf.getSheetid() != null) {
                params.add(mf.getSheetid());
            } else {
                params.add("null");
            }
//            params.add("1hDoEejT34IPO4sVs4MDKbbj8PVBsHBW5rSCnQ6rklRQ");
            params.add(sm.getUsername());
            params.add(my);
            params.add(mx);
            params.add(mf.latitude);
            params.add(mf.longitude);
//            params.add(sm.getIcon());
            params.add("");
            params.add(sm.getMeil());
            params.add(chettext);
        } else if (method.equals("MailUser")) {
            params.add(inf.mailuser);
        } else if (method.equals("Return")) {
            if (mf.getSheetid() != null) {
                params.add(mf.getSheetid());
            } else {
                params.add("null");
            }
        }

        try {
            //MainActivityで作成したGASを共有して利用する
            GoogleScript gas = MainActivity.getGas();
            //ID,ファンクション名,結果コールバック　後ろのは受け取った管理者APIキー
            if (method.equals("Main")) {
                gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null, "Main", params, new GoogleScript.ScriptListener() {
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
                            handler.post(runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (!mf.getmFlg()) {
                                        if (!(s.get(0).get(0).toString().isEmpty())) {
                                            LinearLayout[] llbox = new LinearLayout[3];
                                            ArrayList<String> user = getAllUser();
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
                                                        setYourLO(ll, context, cnt);
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
                                                    if (llbox[i] != null) {
                                                        mf.chatbox.addView(llbox[i]);
                                                    }
                                                }
                                            }
                                            mf.setButton();
                                            mf.setmFlg(true);
                                        }
                                    } else if (!cf.getmFlg()) {
                                        if (!(s.get(0).get(0).toString().isEmpty())) {
                                            try {
                                                TextView check = cf.getCheck();
                                                LinearLayout chatbox = cf.getChatbox();
                                                ArrayList<String> user = getAllUser();
                                                user = getAllUser();
                                                check.setText(new MainFragment().getCheck());
                                                chatbox.removeAllViews();
                                                for (int index = 0; index < s.size(); index++) {
                                                    if (s.get(index).get(7).equals("")) {
                                                        continue;
                                                    } else if (user.contains(s.get(index).get(0))) {
                                                        TextView tv = new TextView(context);
                                                        LinearLayout ll = new LinearLayout(context);
                                                        tv = setTO(tv, index);
                                                        ll = setYourLO(ll, context, index);
                                                        ll.addView(tv);
                                                        chatbox.addView(ll);
                                                    } else {
                                                        TextView tv = new TextView(context);
                                                        LinearLayout ll = new LinearLayout(context);
                                                        tv = setTO(tv, index);
                                                        ll = setMyLO(ll);
                                                        ll.addView(tv);
                                                        chatbox.addView(ll);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        cf.setButton();
                                        cf.setmFlg(true);
                                    }
                                }
                            });
                        }
                    }
                });
            } else if (method.equals("GetUser")) {
                gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null, "GetUser", null, new GoogleScript.ScriptListener() {
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
                                    inf.user = new HashMap<String, String>();
                                    for (int i = 0; i < s.size(); i++) {
                                        usertext.add(s.get(i).get(1).toString());
                                        inf.user.put(s.get(i).get(1).toString(), s.get(i).get(2).toString());
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
                                                int index = inf.mailuser.indexOf(inf.user.get(tv.getText().toString()).toString());
                                                if (index == -1) {
                                                    tv.setAlpha(0.5f);
                                                    System.out.println(tv.getText().toString() + "in");
                                                    inf.mailuser.add(inf.user.get(tv.getText().toString()).toString());
                                                } else {
                                                    tv.setAlpha(1f);
                                                    System.out.println(tv.getText().toString() + "out");
                                                    inf.mailuser.remove(inf.mailuser.indexOf(inf.user.get(tv.getText().toString()).toString()));
                                                }
                                            }
                                        });

                                        layout.addView(image);
                                        layout.addView(username);
                                        inf.Veritcal.addView(layout);                                                         //Viewの追加
                                    }
                                }
                            });
                        }
                    }
                });
            } else if (method.equals("MailUser")) {
                gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null, "MailUser", params, new GoogleScript.ScriptListener() {
                    @Override
                    public void onExecuted(GoogleScript script, Operation op) {
                        if (op == null || op.getError() != null) {
                            System.out.println("MailUserScript結果:エラー\n");
                            if (op != null) {
                                System.out.println(op.getError());
                            }
                        } else {
                            System.out.println("MailUserScript結果:成功\n");
                        }
                    }
                });
            } else {
                gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null, "Return", params, new GoogleScript.ScriptListener() {
                    @Override
                    public void onExecuted(GoogleScript script, Operation op) {
                        if (op == null || op.getError() != null) {
                            System.out.println("ReturnScript結果:エラー\n");
                            if (op != null) {
                                System.out.println(op.getError());
                            }
                        } else {
                            //戻ってくる型は、スクリプト側の記述によって変わる
                            s = (ArrayList<ArrayList<Object>>) op.getResponse().get("result");
                            System.out.println("ReturnScript結果:成功");
                            handler.post(runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (!(s.get(0).get(0).toString().isEmpty())) {
                                        LinearLayout[] llbox = new LinearLayout[3];
                                        ArrayList<String> user = getAllUser();
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
                                                    setYourLO(ll, context, cnt);
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
                                                if (llbox[i] != null) {
                                                    mf.chatbox.addView(llbox[i]);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<Object>> getArray() {
        if (s == null) {
            return null;
        } else if (s.get(0).get(0).toString().isEmpty()) {
            return null;
        } else {
            return s;
        }
    }

    public static ArrayList<String> getAllUser() {
        if (getArray() != null) {
            ArrayList<ArrayList<Object>> aao = getArray();
            ArrayList<String> array = new ArrayList<>();
            for (int index = 0; index < aao.size(); index++) {
                if (!(aao.get(index).get(0).equals(sm.getUsername()))) {
                    array.add(aao.get(index).get(0).toString());
                }
            }
            array = new ArrayList<String>(new HashSet<>(array));
            return array;
        }
        return null;
    }

    public static TextView setTO(TextView tv, int i) {
        tv.setText(s.get(i).get(7).toString());
        tv.setBackgroundResource(R.drawable.frame_style_roundness_blue);
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        return tv;
    }

    public static LinearLayout setMyLO(LinearLayout ll) {
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.RIGHT);
        ll.setPadding(10, 10, 10, 10);
        return ll;
    }

    public static LinearLayout setYourLO(LinearLayout ll, Context context, int index) {
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(10, 10, 10, 10);
        if (BitmapUtil.fromBase64(s.get(index).get(5).toString()) != null) {
            ImageView iv = new ImageView(context);
            iv.setImageBitmap(BitmapUtil.setCircle(BitmapUtil.fromBase64(s.get(index).get(5).toString())));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            ll.addView(iv);
        }
        return ll;
    }

}
