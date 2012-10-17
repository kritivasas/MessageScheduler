package com.reminder3;

import java.util.*;
import android.os.Bundle;
import android.provider.ContactsContract;
//import com.example.android.apis.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
//import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
//import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
//import android.telephony.SmsManager;
import android.telephony.SmsManager;
import android.view.*;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddReminder extends Activity {
	/**public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Artists tab");
        setContentView(textview);
    }*/
	 // where we display the selected date and time
    private TextView mDateDisplay;
    private TextView topicdisp;

    // date and time
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String toPic;
    private String phoneNo;
    private String msgToSend;
    private Button btnSendSMS;
    private EditText msg;
    private EditText phno;
    private EditText tpc;
    private DbAdapter ctdb;
    private ArrayList<Map<String, String> > mPeopleList;
    private SimpleAdapter mAdapter;
    private AutoCompleteTextView mTxtPhoneNo;
    static final int TIME_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNo = "";
        msgToSend = "";
        toPic="";
        ctdb = new DbAdapter(this);
        ctdb.open();
        mPeopleList=new ArrayList<Map<String,String>>();
        setContentView(R.layout.date_widgets_example_1);

        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        topicdisp = (TextView) findViewById(R.id.alarmMessage1);

        Button pickDate = (Button) findViewById(R.id.pickDate);
        pickDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        Button pickTime = (Button) findViewById(R.id.pickTime);
        pickTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    /////////////////////////////////////////////////
        tpc = (EditText) findViewById(R.id.topic);
        tpc.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
            	if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                  Toast.makeText(AddReminder.this, tpc.getText(), Toast.LENGTH_SHORT).show();
                  return true;
                }
                return false;
            }
        });
        /////////////////////////////////////////////
        /*phno = (EditText) findViewById(R.id.phno);
        phno.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                  Toast.makeText(AddReminder.this, phno.getText(), Toast.LENGTH_SHORT).show();
                  return true;
                }
                return false;
            }
        });*/
        
        msg = (EditText) findViewById(R.id.txtMessage);
        msg.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                	msg.append("\n");
                  Toast.makeText(AddReminder.this, msg.getText(), Toast.LENGTH_SHORT).show();
                  return true;
                }
                return false;
            }
        });
        
        PopulatePeopleList();
        mTxtPhoneNo = (AutoCompleteTextView) findViewById(R.id.mmWhoNo); 
        mAdapter = new SimpleAdapter(this,
        			mPeopleList, R.layout.customcontview ,
        			new String[] { "Name", "Phone" , "Type" }, 
        			new int[] { R.id.ccontName, R.id.ccontNo, R.id.ccontType }); 
        mTxtPhoneNo.setAdapter(mAdapter);
        mTxtPhoneNo.getItemClickListener();
        /*mTxtPhoneNo.setOnItemSelectedListener(new OnItemSelectedListener(){
        	public void onItemSelected(AdapterView<?> p,View v,int pos,long id){
        		
        	}
        	public void onNothingSelected(AdapterView<?> p) {
        		
        	}
        });*/
        btnSendSMS = (Button) findViewById(R.id.btnSetAlarm);
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	toPic = tpc.getText().toString();
            	String s=mTxtPhoneNo.getText().toString();
            	if ((s.charAt(0))=='{') {
            		phoneNo=s.substring(s.lastIndexOf('=')+1,s.lastIndexOf('}'));
            		
            	}else{
            		phoneNo=s;
            	}
            	
                //phoneNo = phno.getText().toString();
                msgToSend=msg.getText().toString();
                updateDisplay();
                Boolean b=true;
            	if(b == true) {
            		
            		ctdb.createReminderData(mYear, mMonth, mDay, mHour, mMinute,toPic, phoneNo, msgToSend);
            		int keyp=1;
            		if(ctdb.fetchAllReminderData().getCount()>0) {
            			Cursor cc=ctdb.fetchAllReminderData();
            			startManagingCursor(cc);
            			cc.moveToLast();
            			keyp=cc.getInt(cc.getColumnIndex("_id"));
            		}
            		keyp = keyp+100000;
            		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);                 
                    
                    //---get current date and time---
                    Calendar calendar = Calendar.getInstance();       

                    //---sets the time for the alarm to trigger---
                    calendar.set(Calendar.YEAR, mYear);
                    calendar.set(Calendar.MONTH, mMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, mDay);                 
                    calendar.set(Calendar.HOUR_OF_DAY, mHour);
                    calendar.set(Calendar.MINUTE, mMinute);
                    calendar.set(Calendar.SECOND, 0);
                    
                    //---PendingIntent to launch activity when the alarm triggers-
                    Intent i=new Intent(AddReminder.this,SetAlarm.class);
                    i.putExtra("num", phoneNo);
                    i.putExtra("topic", toPic);
                    i.putExtra("msg", msgToSend);
                    Toast.makeText(AddReminder.this,"Alarm Set!!", Toast.LENGTH_SHORT).show();
                    PendingIntent sender = PendingIntent.getBroadcast(AddReminder.this, 
                    		keyp, i, PendingIntent.FLAG_UPDATE_CURRENT); 
                    //---sets the alarm to trigger---
                    alarmManager.set(AlarmManager.RTC_WAKEUP, 
                        calendar.getTimeInMillis(), sender);
            		//startActivity(i);
            		
            	} else {
            		Toast mToast;
            		 mToast = Toast.makeText(AddReminder.this, "Please choose the time for future!!",
                             Toast.LENGTH_SHORT);
            		 mToast.show();

            	}
            	
            	msg.setText("");
            	//phno.setText("");
            	tpc.setText("");
            	mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                updateDisplay();
            }
        }); 
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, mHour, mMinute, false);
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);
        }
        return null;
    }
    private Boolean check() {
    	if(mYear < Calendar.YEAR) {return false;}
    	else {
    		if(mMonth < Calendar.MONTH) {return false;}
    		else {
    			if(mDay < Calendar.DAY_OF_MONTH) {return false;}
    			else {
    				if(mHour < Calendar.HOUR_OF_DAY) {return false;}
    				else {
    					if(mMinute < Calendar.MINUTE) {return true;}
    				}
    			}
    		}
    	}
    	return true;
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                break;
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }    

    private void updateDisplay() {
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("-")
                    .append(mDay).append("-")
                    .append(mYear).append(" ")
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)));

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDisplay();
                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    
	protected void onResume()
	{
	   super.onResume();
	   Calendar c = Calendar.getInstance();
	   msg.setText("");
	   //phno.setText("");
	   tpc.setText("");
	   mYear = c.get(Calendar.YEAR);
	   mMonth = c.get(Calendar.MONTH);
	   mDay = c.get(Calendar.DAY_OF_MONTH);
	   mHour = c.get(Calendar.HOUR_OF_DAY);
	   mMinute = c.get(Calendar.MINUTE);
	   updateDisplay();
	}
	public void PopulatePeopleList() {

		mPeopleList.clear();

		Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		while (people.moveToNext()) {
			String contactName = people.getString(people.getColumnIndex(
					ContactsContract.Contacts.DISPLAY_NAME));

			String contactId = people.getString(people.getColumnIndex(
					ContactsContract.Contacts._ID));
			String hasPhone = people.getString(people.getColumnIndex(
					ContactsContract.Contacts.HAS_PHONE_NUMBER));

			if ((Integer.parseInt(hasPhone) > 0)) {

				// You know have the number so now query it like this
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
						null, null);
				while (phones.moveToNext()) {

					//store numbers and display a dialog letting the user select which.
					String phoneNumber = phones.getString(
							phones.getColumnIndex(
							ContactsContract.CommonDataKinds.Phone.NUMBER));

					String numberType = phones.getString(phones.getColumnIndex(
							ContactsContract.CommonDataKinds.Phone.TYPE));

					Map <String, String> NamePhoneType = new HashMap<String, String>();

					NamePhoneType.put("Name", contactName);
					NamePhoneType.put("Phone", phoneNumber);

					if(numberType.equals("0"))
						NamePhoneType.put("Type", "Work");
					else
						if(numberType.equals("1"))
							NamePhoneType.put("Type", "Home");
						else if(numberType.equals("2"))
							NamePhoneType.put("Type",  "Mobile");
						else
							NamePhoneType.put("Type", "Other");

					//Then add this map to the list.
					mPeopleList.add(NamePhoneType);
				}
				phones.close();
			}
		}
		people.close();

		startManagingCursor(people);
	}
}
