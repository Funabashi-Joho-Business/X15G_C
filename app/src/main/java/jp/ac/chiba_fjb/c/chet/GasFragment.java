package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.api.services.script.model.Operation;

import java.util.ArrayList;
import java.util.List;

import jp.ac.chiba_fjb.c.chet.SubModule.GoogleScript;


/**
 * A simple {@link Fragment} subclass.
 */
public class GasFragment extends Fragment {


	public GasFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_gas, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
//		  EditText userid = (EditText) findViewById(R.id.userid);
//        EditText username = (EditText) findViewById(R.id.username);
//        EditText positionN = (EditText) findViewById(R.id.positionN);
//        EditText positionE = (EditText) findViewById(R.id.positionE);
//        EditText pinN = (EditText) findViewById(R.id.pinN);
//        EditText pinE = (EditText) findViewById(R.id.pinE);
//        EditText imageurl = (EditText) findViewById(R.id.imageurl);
//        EditText parentid = (EditText) findViewById(R.id.parentid);
//		EditText chettext = (EditText) view.findViewById(R.id.chettext1);

		//送信パラメータ
		List<Object> params = new ArrayList<>();
//        params.add(userid);
//        params.add(username);
//        params.add(positionN);
//        params.add(positionE);
//        params.add(pinN);
//        params.add(pinE);
//        params.add(imageurl);
//        params.add(parentid);

		params.add("userid");
		params.add("username");
		params.add("positionN");
		params.add("positionE");
		params.add("pinN");
		params.add("pinE");
		params.add("imageurl");
		params.add("parentid");
		params.add("chettext");

		//MainActivityで作成したGASを共有して利用する
		GoogleScript gas = ((MainActivity)getActivity()).getGas();
		//ID,ファンクション名,結果コールバック　後ろのは受け取った管理者APIキー
		gas.execute("MElQvDuPso7D_yra9PVEL7zGtL2HAWDts", null ,"Main",
			params, new GoogleScript.ScriptListener() {
				@Override
				public void onExecuted(GoogleScript script, Operation op) {
					if(op == null || op.getError() != null) {
//                            userid.append("Script結果:エラー\n");
						System.out.println("Script結果:エラー\n");
					}else {
						//戻ってくる型は、スクリプト側の記述によって変わる
						String s = (String) op.getResponse().get("result");
//                            userid.append("Script結果:" + s + "\n");
						System.out.println("Script結果:成功\n");
					}
				}
			});
	}
}
