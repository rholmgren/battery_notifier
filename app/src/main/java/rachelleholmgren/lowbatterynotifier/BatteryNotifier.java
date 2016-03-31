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


public class BatteryNotifier {
    String msg;


    BatteryNotifier(String msg){
        this.msg = msg;
    }

    BatteryNotifier(){
        this.msg = "Phone is dead. I cannot receive texts or calls at the moment.";
    }


    public void send() {
        Set<String> phonenums = getPhoneNumbers();
        SmsManager sms = SmsManager.getDefault();

        // Send the same message to all phone numbers
        if(phonenums != null) {
                for (String str : phonenums) {
                    sms.sendTextMessage(str, null, msg, null, null);
                }
                setTextSent(true);
        }

    }

    public static void showNotification(Context context){

        Set<String> phonenums = getPhoneNumbers();
        if(phonenums != null) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.lowbattery)
                            .setContentTitle("Low Battery Notifier")
                            .setContentText("Contacts notified of low battery");

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    public static void setTextSent(boolean b){
        SharedPreferences.Editor editor = SplashScreen.sharedPreferences.edit();
        editor.putBoolean("textSent", b);
        editor.apply();
    }
    public static void setTextsEnabled(boolean b){
        SharedPreferences.Editor editor = SplashScreen.sharedPreferences.edit();
        editor.putBoolean("textsEnabled", b);
        editor.apply();
    }

    public static boolean getTextsEnabled(){
        boolean result = SplashScreen.sharedPreferences.getBoolean("textsEnabled", false);
        return result;
    }


    public static Set<String> getPhoneNumbers(){
        Set<String> phonenums = SplashScreen.sharedPreferences.getStringSet("phoneNumbers", null);
        return phonenums;
    }

    public static void resetPhoneNumbers(){
        SharedPreferences.Editor editor = SplashScreen.sharedPreferences.edit();
        editor.putStringSet("phoneNumbers", new HashSet<String>()).apply();
    }
}


