package com.example.veled;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MessageActivity extends Activity{

	private Button btnManda;
	private Button btnOther;
	private EditText msg;
	private String message;
	private String ip;
	private Socket client;
	private PrintWriter printwriter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		msg = (EditText) findViewById(R.id.edit_frase);
		btnManda = (Button) findViewById(R.id.button_manda);
		btnOther = (Button) findViewById(R.id.otherSensor);

		Bundle ext = getIntent().getExtras();
		ip = ext.getString("ip").toString();

		btnManda.setOnClickListener(btnMandaMsg);
		btnOther.setOnClickListener(btnOtherPage);
		
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		
	}

	private OnClickListener btnOtherPage = new OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(v.getContext(), SensorActivity.class);
			intent.putExtra("ip", ip.toString());
			startActivity(intent);
			finish();
		}
	};

	public void onTouch(String msg, boolean res) {
		AlertDialog.Builder mensagem = new AlertDialog.Builder(MessageActivity.this);
		if(res)
			mensagem.setTitle("Sucesso!");
		else
			mensagem.setTitle("Erro!");
		mensagem.setMessage(msg);
		mensagem.setNeutralButton("OK", null);
		mensagem.show();
	}


	private OnClickListener btnMandaMsg = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			message = msg.getText().toString(); //pega a mensagem
			msg.setText("");      //seta a mensagem em ""
			
			try{
				if(msg.getText() == null){
					onTouch("Escreva uma mensagem!", false);
				}else{					
					client = new Socket(ip.toString(), 12345);  //conecta com o servidor
					printwriter = new PrintWriter(client.getOutputStream(),true);
					printwriter.write(message);  //escreve a mensagem na saida

					printwriter.flush();
					printwriter.close();
					client.close();   //fecha a conexao
					onTouch("Mensagem enviada!", true);					
				}

			}catch(Exception e){
				onTouch("Erro: " + e.getMessage(), false);
			}
		}

	};
	
}
