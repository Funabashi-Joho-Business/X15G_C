package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.ac.chiba_fjb.c.chet.R;


public class MenuFragment extends Fragment implements View.OnClickListener{


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        view.findViewById(R.id.menu).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}
