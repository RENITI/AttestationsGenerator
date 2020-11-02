package fr.reniti.generator.listeners;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.logging.Logger;

public class TimeKeyListener implements View.OnKeyListener {


    /*
    TODO : A debug
     */

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        Editable text = ((EditText) v).getText();

        if (text.length() >= 5) {
            text.replace(4, text.length() - 1, "");
            return false;
        }

        if ((text.length() == 2 && text.charAt(1) != ':')) {
            text.append(':');
        }
        return false;
    }
}
