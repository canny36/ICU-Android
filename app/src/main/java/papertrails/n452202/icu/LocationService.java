package papertrails.n452202.icu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Srinivas on 28/09/2016.
 */

public class LocationService extends Service {




    private ConnectivityManager cnnxManager;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("LocationService","onCreate");
        cnnxManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentOnAlarm = new Intent(
                LaunchReceiver.ACTION_PULSE_SERVER_ALARM);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intentOnAlarm, 0);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sendMessage();

        return START_STICKY;


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void sendMessage(){

//        FirebaseMessaging fm = FirebaseMessaging.getInstance();
//        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
//                .setMessageId(Integer.toString(msgId.incrementAndGet()))
//                .addData("my_message", "Hello World")
//                .addData("my_action","SAY_HELLO")
//                .setMessageType()
//                .build());


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://fcm.googleapis.com/fcm/send";
        Location currentLocation = getLocation();
        JSONObject json = new JSONObject();
        try{

            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = s.format(new Date());

            json.put("to","/topics/news");
            JSONObject data = new JSONObject();
            data.put("name",getName());
            if(currentLocation != null){
                data.put("location",""+currentLocation.toString());
            }else{
                data.put("location"," CurrentLocation");
            }
            data.put("time",format);

            json.put("data",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        long timeToAlarm = SystemClock.elapsedRealtime() + 10
                                * 1000;
                        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToAlarm,
                                alarmIntent);
                        Log.v("MainActivity",response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity",error.toString());

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization","key=AIzaSyAhl4l0UyAAqB2uz1eyDQGuaV1H0t9zq2Q");
                return headers;
            }
        };

        RequestManager.getInstance(this).addToRequestQueue(jsObjRequest);

    }


    public Location getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request for permession


            }

            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                return lastKnownLocationGPS;
            } else {
                Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                return loc;
            }
        } else {
            return null;
        }
    }

    private String getName(){

        final SharedPreferences sharedPref = this.getSharedPreferences("icuapp",MODE_PRIVATE);
        return sharedPref.getString("name","Srini");

    }



}
