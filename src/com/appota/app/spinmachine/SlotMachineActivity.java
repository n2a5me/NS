package com.appota.app.spinmachine;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.appota.app.spinmachine.adapter.AbstractWheelAdapter;
import com.appota.app.spinmachine.widget.OnWheelChangedListener;
import com.appota.app.spinmachine.widget.OnWheelScrollListener;
import com.appota.app.spinmachine.widget.WheelView;
import com.appota.network.HttpHelper;
import com.appota.slotmachine.object.Spin;
import com.appota.util.JsonUtil;

public class SlotMachineActivity extends Activity {
	String game_token = null;
	String access_token = null;
	ImageView btn_exit, btn_start;
	Spin mSpin;
	protected boolean isWin = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStrictMode();
		init();
		initView();
	}

	public void init() {
		new GetUserDataAsync().execute(new Void[] {});
		initStrictMode();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.slot_machine_layout);
		initWheel(R.id.slot_1);
		initWheel(R.id.slot_2);
		initWheel(R.id.slot_3);

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

			}
		});
	}

	protected void onStartClick() {
		// TODO Auto-generated method stub
		if (mSpin != null) {
			if (mSpin.isFree) {
				spin();
				new PostGamePlay().execute(new Void[] {});
			} else {
				Toast.makeText(SlotMachineActivity.this,
						"Bạn không còn lượt chơi free", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	protected void spin() {
		mixWheel(R.id.slot_1);
		mixWheel(R.id.slot_2);
		mixWheel(R.id.slot_3);
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
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
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
	private void initWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.setViewAdapter(new SlotMachineAdapter(this));
		wheel.setCurrentItem((int) (Math.random() * 10));

		wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
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
	 * Tests wheels
	 * 
	 * @return true
	 */
	// private boolean test() {
	// // int value = getWheel(R.id.slot_1).getCurrentItem();
	// // return testWheelValue(R.id.slot_2, value)
	// // && testWheelValue(R.id.slot_3, value);
	// }

	/**
	 * Tests wheel value
	 * 
	 * @param id
	 *            the wheel Id
	 * @param value
	 *            the value to test
	 * @return true if wheel value is equal to passed value
	 */
	private boolean testWheelValue(int id, int value) {
		return getWheel(id).getCurrentItem() == value;
	}

	/**
	 * Mixes wheel
	 * 
	 * @param id
	 *            the wheel id
	 */
	private void mixWheel(int id) {
		WheelView wheel = getWheel(id);
		wheel.scroll(-350 + (int) (Math.random() * 50), 3000);
	}

	/**
	 * Slot machine adapter
	 */
	private class SlotMachineAdapter extends AbstractWheelAdapter {
		// Image size
		final int IMAGE_WIDTH = 60;
		final int IMAGE_HEIGHT = 36;
		int deseart_width = 0;
		int deseart_height = 0;
		// Slot machine symbols
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
			Bitmap scaled = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH,
					IMAGE_HEIGHT, true);
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
					bitmap = loadImage(items[index], width, height);
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

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (game_token != null) {
				if (mSpin.isFree)
					HttpHelper.postGamePlay(game_token, "0", "green",
							access_token);
				else {
					HttpHelper.postGamePlay(game_token, "1", "green",
							access_token);
				}
			}
			return null;
		}

	}

	public class GetUserDataAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			initUser();
			return null;
		}
	}

	public void initUser() {
		String username = "tuannx1987";
		String password = "123456";
		String responseLogin = HttpHelper.loginUser(SlotMachineActivity.this,
				username, password);
		access_token = JsonUtil.getAccessToken(responseLogin);
		String res_User_Data = HttpHelper.getUserData(access_token);
		mSpin = JsonUtil.getSpinUserData(res_User_Data);
	}

}
