package com.example.spin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;

public class DeleteSets extends ListActivity {
	
	/**
	 * displays all downloaded tables
	 * deletes selected tables
	 * and returns to main activity
	 * */

	ArrayList<HashMap<String, String>> contactList;
	ArrayList<String> tables = new ArrayList<String>();
	HashMap<String, String> list = new HashMap<String, String>();
	private Database myDatabase;
	private Button mDelete;
	
	private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_QUESTIONS = "mobile";
	private static final int RESULT_CLOSE_ALL = 0;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cram);
		
		String str_cram= "Delete sets from database";
		((TextView)findViewById(R.id.mainCram)).setText(str_cram);
		
		str_cram="Delete";
		((Button)findViewById(R.id.buttonCram)).setText(str_cram);
		
		contactList = new ArrayList<HashMap<String, String>>();
		
		myDatabase = new Database(DeleteSets.this);
		Cursor c = myDatabase.showAllTables();
		HashMap<String, String> contact = new HashMap<String, String>();
		int br=0;
		if (c.moveToFirst()){
			c.moveToNext();
		
			while(!c.isAfterLast()) {
				String id= String.valueOf(br);
				contact.put(TAG_ID, id);
				contact.put(TAG_NAME, c.getString(0));
				myDatabase.defineTable(c.getString(0));
				List<SQLitem> query = myDatabase.getAllItems();
				String number = String.valueOf(query.size());
				String questions= "Number of questions in set: "+number; 
				contact.put(TAG_QUESTIONS, questions);
				contactList.add(contact);
				br++;
				c.moveToNext();
			}
		} 
		
		if (br == 0) {
    	   Toast.makeText(DeleteSets.this, "You need to download sets!",
	          	     Toast.LENGTH_LONG).show();
			DeleteSets.this.finish();
		}
		
		ListAdapter adapter = new SimpleAdapter(
               DeleteSets.this, contactList,
               R.layout.list_item, new String[] { TAG_NAME, TAG_QUESTIONS
                       }, new int[] { R.id.name , R.id.questions});

       setListAdapter(adapter);
       
       mDelete = (Button)findViewById(R.id.buttonCram); //setting reference for the "START" button
       mDelete.setOnClickListener(new View.OnClickListener(){ //creating a listener object
			
			@Override
			public void onClick(View v){
				
				DeleteSets.this.finish();
			}
		});
       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_sets, menu);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		list = (HashMap<String, String>) l.getItemAtPosition(position);
		String output=list.get("name");
		CheckBox check = (CheckBox) v.findViewById(R.id.checkbox);
		if(tables.contains(output)){
			tables.remove(output);
			check.setChecked(false);
		}
		else{
			tables.add(output);
			check.setChecked(true);
		}
		super.onListItemClick(l, v, position, id);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    
		switch(resultCode)
	    {
	    case RESULT_CLOSE_ALL:
	        setResult(RESULT_CLOSE_ALL);
	        finish();
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
}
