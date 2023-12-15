package com.candlestickChart;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;

public class StochasticDataset {

	public static String convertS_OscillatortoJson(Date[] datear, ArrayList<Float> listClose, ArrayList<Float> listHigh,
			ArrayList<Float> listLow) {
		JSONArray jsonArray = new JSONArray();
		ArrayList<Float> percent_K_Value = new ArrayList<Float>();
		ArrayList<Float> percent_D_Value = new ArrayList<Float>();
		float periodforK = 14f;
		float periodforD = 3f;
		// calculate %K value
		percent_K_Value = calculate_K_Value(listClose, listHigh, listLow, periodforK);
		// calculate %D value
		percent_D_Value = calculate_D_Value(percent_K_Value, periodforD, periodforK);
		
		
		double[] percent_K_Valuedata = new double[percent_K_Value.size()];
        for (int i = 0; i < percent_K_Value.size(); i++) {
           percent_K_Valuedata[i] = percent_K_Value.get(i).doubleValue();
        }
        double[] percent_D_Valuedata = new double[percent_D_Value.size()];
        for (int i = 0; i < percent_D_Value.size(); i++) {
            percent_D_Valuedata[i] = percent_D_Value.get(i).doubleValue();
        }
        double[] closeList = new double[listClose.size()];
        for (int i = 0; i < listClose.size(); i++) {
            closeList[i] = listClose.get(i).doubleValue();
        }
        
        //System.out.println("KValue: " + percent_K_Valuedata);
		//System.out.println("DValue: " +  percent_D_Valuedata);
		jsonArray=StochasticDatasettoJson.convertStochasticDatasetToJSON(percent_K_Valuedata,percent_D_Valuedata,datear,closeList);
		return jsonArray.toString();
	}

	private static ArrayList<Float> calculate_K_Value(ArrayList<Float> listClose, ArrayList<Float> listHigh,
			ArrayList<Float> listLow, float period) {
		ArrayList<Float> percent_K_Value = new ArrayList<Float>();
		// %K=(close-lowest low)/(highest high-lowest low)
		float lowestLow = 10000000f;
		float highestHigh = 0f;
		float closePrice = 0f;
		float percentK = 0f;
		for (int i = 0; i < listClose.size(); i++) {
			if (i < period - 1)
				percent_K_Value.add((float) 0);

			else {
				closePrice = listClose.get(i);
				for (int j = i; j >= i - period + 1; j--) {
					if (listLow.get(i) < lowestLow)
						lowestLow = listLow.get(i);
					if (listHigh.get(i) > highestHigh)
						highestHigh = listHigh.get(i);

				}
				percentK = ((closePrice - lowestLow) / (highestHigh - lowestLow)) * 100;
				percent_K_Value.add(percentK);
			}
		}

		return percent_K_Value;
	}

	private static ArrayList<Float> calculate_D_Value(ArrayList<Float> percent_K_Value, float period,
			float periodforK) {
		ArrayList<Float> percent_D_Value = new ArrayList<Float>();
		for (int i = 0; i < percent_K_Value.size(); i++) {
			if (percent_K_Value.get(i) == 0f) {
				percent_D_Value.add(percent_K_Value.get(i));
			} else {
				if (i == periodforK - 1) {
					percent_D_Value.add(percent_K_Value.get(i));
				} else if (i < periodforK - 1 + period - 1) {
					float value = 0f;
					int count = 0;
					for (int j = i; j >= periodforK - 1; j--) {
						count++;
						value = value + percent_K_Value.get(j);
					}
					value = (float) value / count;
					percent_D_Value.add(value);
				} else {
					float value = 0f;
					int j = 0;
					while (j < (int) period) {
						value = value + percent_K_Value.get(i - j);
						j++;
						
					}
					value = value / period;
					percent_D_Value.add(value);
				}
			}
		}
		return percent_D_Value;
	}

}
