package rachelleholmgren.lowbatterynotifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import rachelleholmgren.batterynotifier.R;


/**
 * Created by rachelleholmgren on 1/4/16.
 */
public class SplashScreen extends Activity {

    static Set<String> phoneNumbers;
    public final int PICK_CONTACT = 2015;
    public static SharedPreferences sharedPreferences;
    public static NotificationManager mNotifyMgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        phoneNumbers = new HashSet<>();
        sharedPreferences = this.getSharedPreferences("rachelleholmgren.lowbatterynotifier",
                Context.MODE_PRIVATE);


        Button button = (Button) findViewById(R.id.setNewContactsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putStringSet("phoneNumbers", new HashSet<String>()).apply();
                sharedPreferences.edit().putBoolean("textSent", false).apply();
                final int request_code = 1010;
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Set<String> phoneNums = sharedPreferences.getStringSet("phoneNumbers", new HashSet<String>());

        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNums.add(cursor.getString(column));
            sharedPreferences.edit().putStringSet("phoneNumbers", phoneNums).apply();
            Intent battery = new Intent(getApplicationContext(), RHService.class);
            startService(battery);
            Toast.makeText(this, "Contacts Set!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
