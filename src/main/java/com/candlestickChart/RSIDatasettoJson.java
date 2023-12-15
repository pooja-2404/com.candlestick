package com.candlestickChart;

import java.util.Date;

import org.jfree.data.xy.DefaultHighLowDataset;
import org.json.JSONArray;
import org.json.JSONObject;

public class RSIDatasettoJson {
	public static JSONArray convertRSIDatasetToJSON(double[] relativeStrengthIndexdata, Date[] datear, double[] closeList) {
        JSONArray jsonArray = new JSONArray();
        float overboughtThreshold=70f;
        float undersellThreshold=30f;
        for(int i=0;i<closeList.length;i++) {
        	JSONObject dataPoint = new JSONObject();
        	dataPoint.put("x", datear[i].getTime());
            dataPoint.put("close",closeList[i]);
            dataPoint.put("relativestrength", relativeStrengthIndexdata[i]);
            dataPoint.put("overbought",overboughtThreshold);
            dataPoint.put("undersell",undersellThreshold);
            jsonArray.put(dataPoint);
        }

        
        return jsonArray;
    }
	}
