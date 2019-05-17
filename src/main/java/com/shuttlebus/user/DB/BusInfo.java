package com.shuttlebus.user.DB;

public class BusInfo {
    private String str_user_id;
    private String str_datetime;
    private String str_latitude;
    private String str_longitude;


    public String getID(){
        return str_user_id;
    }

    public String getTime(){
        return str_datetime;
    }
    public String getLatitude(){
        return str_latitude;
    }
    public String getLongitude(){
        return str_longitude;
    }

    public void setID(String id){
        str_user_id = id;
    }
    public void setTime(String time){
        str_datetime = time;
    }

    public void setLatitude(String latitude){
        str_latitude = latitude;
    }
    public void setLongitude(String longitude){
        str_longitude = longitude;
    }
}
