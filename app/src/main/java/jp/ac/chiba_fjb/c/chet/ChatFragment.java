package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static jp.ac.chiba_fjb.c.chet.GasMain.getAllUser;
import static jp.ac.chiba_fjb.c.chet.GasMain.getArray;
import static jp.ac.chiba_fjb.c.chet.GasMain.setMyLO;
import static jp.ac.chiba_fjb.c.chet.GasMain.setTO;
import static jp.ac.chiba_fjb.c.chet.GasMain.setYourLO;

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }

    private TextView check;
    private LinearLayout chatbox;
    private static  Button b;
    private ArrayList<ArrayList<Object>> s;
    private ArrayList<String> user;
    private Handler handler;
    private Runnable r;
    private GasMain gm;
    private static boolean mFlg = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        check = view.findViewById(R.id.check);
        b = view.findViewById(R.id.Transmission);

        chatbox = view.findViewById(R.id.chatbox2);

        gm = new GasMain();

        reload();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlg = false;
                b.setEnabled(false);
                b.setText("送信中");
                EditText e = (EditText) getView().findViewById(R.id.chettext2);
                new MainActivity().text = e.getText().toString();
                gm.main(getActivity(), getContext(), "Main");
            }
        });

        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay, new MainFragment());
                handler.removeCallbacks(r);
                ft.commit();
            }
        });

        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                if (mFlg) {
                    reload();
                    handler.postDelayed(this, 5000);
                }
            }
        };
        handler.post(r);
        return view;
    }

    @Override
    public void onStart() {
        this.mFlg = true;
        super.onStart();
    }

    @Override
    public void onStop() {
        this.mFlg = false;
        super.onStop();
    }

    public void reload() {
        try {
            gm.main(getActivity(), getContext(), "Return");
            s = getArray();
            user = getAllUser();
            check.setText(new MainFragment().getCheck());
            chatbox.removeAllViews();
            for (int index = 0; index < s.size(); index++) {
                if (s.get(index).get(7).equals("")) {
                    continue;
                } else if (user.contains(s.get(index).get(0))) {
                    TextView tv = new TextView(getContext());
                    LinearLayout ll = new LinearLayout(getContext());
                    tv = setTO(tv, index);
                    ll = setYourLO(ll, getContext(), index);
                    ll.addView(tv);
                    chatbox.addView(ll);
                } else {
                    TextView tv = new TextView(getContext());
                    LinearLayout ll = new LinearLayout(getContext());
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

    public void setButton() {
        b.setEnabled(true);
        b.setText("送信");
    }

    public boolean getmFlg() {
        return this.mFlg;
    }

    public void setmFlg(boolean flg) {
        this.mFlg = flg;
    }
    public TextView getCheck(){
        return check;
    }
    public LinearLayout getChatbox(){
        return chatbox;
    }
    public ArrayList<String> getUser(){
        return user;
    }
}
