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

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }

    private TextView check;
    private LinearLayout chatbox;
    private Button b;
    private ArrayList<ArrayList<Object>> s;
    private ArrayList<String> user;
    private Handler handler;
    private Runnable r;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        check = view.findViewById(R.id.check);
        b = view.findViewById(R.id.Transmission);

        chatbox = view.findViewById(R.id.chatbox2);

        reload();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText e = (EditText) getView().findViewById(R.id.chettext2);
                new MainActivity().text = e.getText().toString();
                new GasMain().main(getActivity(),getContext(),"Main");
            }
        });

        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new MainFragment());
                handler.removeCallbacks(r);
                ft.commit();
            }
        });

        handler = new Handler();
        r = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                    reload();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(r);
        return view;
    }

    public void reload(){
        new GasMain().main(getActivity(),getContext(),"Return");
        s = new GasMain().getArray();
        user = new GasMain().getAllUser(getContext());
        check.setText(new MainFragment().getCheck());
        chatbox.removeAllViews();
        for(int index = 0;index<s.size();index++){
            if(s.get(index).get(7).equals("")){
                continue;
            }else if(user.contains(s.get(index).get(0))) {
                TextView tv = new TextView(getContext());
                LinearLayout ll = new LinearLayout(getContext());
                tv = new GasMain().setTO(tv, index);
                ll = new GasMain().setYourLO(ll,getContext());
                ll.addView(tv);
                chatbox.addView(ll);
            }else{
                TextView tv = new TextView(getContext());
                LinearLayout ll = new LinearLayout(getContext());
                tv = new GasMain().setTO(tv, index);
                ll = new GasMain().setMyLO(ll);
                ll.addView(tv);
                chatbox.addView(ll);
            }
        }
    }
}
