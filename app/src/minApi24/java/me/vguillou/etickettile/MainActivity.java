package me.vguillou.etickettile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import me.vguillou.etickettile.helper.ETicketTileHelper;

public class MainActivity extends BaseMainActivity {

    private ETicketTileHelper mETicketTileHelper;

    private boolean mTileShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mETicketTileHelper = new ETicketTileHelper(this);
        mTileShown = mETicketTileHelper.isTileShown();
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
        btnAppLauncherToggle.setVisibility(tileShown ? View.VISIBLE : View.GONE);
        findViewById(R.id.textView_step_img).setVisibility(tileShown ? View.GONE : View.VISIBLE);
        ((TextView) findViewById(R.id.textView_step_content)).setText(tileShown ?
                R.string.activity_main_step_end_content : R.string.activity_main_step_1_content);
    }
}
