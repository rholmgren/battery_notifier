package rachelleholmgren.lowbatterynotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rachelleholmgren on 1/5/16.
 */
public class BootCompletedReceiver extends BroadcastReceiver{

    // When the phone turns on, restart the service that registers
    // the broadcast receiver (notified of battery changed).

    @Override
    public void onReceive(Context context, Intent intent) {
        SplashScreen.sharedPreferences = context.getSharedPreferences("rachelleholmgren.lowbatterynotifier", Context.MODE_PRIVATE);
        BatteryNotifier.setTextSent(false);
        boolean currentPreference = BatteryNotifier.getTextsEnabled();
        if(currentPreference) {
            Intent service = new Intent(context, RHService.class);
            context.startService(service);
        }
    }
}
