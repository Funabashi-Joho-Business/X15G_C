package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment implements View.OnClickListener{

    MainActivity ma = new MainActivity();
    FragmentTransaction ft;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        view.findViewById(R.id.menu).setOnClickListener(this);
        view.findViewById(R.id.log).setOnClickListener(this);
        view.findViewById(R.id.invitaion).setOnClickListener(this);
        view.findViewById(R.id.setting).setOnClickListener(this);
        view.findViewById(R.id.secession).setOnClickListener(this);
        view.findViewById(R.id.test).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        new MainFragment().setFlg(true);
        switch (view.getId()){
            case R.id.menu:
                getFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.log:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new ChatFragment());
                ft.commit();
                break;
            case R.id.invitaion:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new InvitationFragment());
                ft.commit();
                break;
            case R.id.setting:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new SettingFragment());
                ft.commit();
                break;
            case R.id.secession:
                DialogFragment newFragment = new SecessionFragment();
                newFragment.show(getFragmentManager(),null);
                break;
            case R.id.test:
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new SignupFragment());
                ft.commit();
                break;
        }
    }
}
