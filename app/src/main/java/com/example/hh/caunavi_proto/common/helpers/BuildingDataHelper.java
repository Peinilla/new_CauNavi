package com.example.hh.caunavi_proto.common.helpers;

import android.content.Context;
import android.content.res.AssetManager;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import com.example.hh.caunavi_proto.MapManager;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BuildingDataHelper {
    Context mContext;
    ArrayList<BuildingData> buildingData;

    public BuildingDataHelper(Context context) {
        mContext = context;
        AssetManager am = mContext.getResources().getAssets();
        InputStream is = null;
        Toast mToast = new Toast(mContext.getApplicationContext());
        buildingData = new ArrayList<>();
        BuildingData b = new BuildingData();
        Gson gson;

        try {
            is = am.open("info/new.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

             gson = new GsonBuilder().setPrettyPrinting().create();

            reader.beginArray();
            String str;
            while(reader.hasNext()){
                b = gson.fromJson(reader, BuildingData.class);
                buildingData.add(b);
            }
            reader.endArray();
            reader.close();
            is.close();
        } catch (Exception e) {
            Log.i("test", e.getMessage());
        }

        Log.i("test", buildingData.get(20).Text);
    }
}