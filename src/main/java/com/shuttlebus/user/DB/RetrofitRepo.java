package com.shuttlebus.user.DB;

import java.util.List;

public class RetrofitRepo {
    private List<Arrived> arrived;
    private List<BusInfo> LatLon;

    public List<Arrived> getArrived(){
        return arrived;
    }
    public List<BusInfo> getLatLon(){
        return LatLon;
    }

    public void setArrivedList(List<Arrived> arrived) {
        this.arrived = arrived;
    }

    public void setLatLonList(List<BusInfo> LatLon){
        this.LatLon = LatLon;
    }

}
