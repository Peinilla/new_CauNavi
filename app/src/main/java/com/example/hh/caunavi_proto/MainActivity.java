package com.example.hh.caunavi_proto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.hh.caunavi_proto.common.helpers.BuildingDataHelper;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BuildingDataHelper bdh = new BuildingDataHelper(this);
    }

    public void onClick(View v){

        switch (v.getId()){
            case R.id.button3 :
                Intent intent=new Intent(MainActivity.this,ARActivity.class);
                startActivity(intent);
                break;
            case R.id.button4 :
                break;
            case R.id.button5 :
                break;
        }
    }
}