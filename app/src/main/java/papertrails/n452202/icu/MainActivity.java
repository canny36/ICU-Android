package papertrails.n452202.icu;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import static papertrails.n452202.icu.R.id.map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BroadcastReceiver broadcastReceiver;
    private ListView listView;
    private MapView mapView;
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mapView =  (MapView)findViewById(map);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        checkPermessions();

    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        registerMsgReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        unRegisterMsgReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.history){
            startActivity(new Intent(MainActivity.this,HistoryActivity.class));
        }else if(item.getItemId() == R.id.refresh){
            refreshMarkersOnMap();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<LocMessage> fetchMessages(){

       return LocMessage.getAll();
    }

    private void checkPermessions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(canAccessLocation()){
                requestPermissions( new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION },
                        101);
                return;
            }
        }

        startLocationService();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 101){
                if (canAccessLocation()){

                    startLocationService();
                }
        }
    }

    private boolean canAccessLocation() {

        return  (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
        // Request for permession
    }

    
    private void updateListView(){
        
        
    }
    private void startLocationService(){

        Intent intent = new Intent();
        intent.setAction("com.papertrails.CUSTOM_INTENT");
        sendBroadcast(intent);

    }

//        https://fcm.googleapis.com/fcm/send
//        Content-Type:application/json
//        Authorization:key=AIzaSyZ-1u...0GBYzPu7Udno5aA
//
//        {
//            "to": "/topics/foo-bar",
//                "data": {
//            "message": "This is a Firebase Cloud Messaging Topic Message!",
//        }
//        }


    private void registerMsgReceiver(){

        IntentFilter filter = new IntentFilter();
        filter.addAction("ICU_MESSAGE");

       broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshMarkersOnMap();
            }
        };

        registerReceiver(broadcastReceiver,filter);
    }


    private void  unRegisterMsgReceiver(){

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        List<LocMessage> messages =  LocMessage.getAll();
        for( LocMessage message : messages){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(message.getLat(), message.getLng()))
                    .title(message.getName()));
        }
    }


    private  void refreshMarkersOnMap(){
        this.googleMap.clear();
        List<LocMessage> messages =  LocMessage.getAll();
        for( LocMessage message : messages){
            googleMap.setMaxZoomPreference(17);

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(message.getLat(), message.getLng()))
                    .title(message.getName()));
        }
    }




}
