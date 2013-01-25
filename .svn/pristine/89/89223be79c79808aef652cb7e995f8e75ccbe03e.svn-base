package com.appota.app.spinmachine;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appota.app.spinmachine.adapter.AbstractWheelAdapter;
import com.appota.app.spinmachine.widget.OnWheelChangedListener;
import com.appota.app.spinmachine.widget.OnWheelScrollListener;
import com.appota.app.spinmachine.widget.WheelView;
import com.appota.network.HttpHelper;
import com.appota.slotmachine.object.Reward;
import com.appota.slotmachine.object.Spin;
import com.appota.util.CommonUtils;
import com.appota.util.JsonUtil;

public class SlotMachineActivity extends Activity {
	String game_token = null;
	String access_token = null;
	ImageView btn_exit, btn_start;
	Spin mSpin;
	protected boolean isWin = false;
	private WheelView wheel1;
	private WheelView wheel2;
	private WheelView wheel3;
	private ArrayList<Reward> rewards;
	private Reward receivedReward = null;
	private boolean willbeRecievedReward = true;
	private int indexOfReward = 1;
	private ProgressDialog proDialog;
	private Handler handler;
	boolean isRunning = false;
	AnimationDrawable animationDrawable;
	private boolean enableSpin = true;
	private String rewardItem;
	private boolean serverError = false;
	private SoundPool soundPool;
	private int soundSpinning;
	private int soundEndOfSpinning;
	private int soundWinning;
	private boolean _onPause=false;
	private TextView playtimes;
	private TextView ads;
	private ProgressDialog proDialogWaitingSpin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStrictMode();
		init();
		initView();
	}

	public void init() {
		_onPause=false;
		handler = new Handler();
		soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 128);
		soundSpinning = soundPool.load(getContext(), R.raw.spinning, 1);
		soundEndOfSpinning = soundPool
				.load(getContext(), R.raw.stopspinning, 1);
		soundWinning = soundPool.load(getContext(), R.raw.cheering, 1);
		proDialog = new ProgressDialog(this);
		proDialogWaitingSpin=new ProgressDialog(this);
		proDialogWaitingSpin.setCancelable(false);
		proDialogWaitingSpin.setMessage("Hãy chờ xíu trước khi quay..");
		proDialog.setMessage("Đang lấy thông tin từ máy chủ...");
		proDialog.setCancelable(false);
		if (isOnline()) {
			new GetUserDataAsync().execute(new Void[] {});
		} else {
			Toast.makeText(SlotMachineActivity.this,
					"Không có network nên không thể chơi. Hãy kiểm tra lại.",
					Toast.LENGTH_LONG).show();
		}
		
		// initStrictMode();
		initView();
	}

	private Context getContext() {
		return SlotMachineActivity.this;
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.slot_machine_layout);
		ImageView img = (ImageView) findViewById(R.id.img);
		playtimes=(TextView)findViewById(R.id.playtimes);
		ads=(TextView)findViewById(R.id.ads);
		animationDrawable = (AnimationDrawable) img.getBackground();
		animationDrawable.setCallback(img);
		animationDrawable.setVisible(false, true);

		wheel1 = initWheel(R.id.slot_1);
		wheel2 = initWheel(R.id.slot_2);
		wheel3 = initWheel(R.id.slot_3);

		btn_exit = (ImageView) findViewById(R.id.btn_exit);
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_start = (ImageView) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onStartClick();
				btn_exit.setEnabled(enableSpin);
			}
		});
	}

	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// super.onConfigurationChanged(newConfig);
	// setContentView(R.layout.slot_machine_layout);
	// }
	private void informUserFreeSpin() {
		if (!serverError) {
			if (mSpin.isFree) {
				AlertDialog.Builder info = new AlertDialog.Builder(
						SlotMachineActivity.this);
				info.setTitle("Thông báo");
				info.setMessage("Bạn còn 1 lượt chơi miễn phí trong ngày. Chiến thôi !");
				info.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								// Toast.makeText(FastStatus.this,
								// "Byeeeeeeeee!", Toast.LENGTH_LONG);

							}
						});
				if(!_onPause)
				{
					info.show();
				}
				serverError=false;
			} else {
				AlertDialog.Builder info = new AlertDialog.Builder(
						SlotMachineActivity.this);
				info.setTitle("Thông báo");
				info.setMessage("Bạn đã hết lượt chơi free trong ngày. Ngày mai quay tiếp nhé!");
				info.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								// Toast.makeText(FastStatus.this,
								// "Byeeeeeeeee!", Toast.LENGTH_LONG);

							}
						});
				if(!_onPause)
				{
					info.show();
				}
			}
		} else {
			AlertDialog.Builder info = new AlertDialog.Builder(
					SlotMachineActivity.this);
			info.setTitle("Thông báo");
			info.setMessage("Server đang quá tải. Hãy thử lại sau ít phút nữa nhé.");
			info.setPositiveButton("Okay",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			if(!_onPause)
			{
				info.show();
			}
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		
	}

	protected void onStartClick() {

		if (!serverError) {
			if (mSpin != null) {

				if (enableSpin)// Do Spinning
				{
					if (mSpin.isFree) {

						if (isOnline()) {
							enableSpin = false;

							new PostGamePlay().execute(new Void[] {});
						} else {
							Toast.makeText(
									SlotMachineActivity.this,
									"Không có network nên không thể chơi. Hãy kiểm tra lại.",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(
								SlotMachineActivity.this,
								"Bạn đã hết lượt chơi free. Mai quay tiếp nhé.",
								Toast.LENGTH_SHORT).show();
					}
				}
				// else
				// {
				// Toast.makeText(SlotMachineActivity.this,
				// "Bạn đã hết lượt chơi free. Mai quay tiếp nhé.",
				// Toast.LENGTH_SHORT)
				// .show();
				// }
			}
		} else {
			AlertDialog.Builder info = new AlertDialog.Builder(
					SlotMachineActivity.this);
			info.setTitle("Thông báo");
			info.setMessage("Server đang quá tải. Hãy thử lại sau ít phút nữa nhé.");
			info.setPositiveButton("Okay",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			info.show();
		}
	}

	protected void spin() {
		if (willbeRecievedReward) {
			// reset wheels
			wheel1.setCurrentItem(0, false);
			wheel2.setCurrentItem(0, false);
			wheel3.setCurrentItem(0, false);
			// Temporary take random index
			Random random = new Random();
			indexOfReward = random.nextInt(3);
			if (indexOfReward == 0) {
				rewardItem = "Sao Vàng";
			} else if (indexOfReward == 1) {
				rewardItem = "Tim Xanh";
			} else {
				rewardItem = "Hộp Quà";
			}
			
			setItemForEachWheel(wheel1, indexOfReward);
			setItemForEachWheel(wheel2, indexOfReward);
			setItemForEachWheel(wheel3, indexOfReward);
			// index of item/reward
			// check what item will be the rewarded item
			if (indexOfReward == 0) {
				wheel1.scroll(-30000 * wheel1.getCycle(), 3000);
				wheel2.scroll(-30000 * wheel2.getCycle(), 4500);
				wheel3.scroll(-30000 * wheel3.getCycle(), 6000);
				Log.d("Item", "Sao vang`");
			} else if (indexOfReward == 1) {
				wheel1.scroll(-30000 * wheel1.getCycle() + 1, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 1, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 1, 6000);
				Log.d("Item", "Tim xanh");
			} else {
				wheel1.scroll(-30000 * wheel1.getCycle() + 2, 3000);
				wheel2.scroll(-30000 * wheel2.getCycle() + 2, 4500);
				wheel3.scroll(-30000 * wheel3.getCycle() + 2, 6000);
				Log.d("Item", "Qua tang bi' mat");
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
			wheelScrolled = true;
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
		private final int items[] = new int[] { R.drawable.star,
				R.drawable.tym, R.drawable.crow

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
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Bitmap scaled = Bitmap.createScaledBitmap(bitmap, width, height,
					true);
			bitmap.recycle();
			return scaled;
		}

		private Bitmap loadImage(int id, int width, int height) {
			Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), id);
			Bitmap scaled = Bitmap.createScaledBitmap(bitmap, width, height,
					true);
			bitmap.recycle();
			return scaled;
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
			if (game_token != null) {

				if (mSpin.isFree) {
					receivedReward = JsonUtil.getRewardData(HttpHelper
							.postGamePlay(game_token, "0", "green",
									access_token));
					mSpin.isFree = false;
				}

				else {
					receivedReward = JsonUtil.getRewardData(HttpHelper
							.postGamePlay(game_token, "1", "green",
									access_token));
					;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			proDialogWaitingSpin.dismiss();
			spin();
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
		info.setTitle("Thông báo");
		info.setMessage("Không thể kết nối đến server để lấy dữ liệu. Xin kiểm tra lại mạng.");
		info.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				// Toast.makeText(FastStatus.this,
				// "Byeeeeeeeee!", Toast.LENGTH_LONG);

			}
		});
		info.show();

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		proDialog.dismiss();
		_onPause=true;
	}
	public class GetUserDataAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
//			handler.post(new Runnable() {
//				public void run() {
			if(!proDialog.isShowing())
					proDialog.show();
//				}
//			});

		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
//			handler.post(new Runnable() {
//				public void run() {
					if (proDialog.isShowing()) {
						proDialog.dismiss();
					}
					informUserFreeSpin();
//				}
//			});

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				initUser();
			} catch (Exception e) {
				serverError = true;
				e.printStackTrace();
				
			}
			return null;
		}
	}

	public void initUser() {
		
		String username = "machequyhon2012";
		String password = "tamthanphanliet";
//		String username = "n2a5me";
//		String password = "n123456789";
		String responseLogin = HttpHelper.loginUser(SlotMachineActivity.this,
				username, password);
		access_token = JsonUtil.getAccessToken(responseLogin);
		String res_User_Data = HttpHelper.getUserData(access_token);
		mSpin = JsonUtil.getSpinUserData(res_User_Data);
		if(mSpin.isFree)
		{
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playtimes.setText("Bạn còn 1 lượt quay thưởng!");
					btn_start.setEnabled(true);
					ads.setText(mSpin.ads.getDescription());
				}
			});
			
		}else
		{
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playtimes.setText("Bạn đã hết lượt quay thưởng. Vui lòng quay trở lại vào :"+CommonUtils.convertUnixTime(mSpin.timeavailable));
					btn_start.setEnabled(true);
					ads.setText(mSpin.ads.getDescription());
				}
			});
			
		}
		
		game_token = mSpin.game_token;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!wheelScrolled)
		{
			super.onBackPressed();
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
			enableSpin = true;
			wheelScrolled = false;
			btn_exit.setEnabled(enableSpin);
			if(soundPool!=null)
			{
				soundPool.stop(soundSpinning);
			}
			
			if (checkTheResult()) {
				if(soundPool!=null)
				{
					soundPool.play(soundWinning, 0.99f, 0.99f, 0, 0, 1);
				}
				animationDrawable.setVisible(true, true);
				animationDrawable.start();
				AlertDialog.Builder info = new AlertDialog.Builder(
						SlotMachineActivity.this);
				info.setTitle("Thông báo");
				if(receivedReward==null)
				{
					info.setMessage("\nSự cố khi lấy thông tin về phần thưởng.");
					info.setPositiveButton("Okay",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									// Toast.makeText(FastStatus.this,
									// "Byeeeeeeeee!", Toast.LENGTH_LONG);

								}
							});
					info.show();
				}else
				{
					info.setMessage("\nChúc mừng bạn! "
							+ receivedReward.getDescription()+" vào tài khoản. Bạn sẽ có thêm lượt quay vào ngày hôm sau!");
					info.setPositiveButton("Okay",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									// Toast.makeText(FastStatus.this,
									// "Byeeeeeeeee!", Toast.LENGTH_LONG);

								}
							});
					info.show();
				}
				

			} else {
				AlertDialog.Builder info = new AlertDialog.Builder(
						SlotMachineActivity.this);
				info.setTitle("Thông báo");
				info.setMessage("\nThật tiếc, bạn không trúng thưởng trong lượt quay này.\nBạn sẽ có thêm lượt quay vào ngày hôm sau! Chúc bạn may mắn lần sau!");
				info.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
							}
						});
				info.show();
			}
//			handler.postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					Timer autoCheckUserData=new Timer();
//					autoCheckUserData.schedule(new TimerTask() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							handler.post(new Runnable() {
//								
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									new GetUserDataAsync().execute(new Void[] {});
//								}
//							});
//						}
//					}, 120000);
//				}
//			}, 120000);
			
				playtimes.setText("Bạn đã hết lượt chơi. Hãy quay lại vào "+CommonUtils.convertUnixTime(mSpin.timeavailable) +" để chơi tiếp!");
				btn_start.setEnabled(false);
			
			
		}
		if(!_onPause)
		{
			soundPool.play(soundEndOfSpinning, 0.99f, 0.99f, 0, 0, 1);
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

	// Check whether the device's network is available.
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

}
