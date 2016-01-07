package rachelleholmgren.lowbatterynotifier;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.Toast;

/**
 * Created by rachelleholmgren on 1/4/16.
 */
public class RHService extends Service {
    private BatteryReceiver breceiver;


    @Override
    public void onCreate() {
        breceiver = new BatteryReceiver();
        registerReceiver(breceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(breceiver);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
