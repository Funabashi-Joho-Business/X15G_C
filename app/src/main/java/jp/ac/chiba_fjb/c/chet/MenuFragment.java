package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.R.attr.fragment;
import static jp.ac.chiba_fjb.c.chet.MainActivity.ft;


public class MenuFragment extends Fragment implements View.OnClickListener{


    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.log).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu:
                getFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.log:

                break;
    }

    }
}
