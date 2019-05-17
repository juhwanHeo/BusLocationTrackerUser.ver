package com.shuttlebus.user.DB;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;

import java.net.ConnectException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DB_GetData {

    // DB Connection
    private static final String allAddress = "address";
    private static final String ADDRESS = "address";
    private static final String TAG_ARRIVED = "arrived";
    private static final String TAG_LATLON = "LatLon";

    // DataStructure
    private static List<BusInfo> busInfo;
    private static List<Arrived> arrivedList;

    // value getting
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


    public static void getData(final Context mContext) throws NullPointerException, ConnectException {

        // It checks connection for Network
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getActiveNetworkInfo()==null) {
            Log.d("Test", "?�트?�크?�결 ?�됨");
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

            alertDialog.setTitle("Network Error");
            alertDialog.setMessage("\n?�터???�결???�인?�주?�요");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"?�인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            throw new ConnectException("Network Connect Error");
        }

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
                        throw new NullPointerException("JSON?�류");
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
//                progressDialog = ProgressDialog.show(mContext, "Please Wait", "?�시�?기달??주세??n?�이?��? 불러?�고 ?�습?�다."
//                        , false, false);
//
//                //?�업 준�?코드 ?�성
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
//                                throw new NullPointerException("JSON?�류");
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