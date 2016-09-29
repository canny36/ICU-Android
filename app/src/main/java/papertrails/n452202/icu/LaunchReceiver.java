package papertrails.n452202.icu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LaunchReceiver extends BroadcastReceiver {

    public static final String ACTION_PULSE_SERVER_ALARM = 
            "com.proofbydesign.homeboy.ACTION_PULSE_SERVER_ALARM";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("LaunchReceiver","OnReceive for " + intent.getAction());
       // AppGlobal.logDebug("OnReceive for " + intent.getAction());
        //AppGlobal.logDebug(intent.getExtras().toString());
        Intent serviceIntent = new Intent(context,
                LocationService.class);
        context.startService(serviceIntent);
    }
}