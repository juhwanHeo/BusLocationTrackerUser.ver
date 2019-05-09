package com.shuttlebus.user;

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

public class MainActivity extends AppCompatActivity {
    private String id;
    private String time;
    private String lat;
    private String lon;
    private Button toRecycler_btn;
    private Button getData_btn;
    private Button clear_btn;

    public static TextView location_tv;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            DB_GetData.getData(this);
//            getData = new DB_GetData();
//            getData.listBus("getBusInfo");
        }catch (Exception e){
            e.printStackTrace();
        }


        location_tv = (TextView) findViewById(R.id.location_tv);
        toRecycler_btn = (Button) findViewById(R.id.toRecycler_btn);
        getData_btn = (Button) findViewById(R.id.getData_btn);
        clear_btn = (Button) findViewById(R.id.clear_btn);

        toRecycler_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BusActivity.class);
                startActivity(intent);

            }
        });
//
//        seekBar.setMax(100);
//        seekBar2.setMax(100);
//
//        seekBar.setClickable(false);
//        seekBar2.setClickable(false);
//
//
//        seekBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//
//        seekBar2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

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
//                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
//                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        alert.show();

    }




}
