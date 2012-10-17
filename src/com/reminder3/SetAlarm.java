package com.reminder3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;


public class SetAlarm extends BroadcastReceiver {

    private String toPic;
    private String phoneNo;
    private String msgToSend;
    private SmsManager sms;
    /*private void aLarmSetup(int Year,int Month,int Day,int Hour,int Minute,String topic,
    		String phoneno,
    		String msgtosend){
    	mYear=Year;
    	mMonth=Month;
    	mDay=Day;
    	mHour=Hour;
    	mMinute=Minute;
    	toPic=topic;
    	phoneNo=phoneno;
    	msgToSend=msgtosend;    	
    }*/
    
    public void onReceive(Context context, Intent intent) {
    	try {
    		Bundle b = intent.getExtras();
    		toPic=b.getString("topic");
    		phoneNo=b.getString("num");
    		msgToSend=b.getString("msg");
    		Toast.makeText(context, toPic+" Activated!!\n"+msgToSend+" sent on "+phoneNo, Toast.LENGTH_SHORT).show();
    		if(!phoneNo.equalsIgnoreCase("")) {
    			sms = SmsManager.getDefault();
            	sms.sendTextMessage(phoneNo, null, msgToSend, null, null);
    		}
    	}catch (Exception e) {
    		
    	}
    }
/*    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        aLarmSetup(b.getInt("yr"),b.getInt("mnth"),b.getInt("day"),b.getInt("hr"),
        		b.getInt("mint"),
        		b.getString("topic"),
        		b.getString("phn"),b.getString("msg"));
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
        Intent i=new Intent(this,AlarmAction.class);
        i.putExtra("num", phoneNo);
        i.putExtra("msg", msgToSend);
        PendingIntent displayIntent = PendingIntent.getActivity(
            getBaseContext(), 0, 
            i, 0);   
        //---sets the alarm to trigger---
        alarmManager.set(AlarmManager.RTC_WAKEUP, 
            calendar.getTimeInMillis(), displayIntent);

    }*/

}
