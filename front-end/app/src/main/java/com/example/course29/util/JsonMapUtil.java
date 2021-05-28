package com.example.course29.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonMapUtil {
    public static Map<String, Object> getMap(String jsonString) {
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonString);   @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext())
            {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJson (Map map) {
        if(map.isEmpty() || map ==null) return null;
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        return jsonStr;
    }

    public static List<Map<String, Object>> jsonArrayToList (JSONArray list) throws JSONException {
        if(list.length()<1 || list ==null) return null;
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        for (int i=0; i< list.length(); i++) {
            String strJson = list.getJSONObject(i).toString();
            Map<String, Object> valueMap = getMap(strJson);
            res.add(valueMap);
        }
        return res;
    }
}
