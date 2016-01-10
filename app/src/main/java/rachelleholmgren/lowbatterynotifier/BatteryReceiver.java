package rachelleholmgren.lowbatterynotifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;



/**
 * Created by rachelleholmgren on 1/4/16.
 *
 */


public class BatteryReceiver extends BroadcastReceiver {

    // 1. Send text and show notification when battery life
    //    drops below lowerBoundText,
    // 2. If a) battery life reaches above upperBoundText and
    //       b) battery life falls below lowerBoundText again:
    //          Resend text to contacts and show notification.

    int lowerBoundText = 4;
    int upperBoundText = 5;

    String msg = "Phone is about to die. May be unreachable soon.";

    BatteryNotifier batteryNotifier = new BatteryNotifier(msg);

    @Override

    // Receive notifications of changed battery life.
    // Calculate remaining battery percentage.

    public void onReceive(Context context, Intent intent) {
        SplashScreen.sharedPreferences = context.getSharedPreferences("rachelleholmgren.lowbatterynotifier", Context.MODE_PRIVATE);
        int    level   = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int    scale   = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int    percent = (level*100)/scale;
        boolean textSent = SplashScreen.sharedPreferences.getBoolean("textSent", false);

        // Check if battery is below lowerBound and text hasn't already been sent
        if( percent < lowerBoundText && textSent == false) {
            batteryNotifier.send();
            batteryNotifier.showNotification(context);
        }

        // Otherwise forget about previously  texts,
        // and prepare to send text again if battery life
        // falls below the lower bound.
        else if (percent > upperBoundText){
            batteryNotifier.setTextSent(false);
        }
    }
}
