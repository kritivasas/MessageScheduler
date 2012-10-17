package com.reminder3;

import android.app.TabActivity;
import android.os.Bundle;
import android.content.res.Resources;
import android.widget.TabHost;
import android.content.Intent;

public class ReminderTabbed extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources(); 
		TabHost tabHost = getTabHost(); 
		TabHost.TabSpec spec;  
		Intent intent;  

	    
	    intent = new Intent().setClass(this, Remindmain.class);
	
	    spec = tabHost.newTabSpec("main").setIndicator("",
	                      res.getDrawable(R.drawable.ic_remindmain))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	
	    intent = new Intent().setClass(this, AddReminder.class);
	    spec = tabHost.newTabSpec("addRemind").setIndicator("",
	                      res.getDrawable(R.drawable.ic_remindadd))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    tabHost.setCurrentTab(0);
	}
}
