package rachelleholmgren.lowbatterynotifier;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import rachelleholmgren.batterynotifier.R;


/**
 * Created by rachelleholmgren on 1/4/16.
 */
public class BatteryReceiver extends BroadcastReceiver {

    BatteryNotifierActivity batteryNotifierActivity = new BatteryNotifierActivity();

    @Override
    public void onReceive(Context context, Intent intent) {
        SplashScreen.sharedPreferences = context.getSharedPreferences("rachelleholmgren.lowbatterynotifier", Context.MODE_PRIVATE);
        int    level   = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int    scale   = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int    percent = (level*100)/scale;
        if( percent < 6 && SplashScreen.sharedPreferences.getBoolean("textSent", false) == false) {
            batteryNotifierActivity.send();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.lowbattery)
                            .setContentTitle("Low Battery Notifier")
                            .setContentText("Contacts notified of low battery");

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());



        }
        if (percent > 50){
            SharedPreferences.Editor editor = SplashScreen.sharedPreferences.edit();
            editor.putBoolean("textSent", false);
            editor.apply();

        }
    }




}
