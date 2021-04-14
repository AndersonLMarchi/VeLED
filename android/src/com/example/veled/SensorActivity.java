package com.example.veled;

import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;


public class SensorActivity extends Activity{

	private RadioGroup radioOP;
	private Button btTemp;
	private Button btUmid;
	private Button btVoltar;
	private Socket client;
	private PrintWriter printwriter;
	private String ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensors);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		btTemp = (Button) findViewById(R.id.temp);
		btUmid = (Button) findViewById(R.id.umid);
		btVoltar = (Button) findViewById(R.id.voltar);

		radioOP = (RadioGroup) findViewById(R.id.radBG);
		
		Bundle ext = getIntent().getExtras();
		ip = ext.getString("ip").toString();
		
		btVoltar.setOnClickListener(btnReturn);
		btUmid.setOnClickListener(btnMandaUmid);
		btTemp.setOnClickListener(btnMandaTemp);
		
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);

	}

	public void onTouch(String msg) {
		Toast.makeText(SensorActivity.this, msg, Toast.LENGTH_SHORT).show();		
	}

	private OnClickListener btnReturn = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("ip", ip.toString());
			intent.setClass(v.getContext(), MessageActivity.class);
			startActivity(intent);
			finish();
		}
	};


	private OnClickListener btnMandaTemp = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int opRB = radioOP.getCheckedRadioButtonId();
			String mostrar = "";
			switch (opRB) {
			case R.id.TCel:
				mostrar = "TCelcius";
				break;
			case R.id.TKel:
				mostrar = "TKelvin";
				break;
			case R.id.TFah:
				mostrar = "TFahrenheit";
				break;

			default:
				onTouch("Nenhuma opção selecionada!");
				break;
			}

			try{

				client = new Socket(ip, 12345);  //conecta com o servidor
				printwriter = new PrintWriter(client.getOutputStream(),true);
				printwriter.write(mostrar);  //escreve a mensagem na saida
				//essa variável String mostrar pode possuir 3 valores:
					// 1 - TCelcius: que pede a temperatura em Celcius
					// 2 - TKelvin: que pede a temperatura em Kelvin
					// 3 - TFahrenheit: que pede a temperatura em Fahrenheit
				printwriter.flush();
				printwriter.close();
				client.close();   //fecha a conexao

			}catch(Exception e){
				onTouch("Erro: " + e);
			}
		}
	};


	private OnClickListener btnMandaUmid = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{
				client = new Socket(ip, 12345);  //conecta com o servidor
				printwriter = new PrintWriter(client.getOutputStream(),true);
				printwriter.write("UmidRelAr");  //escreve a mensagem na saida

				printwriter.flush();
				printwriter.close();
				client.close();   //fecha a conexao
			}catch(Exception e){
				onTouch("Erro: " + e);
			}
		}
	};


}
