package com.appota.app.spinmachine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appota.app.spinmachine.network.HttpHelper;
import com.appota.app.spinmachine.object.Spin;
import com.appota.app.spinmachine.util.CommonStatic;
import com.appota.app.spinmachine.util.JsonUtil;

public class FirstScreen extends Activity {
	private ViewFlipper mFlipperBronze, mFlipperGold, mFlipperSilver;
	private LinearLayout bronzeLayout, silverLayout, goldLayout;
	private RelativeLayout firstScreenLayout;
	private ProgressDialog proDialog;
	private boolean serverError = false, _onPause = false, flag_return = true;
	private TextView goldTicketTotal, greenTYMTotal, purpleTYMTotal,
			goldTYMTotal, goldTicketCard, green_tym_bet, purple_tym_bet,freeTextview;
	private String game_token = null;
	private ImageView bronzeTween, goldCardTicketBlink;
	private Handler handler;
	private String access_token = null;
	private Spin mSpin;
	private LinearLayout coverLayout;
	private SharedPreferences shared;
	private AlertDialog dialog;
	private ProgressDialog dialog_getgametoken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_screen_layout);
		firstScreenLayout = (RelativeLayout) findViewById(R.id.firstScreenLayout);
		coverLayout = (LinearLayout) findViewById(R.id.coverLayout);
		ImageView closeAppBtn = (ImageView) findViewById(R.id.closeAppBtn);
		goldTicketTotal = (TextView) findViewById(R.id.goldTicketTotal);
		greenTYMTotal = (TextView) findViewById(R.id.greenTYMTotal);
		purpleTYMTotal = (TextView) findViewById(R.id.purpleTYMTotal);
		goldTYMTotal = (TextView) findViewById(R.id.goldTYMTotal);
		goldTicketCard = (TextView) findViewById(R.id.goldTicketCard);
		green_tym_bet = (TextView) findViewById(R.id.green_tym_bet);
		freeTextview = (TextView) findViewById(R.id.freeTextview);
		purple_tym_bet = (TextView) findViewById(R.id.purple_tym_bet);
		bronzeTween = (ImageView) findViewById(R.id.bronzeTween);
		goldCardTicketBlink = (ImageView) findViewById(R.id.goldCardTicketBlink);
		shared = getSharedPreferences("spinPreferences", MODE_PRIVATE);
		if (checkAccessTokenAvailable(shared.getString("expires_in", ""))) {
			access_token = shared.getString("access_token", "");
		}
		closeAppBtn.setOnClickListener(closeDailySpinListener);
		bronzeLayout = (LinearLayout) findViewById(R.id.bronzeLayout);
		bronzeLayout.setOnClickListener(freeGotoMainScreenListener);
		silverLayout = (LinearLayout) findViewById(R.id.silverLayout);
		goldLayout = (LinearLayout) findViewById(R.id.goldLayout);
		goldLayout.setOnClickListener(goldGotoMainScreenListener);
		silverLayout.setOnClickListener(silverGotoMainScreenListener);
		mFlipperBronze = ((ViewFlipper) this.findViewById(R.id.flipperbronze));
		mFlipperSilver = ((ViewFlipper) this.findViewById(R.id.flippersilver));
		mFlipperGold = ((ViewFlipper) this.findViewById(R.id.flippergold));
		mFlipperBronze.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipperBronze.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		//
		mFlipperSilver.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipperSilver.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		//
		mFlipperGold.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipperGold.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		//
		mFlipperBronze.startFlipping();
		mFlipperSilver.startFlipping();
		mFlipperGold.startFlipping();
		proDialog = new ProgressDialog(FirstScreen.this);
		proDialog.setMessage(getResources().getString(
				R.string.getting_data_from_server));
		proDialog.setCancelable(false);
		dialog_getgametoken=new ProgressDialog(FirstScreen.this);
		dialog_getgametoken.setMessage(getResources().getString(R.string.checkingServer));
		dialog_getgametoken.setCancelable(false);
		handler = new Handler();
		_onPause = false;

		if (isOnline()) {
			new GetUserDataAsync().execute(new Void[] {});
		} else {
			AlertDialog.Builder builder = new Builder(FirstScreen.this);
			builder.setMessage(
					getResources().getString(
							R.string.no_network_connection_for_starting))
					.setTitle(
							getResources().getString(
									R.string.message_title_dialog))
					.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
									dialog.dismiss();
								}
							});
			builder.show();
		}
