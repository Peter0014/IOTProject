package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

import g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401.AlarmDatePickerFragment;
import g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401.MainActivity;

/**
 * Created by penderiko on 18.12.16.
 */

public class AlarmTimePickerFragment extends DialogFragment {

    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = mainActivity.addAlarmCalendar;

        return new TimePickerDialog(getActivity(),timePickerListener,cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE),true);
    }

    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar cal = mainActivity.addAlarmCalendar;
            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
            cal.set(Calendar.MINUTE,minute);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);

            new AlarmDatePickerFragment().show(getActivity().getFragmentManager(),"pickDate");
        }
    };
}
