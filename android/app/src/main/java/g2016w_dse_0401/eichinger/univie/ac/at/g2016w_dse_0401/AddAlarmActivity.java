package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class AddAlarmActivity extends AppCompatActivity {

    private final String SERVER_URL = "http://10.0.2.2:9000/acrestservice/";
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
                }

                SimpleDateFormat fmtd = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat fmthh = new SimpleDateFormat("HH");
                SimpleDateFormat fmtdd = new SimpleDateFormat("mm");*/

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
