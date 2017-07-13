package me.vguillou.etickettile;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import me.vguillou.etickettile.helper.ETicketTileHelper;
import me.vguillou.etickettile.helper.PreferenceHelper;
import me.vguillou.etickettile.helper.QsTileHelper;

public class MainActivity extends BaseMainActivity {

    private ETicketTileHelper mETicketTileHelper;

    /**
     * The preference helper
     */
    private PreferenceHelper mPreferenceHelper;

    private boolean mTileShown = false;

    /**
     * Switch between Quick Settings activation mode
     */
    protected Switch switchMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mETicketTileHelper = new ETicketTileHelper(this);
        mPreferenceHelper = new PreferenceHelper(this);
        mTileShown = mETicketTileHelper.isTileShown();

        // The QS mode switch
        switchMode = (Switch) findViewById(R.id.mode_switch);
        switchMode.setChecked(QsTileHelper.isAlternativeMode(mPreferenceHelper));
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                QsTileHelper.setAlternativeMode(mPreferenceHelper, isChecked);
            }
        });
        updateView(mTileShown);
    }

    /**
     * Sort of onResume / onPause, but triggered also when the notification shade is pulled up / down
     *
     * @param hasFocus If the Activity has the focus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mETicketTileHelper.isTileShown() != mTileShown) {
            mTileShown = !mTileShown;
            updateView(mTileShown);
        }
    }

    /**
     * Update the views according to the tile status
     *
     * @param tileShown Must be {@code true} if the user added the tile to the Quick Settings
     */
    private void updateView(final boolean tileShown) {
        switchMode.setVisibility(tileShown ? View.VISIBLE : View.GONE);
        btnAppLauncherToggle.setVisibility(tileShown ? View.VISIBLE : View.GONE);
        findViewById(R.id.textView_step_img).setVisibility(tileShown ? View.GONE : View.VISIBLE);
        ((TextView) findViewById(R.id.textView_step_content)).setText(tileShown ?
                R.string.activity_main_step_end_content : R.string.activity_main_step_1_content);
    }
}
