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

// ContactActivity extends ListActivity implements OnItemListener

//    getAllContacts(this.getContentResolver());
//    ListView lv = (ListView) findViewById(R.id.lv);
//    ma = new MyAdapter();
//    lv.setAdapter(ma);
//    lv.setOnItemClickListener(this);
//    lv.setItemsCanFocus(false);
//    lv.setTextFilterEnabled(true);
//    // adding
//    select = (Button) findViewById(R.id.button1);
//    select.setOnClickListener(new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            StringBuilder checkedcontacts = new StringBuilder();
//
//            for (int i = 0; i < name1.size(); i++)
//
//            {
//                if (ma.mCheckStates.get(i) == true) {
//                    checkedcontacts.append(name1.get(i).toString());
//                    checkedcontacts.append("\n");
//
//                } else {
//
//                }
//
//            }
//
//            Toast.makeText(Display.this, checkedcontacts, 1000).show();
//        }
//    });
//
//}
//
//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        // TODO Auto-generated method stub
//        ma.toggle(arg2);
//    }
//
//    public void getAllContacts(ContentResolver cr) {
//
//        Cursor phones = cr.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
//                null, null);
//        while (phones.moveToNext()) {
//            String name = phones
//                    .getString(phones
//                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String phoneNumber = phones
//                    .getString(phones
//                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            name1.add(name);
//            phno1.add(phoneNumber);
//        }
//
//        phones.close();
//    }
//
//class MyAdapter extends BaseAdapter implements
//        CompoundButton.OnCheckedChangeListener {
//    private SparseBooleanArray mCheckStates;
//    LayoutInflater mInflater;
//    TextView tv1, tv;
//    CheckBox cb;
//
//    MyAdapter() {
//        mCheckStates = new SparseBooleanArray(name1.size());
//        mInflater = (LayoutInflater) Display.this
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return name1.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//
//        return 0;
//    }
//
//    @Override
//    public View getView(final int position, View convertView,
//                        ViewGroup parent) {
//        // TODO Auto-generated method stub
//        View vi = convertView;
//        if (convertView == null)
//            vi = mInflater.inflate(R.layout.row, null);
//        tv = (TextView) vi.findViewById(R.id.textView1);
//        tv1 = (TextView) vi.findViewById(R.id.textView2);
//        cb = (CheckBox) vi.findViewById(R.id.checkBox1);
//        tv.setText("Name :" + name1.get(position));
//        tv1.setText("Phone No :" + phno1.get(position));
//        cb.setTag(position);
//        cb.setChecked(mCheckStates.get(position, false));
//        cb.setOnCheckedChangeListener(this);
//
//        return vi;
//    }
//
//    public boolean isChecked(int position) {
//        return mCheckStates.get(position, false);
//    }
//
//    public void setChecked(int position, boolean isChecked) {
//        mCheckStates.put(position, isChecked);
//    }
//
//    public void toggle(int position) {
//        setChecked(position, !isChecked(position));
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView,
//                                 boolean isChecked) {
//        // TODO Auto-generated method stub
//
//        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
//    }
//}
