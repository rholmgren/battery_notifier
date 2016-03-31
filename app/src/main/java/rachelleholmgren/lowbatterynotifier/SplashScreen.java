package rachelleholmgren.lowbatterynotifier;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
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
    Switch textsEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        phoneNumbers = new HashSet<>();
        sharedPreferences = this.getSharedPreferences("rachelleholmgren.lowbatterynotifier",
                Context.MODE_PRIVATE);


        textsEnabled = (Switch) findViewById(R.id.textsEnabled);
        boolean currentPreference = BatteryNotifier.getTextsEnabled();
        textsEnabled.setChecked(currentPreference);
        textsEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    BatteryNotifier.setTextsEnabled(true);
                    startBatteryService();
                    Toast.makeText(getApplicationContext(), "Texts enabled!", Toast.LENGTH_LONG).show();
                }
                else{
                    BatteryNotifier.setTextsEnabled(false);
                    stopBatteryService();
                    Toast.makeText(getApplicationContext(), "Texts disabled!", Toast.LENGTH_LONG).show();
                }
            }
        });


        Button button = (Button) findViewById(R.id.setNewContactsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BatteryNotifier.resetPhoneNumbers();
                BatteryNotifier.setTextSent(false);
                startContactPicker();
            }
        });
    }

    public void startContactPicker(){
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT);
    }

    public void startBatteryService(){
        Intent battery = new Intent(getApplicationContext(), RHService.class);
        startService(battery);
    }

    public void stopBatteryService(){
        Intent battery = new Intent(getApplicationContext(), RHService.class);
        stopService(battery);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Set<String> phoneNums = sharedPreferences.getStringSet("phoneNumbers", new HashSet<String>());

        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {

            // Query for selected phone numbers
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNums.add(cursor.getString(column));

            // Put the phone numbers into shared preferences & start service if
            // texts are enabled.
            sharedPreferences.edit().putStringSet("phoneNumbers", phoneNums).apply();
            if(textsEnabled.isChecked()) {
                startBatteryService();
                Toast.makeText(this, "Contacts Set!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}

