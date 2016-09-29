package papertrails.n452202.icu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initViews();
    }

    private void initViews(){

        final EditText editText = (EditText) findViewById(R.id.edit_name);

        final SharedPreferences sharedPref = this.getSharedPreferences("icuapp",MODE_PRIVATE);
        Boolean isSubscribed = sharedPref.getBoolean("subscribed",false);

        if(isSubscribed){

            startActivity(new Intent(this,MainActivity.class));

        }else{

            findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sharedPref.edit().putBoolean("subscribed",true).putString("name",editText.getText().toString()).apply();
                    startActivity(new Intent(v.getContext(),MainActivity.class));
                }
            });

        }

    }
}
