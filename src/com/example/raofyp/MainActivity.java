package com.example.raofyp;

import android.support.v7.app.ActionBarActivity;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.gesture.Sgesture;
import com.samsung.android.sdk.gesture.SgestureHand;
import com.samsung.android.sdk.gesture.SgestureHand.Info;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Console;
import java.util.Timer;

public class MainActivity extends ActionBarActivity implements SensorEventListener {
	private static final String TAG = "[DM]SHAMI";  
	private SgestureHand mGestureHand;  
	private Sgesture mGesture; 
	private SensorManager mSensorManager;
	private Sensor mlight;
	private Sensor mtemp;
	SQLiteDatabase mydatabase;
	Intent sitting;
	Intent dining;
	TextView title;
	TextView subtitle;
	TextView subtitle1;
	TextView Selection;
	Animation righttoleft;
	Animation lefttoright;
	int i;
	String []Value;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		i=0;
		Value=new String[3];
		righttoleft= AnimationUtils.loadAnimation(this, R.anim.animationfile);
	    lefttoright=AnimationUtils.loadAnimation(this, R.anim.fromlefttoright);
		title=(TextView)findViewById(R.id.textView1);
		title.setText("Hand Gesture");
		title.startAnimation(righttoleft);
		subtitle=(TextView)findViewById(R.id.textView2);
	    subtitle.setText("Based Smart");
	    subtitle.startAnimation(righttoleft);
	    subtitle1=(TextView)findViewById(R.id.textView3);
	    subtitle1.setText("Controller");
	    subtitle1.startAnimation(righttoleft);
	    Selection=(TextView)findViewById(R.id.textView4);
	    Value[0]="Select DINNING";
	    Value[1]="Select Sitting";
	    Value[2]="Select Bedroom";
		mGesture=new Sgesture();
		mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mlight=mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mydatabase= openOrCreateDatabase("Readings",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Gesturereading(ANGEL INT,Speed INT);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SensorReading(ANGEL INT);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Timing(Start_Time String,End_Time String,Device String);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Fanreadings(Start_Time String,End_Time String,temp int);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Lightreadings(Start_Time String,End_Time String,sensor int);");
        sitting=new Intent(this,Sitting.class);	
        dining=new Intent(this,Dining.class);
	
		/*
		final Button button3=(Button)findViewById(R.id.button3);
		
		button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
			}
		});
*/
		
		
		final Button button4=(Button)findViewById(R.id.button4);
		button4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Cursor resultSet = mydatabase.rawQuery("Select * from SensorReading",null);
				resultSet.moveToFirst();
				while(resultSet.moveToNext()==true)
				{
				int accuracy=resultSet.getInt(0);
				System.out.println(accuracy);
				}
				///////////Readings for Gestures
				Cursor resultSet2 = mydatabase.rawQuery("Select * from Gesturereading",null);
				resultSet2.moveToFirst();
				while(resultSet2.moveToNext()==true)
				{
				int angel=resultSet2.getInt(0);
				int speed = resultSet2.getInt(1);
				System.out.println(angel+"     "+speed);
				}
				//////////////////Timing Reading
				Cursor resultSet3 = mydatabase.rawQuery("Select * from Timing",null);
				resultSet3.moveToFirst();
				while(resultSet3.moveToNext()==true)
				{
				String starttime=resultSet3.getString(0);
				String endtime = resultSet3.getString(1);
				String fan = resultSet3.getString(2);
				System.out.println(starttime+"     "+endtime+"       "+fan);
			    //////////////////////Fan reading
				Cursor resultSet5 = mydatabase.rawQuery("Select * from Fanreadings",null);
				resultSet5.moveToFirst();
				while(resultSet5.moveToNext()==true)
				{
					String fstarttime=resultSet5.getString(0);
					String fendtime = resultSet5.getString(1);
					int temp = resultSet5.getInt(2);
					System.out.println(starttime+"     "+endtime+"       "+temp);
					
				}
			
				}
			}
		});
		
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
	final SgestureHand.ChangeListener changeListener = new
	
	SgestureHand.ChangeListener() {
		
		@Override  
		public void onChanged(Info info) { 
			// TODO Auto-generated method stub 
		
		if(info.getType() == Sgesture.TYPE_HAND_PRIMITIVE){ 
	///////////////Intent opening	    
			
				if(info.getAngle()<150&&info.getSpeed()>80)
				{   i= (i+ 1) % 2;
				
				    Selection.setText(Value[i]);
				    Selection.startAnimation(lefttoright);
					
				}
				else if(info.getAngle()>150&&info.getSpeed()>80 )
				{ 
			      if(i==0)
			      {
			    	  i=1;
			      }
			      else
			      {
			    	  i--;
			      }
			      Selection.setText(Value[i]);
			      Selection.startAnimation(righttoleft);
			     
					//	
				}
				else if(info.getAngle()>150&&info.getSpeed()<80)
				{
					if(i==0)
					{
						startActivity(dining);
					}else if(i==1)
					{
						startActivity(sitting);
					}
					
				}
	///////////////Entering reading in database 
				System.out.println("Gesture SDK Values"); 
	            System.out.println("Angel = "+info.getAngle());
	            System.out.println("Speed = "+info.getSpeed());
	          //  mydatabase.execSQL("INSERT INTO Gesturereading VALUES("+info.getAngle()+","+info.getSpeed()+");");
			}
			}
		};
			
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
