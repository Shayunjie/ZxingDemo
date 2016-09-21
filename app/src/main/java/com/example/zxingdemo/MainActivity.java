package com.example.zxingdemo;

import com.google.zxing.WriterException;
import com.karics.library.zxing.android.CaptureActivity;
import com.karics.library.zxing.encode.CodeCreator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	private static final int REQUEST_CODE_SCAN = 0x0000;

	private static final String DECODED_CONTENT_KEY = "codedContent";
	private static final String DECODED_BITMAP_KEY = "codedBitmap";
	private Toolbar toolbar;
	private DrawerLayout mDrawerLayout;
	private String[] lvs = {"设置"};
	private ArrayAdapter arrayAdapter;
	private ListView mlistview;
	private ActionBarDrawerToggle mDrawerToggle;
	TextView qrCoded;
	ImageView qrCodeImage;
	FloatingActionButton  scanner;
	String upContent;
	JSONObject object;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = (Toolbar)findViewById(R.id.tl_custom);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.dl_left);
		mlistview = (ListView)findViewById(R.id.lv_left_menu);
		qrCoded = (TextView) findViewById(R.id.ECoder_title);
		qrCodeImage = (ImageView) findViewById(R.id.ECoder_image);
		scanner = (FloatingActionButton) findViewById(R.id.ECoder_scaning);
		qrCoded.setText(MobileInfo.getLinuxCore_Ver());
		Log.e("----------",MobileInfo.getBaseBandVersion());
		Log.e("----------",MobileInfo.getLinuxCore_Ver());
		Log.e("----------",MobileInfo.androidVersion);
		Log.e("----------",MobileInfo.firmwareVersion);
		Log.e("----------",MobileInfo.name);
		scanner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SCAN);
			}
		});
		toolbar.setTitle("产品名称");
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.open,R.string.close){
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,lvs);
		mlistview.setAdapter(arrayAdapter);
		mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this,SettingActivity.class);
				startActivity(intent);
			}
		});
		makeJson();
		System.out.println(object);
		putJson(object);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {

				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
				upContent = content;
				Log.d("====================",content);
				qrCoded.setText("解码结果： \n" + content);
				qrCodeImage.setImageBitmap(bitmap);
			}
		}
	}
	void   makeJson(){
		object = new JSONObject();
		try{
			object.put("clientLd"," ");
			object.put("name",MobileInfo.name);
			object.put("baseBandVersion",MobileInfo.getBaseBandVersion());
			object.put("knernelVersion",MobileInfo.getLinuxCore_Ver());
			object.put("firmwareVersion",MobileInfo.firmwareVersion);
			object.put("androidVersion",MobileInfo.androidVersion);
			object.put("deviceGroupName"," ");
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	void putJson(JSONObject json)
	{
		//upload content
		OkHttpClient mOkHttpClient = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
		Request request = new Request.Builder()
				.url("http://192.168.3.178:8080/device/bind")
				.post(requestBody)
				.build();
			mOkHttpClient.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(Request request, IOException e) {

				}

				@Override
				public void onResponse(Response response) throws IOException {
					Log.d("-----------",response.body().string());
				}
			});
	}


}
