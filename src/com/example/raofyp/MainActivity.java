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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mGesture=new Sgesture();
		mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mlight=mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mydatabase= openOrCreateDatabase("Readings",MODE_PRIVATE,null);
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Gesturereading(ANGEL INT,Speed INT);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS SensorReading(ANGEL INT);");
		final Intent intent=new Intent(this,SettingsActivity.class);
		final ImageButton imgbutton=(ImageButton)findViewById(R.id.imageButton1);
		imgbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES('admin','admin');");
				startActivity(intent);
				
			}
		});
		
		final Intent dining=new Intent(this,Dining.class);
		final Button button3=(Button)findViewById(R.id.button3);
		
		button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(dining);
				
			}
		});

		final Intent bedroom=new Intent(this,BedRoom.class);
		final Button button1=(Button)findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(bedroom);
			}
		});
		final Intent sitting=new Intent(this,Sitting.class);
		final Button button2=(Button)findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(sitting);
			}
		});
		
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
		     	System.out.println("Gesture SDK Values"); 
	            System.out.println("Angel = "+info.getAngle());
	            System.out.println("Speed = "+info.getSpeed());
	            mydatabase.execSQL("INSERT INTO Gesturereading VALUES("+info.getAngle()+","+info.getSpeed()+");");
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
