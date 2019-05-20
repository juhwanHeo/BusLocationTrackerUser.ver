package com.shuttlebus.user.DB;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoadData {
    // com.shuttlebus.user.DB Connection
    private static final String address = "address";  //주소 바뀜
    private static final String TAG_DATA="data";
    private static final String TAG_SPEED="avgspeed";


    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "str_user_id";
    private static final String TAG_TIME = "str_datetime";
    private static final String TAG_LAT = "str_latitude";
    private static final String TAG_LON = "str_longitude";

    // json
    private String myJSON;
    private JSONArray speed = null;

    // value getting
    private String avgSpeed;
    private String id;
    private String time;
    private String latitude;
    private String longitude;


    protected void getDBData() {
        Log.e("myJson: "+myJSON, toString());
        if(myJSON != null){
            try {
                JSONObject jsonObj = new JSONObject(myJSON);
                speed = jsonObj.getJSONArray(TAG_DATA);
                JSONObject c = speed.getJSONObject(0);

                for(int i=0; i<speed.length();i++){
                    avgSpeed = c.getString(TAG_SPEED);
                }
                Log.e("avgSpeed: " + avgSpeed , jsonObj.toString());

            } catch (JSONException e) {
                Log.e("json error: "+ e, toString());
                e.printStackTrace();
            }
        }

    }

    public void getData() {
        class GetData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                String uri = strings[0];
                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                Log.e("myJson: "+myJSON, toString());
                getDBData();
            }
        }

        GetData g = new GetData();
        g.execute(address);
    }

}

//        class DownloadTask extends AsyncTask {
//
//            private ProgressDialog progressDialog;
//
//            @Override
//            protected void onPreExecute() {
//                progressDialog = ProgressDialog.show(mContext, "Please Wait", "잠시만 기달려 주세요\n데이터를 불러오고 있습니다."
//                        , false, false);
//
//                //작업 준비 코드 작성
//
//                super.onPreExecute();
//            }
//
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(ADDRESS)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                BusLocationService busLocationService = retrofit.create(BusLocationService.class);
//                Call<RetrofitRepo> call = busLocationService.getPostLocationData(TAG_ARRIVED, TAG_LATLON);
//                call.enqueue(new Callback<RetrofitRepo>() {
//                    @Override
//                    public void onResponse(Call<RetrofitRepo> call, Response<RetrofitRepo> response) {
//                        // connection success!!!
//                        try {
//                            if (response.body() != null) {
//                                busInfo = response.body().getLatLon();
//                                arrivedList = response.body().getArrived();
//                            } else {
//                                throw new NullPointerException("JSON오류");
//                            }
//
//                            Thread.sleep(1000);
//                            getArrivedData();
//                            getLatLonData();
//                        } catch (NullPointerException e) {
//                            Log.e("[ERROR]", e.getMessage());
//                        } catch (Exception e) {
//                            Log.e("[ERROR]", "Error");
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<RetrofitRepo> call, Throwable t) {
//                        // connection fail...
//                    }
//                });
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//        }
//        new DownloadTask().execute();
////        d.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }
