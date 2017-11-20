package jp.ac.chiba_fjb.c.chet;


import android.app.Application;
import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;
import jp.ac.chiba_fjb.c.chet.SubModule.Json;

public class InvitationFragment extends Fragment  implements View.OnClickListener{
        private ArrayList<ArrayList<Object>> s;
    HashMap<String,String> user;
        LinearLayout layout;
        ImageView image;
        TextView username;


    public InvitationFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);

//        LinearLayout Vertical = (LinearLayout)view.findViewById(R.id.Vertical);

        //Userシートの2列目をすべて取得
        new GasMain().main(getActivity(),getContext(),"GetUser");
        //レイアウトを友達の数分、追加する
        s = new GasMain().getArray();
        ArrayList<String> subuser = new ArrayList<>();
        user = new HashMap<String,String>();
        for(int i = 0;i<s.size();i++) {
            subuser.add(s.get(i).get(1).toString());
        }
        for(int i = 0;i<user.size();i++){
            //追加先のインスタンスの取得
            LinearLayout Veritcal = (LinearLayout) view.findViewById(R.id.Vertical);

            //fragment_invitation_userのレイアウトを読み込んで追加
            layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_invitation_user, null);   //レイアウトをその場で生成
            layout.setOrientation(LinearLayout.HORIZONTAL);

            image = (ImageView)view.findViewById(R.id.image);

            username = new TextView(getContext());

            //取得したレイアウトにidを設定する
            image.setId(i);
            username.setId(i);
            //setImageResource(ID);
            username.setText(subuser.get(i));

            username.setOnClickListener(this);

            layout.addView(image);
            layout.addView(username);
            Veritcal.addView(layout);                                                         //Viewの追加
        }


        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay, new MainFragment());
                ft.commit();
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        TextView tv = view.findViewById(view.getId());
        tv.setBackgroundColor(101010);
        tv.setAlpha(0.5f);

        user.put(String.valueOf(view.getId()),tv.getText().toString());
    }
}
