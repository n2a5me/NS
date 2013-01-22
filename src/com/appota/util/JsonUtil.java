package com.appota.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appota.network.API;
import com.appota.slotmachine.object.GreenTym;
import com.appota.slotmachine.object.PurpleTym;
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
				boolean status_spinner = freeJsonObject.getBoolean("status");
				Spin spin = new Spin(purpleTym, greenTym, gameToken,
						status_spinner);
				return spin;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

}
