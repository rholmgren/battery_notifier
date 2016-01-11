package rachelleholmgren.lowbatterynotifier;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rachelleholmgren.batterynotifier.R;

/**
 * Created by rachelleholmgren on 1/10/16.
 */
public class ContactActivity extends ListActivity implements AdapterView.OnItemClickListener {
    public static Set<String> namesSet = new HashSet<>();
    public static Set<String> phonesSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getAllContacts(this.getContentResolver());
        super.onCreate(savedInstanceState);
    }

        public void getAllContacts(ContentResolver cr) {

        Cursor phones = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {
            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            namesSet.add(name);
            phonesSet.add(phoneNumber);
        }

        phones.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



    ListView lv = (ListView) findViewById(R.id.lv);
    MyAdapter ma = new MyAdapter();
    lv.setAdapter(ma);
    lv.setOnItemClickListener(this);
    lv.setItemsCanFocus(false);
    lv.setTextFilterEnabled(true);
    // adding
    select = (Button) findViewById(R.id.button1);
    select.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            StringBuilder checkedcontacts = new StringBuilder();

            for (int i = 0; i < name1.size(); i++)

            {
                if (ma.mCheckStates.get(i) == true) {
                    checkedcontacts.append(name1.get(i).toString());
                    checkedcontacts.append("\n");

                } else {

                }

            }

            Toast.makeText(Display.this, checkedcontacts, 1000).show();
        }
    });

}

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        ma.toggle(arg2);
    }


}

class MyAdapter extends BaseAdapter implements
        CompoundButton.OnCheckedChangeListener {
    private SparseBooleanArray mCheckStates;
    LayoutInflater mInflater;
    TextView tv1, tv;
    CheckBox cb;

    MyAdapter() {
        mCheckStates = new SparseBooleanArray(ContactActivity.namesSet.size());
        mInflater = (LayoutInflater) Display.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ContactActivity.namesSet.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub

        return 0;
    }


    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (convertView == null)
            vi = mInflater.inflate(R.layout.row, null);
        tv = (TextView) vi.findViewById(R.id.textView1);
        tv1 = (TextView) vi.findViewById(R.id.textView2);
        cb = (CheckBox) vi.findViewById(R.id.checkBox1);
        tv.setText("Name :" + name1.get(position));
        tv1.setText("Phone No :" + phno1.get(position));
        cb.setTag(position);
        cb.setChecked(mCheckStates.get(position, false));
        cb.setOnCheckedChangeListener(this);

        return vi;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);
    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        // TODO Auto-generated method stub

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }
}
