package com.resto.cuisinier;

import java.util.ArrayList;



import Model.OrderLineModel;
import Model.Orderline;
import Parser.OrderLineParser;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class OrderFragment extends Fragment{
	int tableId;
	public static int index;
	OrderLineParser orderLineParser;
	CustomorderLineAdapter adapter;
	ArrayList<Orderline> orderlines;
	ExpandableListView orderLineListView;
	ArrayList<OrderLineModel> ordersModel = new ArrayList<OrderLineModel>();
	public OrderFragment(int id) {
		this.tableId = id;
		Log.i("yable", ""+tableId);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 
		View rootView = inflater.inflate(R.layout.order_fragment, container, false);
		orderLineListView = (ExpandableListView) rootView.findViewById(R.id.orderline);
		final ProgressDialog dialog = ProgressDialog.show(getActivity(), "","Attendez SVP..." , true);
		dialog.show();
		final ArrayList<Object> list = new ArrayList<Object>();
	    Handler hand = new Handler()
	    {
	    	public void handleMessage(Message msg)
	    	{	
		    	dialog.dismiss();
		    	MainActivity.clic=0;
		    	ArrayList<String> strings = new ArrayList<String>();
		    	orderlines= orderLineParser.orders;
		    	int b = 0;
		    	for(int i=0;i<orderlines.size();i++){
		    		int k=0;
		    		 ArrayList<Orderline> array = new ArrayList<Orderline>();
		    		 if(orderlines.get(i).getProductId()!=0)
		    		 {
		    			 array.add(orderlines.get(i));
		    			orderlines.get(i).setProductId(0);
		    		 
		    		for(int j = 0;j<orderlines.size();j++){
			    			if(orderlines.get(j).getProductId()!=0){
			    				if(orderlines.get(j).getProductName().equals(array.get(k).getProductName())){
			    					array.add(orderlines.get(j));
			    					orderlines.get(j).setProductId(0);
			    				}
			    			}
			    		
		    		}
		    		list.add(array);
		    		for (int l = 0; l < array.size(); l++) {
		    			 Log.i("orderline",""+array.get(l).getProductName());

					}
		    		OrderLineModel  o = new OrderLineModel();
		    		o.setOrders(array);
		    		o.setName(array.get(0).getProductName());
		    		ordersModel.add(o);
		    	
		    		}
		    	}
		    	Log.i("name", ""+strings.size());
		    	
		    	adapter = new CustomorderLineAdapter(getActivity(), ordersModel,tableId);
				orderLineListView.setAdapter(adapter);
				int count = adapter.getGroupCount();
				for (int position = 1; position <= count; position++)
				{
				    orderLineListView.expandGroup(position - 1);
				}
	    	}
	    };
	    orderLineParser = new OrderLineParser(tableId, hand);
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(getActivity()); 
		String token = prefs.getString("token","");
		orderLineParser.fetchJSON(token);

		return rootView;
	}
}
