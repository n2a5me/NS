package com.appota.app.spinmachine.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appota.app.spinmachine.R;
import com.appota.app.spinmachine.network.API;
import com.appota.app.spinmachine.object.Ads;
import com.appota.app.spinmachine.object.GreenTym;
import com.appota.app.spinmachine.object.PurpleTym;
import com.appota.app.spinmachine.object.Reward;
import com.appota.app.spinmachine.object.Spin;
import com.appota.app.spinmachine.util.CommonStatic.REWARD;
import com.appota.app.spinmachine.widget.GiftItem;

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
				Log.e(TAG, "error user status" + error_message);
				return false;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static Spin getSpinUserData(String str) {
		Spin spin=null;
//		///
//		GreenTym _greenTym = new GreenTym(1, true);
//		String _gameToken = "";
//		PurpleTym _purpleTym = new PurpleTym(2, true);
//		Ads _ads=new Ads("", "", "", "", "");
//		int _timeavailable=232332;
//		Spin _spin = new Spin(_purpleTym, _greenTym, _gameToken,
//				true,_timeavailable,_ads);
//		return _spin;
//		///
		
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
				JSONObject freeSpin = spinJsonObject
						.getJSONObject("free_spin");
				boolean freeSpinStatus=freeSpin.getBoolean("status");
				int free_time_available=freeSpin.getInt("time_available");
				
				JSONObject silverSpin = spinJsonObject.getJSONObject("sivler_spin");
				boolean silverSpinStatus=silverSpin.getBoolean("status");
				int silver_spin_purple_tym=silverSpin.getInt("purple_tym_available");
				int silver_spin_bet_purple=silverSpin.getInt("bet_purple_tym");
				
				JSONObject goldSpin = spinJsonObject.getJSONObject("gold_spin");
				boolean goldSpinStatus=goldSpin.getBoolean("status");
				int gold_spin_green_tym=goldSpin.getInt("green_tym_available");
				int gold_spin_bet_green=goldSpin.getInt("bet_green_tym");
				int gold_ticket=goldSpin.getInt("gold_ticket");
				int yellow_tym=settingsObj.getInt("yellow_tym_available");
				String gameToken = settingsObj.getString("game_token");
				spin=new Spin();
				spin.setAds(ads);
				spin.setFree_spin_status(freeSpinStatus);
				spin.setFree_spin_time_available(free_time_available);
				spin.setSilver_spin_status(silverSpinStatus);
				spin.setSilver_spin_purple_tym(silver_spin_purple_tym);
				spin.setSilver_spin_bet_purple_tym(silver_spin_bet_purple);
				spin.setGold_spin_status(goldSpinStatus);
				spin.setGold_spin_green_tym(gold_spin_green_tym);
				spin.setGold_spin_bet_green_tym(gold_spin_bet_green);
				spin.setGold_spin_gold_ticket(gold_ticket);
				spin.setGame_token(gameToken);
				spin.setYellow_tym(yellow_tym);
				return spin;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return spin;
	}
	public static Reward getRewardData(String str) {
		if (checkUserStatus(str)) {
			Reward reward = null;
			ArrayList<GiftItem> gifts=new ArrayList<GiftItem>();
			try {
				JSONObject jsonObject = new JSONObject(str);
				JSONObject dataJsonObject = jsonObject.getJSONObject("data");
				JSONObject _reward=dataJsonObject.getJSONObject("reward");
				String type=_reward.getString("type");
				if(type.equalsIgnoreCase(REWARD.giftbox.toString()))
				{
					JSONArray giftsArr=_reward.getJSONArray("rewards");
					//add gift
					for(int i=0;i<giftsArr.length();i++)
					{
						GiftItem gift=new GiftItem();
						String _type=giftsArr.getJSONObject(i).getString("type");
						String _value=giftsArr.getJSONObject(i).getString("value");
						String _desctiption=giftsArr.getJSONObject(i).getString("description");
						gift.setType(_type);
						int src=R.drawable.reward_8_2x;
						if(_type.equals("purple_tym"))
						{
							src=R.drawable.reward_6_2x;
						}else if(_type.equals("yellow_tym"))
						{
							src=R.drawable.reward_5_2x;
						}else if(_type.equals("green_tym"))
						{
							src=R.drawable.reward_7_2x;
						}else if(_type.equals("gold_ticket"))
						{
							src=R.drawable.bonus_gold_ticket2x;
						}else if(_type.equals("apple_giftcard"))
						{
							src=R.drawable.reward_3_2x;
						}else if(_type.equals("google_giftcard"))
						{
							src=R.drawable.reward_8_2x;
						}else if(_type.equals("viettel_phonecard"))
						{
							src=R.drawable.reward_8_2x;
						}else if(_type.equals("vinaphone_phonecard"))
						{
							src=R.drawable.reward_8_2x;
						}
						else if(_type.equals("mobifone_phonecard"))
						{
							src=R.drawable.reward_8_2x;
						}
						gift.setDescription(_desctiption);
						gift.setValue(_value);
						gift.setSrc(src);
						gifts.add(gift);
					}
				}	int new_purple_tym=dataJsonObject.getInt("new_purple_tym");
					int new_green_tym=dataJsonObject.getInt("new_green_tym");
					int new_yellow_tym=dataJsonObject.getInt("new_yellow_tym");
					String description = _reward.getString("description");
					String code="";
					if(_reward.has("code"))
					{
						code=_reward.getString("code");
					}
					int value=0;
					if(_reward.has("value"))
					{
						value=_reward.getInt("value");
					}
					reward=new Reward(description,type,value,new_purple_tym,new_green_tym,new_yellow_tym);
					reward.setCode(code);
					reward.setGifts(gifts);
				
				
			} catch (JSONException e) {
				e.printStackTrace();
				
			}
			return reward;
		}else
		{
			return null;
		}

}
}
