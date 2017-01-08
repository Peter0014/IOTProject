package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class AlarmDatePickerFragment extends DialogFragment {

    //private final String SERVER_URL = "http://10.0.2.2:9000/acrestservice/";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    final int DialogID = 1;

    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {

        Calendar cal = mainActivity.addAlarmCalendar;

        return new DatePickerDialog(getActivity(),datePickerListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
    }

    protected DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = mainActivity.addAlarmCalendar;
            cal.set(year, monthOfYear, dayOfMonth);
            AsyncRESTClient arc = new AsyncRESTClient(getResources().getString(R.string.url_base));
            arc.post("postalarm", "msdate=" + String.valueOf(cal.getTimeInMillis()), new AsyncRESTClient.ResultHandler() {
                @Override
                public void onResult(String result, Integer statusCode) {
                    Log.i("AddAlarmDialog","Http Status: " + statusCode);
                    if (statusCode > 210) Toast.makeText(mainActivity,"Http Status: "+statusCode,Toast.LENGTH_LONG).show();

                    mainActivity.pollAlarms();
                }
            });
        }
    };
}