package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.ac.chiba_fjb.c.chet.SubModule.DataStorage;

public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    Button singup;
    TextView username;
    TextView usermeil;
    File file;
    byte[] name;
    byte[] meil;
    byte[] deta;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        username = view.findViewById(R.id.username);
        usermeil = view.findViewById(R.id.meil);
        singup = view.findViewById(R.id.signup);
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(username.getText().equals("")) && !(usermeil.getText().equals(""))){
                    try {
                        name = (username.getText().toString()+",").getBytes();
                        meil = usermeil.getText().toString().getBytes();

                        file = new File(getContext().getFilesDir(),"Chet");
                        FileOutputStream outputStream = getActivity().openFileOutput("Chet", getContext().MODE_PRIVATE);
                        outputStream.write(name);
                        outputStream.write(meil);
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SignupMain sup = new SignupMain();
                    sup.loadData(getActivity(),getContext());
                    sup.main(getActivity());

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