//		 // Add code to print out the key hash
//	    try {
//	        PackageInfo info = getPackageManager().getPackageInfo(
//	                "com.appota.app.spinmachine", 
//	                PackageManager.GET_SIGNATURES);
//	        for (Signature signature : info.signatures) {
//	            MessageDigest md = MessageDigest.getInstance("SHA");
//	            md.update(signature.toByteArray());
//	            Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//	            }
//	    } catch (NameNotFoundException e) {
//
//	    } catch (NoSuchAlgorithmException e) {
//
//	    }
	    ///
//	    Intent shortcutIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://apstore.vn"));
//	    Intent addIntent = new Intent();
//	    addIntent
//	            .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "AppStoreVn");
//	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
//	            Intent.ShortcutIconResource.fromContext(getApplicationContext(),
//	                    R.drawable.reward_6_2x));
//	    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//	    getApplicationContext().sendBroadcast(addIntent);
	    ///

	}
	
	private boolean checkAccessTokenAvailable(String access_token_exprired) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss z");
		Date date;
		Log.e("AccessTokenExpried",""+access_token_exprired);
		try {
			
			date = simpleDateFormat.parse(access_token_exprired);
			if (access_token_exprired != null & date.compareTo(new Date()) < 0) {
				return false;
			} else {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	OnClickListener freeGotoMainScreenListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!mSpin.isFree_spin_status()) {
				showDialogAlert("",
						getResources().getString(R.string.nofreespin),
						onClickFreeSpinListener, false);
			} else {

				showDialogAlert("",
						getResources().getString(R.string.start_free_spin),
						onClickFreeSpinListener, true);
			}

		}
	};
	OnClickListener silverGotoMainScreenListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!mSpin.isSilver_spin_status()&&mSpin.getSilver_spin_purple_tym()<mSpin.getSilver_spin_bet_purple_tym()) {
				showDialogAlert("",
						getResources().getString(R.string.not_enough_tym) + "("
								+ mSpin.getSilver_spin_bet_purple_tym()
								+ getResources().getString(R.string.tym_purple)
								+ ")", onClickSilverSpinListener, false);
			} else {
				showDialogAlert(
						"",
						getResources()
								.getString(R.string.require_number_of_tym)
								+ ":"
								+ mSpin.getSilver_spin_bet_purple_tym()
								+ getResources().getString(R.string.tym_purple),
						onClickSilverSpinListener, true);
			}

		}
	};
	public class GetGameToken extends AsyncTask<Void, Void, String>
	{
		public String spin_type;
		public GetGameToken(String spin_type)
		{
			this.spin_type=spin_type;
		}
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String res_User_Data = HttpHelper.getUserData(access_token);
			Spin spin = JsonUtil.getSpinUserData(res_User_Data);
			if(spin!=null)
			{
				mSpin.setGame_token(spin.getGame_token());
				game_token=spin.getGame_token();
				return spin.getGame_token();
			}else
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			dialog_getgametoken.dismiss();
			if(result!=null)
			{
				continueSpinAfterGettingnewGameToken(spin_type);
			}else
			{
				showDialogAlert("", getResources().getString(R.string.checkingServerFailed),new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
					
					
				}, false);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog_getgametoken.show();
		}
		
	}
	public class GetGameTokenOnActivityResume extends AsyncTask<Void, Void, String>
	{
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.e("GetGameTokenOnActivityResume", "doInBackground");
			mSpin.setGame_token(null);
			game_token=null;
			String res_User_Data = HttpHelper.getUserData(access_token);
			Spin spin = JsonUtil.getSpinUserData(res_User_Data);
			if(spin!=null)
			{
				mSpin.setGame_token(spin.getGame_token());
				game_token=spin.getGame_token();
				return spin.getGame_token();
			}else
			return null;
		}

		
	}
	OnClickListener goldGotoMainScreenListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!mSpin.isGold_spin_status()&&mSpin.getGold_spin_green_tym()<mSpin.getGold_spin_bet_green_tym()) {
				showDialogAlert("",
						getResources().getString(R.string.not_enough_tym) + "("
								+ mSpin.getGold_spin_bet_green_tym()
								+ getResources().getString(R.string.tym_green)
								+ ")", onClickGoldSpinListener, false);
			} else {
				showDialogAlert("",
						getResources()
								.getString(R.string.require_number_of_tym)
								+ " "
								+ mSpin.getGold_spin_bet_green_tym()
								+ getResources().getString(R.string.tym_green),
						onClickGoldSpinListener, true);
			}
		}
	};
	DialogInterface.OnClickListener onClickGoldSpinListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			Log.e("onClickGoldSpinListener", "onClickGoldSpinListener");
			if (mSpin.isGold_spin_status()) {
				if(game_token==null)
				{
					new GetGameToken(CommonStatic.BET.gold_spin.toString()).execute();
					return;
				}
				Intent intent = new Intent(FirstScreen.this,
						SlotMachineActivity.class);
				intent.putExtra("bet", CommonStatic.BET.gold_spin.toString());
				intent.putExtra("game_token", game_token);
				intent.putExtra("access_token", access_token);
				intent.putExtra("theme", shared.getString("theme", ""));
				startActivityForResult(intent,1);
			}
		}
	};
	DialogInterface.OnClickListener onClickSilverSpinListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			Log.e("onClickSilverSpinListener", "onClickSilverSpinListener");
			if (mSpin.isSilver_spin_status()) {
				if(game_token==null)
				{
					new GetGameToken(CommonStatic.BET.silver_spin.toString()).execute();
					return;
				}
				Intent intent = new Intent(FirstScreen.this,
						SlotMachineActivity.class);
				intent.putExtra("bet", CommonStatic.BET.silver_spin.toString());
				intent.putExtra("game_token", game_token);
				intent.putExtra("access_token", access_token);
				intent.putExtra("theme", shared.getString("theme", ""));
				startActivityForResult(intent,1);
			}
		}
	};
	DialogInterface.OnClickListener onClickFreeSpinListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			Log.e("onClickSilverSpinListener", "onClickSilverSpinListener");
			
			if (mSpin.isFree_spin_status()) {
				if(game_token==null)
				{
					new GetGameToken(CommonStatic.BET.free_spin.toString()).execute();
					return;
				}
				Intent intent = new Intent(FirstScreen.this,
						SlotMachineActivity.class);
				intent.putExtra("bet", CommonStatic.BET.free_spin.toString());
				intent.putExtra("game_token", game_token);
				intent.putExtra("access_token", access_token);
				intent.putExtra("theme", shared.getString("theme", ""));
				startActivityForResult(intent,1);
			}
		}
	};
	private void continueSpinAfterGettingnewGameToken(String gametype)
	{
		Intent intent = new Intent(FirstScreen.this,
				SlotMachineActivity.class);
		intent.putExtra("bet", gametype);
		intent.putExtra("game_token", game_token);
		intent.putExtra("access_token", access_token);
		intent.putExtra("theme", shared.getString("theme", ""));
		startActivityForResult(intent,1);
	}
	public class GetUserDataAsync extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
			// handler.post(new Runnable() {
			// public void run() {
			if (!proDialog.isShowing())
				proDialog.show();
			// }
			// });

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result) {
				AlertDialog.Builder builder = new Builder(FirstScreen.this);
				builder.setMessage(
						getResources().getString(
								R.string.couldnot_retrieve_data))
						.setTitle(
								getResources().getString(
										R.string.message_title_dialog))
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										finish();
										dialog.dismiss();
									}
								});
				builder.show();

			} else {
				coverLayout.setVisibility(View.GONE);
				purpleTYMTotal.setText(mSpin.getSilver_spin_purple_tym() + "");
				greenTYMTotal.setText(mSpin.getGold_spin_green_tym() + "");
				goldTYMTotal.setText(mSpin.getYellow_tym() + "");
				goldTicketTotal.setText(mSpin.getGold_spin_gold_ticket() + "");
				purple_tym_bet.setText(mSpin.getSilver_spin_bet_purple_tym()
						+ "");
				green_tym_bet.setText(mSpin.getGold_spin_bet_green_tym() + "");
				if (mSpin.getGold_spin_gold_ticket() > 0) {
					goldTicketCard.setText("X1");
					Animation myBlinkAnimation = AnimationUtils.loadAnimation(
							FirstScreen.this, R.anim.rotategoldcard);
					goldCardTicketBlink.startAnimation(myBlinkAnimation);
					goldTicketCard.startAnimation(myBlinkAnimation);
				} else {
					goldTicketCard.setText("X0");
					goldCardTicketBlink.setAnimation(null);
					goldTicketCard.setAnimation(null);
				}
				if (mSpin.isFree_spin_status()) {
					Animation myFadeInAnimation = AnimationUtils.loadAnimation(
							FirstScreen.this, R.anim.tween);
					bronzeTween.startAnimation(myFadeInAnimation);
					freeTextview.setText(getResources().getString(R.string.free));
				} else {
					bronzeTween.setAnimation(null);
					freeTextview.setText(getResources().getString(R.string.used));
				}
				informUserFreeSpin();
			}
			if (proDialog.isShowing()) {
				try {
					proDialog.dismiss();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			boolean status = false;
			try {
				status = initUser();
			} catch (Exception e) {
				serverError = true;
				e.printStackTrace();
				status = false;

			}
			return status;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		closeDailySpin();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		_onPause = true;
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data.getExtras().containsKey("new_purple_tym")) {
			int new_purple_tym=data.getIntExtra("new_purple_tym",mSpin.getSilver_spin_purple_tym());
			purpleTYMTotal.setText(new_purple_tym+"");
			mSpin.setSilver_spin_purple_tym(new_purple_tym);
		}
		if (data.getExtras().containsKey("new_green_tym")) {
			int new_green_tym=data.getIntExtra("new_green_tym",mSpin.getGold_spin_green_tym());
			greenTYMTotal.setText(new_green_tym+"");
			mSpin.setGold_spin_green_tym(new_green_tym);
		}
		if (data.getExtras().containsKey("new_yellow_tym")) {
			int new_yellow_tym=data.getIntExtra("new_yellow_tym",mSpin.getYellow_tym());
			goldTYMTotal.setText(new_yellow_tym+"");
			mSpin.setYellow_tym(new_yellow_tym);
		}
		if (data.getExtras().containsKey("is_used_free_spin")) {
			boolean is_used_free_spin=data.getBooleanExtra("is_used_free_spin",false);
			mSpin.setFree_spin_status(!is_used_free_spin);
			Log.e("4Result", "is_used_free_spin:"+is_used_free_spin);
			if(!is_used_free_spin)
			{
				Animation myFadeInAnimation = AnimationUtils.loadAnimation(
						FirstScreen.this, R.anim.tween);
				bronzeTween.startAnimation(myFadeInAnimation);
				freeTextview.setText(getResources().getString(R.string.free));
			}else
			{
				bronzeTween.setAnimation(null);
				freeTextview.setText(getResources().getString(R.string.used));
			}
		}
		//request game_token for new spin
		new GetGameTokenOnActivityResume().execute();
	}

	public boolean initUser() {
		flag_return = true;
		// String username = "machequyhon2012";
		// String password = "tamthanphanliet";
//		 String username = "n2a5me";
//		 String password = "n123456789";
//		 String username = "ngoctest";
//		 String password = "n12345";
//		String username = "n2a5me2";
//		String password = "n123456789";
//		String username = "n2a5me3";
//		String password = "n123456789";
//		String username = "n2a5me4";
//		String password = "n123456789";
//		String username = "n2a5me5";
//		String password = "n123456789";
//		String username = "tgioihan1";
//		String password = "123456";
		String username = "toantq";
		String password = "12345678";
		String responseLogin = HttpHelper.loginUser(FirstScreen.this, username,
				password, access_token);
		Log.e("ResponseLogin", responseLogin);
		String expires_in = "";
		try {
			if(!responseLogin.equals(""))
			{
				JSONObject jsonObject = new JSONObject(responseLogin);
				expires_in = jsonObject.getString("expires_in");
			}else
			{
				Log.e("FirstScreen","Couldn't get response");
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (access_token == null) {
			access_token = JsonUtil.getAccessToken(responseLogin);
		}
		// Log.e("access_token",access_token);
		String res_User_Data = HttpHelper.getUserData(access_token);
		mSpin = JsonUtil.getSpinUserData(res_User_Data);

		if (mSpin == null) {
			flag_return = false;
			return flag_return;

		}
		final String previousTheme = shared.getString("themeurl", "");
		final String currentTheme = shared.getString("theme", "");
		SharedPreferences.Editor editor = shared.edit();
		editor.putString("game_token", mSpin.getGame_token());
		editor.putString("access_token", access_token);
		editor.putString("expires_in", expires_in);
		editor.commit();

		if (!previousTheme.equalsIgnoreCase(mSpin.getAds().getImage())) {
			DownloadFile downloadFile = new DownloadFile();
			downloadFile.execute(mSpin.getAds().getImage());
		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Drawable bgDrawable = Drawable.createFromPath(currentTheme);
					firstScreenLayout.setBackgroundDrawable(bgDrawable);
					Log.e("getThemeFromLocal", "path:" + currentTheme);
				}
			});

		}
		game_token = mSpin.getGame_token();
		return flag_return;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_screen, menu);
		return true;
	}

	private class DownloadFile extends AsyncTask<String, Integer, String> {
		boolean get_theme_success = false;

		@Override
		protected String doInBackground(String... sUrl) {
			try {
				Log.e("DownloadThemeURL", sUrl[0].toString());
				URL url = new URL(sUrl[0]);
				URLConnection connection = url.openConnection();
				connection.connect();
				// this will be useful so that you can show a typical 0-100%
				// progress bar
				int fileLength = connection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				PackageManager m = getPackageManager();
				String fp = getPackageName();
				try {
					PackageInfo p = m.getPackageInfo(fp, 0);
					fp = p.applicationInfo.dataDir;
				} catch (NameNotFoundException e) {
					Log.e("NameNotFoundException",
							"Error Package name not found ", e);
				}
				File folder = new File(fp);
				Log.e("DownloadFile", "AppDataDir:" + folder.getAbsolutePath());
				boolean success = true;
				String filePath = null;
				if (!folder.exists()) {
					success = folder.mkdir();
				}
				if (success) {
					// Do something on success
					String _url = mSpin.getAds().getImage();
					int lastSplash = 0;
					for (int i = _url.length() - 1; i > 0; i--) {
						if (_url.charAt(i) == '/') {
							lastSplash = i;
							break;
						}
					}
					String fileName = _url.substring(lastSplash + 1,
							_url.length());
					Log.e("ThemeFileName", fileName);
					filePath = folder.getAbsolutePath() + "/" + fileName;
					Log.e("FilePath", filePath);
				} else {
					// Do something else on failure
				}
				SharedPreferences.Editor editor = shared.edit();
				editor.putString("themeurl", mSpin.getAds().getImage());
				editor.commit();
				mSpin.getAds().setImage("");
				OutputStream output = new FileOutputStream(filePath);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
				mSpin.getAds().setImage(filePath);
				output.flush();
				output.close();
				input.close();
				get_theme_success = true;
			} catch (Exception e) {
				e.printStackTrace();
				get_theme_success = false;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			if (progress[0] == 100) {

			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (get_theme_success) {
				Drawable bgDrawable = Drawable.createFromPath(mSpin.getAds()
						.getImage());
				firstScreenLayout.setBackgroundDrawable(bgDrawable);
				SharedPreferences.Editor editor = shared.edit();
				editor.putString("theme", mSpin.getAds().getImage());
				editor.commit();
				Log.e("getThemFromServer", "getThemFromServer");
			} else {
				Toast.makeText(FirstScreen.this, "Couldn't get theme",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	// Check whether the device's network is available.
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	private void informUserFreeSpin() {
		if (!serverError) {
			if (mSpin.isFree_spin_status()) {
				AlertDialog.Builder info = new AlertDialog.Builder(
						FirstScreen.this);
				info.setTitle(R.string.new_free_spin);
				info.setMessage(R.string.onemorefreespin);
				info.setPositiveButton(getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
				if (!_onPause) {
					info.show();
				}
				serverError = false;
			}
//			else {
//				AlertDialog.Builder info = new AlertDialog.Builder(
//						FirstScreen.this);
//				info.setTitle(R.string.message_title_dialog);
//				info.setMessage(R.string.nofreespin);
//				info.setPositiveButton(R.string.ok,
//						new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//
//							}
//						});
//				if (!_onPause) {
//					info.show();
//				}
//			}
		} else {
			AlertDialog.Builder info = new AlertDialog.Builder(FirstScreen.this);
			info.setTitle(R.string.message_title_dialog);
			info.setMessage(R.string.couldnot_retrieve_data);
			info.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			if (!_onPause) {
				info.show();
			}
		}

	}

	private void showDialogAlert(String title, String message,
			DialogInterface.OnClickListener confirmListener,
			boolean viewCancelButton) {
		AlertDialog.Builder builder = new Builder(FirstScreen.this);
		if (!title.equals("")) {
			builder.setTitle(title);
		}
		builder.setMessage(message);
		if (viewCancelButton) {
			builder.setNegativeButton(
					getResources().getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
		}
		builder.setPositiveButton(getResources().getString(R.string.ok),
				confirmListener);
		builder.show();
	}

	private OnClickListener closeDailySpinListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			closeDailySpin();
		}
	};

	private void closeDailySpin() {
		AlertDialog.Builder builder = new Builder(FirstScreen.this);
		builder.setMessage(R.string.close_app_confirm);
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		builder.show();
	}
}
