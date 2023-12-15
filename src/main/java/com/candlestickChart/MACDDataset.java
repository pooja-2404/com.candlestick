package com.candlestickChart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

public class MACDDataset {

	public static String convertMACDtoJson(Date[] datear, ArrayList<Float> list) {

		ArrayList<Float> macdLine = new ArrayList<>();
		ArrayList<Float> macd26 = new ArrayList<>();
		ArrayList<Float> macd12 = new ArrayList<>();
		ArrayList<Float> signalLine = new ArrayList<>();
		JSONArray jsonArray = new JSONArray();

		macd26 = calculateEMA(list, 26);
		macd12 = calculateEMA(list, 12);

		for (int i = 0; i < macd26.size(); i++) {
			float macd_val = macd12.get(i) - macd26.get(i);
			macdLine.add(macd_val);
		}
		signalLine = calculateEMA(macdLine, 9);
		//System.out.println("macd26: " + macd26);
		//System.out.println("macd12: " + macd12);
		
		double[] macdLinedata = new double[macdLine.size()];
        for (int i = 0; i < macdLine.size(); i++) {
            macdLinedata[i] = macdLine.get(i).doubleValue();
        }
        double[] signalLinedata = new double[signalLine.size()];
        for (int i = 0; i < signalLine.size(); i++) {
            signalLinedata[i] = signalLine.get(i).doubleValue();
        }
        double[] closeList = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            closeList[i] = list.get(i).doubleValue();
        }
        //System.out.println("macd: " + macdLinedata);
		///System.out.println("signal: " + signalLinedata);
		jsonArray=MACDDatasettoJson.convertMACDDatasetToJSON(macdLinedata,signalLinedata,datear,closeList);
		return jsonArray.toString();

	}

	private static ArrayList<Float> calculateEMA(List<Float> list, float period) {
		// 26,12,9

		float smoothing_factor = 2 / (1 + period);

		float ema = 0;
		if (period == 26) {
			ArrayList<Float> macdLine_26 = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					ema = list.get(i) * smoothing_factor;
					macdLine_26.add(ema);
				} else {
					ema = list.get(i) * smoothing_factor + macdLine_26.get(i - 1) * (1 - smoothing_factor);
					macdLine_26.add(ema);
				}
			}

			return macdLine_26;
		} else if (period == 12) {
			ArrayList<Float> macdLine_12 = new ArrayList<>();

			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					ema = list.get(i) * smoothing_factor;
					macdLine_12.add(ema);
				} else {
					ema = list.get(i) * smoothing_factor + macdLine_12.get(i - 1) * (1 - smoothing_factor);
					macdLine_12.add(ema);
				}
			}

			return macdLine_12;
		} else if (period == 9) {
			ArrayList<Float> macdLine_9 = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {

					ema = list.get(i) * smoothing_factor;
					macdLine_9.add(ema);
				} else {
					ema = list.get(i) * smoothing_factor + macdLine_9.get(i - 1) * (1 - smoothing_factor);
					macdLine_9.add(ema);
				}

			}

			return macdLine_9;
		}

		return null;
	}

}
