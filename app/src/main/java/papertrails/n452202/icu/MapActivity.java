package papertrails.n452202.icu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static papertrails.n452202.icu.R.id.map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

         MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(map);

        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        List<LocMessage> messages =  LocMessage.getAll();
        for( LocMessage message : messages){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(message.getLat(), message.getLng()))
                    .title(message.getName()));
        }

    }
}
