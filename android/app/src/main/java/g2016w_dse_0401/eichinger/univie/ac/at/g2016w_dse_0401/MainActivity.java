package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Goal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
>>>>>>> android

import g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401.model.AlarmItem;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AlarmItem> alarms;

    private class AlarmListAdapter extends ArrayAdapter<AlarmItem> {
        private final Context context;


        public AlarmListAdapter(Context context, int id, ArrayList<AlarmItem> list) {
            super(context, id, list);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup root) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.alarm_view, root, false);


            AlarmItem item = getItem(position);

            TextView time = (TextView) row.findViewById(R.id.alarmtime);
            TextView topic = (TextView) row.findViewById(R.id.alarmTopic);

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            time.setText(sdf.format(item.alarmtime));
            topic.setText(item.topic);

            Button btn_del_alarm = (Button)row.findViewById(R.id.btn_del_alarm);

            final long dd = item.alarmtime.getTime();

            btn_del_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //String msg = "date=" + fmtd.format(dd) + "&time=" + fmthh.format(dd) + "%3A" + fmtdd.format(dd);
                    String msg = "msdate=" + dd;

                    new AsyncRESTClient(getResources().getString(R.string.url_base))
                            .delete("delalarm", msg, new AsyncRESTClient.ResultHandler() {
                                @Override
                                public void onResult(String result, Integer statusCode) {
                                    if (statusCode > 210) Toast.makeText(MainActivity.this,"Http Status: "+statusCode,Toast.LENGTH_LONG).show();
                                    else
                                        pollAlarms();
                                }
                            });
                }
            });
            return row;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connected to Alarm Device");

        alarms = new ArrayList<>();

        TextView clocktime = (TextView) findViewById(R.id.clocktime);
        clocktime.setText(new SimpleDateFormat("HH:mm").format(pollCurrentTime()));

        Button btn = (Button) findViewById(R.id.addAlarmButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                startActivity(intent);
            }
        });

        Button btn_refresh = (Button) findViewById(R.id.refresh_btn);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollAlarms();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        pollAlarms();
    }

    private void pollAlarms() {

        AsyncRESTClient arc = new AsyncRESTClient(getResources().getString(R.string.url_base));

        arc.get("getalarms", new AsyncRESTClient.ResultHandler() {
            @Override
            public void onResult(String result, Integer statusCode) {

                Log.i("MainActivity","Http Status: " + statusCode);
                try {
                    JsonReader reader = new JsonReader(new StringReader(result));
                    reader.beginArray();
                    alarms.clear();
                    while (reader.hasNext()) {
                        Long date = reader.nextLong();
                        alarms.add(new AlarmItem(new Date(date), "", "" ));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ListView lv = (ListView)findViewById(R.id.alarms);
                lv.setAdapter(new AlarmListAdapter(MainActivity.this, R.layout.alarm_view, alarms));
            }

            @Override
            public void onIOException(IOException e) {
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

<<<<<<< HEAD
    }

    /*private void delAlarm(Date a) {

        AsyncRESTClient arc = new AsyncRESTClient(getResources().getString(R.string.url_base));
    }*/
=======

    }

    private void delAlarm(Date a) {

        AsyncRESTClient arc = new AsyncRESTClient(getResources().getString(R.string.url_base));
    }
>>>>>>> android

    private Date pollCurrentTime() {
        return new Date();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
