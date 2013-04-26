package com.appota.app.spinmachine;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appota.app.spinmachine.adapter.AbstractWheelAdapter;
import com.appota.app.spinmachine.network.HttpHelper;
import com.appota.app.spinmachine.object.Reward;
import com.appota.app.spinmachine.util.JsonUtil;
import com.appota.app.spinmachine.util.CommonStatic.REWARD;
import com.appota.app.spinmachine.widget.OnWheelChangedListener;
import com.appota.app.spinmachine.widget.OnWheelScrollListener;
import com.appota.app.spinmachine.widget.WheelView;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;

public class SlotMachineActivity extends Activity {
	String game_token = null;
	String access_token = null;
	String theme = null;
	protected boolean isWin = false;
	private WheelView wheel1;
	private WheelView wheel2;
	private WheelView wheel3;
	private Reward receivedReward = null;
	private boolean willbeRecievedReward = true;
	private int indexOfReward = 1;
	private ProgressDialog proDialog;
	boolean isRunning = false;
	private SoundPool soundPool;
	private int soundSpinning;
	private int soundEndOfSpinning;
	private int soundWinning;
	private int soundMissedGift;
	private boolean _onPause = false;
	private String bet;
	private RelativeLayout slotmachineLayout;
	private ProgressDialog proDialogWaitingSpin;
	private boolean spinned = false;
	private boolean receive_free_ticket = false;
	private AlertDialog dialog;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStrictMode();
		init();
		initView();

	}

	public void init() {
		_onPause = false;
		Intent receivedSpinType = getIntent();
		bet = receivedSpinType.getStringExtra("bet");
		game_token = receivedSpinType.getStringExtra("game_token");
		access_token = receivedSpinType.getStringExtra("access_token");
		theme = receivedSpinType.getStringExtra("theme");
		soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 128);
		soundSpinning = soundPool.load(getContext(), R.raw.spinning, 1);
		soundEndOfSpinning = soundPool
				.load(getContext(), R.raw.stopspinning, 1);
		soundMissedGift=soundPool.load(getContext(), R.raw.missedgift, 1);
		soundWinning = soundPool.load(getContext(), R.raw.cheering, 1);
		proDialog = new ProgressDialog(this);
		proDialogWaitingSpin = new ProgressDialog(this);
		proDialogWaitingSpin.setCancelable(false);
		proDialogWaitingSpin.setMessage(getResources().getString(
				R.string.getting_data_from_server));
		progressDialog=new ProgressDialog(SlotMachineActivity.this);
	}

	private Context getContext() {
		return SlotMachineActivity.this;
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.slot_machine_layout);
		slotmachineLayout = (RelativeLayout) findViewById(R.id.slotmachineLayout);
		Drawable themeDrawable = Drawable.createFromPath(theme);
		ImageView logoBlink=(ImageView)findViewById(R.id.logoBlink);
		Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.tween);
		logoBlink.startAnimation(myFadeInAnimation);
		slotmachineLayout.setBackgroundDrawable(themeDrawable);
		wheel1 = initWheel(R.id.slot_1);
		wheel2 = initWheel(R.id.slot_2);
		wheel3 = initWheel(R.id.slot_3);
		onStartSpin();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

	}

	protected void onStartSpin() {

		new PostGamePlay().execute(new Void[] {});
	}

	protected void spin() {
		if (willbeRecievedReward) {
			// reset wheels
			wheel1.setCurrentItem(0, false);
			wheel2.setCurrentItem(0, false);
			wheel3.setCurrentItem(0, false);
			// Temporary take random index
			Random random = new Random();
			indexOfReward = random.nextInt(8);
			setItemForEachWheel(wheel1, indexOfReward);
			setItemForEachWheel(wheel2, indexOfReward);
			setItemForEachWheel(wheel3, indexOfReward);
			// index of item/reward
			// check what item will be the rewarded item
			if (indexOfReward == 0) {
				wheel1.scroll(-30000 * wheel1.getCycle(), 3000);
				wheel2.scroll(-30000 * wheel2.getCycle(), 4500);
				wheel3.scroll(-30000 * wheel3.getCycle(), 6000);
				Log.e("Item", "Bi' mat");
			} else if (indexOfReward == 1) {
				wheel1.scroll(-30000 * wheel1.getCycle() + 1, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 1, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 1, 6000);
				Log.e("Item", "Mario");
			} else if (indexOfReward == 2) {
				wheel1.scroll(-30000 * wheel1.getCycle() + 2, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 2, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 2, 6000);
				Log.e("Item", "Tao Do");
			} else if (indexOfReward == 3) {
				wheel1.scroll(-30000 * wheel1.getCycle() + 3, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 3, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 3, 6000);
				Log.e("Item", "Tim Do");
			} else if (indexOfReward == 4) {
				wheel1.scroll(-30000 * wheel1.getCycle() + 4, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 4, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 4, 6000);
				Log.e("Item", "Tim Vang");
			} else if (indexOfReward == 5) {
				wheel1.scroll(-30000 * wheel1.getCycle() + 5, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 5, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 5, 6000);
				Log.e("Item", "Tim Tim");
			} else if (indexOfReward == 6) {
				wheel1.scroll(-30000 * wheel1.getCycle() + 6, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 6, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 6, 6000);
				Log.e("Item", "Tim xanh");
			} else {
				wheel1.scroll(-30000 * wheel1.getCycle() + 7, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 7, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 7, 6000);
				Log.e("Item", "Hop Qua");
			}
		} else {
			int distance1 = -6000 + (int) (Math.random() * 50);
			int distance2 = -6000 + (int) (Math.random() * 50);
			int distance3 = -6000 + (int) (Math.random() * 50);
			Log.d("distance 1 :", "" + distance1 + " %3 : " + distance1 % 3);
			Log.d("distance 2 :", "" + distance2 + " %3 : " + distance2 % 3);
			Log.d("distance 3 :", "" + distance3 + " %3 : " + distance3 % 3);

			if (distance1 % 3 == distance2 % 3
					&& distance1 % 3 == distance3 % 3) {
				Log.d("An Exception", "Exception has been processed");
				while (distance3 % 3 == distance1 % 3) {
					distance3 = -600 + (int) (Math.random() * 50);
				}
				Log.d("distance 3 re-assigned to :", "" + distance3 + " %3 : "
						+ distance3 % 3);
			}
			wheel1.scroll(distance1, 3000);
			wheel2.scroll(distance2, 4500);
			wheel3.scroll(distance3, 6000);
		}
		soundPool.play(soundSpinning, 0.99f, 0.99f, 0, -1, 1);
	}

	private void setItemForEachWheel(WheelView wheel, int index) {

		wheel.setIndexOfWheel(index);
		int cycle = 0;
		while (cycle == 0) {
			cycle = (int) (Math.random() * 10);
		}
		wheel.setCycle(cycle);
	}

	private void initStrictMode() {
		// TODO Auto-generated method stub
		try {
			Class strictModeClass = Class.forName("android.os.StrictMode");
			Class strictModeThreadPolicyClass = Class
					.forName("android.os.StrictMode$ThreadPolicy");
			Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(
					null);
			Method method_setThreadPolicy = strictModeClass.getMethod(
					"setThreadPolicy", strictModeThreadPolicyClass);
			method_setThreadPolicy.invoke(null, laxPolicy);
		} catch (Exception e) {

		}
	}

	// Wheel scrolled flag
	private boolean wheelScrolled = false;

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			Log.d("start", wheelScrolled + "");
		}

		public void onScrollingFinished(WheelView wheel) {

			updateStatus();
			Log.d("finish", wheelScrolled + "");
		}
	};

	// Wheel changed listener
	private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			if (!wheelScrolled) {
			}
		}
	};

	/**
	 * Initializes wheel
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	private WheelView initWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new SlotMachineAdapter(this));
		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
		return wheel;
	}

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * Slot machine adapter
	 */
	private class SlotMachineAdapter extends AbstractWheelAdapter {
		private final int items[] = new int[] { R.drawable.reward_1_2x,
				R.drawable.reward_2_2x, R.drawable.reward_3_2x,
				R.drawable.reward_4_2x, R.drawable.reward_5_2x,
				R.drawable.reward_6_2x, R.drawable.reward_7_2x,
				R.drawable.reward_8_2x

		};

		// Cached images
		private List<SoftReference<Bitmap>> images;

		// Layout inflater
		private Context context;

		/**
		 * Constructor
		 */
		public SlotMachineAdapter(Context context) {
			this.context = context;
			images = new ArrayList<SoftReference<Bitmap>>(items.length);
			for (int id : items) {
				images.add(new SoftReference<Bitmap>(loadImage(id)));
			}
		}

		/**
		 * Loads image from resources
		 */
		private Bitmap loadImage(int id) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), id);
			// int width = bitmap.getWidth();
			// int height = bitmap.getHeight();
			// Bitmap scaled = Bitmap.createScaledBitmap(bitmap, width, height,
			// true);
			// bitmap.recycle();
			return bitmap;
		}

		@Override
		public int getItemsCount() {
			return items.length;
		}

		// Layout params for image view
		final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent,
				int width, int height) {
			ImageView img;
			if (cachedView != null) {
				img = (ImageView) cachedView;
			} else {
				img = new ImageView(context);
			}
			img.setLayoutParams(params);
			SoftReference<Bitmap> bitmapRef = images.get(index);
			Bitmap bitmap = bitmapRef.get();
			if (bitmap == null) {
				if (width != 0 && height != 0) {
					bitmap = loadImage(items[index]);
					images.set(index, new SoftReference<Bitmap>(bitmap));
				} else {
					bitmap = loadImage(items[index]);
					images.set(index, new SoftReference<Bitmap>(bitmap));
				}

			}
			img.setImageBitmap(bitmap);

			return img;
		}
	}

	public class PostGamePlay extends AsyncTask<Void, Void, Void> {
		boolean isError = false;
		String error_msg;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String resultPostGamePlay = HttpHelper.postGamePlay(game_token,
					bet, access_token);
			if (!resultPostGamePlay.equals("")) {
				receivedReward = JsonUtil.getRewardData(resultPostGamePlay);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			proDialogWaitingSpin.dismiss();
			Log.e("onPostOfGettingReward", "start spinning");
			if (receivedReward != null) {
				spinned = true;
				spin();
			} else {
				showErrorDialog();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			proDialogWaitingSpin.show();
			super.onPreExecute();

		}

	}

	public void showErrorDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder info = new AlertDialog.Builder(
				SlotMachineActivity.this);
		info.setTitle(getResources().getString(R.string.message_title_dialog));
		info.setMessage(getResources().getString(
				R.string.couldnot_retrieve_data));
		info.setPositiveButton(getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent i = new Intent();
						i.putExtra("is_used_free_spin", false);
						setResult(RESULT_OK, i);
						finish();
					}
				});
		info.show();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		proDialog.dismiss();
		_onPause = true;

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (wheelScrolled) {
			Log.e("SlotMachineActivity", "onBackPressed-wheelScrolled");
			Intent i = new Intent();
			if (spinned) {
				i.putExtra("is_used_free_spin", true);
				if (receivedReward != null) {
					i.putExtra("new_purple_tym",
							receivedReward.getNew_purple_tym());
					i.putExtra("new_green_tym",
							receivedReward.getNew_green_tym());
					i.putExtra("new_yellow_tym",
							receivedReward.getNew_yellow_tym());
				}
			} else {
				i.putExtra("is_used_free_spin", receive_free_ticket);
			}
			setResult(RESULT_OK, i);
			Log.e("receive_free_ticket on back pressed",receive_free_ticket+"");
			finish();

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		soundPool.release();
		soundPool = null;
	}

	/**
	 * Updates status
	 */

	private void updateStatus() {
		// process if the wheels run separately
		if (!wheel1.isScrollingPerformed() && !wheel2.isScrollingPerformed()
				&& wheel3.isScrollingPerformed()) {
			wheelScrolled = true;
			if (soundPool != null) {
				soundPool.stop(soundSpinning);
			}

			if (checkTheResult()) {
				if (soundPool != null) {
					if (receivedReward.getType().equalsIgnoreCase("OOS"))
					{
						soundPool.play(soundMissedGift, 0.99f, 0.99f, 0, 0, 1);
					}else
					{
						soundPool.play(soundWinning, 0.99f, 0.99f, 0, 0, 1);
					}
					
				}
				AlertDialog.Builder winDialog = new AlertDialog.Builder(
						SlotMachineActivity.this);
				LayoutInflater factory = LayoutInflater.from(this);
				final View view;
				LinearLayout giftListLayout;
				if (receivedReward.getGifts().size() == 0) {
					view = factory.inflate(R.layout.win_item_bg_layout, null);
					ImageView giftIcon = (ImageView) view
							.findViewById(R.id.giftIcon);

					if (receivedReward.getType().equalsIgnoreCase(
							REWARD.free_ticket.toString())) {
						receive_free_ticket = true;
						giftIcon.setImageResource(R.drawable.bonus_free_spin2x);
					} else if (receivedReward.getType().equalsIgnoreCase(
							REWARD.gold_ticket.toString())) {
						giftIcon.setImageResource(R.drawable.bonus_gold_ticket2x);
					} else if (receivedReward.getType().equalsIgnoreCase(
							REWARD.purple_tym.toString())) {
						giftIcon.setImageResource(R.drawable.reward_6_2x);
					} else if (receivedReward.getType().equalsIgnoreCase(
							REWARD.yellow_tym.toString())) {
						giftIcon.setImageResource(R.drawable.reward_5_2x);
					}
					if(receivedReward.getType().contains("card"))
					{
						RelativeLayout giftCodeLayOut = (RelativeLayout) view
								.findViewById(R.id.giftCodeLayOut);
						giftCodeLayOut.setVisibility(View.VISIBLE);
						TextView giftCode = (TextView) view
								.findViewById(R.id.giftCode);
						giftCode.setText(receivedReward.getCode());
						giftCode.setTextColor(Color.BLUE);
						final String msg=getResources().getString(R.string.copyCode);
						giftCode.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								
								ClipboardManager clipboard=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
								clipboard.setText(receivedReward.getCode());
								Toast info=Toast.makeText(getContext(),msg, Toast.LENGTH_LONG);
								info.show();
								
							}
						});
					}
					
					
				} else {
					view = factory.inflate(R.layout.win_items_bg_layout, null);
					giftListLayout = (LinearLayout) view
							.findViewById(R.id.giftListLayout);
					for (int i = 0; i < receivedReward.getGifts().size(); i++) {
						LinearLayout fLayout = new LinearLayout(
								SlotMachineActivity.this);
						fLayout.setLayoutParams(new LinearLayout.LayoutParams(
								0, ViewGroup.LayoutParams.FILL_PARENT, 1f));
						RelativeLayout rel = new RelativeLayout(
								SlotMachineActivity.this);
						RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						rel.setBackgroundResource(R.drawable.gift_bordert);
						rel.setLayoutParams(rlp);
						ImageView icon = new ImageView(SlotMachineActivity.this);
						icon.setImageResource(receivedReward.getGifts().get(i)
								.getSrc());
						icon.setId(i);
						ImageView cardbg=new ImageView(SlotMachineActivity.this);
						cardbg.setImageResource(R.drawable.card_bg2x);
						rlp.addRule(RelativeLayout.BELOW, i);
						rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
						rlp.addRule(RelativeLayout.CENTER_VERTICAL);
						TextView cardNo = new TextView(
								SlotMachineActivity.this);
						cardNo.setTextColor(Color.BLUE);
						cardNo.setText(receivedReward.getGifts().get(i).getValue()+"");
						cardbg.setLayoutParams(rlp);
						cardNo.setLayoutParams(rlp);
						rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
						rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
						rlp.addRule(RelativeLayout.CENTER_VERTICAL);
						icon.setLayoutParams(rlp);
						rel.addView(icon);
						if(receivedReward.getGifts().get(i).getType().contains("card"))
						{
							rel.addView(cardbg);
							rel.addView(cardNo);
						}
						TextView giftDescription = new TextView(
								SlotMachineActivity.this);
						if(receivedReward.getGifts().get(i).getType().equalsIgnoreCase("apple_giftcard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("google_giftcard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("viettel_phonecard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("vinaphone_phonecard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("mobifone_phonecard"))
						{
							giftDescription.setText(receivedReward.getGifts()
									.get(i).getType().replace('_', ' ')+" : "+receivedReward.getValue() +".");
						}else
						{
							giftDescription.setText(receivedReward.getGifts()
									.get(i).getValue() +" "+ receivedReward.getGifts()
									.get(i).getType().replace('_', ' '));
						}
						
						LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
						giftDescription.setLayoutParams(linear);
						fLayout.setOrientation(LinearLayout.VERTICAL);
						fLayout.setGravity(Gravity.CENTER_HORIZONTAL);
						fLayout.addView(rel);
						fLayout.addView(giftDescription);
						giftListLayout.addView(fLayout);
					}

				}

				TextView giftDescription = (TextView) view
						.findViewById(R.id.giftDescription);

				Button continue_btn = (Button) view
						.findViewById(R.id.continueBtn);
				continue_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Log.e("SlotMachineActivity", "onClick-Continue Btn");
						Intent i = new Intent();
						if (spinned) {
							i.putExtra("is_used_free_spin", receive_free_ticket);
							if (receivedReward != null) {
								i.putExtra("new_purple_tym",
										receivedReward.getNew_purple_tym());
								i.putExtra("new_green_tym",
										receivedReward.getNew_green_tym());
								i.putExtra("new_yellow_tym",
										receivedReward.getNew_yellow_tym());
							}
						} else {
							i.putExtra("is_used_free_spin", false);
						}
						setResult(RESULT_OK, i);
						Log.e("receive_free_ticket",receive_free_ticket+"");
						finish();
					}
				});
				Button shareBtn = (Button)view. findViewById(R.id.sharebtn);
				shareBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						progressDialog.setMessage(getResources().getString(R.string.sharing_on_facebook));
						progressDialog.show();
						final ArrayList<String> permis = new ArrayList<String>();
						final JSONObject pram = new JSONObject();
						try {
//							if(receivedReward.getGifts().size()>0)
//							{
//								String allGifts=receivedReward.getDescription()+"(";
//								for(int i=0;i<receivedReward.getGifts().size();i++)
//								{
//									if(receivedReward.getGifts().get(i).getType().equalsIgnoreCase("apple_giftcard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("google_giftcard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("viettel_phonecard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("vinaphone_phonecard")||receivedReward.getGifts().get(i).getType().equalsIgnoreCase("mobifone_phonecard"))
//									{
//										allGifts+=receivedReward.getGifts()
//												.get(i).getType().replace('_', ' ')+" : "+receivedReward.getValue() +".";
//									}else
//									{
//										allGifts+=receivedReward.getGifts()
//												.get(i).getValue() +" "+ receivedReward.getGifts()
//												.get(i).getType().replace('_', ' ');
//									}
//								}
//								allGifts+=")";
//								pram.put("message", allGifts);
//							}else
//							{
//								pram.put("message", receivedReward.getDescription());
//							}
							pram.put("message", receivedReward.getDescription());
							pram.put("link", "https://play.google.com/store/apps/details?id=com.appstoregp.vn");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						permis.add("publish_actions");
						permis.add("publish_stream");
						if (Session.getActiveSession() == null ||
				                Session.getActiveSession().isClosed()) {
				        	Log.e("openActiveSession because it is null or is closed", "openActiveSession because it is null or is closed");
				            Session.openActiveSession(SlotMachineActivity.this, true, new Session.StatusCallback() {
								
								@Override
								public void call(Session session, SessionState state, Exception exception) {
									// TODO Auto-generated method stub
									if(exception==null)
									{
										 if(!session.getPermissions().contains("publish_actions")||!session.getPermissions().contains("publish_stream"))
									        {
											 try{
									        	session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
												SlotMachineActivity.this, permis));
									        	Log.e("REQUESTPUBLISHpermission", "REQUESTPUBLISHpermission");
									        	Toast.makeText(SlotMachineActivity.this, getResources().getString(R.string.getting_permission_at_first), Toast.LENGTH_LONG).show();
											 }catch (Exception e) {
												// TODO: handle exception
												 e.printStackTrace();
											}
											 Log.e("REQUESTPUBLISHpermission2", "REQUESTPUBLISHpermission2");
									        	return;
									        }
										Request a = Request.newPostRequest(
												Session.getActiveSession(), "/me/feed",
												GraphObject.Factory.create(pram),
												new Request.Callback() {
													@Override
													public void onCompleted(Response response) {
														showPublishOnWallResult(
																response.getGraphObject(),
																response.getError());
													}
												});
										a.executeAsync();
									}
								}
							});
				            
				        }else
				        {
//				        	session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
//									SlotMachineActivity.this, permis));
							Request a = Request.newPostRequest(
									Session.getActiveSession(), "/me/feed",
									GraphObject.Factory.create(pram),
									new Request.Callback() {
										@Override
										public void onCompleted(Response response) {
											showPublishOnWallResult(
													response.getGraphObject(),
													response.getError());
										}
									});
							a.executeAsync();
				        }
						
					}
				});
				if (receivedReward.getGifts().size() == 0 &&!receivedReward.getType().contains("card")&&!receivedReward.getType().equalsIgnoreCase("OOS")) {
					String tym_type="";
					if(receivedReward.getType().replace('_', ' ').contains("purple"))
					{
						tym_type=getResources().getString(R.string.tym_purple);
					}else if(receivedReward.getType().replace('_', ' ').contains("green"))
					{
						tym_type=getResources().getString(R.string.tym_purple);
					}else if(receivedReward.getType().replace('_', ' ').contains("yellow"))
					{
						tym_type=getResources().getString(R.string.tym_yellow);
					}
					giftDescription.setText(receivedReward.getValue()+" "+ tym_type);
				}else if (receivedReward.getGifts().size() == 0 &&receivedReward.getType().contains("card")) {
					
					giftDescription.setText(receivedReward.getDescription()+" "+receivedReward.getValue());
				}else if (receivedReward.getType().equalsIgnoreCase(
						"OOS")) {
					giftDescription.setText(getResources().getString(R.string.oos));
					RelativeLayout forOOS=(RelativeLayout)view.findViewById(R.id.forOOS);
					forOOS.setVisibility(View.GONE);
					shareBtn.setEnabled(false);
				}
