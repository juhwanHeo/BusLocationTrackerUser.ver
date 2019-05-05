package com.shuttlebus.user;

import android.view.View;
import java.util.ArrayList;

/**
 * Simple POJO model for example
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Item {
    private String startStation;
    private String endStation;
    private String date;
    private String time;
    private int progress;
    private boolean isArrived;
    private double distance;
    private double lateTime;

    private View.OnClickListener requestBtnClickListener;

    public Item() {
        this(null,null,0,false);
    }

    public Item(String start, String end, int progress, boolean isArrived) {
        this(start,end,progress,isArrived,0.0,null,null,0);
    }

    public Item(String start, String end, int progress, boolean isArrived,double distance, String date, String time, double lateTime) {
        this.startStation = start;
        this.endStation = end;
        this.progress = progress;
        this.isArrived = isArrived;
        this.distance = distance;
        this.date = date;
        this.time = time;
        this.lateTime = lateTime;
    }
    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<Item> getTestingList() {
        ArrayList<Item> items = new ArrayList<>();


        return items;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setArrived(boolean is) {
        this.isArrived = is;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public int getProgress() {
        return progress;
    }

    public boolean getArrived() {
        return isArrived;
    }

    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }

    public double getLateTime(){
        return lateTime;
    }

    public double getDistance(){
        return distance;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
}