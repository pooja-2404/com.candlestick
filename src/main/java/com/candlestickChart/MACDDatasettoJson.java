package com.candlestickChart;

import java.util.Date;

import org.jfree.data.xy.DefaultHighLowDataset;
import org.json.JSONArray;
import org.json.JSONObject;

public class MACDDatasettoJson {
	public static JSONArray convertMACDDatasetToJSON(double[] macdLinedata, double[] signalLinedata, Date[] datear, double[] closeList) {
        JSONArray jsonArray = new JSONArray();

        
        for(int i=0;i<closeList.length;i++) {
        	JSONObject dataPoint = new JSONObject();
            dataPoint.put("x", datear[i].getTime());
            dataPoint.put("close",closeList[i]);
            dataPoint.put("macdline", macdLinedata[i]);
            dataPoint.put("signalline", signalLinedata[i]);
            jsonArray.put(dataPoint);
        }

        return jsonArray;
    }
}
