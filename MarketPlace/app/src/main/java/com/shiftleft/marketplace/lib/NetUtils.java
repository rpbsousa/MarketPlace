package com.shiftleft.marketplace.lib;

import android.content.res.AssetManager;

import org.json.JSONArray;

import java.io.InputStream;

/**
 * Created by rsousa on 06/11/2016.
 */

public class NetUtils {
    public static JSONArray loadJSONFromAsset(AssetManager assets, String fileName) {
        try {
            InputStream is = assets.open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray obj = new JSONArray(json);
            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
