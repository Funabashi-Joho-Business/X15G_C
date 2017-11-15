package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        username = view.findViewById(R.id.username);
        singup = view.findViewById(R.id.signup);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupMain sup = new SignupMain(username.getText().toString());
                sup.main(getActivity());
//                DataStorage.store(getContext(),sup);
//                System.out.println(DataStorage.load(getContext()));
            }
        });
        return view;
    }

}
