package rachelleholmgren.lowbatterynotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import rachelleholmgren.batterynotifier.R;


public class BatteryNotifierActivity extends Activity{
    private Context context;
    static boolean textSent;
    static Set<String> phoneNumbers;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumbers = new HashSet<>();
        this.context = this;
        textSent = false;
        Button doneSettingContacts = (Button) findViewById(R.id.btnDone);

        sharedPreferences = this.getSharedPreferences("rachelleholmgren.lowbatterynotifier",
                Context.MODE_PRIVATE);
        String contactsSet = sharedPreferences.getString("contactsSet", "no");


        Boolean firstRun = sharedPreferences.getBoolean("firstRun", true);

        sharedPreferences.edit().putBoolean("firstRun", false).apply();

        doneSettingContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Phone numbers registered", Toast.LENGTH_LONG).show();
                // Check if each phone number entered by user is valid
                // If so, add it to the list of contacts you will send the message to
                checkValid(phoneNumbers, ((EditText)findViewById(R.id.phoneNumber1)).getText().toString());
                checkValid(phoneNumbers, ((EditText)findViewById(R.id.phoneNumber2)).getText().toString());
//                checkValid(phoneNumbers, ((EditText)findViewById(R.id.phoneNumber3)).getText().toString());
//                checkValid(phoneNumbers, ((EditText)findViewById(R.id.phoneNumber4)).getText().toString());
//                checkValid(phoneNumbers, ((EditText)findViewById(R.id.phoneNumber5)).getText().toString());

                ArrayList<String> arrayList = new ArrayList<String>();
                for (String str : phoneNumbers)
                    arrayList.add(str);
                Intent battery = new Intent(getApplicationContext(), RHService.class);
                startService(battery);

            }
        });


    }

    public void checkValid(Set<String> phoneNumbers, String phoneNumber){
        long phoneNumberInt;
        String phone;

        // If the user entered a number,
        // Parse it to a long, then convert it to a string
        // If it is 10 digits, it's valid and add it to the set of valid numbers
        if(phoneNumber.trim().length() > 0) {
            phoneNumberInt = Long.parseLong(phoneNumber.trim());
            phone = Long.toString(phoneNumberInt);
            if(phone.length() == 10){
                phoneNumbers.add(phone);
                sharedPreferences.edit().putStringSet("phoneNumbers", phoneNumbers);
            }
            else{
                Toast.makeText(this.context, "phone1 is not a valid number", Toast.LENGTH_LONG).show();
            }
        }
    }

    static public void send(Set<String> phonenums) {
        String msg = "Phone is dead. I cannot receive texts or calls at the moment.";
        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();

        // Send the same message to all phone numbers entered by user
        if(textSent == false) {
            if(phonenums != null) {
                for (String str : phonenums) {
                    sms.sendTextMessage(str, null, msg, null, null);
                }
                textSent = true;
            }
        }
    }

}
