package com.candlestickChart;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Date;

public class JsonUtils {

    public static String convertDatasetToJSON(DefaultHighLowDataset dataset) {
        JSONArray jsonArray = new JSONArray();

        int seriesCount = dataset.getSeriesCount();
        
        for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
            int itemCount = dataset.getItemCount(seriesIndex);
            for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                Date date = dataset.getXDate(seriesIndex, itemIndex);
                double low = dataset.getLowValue(seriesIndex, itemIndex);
                double open = dataset.getOpenValue(seriesIndex, itemIndex);
                double close = dataset.getCloseValue(seriesIndex, itemIndex);
                double high = dataset.getHighValue(seriesIndex, itemIndex);

                JSONObject dataPoint = new JSONObject();
                dataPoint.put("x", date.getTime());
                dataPoint.put("low", low);
                dataPoint.put("open", open);
                dataPoint.put("close", close);
                dataPoint.put("high", high);

                jsonArray.put(dataPoint);
            }
        }

        return jsonArray.toString();
    }
}
