package fr.reniti.generator.utils;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import fr.reniti.generator.storage.models.Reason;

public class ReasonTextToggle implements View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

    private final Reason reason;
    private boolean state;

    public ReasonTextToggle(Reason reason)
    {
        this.reason = reason;
        this.state = false;
    }

    @Override
    public boolean onLongClick(View v) {
        toggle(v);
        return false;
    }

    public void toggle(View v)
    {
        state = !state;
        ((CheckBox) v).setText(state ? reason.getLongTextId() : reason.getDisplayName());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        state = isChecked;
        ((CheckBox) buttonView).setText(state ? reason.getLongTextId() : reason.getDisplayName());
    }
}
