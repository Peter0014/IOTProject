package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class AddAlarmActivity extends AppCompatActivity {

    private final String SERVER_URL = "http://10.0.2.2:9000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btn = (Button)findViewById(R.id.addalarm_btn);
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
                    Toast.makeText (AddAlarmActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG);
                }

                try {
                    HttpURLConnection connection = (HttpURLConnection)new URL(SERVER_URL).openConnection();

                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    JsonWriter writer = new JsonWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
                    writer.beginObject();
                    writer.name("ID").value("ALCP001");
                    writer.name("alarmdate").value(c1.getTimeInMillis());
                    writer.endObject();
                    writer.flush();
                    writer.close();

                    connection.disconnect();

                } catch (MalformedURLException e) {
                    Log.e("Malformed URL",e.getLocalizedMessage());
                } catch (IOException e) {
                    Toast.makeText(AddAlarmActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                }
            }
        });

    }



}
