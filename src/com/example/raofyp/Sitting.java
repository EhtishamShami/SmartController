package com.example.raofyp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.raofyp.Dining.HttpAsyncTask;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.gesture.Sgesture;
import com.samsung.android.sdk.gesture.SgestureHand;
import com.samsung.android.sdk.gesture.SgestureHand.Info;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Sitting extends Activity implements SensorEventListener {
//////////////////////////////////Variables for gestures
private SgestureHand mGestureHand;  
private Sgesture mGesture; 
private SensorManager mSensorManager;
///////////////////////////////////////I could see a sparkle when u looks in my eyes
private RatingBar bulb1;
SQLiteDatabase mydatabase;
////////////////////////////////////Variables for Sensors
private Sensor mlight;
private Sensor mtemp;
TextView fansate;
//////////////////////////////////////
String Start_Time;
String End_Time;
float temp;
///////////////////////////////////

TextView text;
TextView fixedtext;
CountDownTimer timer;

boolean lights;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_dining);
lights=false;
text=(TextView)findViewById(R.id.textView5);
fixedtext=(TextView)findViewById(R.id.textView6);
mydatabase= openOrCreateDatabase("Readings",MODE_PRIVATE,null);
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
Random rand = new Random();
temp = rand.nextInt(50) + 1;
Start_Time=sdf.format(cal.getTime());
mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
mlight=mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
fansate=(TextView)findViewById(R.id.textView2);
bulb1=(RatingBar)findViewById(R.id.ratingBar2);
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
public final void onAccuracyChanged(Sensor sensor, int accuracy) {
// Do something here if sensor accuracy changes.
//	System.out.println(accuracy);
//	mydatabase.execSQL("INSERT INTO SensorReading VALUES("+accuracy+");");   
}

public final void onSensorChanged(SensorEvent event) {
float lightintensity = event.values[0];  
//mydatabase.execSQL("INSERT INTO SensorReading VALUES("+lightintensity+");");
//   System.out.println("Light Sensitivity "+lightintensity);
if(lightintensity==0 && lights==false)
{   
text.setVisibility(1);
fixedtext.setVisibility(1);
lights=true;

new CountDownTimer(10000, 1000) {

public void onTick(long millisUntilFinished) {
text.setText("seconds remaining: " + millisUntilFinished / 1000);
}

public void onFinish() {
if(lights==true)
{
HttpAsyncTask hat = new HttpAsyncTask();
hat.execute("http://www.neartechs.com/on.php");
text.setText("Lights ON");
bulb1.setRating(5);         
}
lights=false;
}
}.start();

//finish();

/*	
Context context = getApplicationContext();
Toast mytoast=Toast.makeText(context, "Lights are ON", Toast.LENGTH_SHORT);
mytoast.show();
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
Start_Time=sdf.format(cal.getTime());   
bulb1.setRating(5);
*/


}else
{   //Context context = getApplicationContext();
//  Toast mytoast=Toast.makeText(context, "Lights are OFF", Toast.LENGTH_SHORT);
//  mytoast.show();
//	Calendar cal = Calendar.getInstance();
//  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//End_Time=sdf.format(cal.getTime());
//   mydatabase.execSQL("INSERT INTO Timing VALUES('"+Start_Time+"','"+End_Time+"','BULB');");
//	bulb1.setRating(0);

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
if(info.getAngle()>150&& info.getSpeed()>80)
{ 
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
Start_Time=sdf.format(cal.getTime());   
Context context = getApplicationContext();
Toast mytoast=Toast.makeText(context, "BULB IS ON", Toast.LENGTH_SHORT);
mytoast.show();
fansate.setText("Swipe Right To Turn OFF");
bulb1.setRating(5);
//	  Uri uri = Uri.parse("http://www.neartechs.com/on.php"); // missing 'http://' will cause crashed
//	  Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//	  startActivity(intent);
/////////////Lord Jesus Rastafari
HttpAsyncTask hat = new HttpAsyncTask();
hat.execute("http://www.neartechs.com/on.php");




}else if(info.getAngle()<150&& info.getSpeed()>80)
{  
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
End_Time=sdf.format(cal.getTime());
Context context = getApplicationContext();
Toast mytoast=Toast.makeText(context, "LIGHTS IS OFF", Toast.LENGTH_SHORT);
mytoast.show();
fansate.setText("Swipe Left to turn ON");
mydatabase.execSQL("INSERT INTO Timing VALUES('"+Start_Time+"','"+End_Time+"','BULB');");
mydatabase.execSQL("INSERT INTO Fanreadings VALUES('"+Start_Time+"','"+End_Time+"',"+temp+");");
bulb1.setRating(0);
/*
Uri uri = Uri.parse("http://www.neartechs.com/off.php"); // missing 'http://' will cause crashed
Intent intent = new Intent(Intent.ACTION_VIEW, uri);
lights=false; 
startActivity(intent);
*/
/////////////David MF



HttpAsyncTask hat = new HttpAsyncTask();
hat.execute("http://www.neartechs.com/off.php");


}		      		  

else if(info.getAngle()<150&&info.getSpeed()<80)
{
finish();
}
}
}
};


//////////////////////////////////////////////////////////////
private class HttpAsyncTask extends AsyncTask<String, Void, String> {

protected String doInBackground(String... urls) {

return httpRequestResponse(urls[0]);
}
// onPostExecute displays the results of the AsyncTask.
protected void onPostExecute(String result) {

}
}

//For HttpAsync Functions: sending requests and receiving responses
public static String httpRequestResponse(String url){
InputStream inputStream = null;
String result = "";
try {
// create HttpClient
HttpClient httpclient = new DefaultHttpClient();

// make GET request to the given URL
HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

// receive response as inputStream
inputStream = httpResponse.getEntity().getContent();

// convert InputStream to string
if(inputStream != null)
  result = convertInputStreamToString(inputStream);
else
  result = "InputStream did not work";

} catch (Exception e) {
Log.d("InputStream", e.getLocalizedMessage());
}

return result;
}

private static String convertInputStreamToString(InputStream inputStream) throws IOException{
BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
String line = "";
String result = "";
while((line = bufferedReader.readLine()) != null)
result += line;

inputStream.close();
return result;
}

}
