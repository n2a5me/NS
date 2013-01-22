package com.appota.app.spinmachine;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appota.app.spinmachine.adapter.AbstractWheelAdapter;
import com.appota.app.spinmachine.widget.OnWheelChangedListener;
import com.appota.app.spinmachine.widget.OnWheelScrollListener;
import com.appota.app.spinmachine.widget.WheelView;
import com.appota.network.HttpHelper;
import com.appota.slotmachine.object.Reward;
import com.appota.slotmachine.object.Spin;
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
	private Reward receivedReward=null;
	//private boolean hasRightsToSpin=true;
	private boolean willbeRecievedReward=true;
	private int indexOfReward=1;
	private ProgressDialog proDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStrictMode();
		init();
		initView();
	}

	public void init() {
		proDialog=new ProgressDialog(this);
        proDialog.setMessage("Getting information from server...");
        if(isOnline())
        {
        	new GetUserDataAsync().execute(new Void[] {});
        }else
        {
        	Toast.makeText(SlotMachineActivity.this,"Không có network nên không thể chơi. Hãy kiểm tra lại.",Toast.LENGTH_LONG).show();
        }
        initStrictMode();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.slot_machine_layout);
		wheel1 =initWheel(R.id.slot_1);
		wheel2=initWheel(R.id.slot_2);
		wheel3=initWheel(R.id.slot_3);

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
				
				if(isOnline())
				{
				new PostGamePlay().execute(new Void[] {});
				}else
				{
					Toast.makeText(SlotMachineActivity.this,"Không có network nên không thể chơi. Hãy kiểm tra lại.",Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(SlotMachineActivity.this,
						"Ban khong con luot choi free", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	protected void spin() {
		if(willbeRecievedReward)
    	{
        	setItemForEachWheel(wheel1);
        	setItemForEachWheel(wheel2);
        	setItemForEachWheel(wheel3);
        	//index of item/reward 
        	//check what item will be the rewarded item 
//        	if(receivedReward.getNew_green_tym()!=0)
//        	{
//        		
//        	}
        	//Temporary take random index
        	Random random=new Random();
        	indexOfReward=((int)random.nextInt(3))*10;
        	if(indexOfReward==0)
        	{
    			 wheel1.scroll(-3000*wheel1.getCycle(), 3000);
        		 wheel2.scroll(-3000*wheel2.getCycle(), 3000);
        		 wheel3.scroll(-3000*wheel3.getCycle(), 3000);
        		 Toast.makeText(SlotMachineActivity.this, "Sao vang`", Toast.LENGTH_LONG).show();
        	}else if(indexOfReward==1)
        	{
        		wheel1.scroll(-3000*wheel1.getCycle()+1, 3000);
       		 	wheel2.scroll(-3000*wheel2.getCycle()+1, 3000);
       		 	wheel3.scroll(-3000*wheel3.getCycle()+1, 3000);
       		 	Toast.makeText(SlotMachineActivity.this, "Sao vang`", Toast.LENGTH_LONG).show();
        	}else
        	{
        		wheel1.scroll(-3000*wheel1.getCycle()+2, 3000);
       		 	wheel2.scroll(-3000*wheel2.getCycle()+2, 3000);
       		 	wheel3.scroll(-3000*wheel3.getCycle()+2, 3000);
       		 	Toast.makeText(SlotMachineActivity.this, "Sao vang`", Toast.LENGTH_LONG).show();
        	}
    	}else
    	{
//			Log.d("Index wheel 1 :",""+getWheel(R.id.slot_1).getCurrentItem());
//			Log.d("Index wheel 2 :",""+getWheel(R.id.slot_3).getCurrentItem());
//			Log.d("Index wheel 3 :",""+getWheel(R.id.slot_2).getCurrentItem());
			int distance1=-600 + (int)(Math.random() * 50);
			int distance2=-600 + (int)(Math.random() * 50);
			int distance3=-600 + (int)(Math.random() * 50);
			Log.d("distance 1 :",""+distance1 +" %3 : "+distance1%3);
			Log.d("distance 2 :",""+distance2+" %3 : "+distance2%3);
			Log.d("distance 3 :",""+distance3+" %3 : "+distance3%3);
			wheel1.setCurrentItem(0, false);
			wheel2.setCurrentItem(0, false);
			wheel3.setCurrentItem(0, false);
			
			if(distance1%3==distance2%3&&distance1%3==distance3%3)
			{
				Log.d("An Exception","Exception has been processed");
				while(distance3%3==distance1%3)
				{
					distance3=-600 + (int)(Math.random() * 50);
				}
				Log.d("distance 3 re-assigned to :",""+distance3+" %3 : "+distance3%3);
			}
    		wheel1.scroll(distance1, 3000);
            wheel2.scroll(distance2, 3000);
            wheel3.scroll(distance3, 3000);
    	}
	}
	private  void setItemForEachWheel(WheelView wheel) {
		
		wheel.setIndexOfWheel(1);
		int cycle=0;
		while(cycle==0)
		{
			cycle=(int)(Math.random() * 10);
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
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			Log.d("onScrollingFinished", "updateStatus()");
			updateStatus();
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
		// Image size
		final int IMAGE_WIDTH = 60;
		final int IMAGE_HEIGHT = 36;

		// Slot machine symbols
		private final int items[] = new int[] { 
				R.drawable.star,
				R.drawable.tym, 
				R.drawable.crow

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

		@Override
		public int getItemsCount() {
			return items.length;
		}

		// Layout params for image view
		final LayoutParams params = new LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT);

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
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
				bitmap = loadImage(items[index]);
				images.set(index, new SoftReference<Bitmap>(bitmap));
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
				{
					receivedReward=JsonUtil.getRewardData(HttpHelper.postGamePlay(game_token, "0", "green",access_token));
				}
					
				else {
					receivedReward=JsonUtil.getRewardData(HttpHelper.postGamePlay(game_token, "1", "green",access_token));
					;
				}
				if(receivedReward!=null)
				{
					spin();
				}else
				{
					AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
    	    		alert.setTitle("Thông báo");
    	    		alert.setMessage("Không thể kết nối đến server để lấy dữ liệu. Xin kiểm tra lại mạng.");
    	    		alert.show();
				}
			}
			return null;
		}

	}
	private Context getContext()
	{
		return SlotMachineActivity.this;
	}
	public class GetUserDataAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			super.onPreExecute();
			proDialog.show();
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			proDialog.dismiss();
		}
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
		//Temporary allows run any time
		mSpin.isFree=true;
		if(proDialog.isShowing())
		{
			proDialog.dismiss();
		}
	}
	/**
     * Updates status
     */
    private void updateStatus() {
    	
    	if (checkTheResult()) {
    		
    		AlertDialog.Builder info=new AlertDialog.Builder(SlotMachineActivity.this);
			info.setTitle("Warning !");
			info.setMessage("Chúc mừng ! "+"\n"+receivedReward.getDescription());
			info.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					//Toast.makeText(FastStatus.this, "Byeeeeeeeee!", Toast.LENGTH_LONG);
					
				}
			});
			info.show();
            //Do save store info status, data(reward, new purple/green TYM)
             
        }else
        {
        	Toast.makeText(SlotMachineActivity.this, "May mắn lần sau nhé !", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Tests wheels
     * @return true 
     */
    private boolean checkTheResult() {
    	
    	if(!willbeRecievedReward)	
    	{
    		
    	}
    	// Store info : the TYMs left, game status, rewards if any.. 
    	// temporary just inform users the result
    	//Log.d("Index wheel 1 onCheckTheResult :",""+getWheel(R.id.slot_1).getCurrentItem());
		//Log.d("Index wheel 2 onCheckTheResult :",""+getWheel(R.id.slot_3).getCurrentItem());
		//Log.d("Index wheel 3 onCheckTheResult :",""+getWheel(R.id.slot_2).getCurrentItem());
        if(getWheel(R.id.slot_1).getCurrentItem()==0 && getWheel(R.id.slot_2).getCurrentItem()==0 && getWheel(R.id.slot_3).getCurrentItem()==0)
        {
        	return true;
        }else return false;
       
    }
	//Check whether the device's network is available.
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && 
           cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