//				view.setBackgroundColor(android.graphics.Color.TRANSPARENT);
				winDialog.setView(view);
				dialog = winDialog.create();
//				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setCancelable(false);
				dialog.show();
			} else {
				AlertDialog.Builder info = new AlertDialog.Builder(
						SlotMachineActivity.this);
				info.setTitle(getResources().getString(
						R.string.message_title_dialog));
				info.setMessage(getResources().getString(
						R.string.message_not_win));
				info.setPositiveButton(getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						});
				info.show();
			}
		}
		if (!_onPause) {
			soundPool.play(soundEndOfSpinning, 0.99f, 0.99f, 0, 0, 1);
		}
	}

	private void showPublishOnWallResult(GraphObject result,
			FacebookRequestError error) {
		progressDialog.dismiss();
		int alertMessage;
		if (error == null) {
			alertMessage = R.string.share_fb_success;
		} else {
			alertMessage = R.string.share_fb_error;
			Log.e("ErrorWhenSharing",""+error.getErrorMessage());
		}

		new AlertDialog.Builder(this).setMessage(alertMessage)
				.setPositiveButton("Ok", null).show();
	}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	try {
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	/**
	 * Tests wheels
	 * 
	 * @return true
	 */
	private boolean checkTheResult() {

		if (!willbeRecievedReward) {

		}

		if (getWheel(R.id.slot_1).getCurrentItem() == getWheel(R.id.slot_2)
				.getCurrentItem()
				&& getWheel(R.id.slot_1).getCurrentItem() == getWheel(
						R.id.slot_3).getCurrentItem()) {
			return true;
		} else
			return false;

	}

}
