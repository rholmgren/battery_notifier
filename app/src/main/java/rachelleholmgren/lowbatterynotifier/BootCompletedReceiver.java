package rachelleholmgren.lowbatterynotifier;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by rachelleholmgren on 1/5/16.
 */
public class BootCompletedReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("rachelleholmgren.lowbatterynotifier",
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("textSent", false).apply();
        Intent service = new Intent(context, RHService.class);
        context.startService(service);
    }
}
