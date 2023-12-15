package com.candlestickChart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;

public class RSIDataset {

	public static String convertRSItoJson(Date[] datear, ArrayList<Float> list) {
		JSONArray jsonArray = new JSONArray();
		ArrayList<Float> gain = new ArrayList<>();
		ArrayList<Float> loss = new ArrayList<>();
		ArrayList<Float> relativeStrength = new ArrayList<>();
		ArrayList<Float> relativeStrengthIndex = new ArrayList<>();
		ArrayList<Float> avg_Gain = new ArrayList<>();
		ArrayList<Float> avg_Loss = new ArrayList<>();
		gain.add(0, (float) 0);
		loss.add(0, (float) 0);

		for (int i = 1; i < list.size(); i++) {
			float difference = list.get(i) - list.get(i - 1);
			if (difference >= 0) {
				gain.add(i, difference);
				loss.add(i, (float) 0);

			} else {
				loss.add(i, Math.abs(difference));
				gain.add(i, (float) 0);
			}
		}
		//System.out.println("Gain: " + gain);
		//System.out.println("Loss: " + loss);
		// find average gain
		int period = 14;
		avg_Gain = calculateAverageGain(gain, period);
		//System.out.println("averagegain:" + avg_Gain.size() + " " + avg_Gain);
		// find average loss
		avg_Loss = calculateAverageLoss(loss, period);
		//System.out.println("averageloss: " + avg_Loss.size() + " " + avg_Loss);
		// Find RS

		relativeStrength = calculateRS(avg_Gain, avg_Loss, period);
		//System.out.println("relativeStrength: " + relativeStrength.size() + " " + relativeStrength);

		// Find RSI
		relativeStrengthIndex = calculateRSI(relativeStrength, period);
		
		double[] relativeStrengthIndexdata = new double[relativeStrengthIndex.size()];
        for (int i = 0; i < relativeStrengthIndex.size(); i++) {
            relativeStrengthIndexdata[i] = relativeStrengthIndex.get(i).doubleValue();
        }
        double[] closeList = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            closeList[i] = list.get(i).doubleValue();
        }
        //System.out.println("relativeStrengthIndex: " + relativeStrengthIndex.size() + " " + relativeStrengthIndexdata);
        jsonArray=RSIDatasettoJson.convertRSIDatasetToJSON(relativeStrengthIndexdata,datear,closeList);
		return jsonArray.toString();
	}

	private static ArrayList<Float> calculateAverageLoss(ArrayList<Float> loss, int period) {
		ArrayList<Float> avg_Loss = new ArrayList<>();
		float loss_13period = 0f;
		for (int i = 0; i < period - 1; i++) {
			avg_Loss.add((float) 0);
			loss_13period += loss.get(i);
		}
		for (int i = period - 1; i < loss.size(); i++) {
			if (i == period - 1)
				avg_Loss.add((loss_13period + loss.get(i)) / period);
			else {
				avg_Loss.add(((avg_Loss.get(i - 1) * 13) + loss.get(i)) / period);
			}

		}
		return avg_Loss;

	}

	private static ArrayList<Float> calculateAverageGain(ArrayList<Float> gain, int period) {
		ArrayList<Float> avg_Gain = new ArrayList<>();
		float gain_13period = 0f;
		for (int i = 0; i < period - 1; i++) {
			avg_Gain.add((float) 0);
			gain_13period += gain.get(i);
		}
		for (int i = period - 1; i < gain.size(); i++) {
			if (i == period - 1)
				avg_Gain.add((gain_13period + gain.get(i)) / period);
			else {
				avg_Gain.add(((avg_Gain.get(i - 1) * 13) + gain.get(i)) / period);
			}

		}
		return avg_Gain;
	}

	private static ArrayList<Float> calculateRSI(ArrayList<Float> relativeStrength, int period) {
		ArrayList<Float> relativeStrengthIndex = new ArrayList<>();
		for (int i = 0; i < period - 1; i++) {
			relativeStrengthIndex.add(i, (float) 0);
		}
		for (int i = period - 1; i < relativeStrength.size(); i++) {
			float RSI = (float) (100 - (100.0 / (1 + relativeStrength.get(i))));
			relativeStrengthIndex.add(RSI);
		}
		return relativeStrengthIndex;
	}

	private static ArrayList<Float> calculateRS(List<Float> avg_Gain, List<Float> avg_Loss, int period) {
		ArrayList<Float> relativeStrength = new ArrayList<>();
		for (int i = 0; i < period - 1; i++) {
			relativeStrength.add(i, (float) 0);
		}
		for (int i = period - 1; i < avg_Gain.size(); i++) {
			float strength_value = avg_Gain.get(i) / avg_Loss.get(i);
			relativeStrength.add(strength_value);
		}
		return relativeStrength;

	}

}
