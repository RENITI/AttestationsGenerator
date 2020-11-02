package fr.reniti.generator.listeners;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class DateKeyListener implements View.OnKeyListener {

    /*
    TODO : A debug
     */

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        Editable text = ((EditText) v).getText();

        if (text.length() >= 10) {
            text.replace(9, text.length() - 1, "");
            return false;
        }
        if ((text.length() == 2 && text.charAt(1) != '/') || (text.length() == 5 && text.charAt(4) != '/')) {
            text.append('/');
            return true;
        }

        if(text.length() >= 2 && text.charAt(text.length()-1) == '/' && text.charAt(text.length()-2) == '/')
        {
            text.replace(text.length()-1, text.length(), "");
            return true;
        }
        return false;
    }
}
