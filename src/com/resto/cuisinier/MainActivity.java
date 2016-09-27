package com.resto.cuisinier;



import java.util.ArrayList;



import Model.Order;
import Model.Table;
import Parser.OrderParser;
import Parser.TablesParser;
import android.support.v7.app.ActionBarActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ActionBarActivity {

	TablesParser tableParser;
	OrderParser orderParser;
	public ArrayList<Table> tableArray;
	public ArrayList<Order> orderArray;
	ListView orderList,orderDelevList;
	public static String url="";
	public static int clic =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(MainActivity.this); 
		orderList  = (ListView) findViewById(R.id.orderlist);
		orderDelevList  = (ListView) findViewById(R.id.orderdelevlist);
		String token = prefs.getString("token","");
		getActionBar().setIcon(R.drawable.text);
		getActionBar().setDisplayShowTitleEnabled(false);
	    final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "","Attendez SVP..." , true);
		dialog.show();
	    Handler hand = new Handler()
	    {
	    	public void handleMessage(Message msg)
	    	{
	    	dialog.dismiss();
			tableArray = tableParser.tables;
			Log.i("table", ""+tableArray.size());
	
			orderList.setAdapter(new CustomListAdapter(MainActivity.this,tableArray));
			
	    	}
	    };
	    
	    tableParser = new TablesParser("table",hand);
	    Log.i("tokentable", token);
	    tableParser.fetchJSON(token);
	    orderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(clic==0){
					clic=1;
					Log.i("listview", ""+arg2);
					MainActivity.url = "order";
					OrderFragment orderFragment = new OrderFragment(tableArray.get(arg2).getTable_id());
					
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					
					fragmentTransaction.replace(R.id.framecontent, orderFragment);
					fragmentTransaction.commit();
					
				}
				
				
				
			}
		});
	    
	    final ProgressDialog dia = ProgressDialog.show(MainActivity.this, "","Attendez SVP..." , true);
		dia.show();
	    Handler handler = new Handler()
	    {
	    	public void handleMessage(Message msg)
	    	{
	    	dia.dismiss();
			orderArray = orderParser.orders;
			for(int i=0;i<orderArray.size();i++)
			Log.i("orders", ""+orderArray.get(i).getId());
			orderDelevList.setAdapter(new CustomOrderListAdapter(MainActivity.this,orderArray));
			
	    	}
	    };
	    
	    orderParser = new OrderParser("order",handler);
	    Log.i("tokentable", token);
	    orderParser.fetchJSON(token);
	    orderDelevList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(clic==0){
					clic=1;
					Log.i("listview", ""+orderArray.get(arg2).getId());
					MainActivity.url="orderline";
					OrderFragment orderFragment = new OrderFragment(orderArray.get(arg2).getId());
					
					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					
					fragmentTransaction.replace(R.id.framecontent, orderFragment);
					fragmentTransaction.commit();
					
				}
				
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
