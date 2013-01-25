package com.appota.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appota.network.API;
import com.appota.slotmachine.object.Ads;
import com.appota.slotmachine.object.GreenTym;
import com.appota.slotmachine.object.PurpleTym;
import com.appota.slotmachine.object.Reward;
import com.appota.slotmachine.object.Spin;

public class JsonUtil {

	public static final String TAG = "JsonUtil";

	public static String getRequestToken(String str) {
		String result = "";
		try {
			JSONObject jsonObject = new JSONObject(str);
			String state = jsonObject.getString(API.STATUS);
			if (state.equals("true")) {
				result = jsonObject.getString(API.REQUEST_TOKEN);
			} else {

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static String getAccessToken(String str) {
		String result = "";
		try {
			JSONObject jsonObject = new JSONObject(str);
			String state = jsonObject.getString(API.STATUS);
			if (state.equals("true")) {
				result = jsonObject.getString(API.ACCESS_TOKEN);
			} else {

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static boolean checkUserStatus(String str) {
		try {
			JSONObject jsonObject = new JSONObject(str);
			int error_code = jsonObject.getInt("error_code");
			if (error_code == 0) {
				return true;
			} else {
				String error_message = jsonObject.getString("error_message");
				Log.d(TAG, "error user status" + error_message);
				return false;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static Spin getSpinUserData(String str) {
		if (checkUserStatus(str)) {
			JSONObject spinJsonObject;
			try {
				JSONObject jsonObject = new JSONObject(str);
				JSONObject dataJsonObject = jsonObject.getJSONObject("data");
				spinJsonObject = dataJsonObject.getJSONObject("spin");
				JSONObject settingsObj=spinJsonObject.getJSONObject("settings");
				JSONObject adsObj=settingsObj.getJSONObject("ads");
				String description=adsObj.getString("description");
				String image=adsObj.getString("image");
				String type=adsObj.getString("type");
				String store=adsObj.getString("store");
				String uri=adsObj.getString("uri");
				Ads ads=new Ads(description, image, type, store, uri);
				JSONObject greenTymJsonObject = spinJsonObject
						.getJSONObject("bet_green_tym");
				String green_tym_value = greenTymJsonObject
						.getString("green_tym_available");
				boolean status_green_tym = greenTymJsonObject
						.getBoolean("status");
				GreenTym greenTym = new GreenTym(
						Integer.valueOf(green_tym_value), status_green_tym);
				JSONObject purpleTymJsonObject = spinJsonObject
						.getJSONObject("bet_purple_tym");
				String purple_tym_value = purpleTymJsonObject
						.getString("purple_tym_available");
				boolean status_purple_tym = purpleTymJsonObject
						.getBoolean("status");
				PurpleTym purpleTym = new PurpleTym(
						Integer.valueOf(purple_tym_value), status_purple_tym);
				JSONObject freeJsonObject = spinJsonObject
						.getJSONObject("free");
				JSONObject settingJsonObject = spinJsonObject
						.getJSONObject("settings");
				String gameToken = settingJsonObject.getString("game_token");
				int timeavailable=freeJsonObject.getInt("time_available");
				boolean status_spinner = freeJsonObject.getBoolean("status");
				Spin spin = new Spin(purpleTym, greenTym, gameToken,
						status_spinner,timeavailable,ads);
				return spin;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	public static Reward getRewardData(String str) {
//		if (checkUserStatus(str)) {
			JSONObject rewardJsonObject;
			try {
				JSONObject jsonObject = new JSONObject(str);
				JSONObject dataJsonObject = jsonObject.getJSONObject("data");
				rewardJsonObject = dataJsonObject.getJSONObject("reward");
				int new_purple_tym=dataJsonObject.getInt("new_purple_tym");
				int new_green_tym=dataJsonObject.getInt("new_green_tym");
				String description = rewardJsonObject.getString("description");
				String image = rewardJsonObject.getString("image");
				String gametype = rewardJsonObject.getString("game_type");
				int idreward = rewardJsonObject.getInt("id");
				Reward reward=new Reward(idreward, description,gametype, image,new_purple_tym,new_green_tym);
				return reward;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
//		}

	}


}
