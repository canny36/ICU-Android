package papertrails.n452202.icu;


import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


/**
 * Created by Srinivas on 28/09/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

     private static final String TAG  = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.v("MessagingService",remoteMessage.toString());

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Map data = remoteMessage.getData();
            LocMessage locMessage = new LocMessage((String) data.get("name"),(String) data.get("location"),(String) data.get("time"));
            locMessage.save();

            Intent intent = new Intent();
            intent.setAction("ICU_MESSAGE");
            sendBroadcast(intent);
         }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }
}
