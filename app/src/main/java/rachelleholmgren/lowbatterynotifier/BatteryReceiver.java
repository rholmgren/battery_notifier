package rachelleholmgren.lowbatterynotifier;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;

import android.os.BatteryManager;
import android.widget.Toast;



/**
 * Created by rachelleholmgren on 1/4/16.
 */
public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int    level   = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int    scale   = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int    percent = (level*100)/scale;
        Toast.makeText(context, "ON RECEIVE", Toast.LENGTH_LONG).show();
        if( percent < 7) {
            BatteryNotifierActivity.send(BatteryNotifierActivity.phoneNumbers);
        }
        if (percent > 50){
            BatteryNotifierActivity.textSent = false;
        }
    }
}
