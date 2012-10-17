/**
 * 
 */
package com.reminder3;

//import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.PopupWindow;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//mport android.widget.ListView;
//import android.widget.TextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemClickListener;
/**
 * @author variable
 *
 */
public class Remindmain extends ListActivity {
	private DbAdapter ctdb;
	private Cursor cursor ;//= ctdb.fetchAllReminderData();
	public static final String KEY_ROWID = "_id";
	public static final String KEY_YEAR = "mYear";
	public static final String KEY_MONTH = "mMonth";
	public static final String KEY_DATE = "mDay";
	public static final String KEY_HOUR = "mHour";
	public static final String KEY_MIN = "mMinute";
	public static final String KEY_TOPIC = "topic";
	public static final String KEY_PHNO = "phnNo";
	public static final String KEY_MSG = "msgTosend";
	private Button dltSelctd;
	private Button dltAll;
	private CheckBox chk;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ctdb=new DbAdapter(this);
        ctdb.open();
        dltSelctd = (Button) findViewById(R.id.dtl);
        dltAll = (Button) findViewById(R.id.dtlall);
        
        populate();
        
        dltSelctd.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	Toast.makeText(Remindmain.this,"Click on any item to delete it!!",
                        Toast.LENGTH_LONG).show();
            }
        });
        dltAll.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	ctdb.deleteAllReminderData();
            	populate();
            }
        });
        
        //(ctdb.fetchAllReminderData()).close();
        //if(Reminders.length==0) {
        //	Reminders = new String[]{"No Reminders"};
        //}
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, Reminders));

    }
	static final String[] months = new String[]{
		"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"
	};
	public void populate() {
		cursor=ctdb.fetchAllReminderData();
        startManagingCursor(cursor);
        String[] columns = new String[] {KEY_TOPIC,KEY_DATE,KEY_MONTH,KEY_YEAR,KEY_HOUR,KEY_MIN};
        int[] to = new int[] {R.id.Topic,R.id.Date,R.id.Month,R.id.Year,R.id.Timeh,R.id.Timem};
        SimpleCursorAdapter madpt = new SimpleCursorAdapter(this,R.layout.list_item,cursor,columns,to);

        this.setListAdapter(madpt);
		/////////////////////////////////////////
		/*cursor=ctdb.fetchAllReminderData();
		startManagingCursor(cursor);
		Reminders = new String[]{};
		String time,date,topic;
		time="";
		date="";
		topic="";
		ArrayList<String> list = new ArrayList<String>();
		 if (cursor.getCount()>0) {
	            do {
	            	//Integer rr=cursor.getInt(cursor.getColumnIndex(KEY_HOUR));
	                //time.concat("H:");
	                //time.concat((Integer.toString(cursor.getInt(cursor.getColumnIndex(KEY_MIN)))).concat("M"));
	                //date = ((Integer.toString(cursor.getInt(cursor.getColumnIndex(KEY_DATE)))).concat("-"));
	                //date.concat((months[cursor.getInt(cursor.getColumnIndex(KEY_MONTH))]).concat("-"));
	                //date.concat(Integer.toString(cursor.getInt(cursor.getColumnIndex(KEY_YEAR))));
	                //topic=cursor.getString(cursor.getColumnIndex(KEY_TOPIC));
	                String temp=topic.concat("at ".concat(time.concat(" on ".concat(date))));
	                list.add(temp);
	            	list.add("it works");
	            } while (cursor.moveToNext());
		 }
		 Reminders = (String[]) list.toArray(new String[0]);*/
	}
	protected void onResume()
	{
	   super.onResume();
	   populate();
	}
	public void onListItemClick(ListView l, View v, int position, long id) {

	    super.onListItemClick(l, v, position,  id);

	    //Difference between this:
	    //Cursor c = (Cursor)l.getItemAtPosition(position);
	    //and this??
	    //Cursor c = (Cursor)l.getAdapter().getItem(position);
	    final Cursor c =((SimpleCursorAdapter)l.getAdapter()).getCursor();
	    startManagingCursor(c);
	    c.moveToPosition(position);
	    
	    String phn= c.getString(7);
	    String msg= c.getString(8);
	    Toast.makeText(this,phn+"\n"+msg,
                Toast.LENGTH_LONG).show();
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Do you want to delete clicked item?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   ctdb.deleteReminderData(c.getInt(0));
	            	   populate();
	            	   dialog.cancel();
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	               }
	           });
	    AlertDialog alert = builder.create();
	    alert.show();
	}

	
}


