package papertrails.n452202.icu;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Srinivas on 28/09/2016.
 */
@Table(name = "LocMessage")
public class LocMessage extends Model{

    @Column(name = "Name")
    private String name;

    @Column(name = "locStr")
    private  String locStr;

    @Column(name = "time")
    private String time;

    public LocMessage(){

    }

    public LocMessage(String name, String locStr, String time) {
        this.name = name;
        this.locStr = locStr;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLocStr() {
        return locStr;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return name + " - "+locStr+" - "+time;
    }

    public static List<LocMessage> getAll() {
        return new Select()
                .from(LocMessage.class)
                .orderBy("datetime(time) DESC limit 10")
                .execute();
    }
}
