package com.example.hh.caunavi_proto;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.hh.caunavi_proto.common.helpers.SnackbarHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapManager {

    private Context mContext;
    private ArrayList<mapData> mapDataArrayList = new ArrayList<>();
    private ArrayList<Integer> route = new ArrayList<>();

    private final SnackbarHelper messageSnackbarHelper = new SnackbarHelper();

    private int destinationID;
    private int nearPointID;
    private int nextPointID;
    private int prevPointID;

    private static Toast mToast;

    public class mapData {
        Location location = new Location("map");
        int id;
        String name;
    }

    public MapManager(Context context){
        mContext = context;
        AssetManager am = mContext.getResources().getAssets();
        InputStream is =  null;
        Toast mToast = new Toast(mContext.getApplicationContext());

        try{
            is = am.open("map/backGate_to_208.txt");
            BufferedReader bufrd = new BufferedReader(new InputStreamReader(is,"UTF-8"));

            String line = bufrd.readLine();
            while((line = bufrd.readLine()) != null){
                String str[] = line.split("\t");

                mapData md = new mapData();
                md.location.setLatitude(Double.valueOf(str[0]));
                md.location.setLongitude(Double.valueOf(str[1]));
                md.id = Integer.valueOf(str[2]);
                md.name = str[3];
                mapDataArrayList.add(md);
            }

            bufrd.close();
            is.close();
        }catch (Exception e){
            Log.i("test", e.getMessage());
        }
        init();
    }
    public void init() {
        destinationID = -1;
        nearPointID = -1;
        nextPointID = -1;
        prevPointID = -1;
    }

    public void setDestination(int destinationID, double lat, double lon){
        init();
        this.destinationID = destinationID;
        setNearPointID(lat,lon);
        nextPointID = nearPointID;
        prevPointID = nearPointID;

        // 테스트용
        this.destinationID = 9;
        route.add(0);
        route.add(1);
        route.add(2);
        route.add(3);
        route.add(4);
        route.add(5);
        route.add(6);
        route.add(7);
        route.add(8);
        route.add(9);
        //
    }

    public void setNearPointID(double lat, double lon){
        Location tempLoc = new Location("temp");
        tempLoc.setLatitude(lat);
        tempLoc.setLongitude(lon);

        int distance = Integer.MAX_VALUE;
        int nearID = -1;

        for(int inx = 0; inx < mapDataArrayList.size(); inx ++ ){
            int tempDis = (int)mapDataArrayList.get(inx).location.distanceTo(tempLoc);
            if(distance > tempDis){
                distance = tempDis;
                nearID = mapDataArrayList.get(inx).id;
            }
        }
        if(nearID != -1){
            nearPointID = nearID;
        }
        //위도와 경도를 이용해 현재위치와 가장 가까운 지점의 ID를 설정
    }

    public float getNextPointBearing(double lat, double lon){
        setNearPointID(lat,lon);

        Location tempLoc = new Location("temp");
        tempLoc.setLatitude(lat);
        tempLoc.setLongitude(lon);

        int distNext = (int) tempLoc.distanceTo(mapDataArrayList.get(nextPointID).location);
        if(distNext > 8){
            if(nextPointID != nearPointID && prevPointID != nearPointID){
                setDestination(destinationID,lat,lon); // 경로 재설정
                return getNextPointBearing(lat,lon);
            } else{
                String nameNext = mapDataArrayList.get(nextPointID).name;
                float bearing = tempLoc.bearingTo(mapDataArrayList.get(nextPointID).location);
                if(mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(mContext.getApplicationContext(),nameNext + "/" + distNext + "m"  , Toast.LENGTH_SHORT);
                mToast.show();
                return bearing;
            }
        }else{
            prevPointID = nextPointID;
            nextPointID = getnextPoint();
            return tempLoc.bearingTo(mapDataArrayList.get(nextPointID).location);
        }
    }

    // 현재 위치에서 다음목적지가 아닌, 지나온 목적지에서 다음 목적지로
    public float getNextBearingTest(double lat, double lon){
        setNearPointID(lat,lon);

        Location tempLoc = new Location("temp");
        tempLoc.setLatitude(lat);
        tempLoc.setLongitude(lon);

        int distNext = (int) tempLoc.distanceTo(mapDataArrayList.get(nextPointID).location);
        if(distNext > 8){
            if(nextPointID != nearPointID && prevPointID != nearPointID){
                setDestination(destinationID,lat,lon); // 경로 재설정
                return getNextBearingTest(lat,lon);
            } else if(prevPointID == nextPointID){
                String nameNext = mapDataArrayList.get(nextPointID).name;
                float bearing = tempLoc.bearingTo(mapDataArrayList.get(nextPointID).location);
                if(mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(mContext.getApplicationContext(),nameNext + "/" + distNext + "m"  , Toast.LENGTH_SHORT);
                mToast.show();
                return bearing;
            }else{
                String nameNext = mapDataArrayList.get(nextPointID).name;
                float bearing = mapDataArrayList.get(prevPointID).location.bearingTo(mapDataArrayList.get(nextPointID).location);
                if(mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(mContext.getApplicationContext(),nameNext + "/" + distNext + "m"  , Toast.LENGTH_SHORT);
                mToast.show();
                return bearing;
            }
        }else{
            prevPointID = nextPointID;
            nextPointID = getnextPoint();
            return tempLoc.bearingTo(mapDataArrayList.get(nextPointID).location);
        }
    }

    public int getnextPoint(){
        for(int inx = 0; inx < route.size(); inx ++){
            if(route.get(inx) == nearPointID){
                return route.get(inx + 1);
            }
        }
        return nearPointID;
    }
}


