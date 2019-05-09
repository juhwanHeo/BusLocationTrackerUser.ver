package com.shuttlebus.user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DB_GetData {

    // DB Connection
    private static final String allAddress = "http://...address";
    private static final String ADDRESS = "http://...address";
    private static final String TAG_ARRIVED = "arrived";
    private static final String TAG_LATLON = "LatLon";

    //
    private static List<BusInfo> busInfo;
    private static List<Arrived> arrivedList;

    // value getting at DB
    private static String id;
    private static String time;
    private static String latitude;
    private static String longitude;
    private static boolean[] isArrived;


    private static void getLatLonData() throws NullPointerException {
        id = busInfo.get(0).getID();
        time = busInfo.get(0).getTime();
        latitude = busInfo.get(0).getLatitude();
        longitude = busInfo.get(0).getLongitude();

    }

    private static void getArrivedData() throws NullPointerException {
        isArrived = new boolean[arrivedList.size()];
        for (int i = 0; i < isArrived.length; i++)
            isArrived[i] = Boolean.parseBoolean(arrivedList.get(i).getIsArrived());
    }


    public static void getData(final Context mContext) throws NullPointerException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BusLocationService busLocationService = retrofit.create(BusLocationService.class);
        Call<RetrofitRepo> call = busLocationService.getPostLocationData(TAG_ARRIVED, TAG_LATLON);
        call.enqueue(new Callback<RetrofitRepo>() {
            @Override
            public void onResponse(Call<RetrofitRepo> call, Response<RetrofitRepo> response) {
                // connection success!!!
                try {
                    if (response.body() != null) {
                        busInfo = response.body().getLatLon();
                        arrivedList = response.body().getArrived();
                    } else {
                        throw new NullPointerException("JSON?§Î•ò");
                    }

                    getArrivedData();
                    getLatLonData();
                } catch (NullPointerException e) {
                    Log.e("[ERROR]", e.getMessage());
                } catch (Exception e) {
                    Log.e("[ERROR]", "Error");
                }

            }

            @Override
            public void onFailure(Call<RetrofitRepo> call, Throwable t) {
                // connection fail...
            }
        });


    }
//        class DownloadTask extends AsyncTask {
//
//            private ProgressDialog progressDialog;
//
//            @Override
//            protected void onPreExecute() {
//                progressDialog = ProgressDialog.show(mContext, "Please Wait", "?†ÏãúÎß?Í∏∞Îã¨??Ï£ºÏÑ∏??n?∞Ïù¥?∞Î? Î∂àÎü¨?§Í≥† ?àÏäµ?àÎã§."
//                        , false, false);
//
//                //?ëÏóÖ Ï§ÄÎπ?ÏΩîÎìú ?ëÏÑ±
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
//                                throw new NullPointerException("JSON?§Î•ò");
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


    public static double getLatitude() {
        return Double.parseDouble(latitude);
    }

    public static double getLongitude() {
        return Double.parseDouble(longitude);
    }


    public static String getDBId() {
        return id;
    }

    public static String getDBTime() {
        return time;
    }

    public static String getDBLatitude() {
        return latitude;
    }

    public static String getDBLongitude() {
        return longitude;
    }

    public static boolean[] getIsArrived() {
        return isArrived;
    }


}