package com.candlestickChart;

import java.util.Date;

import org.jfree.data.xy.DefaultHighLowDataset;
import org.json.JSONArray;
import org.json.JSONObject;

public class StochasticDatasettoJson {
	public static JSONArray convertStochasticDatasetToJSON(double[] percent_K_Valuedata, double[] percent_D_Valuedata, Date[] datear, double[] closeList) {
        JSONArray jsonArray = new JSONArray();
        

        for(int i=0;i<closeList.length;i++) {
        	JSONObject dataPoint = new JSONObject();
        	dataPoint.put("x", datear[i].getTime());
            dataPoint.put("close",closeList[i]);
            dataPoint.put("Dvalue", percent_D_Valuedata[i]);
            dataPoint.put("Kvalue", percent_K_Valuedata[i]);
            jsonArray.put(dataPoint);
        }

        return jsonArray;
    }
}
