package papertrails.n452202.icu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

public class HistoryActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");

        listView = (ListView) findViewById(R.id.listview);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            listView.setAdapter(new MessageAdapter(this,LocMessage.getHistory()) );
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();// back
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerMsgReceiver(){

        IntentFilter filter = new IntentFilter();
        filter.addAction("ICU_MESSAGE");

        broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(listView != null)
                    listView.setAdapter(new MessageAdapter(HistoryActivity.this,LocMessage.getHistory()));

            }
        };

        registerReceiver(broadcastReceiver,filter);
    }


    private void  unRegisterMsgReceiver(){

        unregisterReceiver(broadcastReceiver);
    }

}
