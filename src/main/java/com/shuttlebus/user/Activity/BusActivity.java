package com.shuttlebus.user.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import com.shuttlebus.user.Process.BusProgress;
import com.shuttlebus.user.Process.BusStation;
import com.shuttlebus.user.DB.DB_GetData;
import com.shuttlebus.user.Process.Distance;
import com.shuttlebus.user.Adapter.FoldingCellListAdapter;
import com.shuttlebus.user.DB.Item;
import com.shuttlebus.user.Process.Location;
import com.shuttlebus.user.R;
import com.shuttlebus.user.Process.Scheduler;


public class BusActivity extends AppCompatActivity {
    private ArrayList<Item> items = new ArrayList<>();

    private static BusStation[] busStations;
    private static double[] curDis;
    private static double[] maxDis;
    private static int[] progress;
    private static boolean[] isArrived;

    private TextView busTimeTextView;
    private FloatingActionButton fab;
    private EditText tmpEdit;

    private Context mContext = this;

    private ProgressDialog progressDialog;
    private static final int TIME_OUT = 1250;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folding);
        findViews();

        try {
            DB_GetData.getData(mContext);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setAdapter();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DB_GetData.getData(mContext);
                    Log.e("[getData]-->" + getApplicationContext(), "정상실행");
                } catch (NullPointerException e) {
                    Log.e("[getData]NullPoint-->", e.getMessage());
                } catch (ConnectException e) {
                    Log.e("[getData]Connect-->", e.getMessage());
                    return;
                } catch (Exception e) {
                    Log.e("[getData]-->", "알 수 없는 오류");
                }

                if (progressDialog != null && progressDialog.isShowing()) {
                    return;
                }

                fab.setEnabled(false);
                fab.setClickable(false);

                progressDialog = ProgressDialog.show(mContext, "Please Wait", "잠시만 기달려 주세요\n데이터를 불러오고 있습니다."
                        , false, false);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }, TIME_OUT);

                try {
                    items.clear();
                    setAdapter();
                    Log.e("[setAdapter]-->", "정상실행");
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.e("ArrayIndexOut-->", e.getMessage());
                } catch (NullPointerException e) {
                    Log.e("Null[setAdapter]-->", e.getMessage());
                } catch (Exception e) {
                    Log.e("Exc[setAdapter]-->", e.getMessage());
                }

                fab.setEnabled(true);
                fab.setClickable(true);
            }
        });


    }

    private void findViews() {
        busTimeTextView = (TextView) findViewById(R.id.busTimeText);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tmpEdit = (EditText) findViewById(R.id.tmpEdit);


    }

    //    @SuppressLint("RestrictedApi")
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setAdapter() throws ArrayIndexOutOfBoundsException, NullPointerException {

        Scheduler sd = new Scheduler();
//        BusStation.checkTime(sd);
        String strCourse = tmpEdit.getText().toString();
        try {
            char charCourse = strCourse.charAt(0);
            if (charCourse >= 'a' && charCourse <= 'z')
                charCourse -= 32;
            sd.setCurrentCourse(charCourse);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if(sd.getCurrentCourse() == 'E'){
//            // No course
//            Log.e("코스 없음", ""+sd.getCurrentCourse());
//        }

        busStations = BusStation.checkCourse(sd.getCurrentCourse());
        Log.e("length-->", busStations.length + "");
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);

        try {
            if (sd.getCurrentCourse() == 'E') throw new Exception("코스 없음");
                // 현재 진행중인 코스가 있으면 구현
            else busStations = BusStation.checkCourse(sd.getCurrentCourse());

        } catch (Exception e) {
            System.err.println("오류발생: " + e.getMessage());
        }

        String today;
        if (month <= 9)
            today = year + "0" + month;
        else {
            today = year + "" + month;
        }

        if (day <= 9) {
            today = today + "0" + day;
        } else {
            today += day;
        }


        // 임시 테스트용도 if문
        boolean tmp = false;
        if (tmp) {
            // 공휴일 판단.
//        if(Holiday.isHoliday(today)){
//            noItemTextView.setVisibility(View.VISIBLE);
//            noItemTextView.setText("공휴일은 운행하지 않습니다.");
            busTimeTextView.setText("공휴일은 운행하지 않습니다.");
        } else {
            /*
             * 버스 객체가 없다면.
             */
            if (busStations == null) {
//                noItemTextView.setVisibility(View.VISIBLE);

                //busTimeTextView.setText("다음 운행 시간: ");
                /*
                 * busStation 의 마지막 역까지 도착햇다면
                 */
            } else if (busStations[busStations.length - 1].getBusArrived()) {
                // 객체 다시 생성
                busStations = BusStation.checkCourse(sd.getCurrentCourse());
            } else {
                try {
                    processing();
                } catch (NullPointerException e) {
                    busTimeTextView.setText("Processing NULL ERROR");
                }

                if (DB_GetData.getDBLatitude() == null) {
                    busTimeTextView.setText("com.shuttlebus.user.DB ERROR");

                } else {
                    busTimeTextView.setText("운행 시작 시간: " + BusStation.getHour() + "시 "
                            + BusStation.getMinute() + "분\n"
                            + "총 거리: " + String.format("%.2fKm", Distance.allDistance(busStations)));
                }

                for (int i = 0; i < busStations.length; i++) {
                    if (i != busStations.length - 1) {
                        items.add(new Item(busStations[i].getStationName()
//                                , "거리: " + String.format("%.2fKm", maxDis[i]) + "\n진행률: " + progress[i]
                                        , busStations[i + 1].getStationName()
                                        , progress[i]
                                        , isArrived[i]
                                        , maxDis[i]
                                        , cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH) + 1) + "월 " + cal.get(Calendar.DATE) + "일"
                                        , BusStation.getHour() + "시 " + BusStation.getMinute() + "분"
                                        , calLateTime(curDis[i], 0.325))
                        );
                        Log.e("[lateTime]-->" + calLateTime(curDis[i], 0.325), " 남음");
                    }
                }
            }

        }
        ListView theListView = findViewById(R.id.busListView);

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        FoldingCellListAdapter mAdapter = new FoldingCellListAdapter(this, items);

        // add default btn handler for each request btn on each item if custom handler not found
        mAdapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });

        // set elements to adapter
        theListView.setAdapter(mAdapter);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Toast.makeText(getApplicationContext(),position+"클릭",Toast.LENGTH_SHORT).show();

                if (view.findViewById(R.id.list_cell_content).getVisibility() == View.GONE) {
                    view.findViewById(R.id.list_cell_title).setVisibility(View.GONE);
                    view.findViewById(R.id.list_cell_content).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.list_cell_title).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.list_cell_content).setVisibility(View.GONE);

                }
            }
        });

    }

    static double calLateTime(double curDis, double avgSpeed) {
        return curDis / avgSpeed;
    }

    static void processing() throws NullPointerException {
        curDis = new double[busStations.length - 1];
        maxDis = new double[busStations.length - 1];
        progress = new int[busStations.length - 1];
        isArrived = new boolean[busStations.length - 1];

        isArrived = DB_GetData.getIsArrived();
        /*
         * 현재 위치에서 부터의 거리
         */
        Location currentLocation = new Location(DB_GetData.getLatitude(), DB_GetData.getLongitude());
        for (int i = 0; i < curDis.length; i++) {
            curDis[i] = Distance.distance(currentLocation, busStations[i + 1]);
            Log.e("curDis[" + i + "]: ", Double.toString(curDis[i]));
        }
        /*
         * max
         */
        for (int i = 0; i < busStations.length - 1; i++) {
            maxDis[i] = Distance.distance(busStations[i], busStations[i + 1]);
        }

        // Progress
        for (int i = 0; i < progress.length; i++) {
            progress[i] = BusProgress.getProgress(maxDis[i], curDis[i]);
            Log.e("progress[" + i + "]:", Double.toString(progress[i]));
        }

        busStations[0].setIsBusArrived(true);

        for (int i = 0; i < progress.length; i++) {
            // 전 정류장에 먼저 도착햇는지 여부 확인
            if (i != 0 && !busStations[i].getIsBusArrived()) {
                continue;
            } else busCheck(busStations[i + 1], progress[i]);

        }
    }

    static void busCheck(BusStation busStation, int progress) {
        if (progress >= 96) {
            busStation.setIsBusArrived(true);
        }
    }

}


