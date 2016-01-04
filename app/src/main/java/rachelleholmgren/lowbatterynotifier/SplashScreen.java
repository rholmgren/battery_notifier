package rachelleholmgren.lowbatterynotifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import rachelleholmgren.batterynotifier.R;


/**
 * Created by rachelleholmgren on 1/4/16.
 */
public class SplashScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setMessage("Contacts")
                .setPositiveButton("Previously saved contacts", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent battery = new Intent(getApplicationContext(), RHService.class);
                        startService(battery);
                        finish();
                    }
                })
                .setNegativeButton("New contacts", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (BatteryNotifierActivity.phoneNumbers != null) {
                            BatteryNotifierActivity.phoneNumbers.clear();
                        }
                        Intent i = new Intent(SplashScreen.this, BatteryNotifierActivity.class);
                        startActivity(i);
                    }
                }).show();



    }
}
