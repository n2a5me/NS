package com.appota.app.spinmachine.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.appota.app.spinmachine.util.CommonUtils;
import com.appota.app.spinmachine.util.JsonUtil;

public class HttpHelper {

	public static final String TAG = "HttpHelper";

	public static String getUserData() {
		String s = "";
		String lang = Locale.getDefault().getLanguage();
		if (!lang.equalsIgnoreCase("vi")) {
			lang = "en";
		}
		String requestUrl = API.GET_USER_DATA_URL + API.ACCESS_TOKEN + "="
				+ JsonUtil.getAccessToken(getAccesToken()) + "&" + API.LANG
				+ "=" + lang;
		Log.e(TAG, requestUrl);
		HttpGet httpGet = new HttpGet(requestUrl);
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpResponse response;
		try {
			response = client.execute(httpGet);
			Log.e("daff", "connect " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				s = CommonUtils.inputStreamToString(inputStream);
				Log.e(TAG, s);
				client.getConnectionManager().shutdown();
				client = null;
				return s;
			}
			client = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		}
		return s;
	}
	public static String getGiftBoxList(String access_token) {
		String s = "";
		String lang = Locale.getDefault().getLanguage();
		if (!lang.equalsIgnoreCase("vi")) {
			lang = "en";
		}
		String requestUrl = API.GET_GIFT_BOX_LIST_URL + API.ACCESS_TOKEN + "="
				+ access_token + "&" + API.LANG
				+ "=" + lang;
		Log.e(TAG, requestUrl);
		HttpGet httpGet = new HttpGet(requestUrl);
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpResponse response;
		try {
			response = client.execute(httpGet);
			Log.e("HttpHelperGetGiftBoxList", "connect " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				s = CommonUtils.inputStreamToString(inputStream);
				Log.e("HttpHelperGetGiftBoxList", s);
				client.getConnectionManager().shutdown();
				client = null;
				return s;
			}
			client = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		}
		return s;
	}
	public static String openGiftBox(int giftbox_id,String game_token, 
			String bet, String access_token) {
		String lang = Locale.getDefault().getLanguage();
		if (!lang.equalsIgnoreCase("vi")) {
			lang = "en";
		}
		String result = "";
		String url_request = API.OPEN_GIFT_BOX_URL + "?" + API.ACCESS_TOKEN
				+ "=" + access_token + "&lang="+lang;
		Log.e(TAG, url_request);
		HttpPost httpPost = new HttpPost(url_request);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ArrayList<NameValuePair> nameValuePairs = addGamePlayParameterForPost(
				game_token, bet);
		HttpClient client = HttpUtils.getNewHttpClient();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(httpPost);
			Log.e("OpenGiftBox", response.getStatusLine().getStatusCode() + "");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				result = CommonUtils.inputStreamToString(inputStream);
				client = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		}
		Log.e("OpenGiftBox", result);
		return result;
	}
	public static String getAccesToken() {
		String result = "";
		String url_request = API.ACCESS_TOKEN_URL;
		HttpPost httpPost = new HttpPost(url_request);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ArrayList<NameValuePair> nameValuePairs = addParameter();
		HttpClient client = HttpUtils.getNewHttpClient();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				result = CommonUtils.inputStreamToString(inputStream);
				client = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		}
		Log.e(TAG+"getAcessToken", result);
		return result;

	}

	public static ArrayList<NameValuePair> addParameter() {
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		String requestToken = JsonUtil.getRequestToken(getRequestToken());
		Log.e(TAG, requestToken);
		list.add(new BasicNameValuePair(API.REQUEST_TOKEN, requestToken));
		list.add(new BasicNameValuePair(API.CLIENT_ID, API.CLIENT_ID_VALUE));
		list.add(new BasicNameValuePair(API.CLIENT_SECRET,
				API.CLIENT_SECRET_VALUE));
		list.add(new BasicNameValuePair(API.REDIRECT_URI, API.REDIRECT_URI));
		list.add(new BasicNameValuePair(API.GRANT_TYPE, API.GRANT_TYPE_VALUE));
		list.add(new BasicNameValuePair(API.LANG, API.GRANT_TYPE_VALUE));
		return list;
	}

	public static String getRequestToken() {
		String result = "";
		String url_request = API.REQUEST_TOKEN_URI;
		Log.e(TAG, url_request);
		HttpGet httpGet = new HttpGet(url_request);
		HttpClient client = HttpUtils.getNewHttpClient();
		HttpResponse response;
		try {
			response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				result = CommonUtils.inputStreamToString(inputStream);
				Log.e(TAG+"-response from getRequestToken", result);
				client.getConnectionManager().shutdown();
				client = null;
				return result;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client.getConnectionManager().shutdown();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client.getConnectionManager().shutdown();
			client = null;
		}
		Log.e(TAG, result);
		return result;
	}

	public static String loginUser(Context context, String username,
			String password,String access_token) {
		String result = "";
		if(access_token==null)
		{
			access_token=JsonUtil.getAccessToken(getAccesToken());
		}
		String lang = Locale.getDefault().getLanguage();
		if (!lang.equalsIgnoreCase("vi")) {
			lang = "en";
		}
		String url_request = API.LOGIN_URL + "?" + API.ACCESS_TOKEN + "="
				+ access_token + "&lang="+lang;
		Log.e(TAG, url_request);
		HttpPost httpPost = new HttpPost(url_request);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ArrayList<NameValuePair> nameValuePairs = addLoginParameter(context,
				username, password);
		HttpClient client = HttpUtils.getNewHttpClient();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(httpPost);
			Log.e(TAG+"-response from loginUser", response.getStatusLine().getStatusCode() + "");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				result = CommonUtils.inputStreamToString(inputStream);
				client = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		}
		Log.e(TAG, result);
		return result;
	}

	public static String postGamePlay(String game_token, 
			String bet, String access_token) {
		String lang = Locale.getDefault().getLanguage();
		if (!lang.equalsIgnoreCase("vi")) {
			lang = "en";
		}
		String result = "";
		String url_request = API.GET_GAME_PLAY_URL + "?" + API.ACCESS_TOKEN
				+ "=" + access_token + "&lang="+lang;
		Log.e(TAG, url_request);
		HttpPost httpPost = new HttpPost(url_request);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ArrayList<NameValuePair> nameValuePairs = addGamePlayParameterForPost(
				game_token, bet);
		HttpClient client = HttpUtils.getNewHttpClient();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(httpPost);
			Log.e(TAG+"- response from postGamePlay", response.getStatusLine().getStatusCode() + "");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				result = CommonUtils.inputStreamToString(inputStream);
				client = null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		}
		Log.e(TAG, result);
		return result;
	}

	private static ArrayList<NameValuePair> addLoginParameter(Context context,
			String username, String password) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> rs = new ArrayList<NameValuePair>();
		rs.add(new BasicNameValuePair(API.USER_NAME, username));
		rs.add(new BasicNameValuePair(API.PASSWORD, password));
		rs.add(new BasicNameValuePair(API.DEVICE_ID, CommonUtils
				.getDeviceId(context)));
		rs.add(new BasicNameValuePair(API.DEVICE_OS, CommonUtils.getDeviceOs()));
		rs.add(new BasicNameValuePair(API.DEVICE_OS_VERSION, CommonUtils
				.getDeviceVersion()));
		rs.add(new BasicNameValuePair(API.VENDOR, CommonUtils.getVendor()));
		rs.add(new BasicNameValuePair(API.PHONE_NUMBER, CommonUtils
				.getPhoneNumber(context)));
		rs.add(new BasicNameValuePair(API.CLIENT_VERSION, CommonUtils
				.getClientVersion(context) + ""));
		return rs;
	}

	public static String getUserData(String access_token) {
		// TODO Auto-generated method stub
		String s = "";
		String requestUrl = API.GET_USER_DATA_URL + API.ACCESS_TOKEN + "="
				+ access_token + "&" + API.LANG + "=" + API.LANG_VALUE;
		Log.e(TAG, requestUrl);
		HttpPost httpPost = new HttpPost(requestUrl);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("version", String.valueOf(1)));
		HttpClient client = HttpUtils.getNewHttpClient();
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(httpPost);
			Log.e("daff", "connect " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				s = CommonUtils.inputStreamToString(inputStream);
				Log.e(TAG+"- getUserData", s);
				client.getConnectionManager().shutdown();
				client = null;
				return s;
			}
			client = null;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			client = null;
		}
		return s;
	}

	private static ArrayList<NameValuePair> addGamePlayParameterForPost(
			String game_token, String bet) {
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("game_token", game_token));
		list.add(new BasicNameValuePair("bet", bet));
		list.add(new BasicNameValuePair("version", "1"));
		list.add(new BasicNameValuePair("client", "android"));
		return list;
	}

}
