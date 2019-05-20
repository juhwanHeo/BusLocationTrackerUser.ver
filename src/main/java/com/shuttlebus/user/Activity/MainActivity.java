package com.shuttlebus.user.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shuttlebus.user.DB.DB_GetData;
import com.shuttlebus.user.R;

public class MainActivity extends AppCompatActivity {
    private String id;
    private String time;
    private String lat;
    private String lon;
    private Button toRecycler_btn;
    private Button getData_btn;
    private Button clear_btn;
    private Button toTimeTable_btn;
    private Button setting_btn;

    public TextView location_tv;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        try {
            DB_GetData.getData(this);
//            getData = new DB_GetData();
//            getData.listBus("getBusInfo");
        }catch (Exception e){
            e.printStackTrace();
        }

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);

            }
        });

        toRecycler_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BusActivity.class);
                startActivity(intent);

            }
        });

        toTimeTable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimeTableActivity.class);
                startActivity(intent);
            }
        });

        getData_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_tv.setText("로딩중");

                try {
                    DB_GetData.getData(mContext);
//                    Log.e("[TEST]"+getData.listBus("getBusInfo").get(0).getLatitude(),"getBusInfo");
                }catch (Exception e){
                    Log.e("[TEST ERROR]" ,e.getMessage());
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        id = DB_GetData.getDBId();
                        time = DB_GetData.getDBTime();
                        lat = DB_GetData.getDBLatitude();
                        lon = DB_GetData.getDBLongitude();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (lat != null && lon != null) {
                                    location_tv.setText("id: " + id + "\n" + time + "\nlat: " + lat + "\nlon: " + lon);
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location_tv.setText("");

            }
        });
    }

    private void findViews(){
        location_tv = (TextView) findViewById(R.id.location_tv);
        toRecycler_btn = (Button) findViewById(R.id.toRecycler_btn);
        getData_btn = (Button) findViewById(R.id.getData_btn);
        clear_btn = (Button) findViewById(R.id.clear_btn);
        toTimeTable_btn = (Button) findViewById(R.id.toTimeTable_btn);
        setting_btn = (Button) findViewById(R.id.setting_btn);
    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.popupView));

        alertDialog.setView(layout);

        Button cancelBtn = (Button) layout.findViewById(R.id.cancelBtn);
        Button endBtn = (Button) layout.findViewById(R.id.endBtn);

        final AlertDialog alert = alertDialog.create();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });
        alert.show();

    }

}
