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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

         listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new MessageAdapter(this,fetchMessages()));

        checkPermessions();

    }

    @Override
    protected void onResume() {
        super.onResume();

        registerMsgReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterMsgReceiver();
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

        sendMessage();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 101){
                if (canAccessLocation()){

                    sendMessage();
                }
        }
    }

    private boolean canAccessLocation() {

        return  (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
        // Request for permession
    }

    
    private void updateListView(){
        
        
    }
    private void sendMessage(){

        Intent intent = new Intent();
        intent.setAction("com.papertrails.CUSTOM_INTENT");
        sendBroadcast(intent);

//        FirebaseMessaging fm = FirebaseMessaging.getInstance();
//        fm.send(new RemoteMessage.Builder("https://fcm.googleapis.com/fcm/send")
//                .addData("my_message", "Hello World")
//                .addData("my_action","SAY_HELLO")
//                .build());
//

//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://fcm.googleapis.com/fcm/send";
//
//        JSONObject json = new JSONObject();
//        try{
//            json.put("to","/topics/news");
//            JSONObject data = new JSONObject();
//            data.put("message","Message from Srini");
//            data.put("loc","MyHome");
//            json.put("data",data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//
//                        Log.v("MainActivity",response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("MainActivity",error.toString());
//
//                    }
//                }){
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization","key=AIzaSyAhl4l0UyAAqB2uz1eyDQGuaV1H0t9zq2Q");
//                return headers;
//            }
//        };
//
//        ApplicationController.getInstance().addToRequestQueue(jsObjRequest);


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
                listView.setAdapter(new MessageAdapter(MainActivity.this,fetchMessages()));

            }
        };

        registerReceiver(broadcastReceiver,filter);
    }


    private void  unRegisterMsgReceiver(){

        unregisterReceiver(broadcastReceiver);
    }


    public static class MessageAdapter extends ArrayAdapter<LocMessage> {

        private Context context;
        private List<LocMessage> items;
        private MessageVH messageVh;

        public MessageAdapter(Context context, List<LocMessage> objects) {
            super(context, -1, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_msg, null);
                messageVh = new MessageVH(convertView);
                convertView.setTag(messageVh);
            }else{

                messageVh = (MessageVH) convertView.getTag();
            }

            LocMessage message =  this.items.get(position);
            messageVh.updataView(message);

            return convertView;
        }


        public   class MessageVH{

            public TextView nameTextView;
            public  TextView locTextView;
            public  TextView timeTextView;

            public  MessageVH(View view){

                nameTextView =  ((TextView) view.findViewById(R.id.textView));
                locTextView =  ((TextView) view.findViewById(R.id.textView2));
                timeTextView =  ((TextView) view.findViewById(R.id.textView3));

            }

            public  void  updataView(LocMessage locMessage){

                nameTextView.setText(locMessage.getName());
                locTextView.setText(locMessage.getLocStr());
                timeTextView.setText(locMessage.getTime());

            }

        }

    }

}
