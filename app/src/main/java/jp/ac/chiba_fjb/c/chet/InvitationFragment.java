package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class InvitationFragment extends Fragment {

    private static ArrayList<ArrayList<Object>> s;
    public static ArrayList<String> mailuser = new ArrayList<String>();
    public static HashMap<String, String> user;
    public static LinearLayout Veritcal;


    public InvitationFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);

        Veritcal = (LinearLayout) view.findViewById(R.id.Vertical);

        Veritcal.removeAllViews();

        //Userシートの2列目をすべて取得
        new GasMain().main(getActivity(), getContext(), "GetUser");

        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay, new MainFragment());
                ft.commit();
            }
        });
        view.findViewById(R.id.shoutai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GasMain().main(getActivity(), getContext(), "MailUser");
            }
        });
        return view;
    }
}
