package com.be.you.depthchart.api;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonHuobiDepth implements Runnable{

    @Override
    public void run() {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("symbol", "btcusdt");
        param.put("type", "step0");
        JsonParser jsonParser = new JsonParser();
        JSONObject jsonObject = jsonParser.urlConnectGet("https://api.huobipro.com/market/depth", param);


        Log.e("bnbnbnbn", " " +jsonObject);

    }
}
