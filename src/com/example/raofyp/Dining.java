package com.example.raofyp;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ToggleButton;

////////////////////////////////////////////////////////////////
///////Samsung APIS Looks......Never seen anything more beautiful then you 
////////////////////////////////////////////////////////////////
import android.support.v7.app.ActionBarActivity;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.gesture.Sgesture;
import com.samsung.android.sdk.gesture.SgestureHand;
import com.samsung.android.sdk.gesture.SgestureHand.Info;

import android.hardware.Sensor;
////////////////////////////////////////////////////////////////
//////////////////Sesnors APIs and related stuff......Thats my Shami ////////////////////////////////////////////   
///////////////////////////////////////////////////////////////////////
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

public class Dining extends Activity implements SensorEventListener  {
	//////////////////////////////////Variables for gestures
	private SgestureHand mGestureHand;  
	private Sgesture mGesture; 
	private SensorManager mSensorManager;
    ///////////////////////////////////////I could see a sparkle when u looks in my eyes
	private ProgressBar fan;
	private RatingBar bulb1;
	private RatingBar bulb2;
    ////////////////////////////////////Variables for Sensors
	private Sensor mlight;
	private Sensor mtemp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dining);
		mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mlight=mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
		fan=(ProgressBar)findViewById(R.id.progressBar1);
		bulb1=(RatingBar)findViewById(R.id.ratingBar1);
		bulb2=(RatingBar)findViewById(R.id.ratingBar2);
		mGesture=new Sgesture();
		try
		{
			mGesture.initialize(this);
		}
		catch(IllegalArgumentException e)
		{}
		catch(SsdkUnsupportedException e)
		{
			
		}
		if(mGesture.isFeatureEnabled(Sgesture.TYPE_HAND_PRIMITIVE))
		{
			////its a feature check :)
		}
		mGestureHand=new SgestureHand(Looper.getMainLooper(),mGesture);
		mGestureHand.start(Sgesture.TYPE_HAND_PRIMITIVE, changeListener);
	
	}
	@Override
	  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	   //	System.out.println(accuracy);
	//	mydatabase.execSQL("INSERT INTO SensorReading VALUES("+accuracy+");");   
	  }
	
	@Override
	  public final void onSensorChanged(SensorEvent event) {
	    float lightintensity = event.values[0];  
		//mydatabase.execSQL("INSERT INTO SensorReading VALUES("+lightintensity+");");
	 //   System.out.println("Light Sensitivity "+lightintensity);
	    if(lightintensity<5)
	    {
	    	bulb1.setRating(5);
	    	bulb2.setRating(5);
	    }else
	    {
	    	bulb1.setRating(0);
	    	bulb2.setRating(0);
	    }
	    // Do something with this sensor data.
	  }
	@Override
	  protected void onResume() {
	    // Register a listener for the sensor.
	    super.onResume();
	    mSensorManager.registerListener(this, mlight, SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() {
	    // Be sure to unregister the sensor when the activity pauses.
	    super.onPause();
	    mSensorManager.unregisterListener(this);
	  }
	  
	@Override  
	protected void onDestroy() 
	{ // Stop hand gesture     
		mGestureHand.stop(); 
		super.onDestroy(); 
	}   
	//////////////Function related to catching gestures
	final SgestureHand.ChangeListener changeListener = new
			SgestureHand.ChangeListener() { 
				@Override  
				public void onChanged(Info info) { 
					// TODO Auto-generated method stub 
					if(info.getType() == Sgesture.TYPE_HAND_PRIMITIVE){ 
				     	System.out.println("Gesture SDK Values"); 
			            System.out.println("Angel = "+info.getAngle());
			            System.out.println("Speed = "+info.getSpeed());
			          if(info.getAngle()>150)
			          {
			        	  fan.setVisibility(fan.VISIBLE);
			          }else if(info.getAngle()<150)
			          {
			        	  fan.setVisibility(fan.INVISIBLE);
			          }
					}
					}
				};
	
}
