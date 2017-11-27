package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import jp.ac.chiba_fjb.c.chet.SubModule.DataStorage;

public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    Button singup;
    TextView username;
    TextView meil;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        username = view.findViewById(R.id.username);
        meil = view.findViewById(R.id.meil);
        singup = view.findViewById(R.id.signup);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(username.getText().equals("")) && !(meil.getText().equals(""))){
                    SignupMain sup = new SignupMain(username.getText().toString(),meil.getText().toString());
                    sup.main(getActivity());
                    DataStorage.store(getContext(), sup);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.maindisplay,new MainFragment());
                    ft.commit();
//                System.out.println(DataStorage.load(getContext()));
                }
            }
        });
        return view;
    }

}
