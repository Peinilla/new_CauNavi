package com.example.hh.caunavi_proto.common.helpers;

import java.util.ArrayList;

public class BuildingData {

        int ID;
        String Name;
        String Text;
        int Floor;
        int UnderFloor;
        String ImageStr;
        ArrayList<String> FloorImageStr;

        public BuildingData() {
            FloorImageStr = new ArrayList<>();
        }
}

