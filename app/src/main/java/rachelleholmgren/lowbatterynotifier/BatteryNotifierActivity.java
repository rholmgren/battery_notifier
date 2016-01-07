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
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import rachelleholmgren.batterynotifier.R;


public class BatteryNotifierActivity{
    private Context context;
    static boolean textSent;




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
                SplashScreen.sharedPreferences.edit().putStringSet("phoneNumbers", phoneNumbers);
            }
            else{
                Toast.makeText(this.context, "phone1 is not a valid number", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void send() {
        Set<String> phonenums = SplashScreen.sharedPreferences.getStringSet("phoneNumbers", null);
        boolean textSent = SplashScreen.sharedPreferences.getBoolean("textSent", false);
        String msg = "Phone is dead. I cannot receive texts or calls at the moment.";

        SmsManager sms = SmsManager.getDefault();

        System.out.println("PHONE NUMBERS: " + phonenums);
        // Send the same message to all phone numbers entered by user
        if(phonenums != null) {
                System.out.println("PHONE NUMBERS: " + phonenums);
                for (String str : phonenums) {
                    sms.sendTextMessage(str, null, msg, null, null);
                }
                SplashScreen.sharedPreferences.edit().putBoolean("textSent", true).apply();


        }

    }


}


