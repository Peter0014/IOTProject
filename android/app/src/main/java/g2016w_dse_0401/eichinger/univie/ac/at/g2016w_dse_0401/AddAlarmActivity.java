package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity {

    //private final String SERVER_URL = "http://10.0.2.2:9000/acrestservice/";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = (Button) findViewById(R.id.addalarm_btn);
        final EditText date = (EditText) findViewById(R.id.editAlarmDate);
        final EditText time = (EditText) findViewById(R.id.editAlarmTime);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c1 = Calendar.getInstance();

                SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm");
                try {
                    c1.setTime(format.parse(date.getText().toString() + time.getText().toString()));
                } catch (ParseException e) {
                    Toast.makeText(AddAlarmActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                /*
                StringWriter sw = new StringWriter();

                JsonWriter writer = new JsonWriter(sw);

                try {
                    writer.beginObject();
                    writer.name("alarmdate").value(c1.getTimeInMillis());
                    writer.endObject();
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddAlarmActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                }*/

                long dd = c1.getTimeInMillis();
                String msg = "msdate=" + dd;
                new AsyncRESTClient(getResources().getString(R.string.url_base))
                        .post("postalarm", msg, new AsyncRESTClient.ResultHandler() {
                            @Override
                            public void onResult(String res, Integer statusCode) {
                                if (statusCode > 210) Toast.makeText(AddAlarmActivity.this, "Http Status: " + statusCode, Toast.LENGTH_LONG).show();
                            }
                        });

                AddAlarmActivity.this.finish();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AddAlarm Page") // TODO: Define a title for the content shown.
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
}
