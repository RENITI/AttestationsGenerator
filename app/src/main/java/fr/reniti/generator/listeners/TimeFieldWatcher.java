package fr.reniti.generator.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class TimeFieldWatcher implements TextWatcher {

    private Calendar cal = Calendar.getInstance();
    private static final String hhmm = "HHMM";

    private EditText view;
    private String current;

    public TimeFieldWatcher(EditText view,  String defaultValue)
    {
        this.view = view;
        this.current =  defaultValue;

        view.setText(defaultValue);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 4; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 5) {
                clean = clean + hhmm.substring(clean.length());
            } else {
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int hours = Integer.parseInt(clean.substring(0, 2));
                int min = Integer.parseInt(clean.substring(2, 4));

                hours = hours < 0 ? 0 : hours > 23 ? 23 : hours;
                cal.set(Calendar.HOUR, hours);

                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                min = min < 0 ? 0 : min > 59 ? 59 : min;
                clean = String.format("%02d%02d", hours, min);
            }

            clean = String.format("%s:%s", clean.substring(0, 2),
                    clean.substring(2, 4));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            view.setText(current);
            view.setSelection(sel < current.length() ? sel : current.length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
