package com.example.veled;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btConnect;
	private EditText txtIP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtIP = (EditText) findViewById(R.id.ip_read);
		btConnect = (Button) findViewById(R.id.readIp);

		btConnect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					InputFilter[] filters = new InputFilter[1];
					filters[0] = new InputFilter() {	
						@Override
						public CharSequence filter(CharSequence source, int start, int end,
								Spanned dest, int dstart, int dend) {
							if (end > start) {
								String destTxt = dest.toString();
								String resultingTxt = destTxt.substring(0, dstart) +
										source.subSequence(start, end) +
										destTxt.substring(dend);
								if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
									return "";
								} else {
									String[] splits = resultingTxt.split("\\.");
									for (int i = 0; i < splits.length; i++) {
										if (Integer.valueOf(splits[i]) > 255) {
											return "";
										}
									}
								}
							}
							return null;
						}
					};
					txtIP.setFilters(filters);
				} catch (Exception e) {
					Toast.makeText(MainActivity.this, "Erro: " + e, Toast.LENGTH_LONG).show();
				}finally{
					Intent intent = new Intent();				
					intent.putExtra("ip", txtIP.getText().toString());				
					intent.setClass(v.getContext(), MessageActivity.class);				
					startActivity(intent);
					finish();	
				}		
			}
		});
	}

	// função vazia para desabilitar o botão Back do dispositivo, não fechar o app ao querer voltar a tela
	public void onBackPressed(){}

}
