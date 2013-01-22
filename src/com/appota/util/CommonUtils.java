package com.appota.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @author NGUYEN VAN DANG
 * 
 */
public class CommonUtils {
	private static final String TAG = CommonUtils.class.getName();

	public static String inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			Log.e(TAG, "Error read data");
			return null;
		}
		// Return full string
		return total.toString();
	}

	public static String getDeviceId(Context context) {
		TelephonyManager tManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String device_id = tManager.getDeviceId();
		Log.d("Device Id", device_id);
		return device_id;
	}

	public static String getDeviceVersion() {
		Log.d(TAG, android.os.Build.VERSION.RELEASE);
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getDeviceOs() {
		return "android";
	}

	public static String getVendor() {
		Log.d(TAG, android.os.Build.MANUFACTURER);
		return android.os.Build.MANUFACTURER;
	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager tMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		Log.d(TAG, tMgr.getLine1Number());
		return tMgr.getLine1Number();
	}

	public static int getClientVersion(Context context) {
		try {
			Log.d(TAG,
					context.getPackageManager().getPackageInfo(
							context.getPackageName(), 0).versionCode
							+ "");
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return -1;
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to device independent pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent db equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;

	}
}
