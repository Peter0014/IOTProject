package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401.model.AlarmItem;

public class MainActivity extends AppCompatActivity {

    public Calendar addAlarmCalendar;

    final int DialogID = 0;
    private String urlBase;

    private ArrayList<AlarmItem> alarms;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

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

            Button btn_del_alarm = (Button) row.findViewById(R.id.btn_del_alarm);

            final long dd = item.alarmtime.getTime();

            btn_del_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //String msg = "date=" + fmtd.format(dd) + "&time=" + fmthh.format(dd) + "%3A" + fmtdd.format(dd);
                    String msg = "msdate=" + dd;

                    new AsyncRESTClient(urlBase)
                            .delete("delalarm", msg, new AsyncRESTClient.ResultHandler() {
                                @Override
                                public void onResult(String result, Integer statusCode) {
                                    if (statusCode > 210)
                                        Toast.makeText(MainActivity.this, "Http Status: " + statusCode, Toast.LENGTH_LONG).show();
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
    protected void onCreate(final Bundle savedInstanceState) {
        urlBase = getResources().getString(R.string.url_base);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connected to Alarm Device");

        findConnection();
        alarms = new ArrayList<>();

        TextView clocktime = (TextView) findViewById(R.id.clocktime);
        clocktime.setText(new SimpleDateFormat("HH:mm").format(pollCurrentTime()));

        Button btn = (Button) findViewById(R.id.addAlarmButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarmCalendar = Calendar.getInstance();
                new AlarmTimePickerFragment().show(getFragmentManager(), "pickTime");
            }
        });

        Button btn_refresh = (Button) findViewById(R.id.refresh_btn);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollAlarms();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pollAlarms();
    }


    public void findConnection() {

        new AsyncTask<Void, Void, InetAddress>() {

            @Override
            protected InetAddress doInBackground(Void... params) {

                InetAddress acAddress = null;
                DatagramSocket socket = null;
                InetAddress ip;
                int serverPort = 29902;
                try {
                    socket = new DatagramSocket(serverPort);

                    byte[] buf = new byte[512];
                    DatagramPacket packet = new DatagramPacket(buf,buf.length);
                    try {
                        socket.setSoTimeout(10000);
                        socket.setBroadcast(true);
                        socket.receive(packet);
                        acAddress = packet.getAddress();
                    } catch (SocketTimeoutException e) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Connection timeout!!", Toast.LENGTH_LONG);
                            }
                        });
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Logger.getLogger("Test").log(Level.ALL, buf.toString());

                } catch (SocketException e) {
                    e.printStackTrace();
                }
                return acAddress;

            }

            @Override
            protected void onPostExecute(final InetAddress address) {
                if (address != null) {
                    urlBase = address.getHostName() + ":9000/acrestservice/";
                    pollAlarms();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Connection established with " + address.getHostName(), Toast.LENGTH_LONG);
                        }
                    });
                }
            }

        }.execute();


    }

    public void pollAlarms() {

        AsyncRESTClient arc = new AsyncRESTClient(urlBase);
        arc.get("getalarms", new AsyncRESTClient.ResultHandler() {

            @Override
            public void onResult(String result, Integer statusCode) {
                Log.i("MainActivity", "Http Status: " + statusCode);

                try {
                    JsonReader reader = new JsonReader(new StringReader(result));
                    reader.beginArray();
                    alarms.clear();
                    while (reader.hasNext()) {
                        Long date = reader.nextLong();
                        alarms.add(new AlarmItem(new Date(date), "", ""));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Collections.sort(alarms, new Comparator<AlarmItem>() {
                    @Override
                    public int compare(AlarmItem lhs, AlarmItem rhs) {
                        return lhs.alarmtime.compareTo(rhs.alarmtime);
                    }
                });
                ListView lv = (ListView) findViewById(R.id.alarms);
                lv.setAdapter(new AlarmListAdapter(MainActivity.this, R.layout.alarm_view, alarms));
            }

            @Override
            public void onIOException(IOException e) {
                final IOException exe = e;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, exe.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    /*private void delAlarm(Date a) {

        AsyncRESTClient arc = new AsyncRESTClient(getResources().getString(R.string.url_base));
    }*/

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