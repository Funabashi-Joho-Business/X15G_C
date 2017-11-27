package jp.ac.chiba_fjb.c.chet.SubModule;


import android.app.Activity;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GoogleScript extends GoogleAccount
{
	public static interface ScriptListener{
		public void onExecuted(GoogleScript script, Operation op);
	}
	static class ScriptInfo{
		public String scriptId;
		public String apiKey;
		public String functionName;
		public List<Object> params;
		public ScriptListener listener;
	}



	private static final String[] SCOPES = {"https://www.googleapis.com/auth/drive"};
	private Set<ScriptInfo> mScripts = new HashSet<>();
	private Activity mContext;
	private Script mService;

	private static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
		return new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest httpRequest)
					throws IOException {
				requestInitializer.initialize(httpRequest);
				httpRequest.setReadTimeout(380000);
			}
		};
	}

	public GoogleScript(Activity activity, String[] scope) {
		super(activity,scope);
		//Activityの保存
		mContext = activity;
	}

	public void execute(String scriptId, String apiKey, String name,List<Object> params, ScriptListener listener){
		//実行に必要な情報を保存
		ScriptInfo info = new ScriptInfo();
		info.scriptId = scriptId;
		info.apiKey = apiKey;
		info.functionName = name;
		info.params = params;
		info.listener = listener;
		mScripts.add(info);

		//サーバに要求開始
		call();
	}

	protected void onExec() throws IOException {
		HttpTransport transport = AndroidHttp.newCompatibleTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		mService = new Script.Builder(
				transport, jsonFactory, setHttpTimeout(getCredential()))
				.setApplicationName("Google Apps Script Execution API")
				.build();

		for(final ScriptInfo info : mScripts) {
			ExecutionRequest request = new ExecutionRequest().setFunction(info.functionName);
			if (info.params != null)
				request.setParameters(info.params);
//			request.setDevMode(true);//デベロッパーモード
			final Operation op = mService.scripts().run(info.scriptId, request).setKey(info.apiKey).execute();
			if (info.listener != null) {
				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						info.listener.onExecuted(GoogleScript.this, op);
					}
				});
			}

		}
		mScripts.clear();
	}
	protected void onError() {
		//エラーを通知し、実行キューを解除
		for(ScriptInfo i : mScripts) {
			final ScriptInfo info = i;
			if(info.listener != null)
				mContext.runOnUiThread(new Runnable() {
					                       @Override
					                       public void run() {
						                       info.listener.onExecuted(GoogleScript.this, null);

					                       }
				                       });
			mScripts.remove(info);
		}
	}
}