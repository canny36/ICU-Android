package papertrails.n452202.icu;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Srinivas on 28/09/2016.
 */
@Table(name = "LocMessage")
public class LocMessage extends Model{

    @Column(name = "Name")
    private String name;

    @Column(name = "lat")
    private  double lat;

    @Column(name = "lng")
    private  double lng;

    @Column(name = "time")
    private String time;

    public LocMessage(){

    }

    public LocMessage(String name, double lat,double lng, String time) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name + " - "+lng+"  - "+ lat +" - " +time;
    }

    public static  List<LocMessage> getHistory(){
        List<LocMessage> messages =  new Select()
                .from(LocMessage.class)
                .groupBy("Name")
                .execute();
        List<LocMessage> history = new ArrayList<>();
        for (LocMessage message:messages) {
           List<LocMessage> historyMsgs =  new Select()
                    .from(LocMessage.class)
                    .where("Name = ?",message.getName())
                    .orderBy("datetime(time) DESC limit 10")
                    .execute();
            history.addAll(historyMsgs);
        }

        Log.v("LocaMessage"," Size of history "+history.size());
        return  history;
    }

    public static List<LocMessage> getAll() {
        return new Select()
                .from(LocMessage.class)
                .orderBy("datetime(time) DESC limit 10")
                .groupBy("Name")
                .execute();
    }
}
