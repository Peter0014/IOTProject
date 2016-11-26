package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401.model;

import java.util.Date;

/**
 * Created by penderiko on 26.10.16.
 */

public class AlarmItem {
    public Date alarmtime;
    public String topic;
    public String description;

    public AlarmItem(Date alarmtime, String topic, String description) {
        this.alarmtime = alarmtime;
        this.topic = topic;
        this.description = description;
    }

}