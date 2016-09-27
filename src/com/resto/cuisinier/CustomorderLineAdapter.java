package com.resto.cuisinier;

import java.util.ArrayList;

import Model.OrderLineModel;
import Model.Orderline;
import Parser.OrderLineParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class CustomorderLineAdapter extends BaseExpandableListAdapter{
	Activity context;
	LayoutInflater inflater;
	int table;
	ArrayList<Object> list,statesArray;
	ArrayList<String> strings;
	ArrayList<Short> states = new ArrayList<Short>();
	static ArrayList<OrderLineModel> orders;
	
	public CustomorderLineAdapter(Activity context, ArrayList<OrderLineModel> orders, int id) {
		this.context = context;
		this.table = id;
		this.orders = orders;
		strings = new ArrayList<String>();
		for (int i = 0; i < this.orders.size(); i++) {
			for (int j = 0; j < orders.get(i).getOrders().size(); j++) {
				orders.get(i).getOrders().get(j).setState(0);
			}
		strings.add(orders.get(i).getName());	
		}
		
		inflater = LayoutInflater.from(context);
		
	}

	public class ViewHolder
	{
		int state;
		TextView orderName,optionValue;
		RadioButton state1,state2,state3;
		RadioGroup rg;
		
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return orders.get(groupPosition).getOrders().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return orders.get(groupPosition).getOrders().get(childPosition).getState();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		final ProgressDialog dialog;
		final Orderline orderline = (Orderline) getChild(groupPosition, childPosition);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.order_line_item, null);
			viewHolder = new ViewHolder();
			viewHolder.orderName = (TextView) convertView.findViewById(R.id.name);
			viewHolder.optionValue = (TextView) convertView.findViewById(R.id.value);
			viewHolder.rg = (RadioGroup) convertView.findViewById(R.id.radiogroup);
			viewHolder.state1 = (RadioButton) convertView.findViewById(R.id.cbox_get_it);
			viewHolder.state2 = (RadioButton) convertView.findViewById(R.id.cbox_ready);
			viewHolder.state3 = (RadioButton) convertView.findViewById(R.id.cbox_served);
			viewHolder.state1.setChecked(false);
			viewHolder.state2.setChecked(false);
			viewHolder.state1.setChecked(false);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
			
		}
		viewHolder.orderName.setText(orderline.getProductName());
		viewHolder.optionValue.setText("valeur");
		
		
		viewHolder.rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(context); 
				String token = prefs.getString("token","");
				OrderLineParser orderLineParser;
				switch (checkedId) {
				case R.id.cbox_get_it:
					viewHolder.state = 1;
					final ProgressDialog dialog = ProgressDialog.show(context, "","Attendez SVP..." , true);
					dialog.show();
					Log.i("sow", "show");
					 Handler hand = new Handler()
					    {
					    	public void handleMessage(Message msg)
					    	{
					    		dialog.dismiss();
					    		Log.i("dismiss", "dismis");
					    		orders.get(groupPosition).getOrders().get(childPosition).setState(1);
								orders.get(groupPosition).getOrders().get(childPosition).getState();
					    	}
					    };
					orderLineParser = new OrderLineParser(orders.get(groupPosition).getOrders().get(childPosition).getOrderLine(),hand);
					
					orderLineParser.setServed(token, 1);
					break;
				case R.id.cbox_ready:
					viewHolder.state = 2;
					final ProgressDialog dia = ProgressDialog.show(context, "","Attendez SVP..." , true);
					dia.show();
					  hand = new Handler()
					    {
					    	public void handleMessage(Message msg)
					    	{
					    		dia.dismiss();
					    	}
					    };
					orderLineParser = new OrderLineParser(orders.get(groupPosition).getOrders().get(childPosition).getOrderLine(),hand);
					orders.get(groupPosition).getOrders().get(childPosition).setState(2);
					orderLineParser.setServed(token, 2);
					break;
				case R.id.cbox_served:
					viewHolder.state = 3;
					final ProgressDialog dial = ProgressDialog.show(context, "","Attendez SVP..." , true);
					dial.show();
					  hand = new Handler()
					    {
					    	public void handleMessage(Message msg)
					    	{
					    		Log.i("gghgg", "kkkkkk");
					    		dial.dismiss();
					    	}
					    };
					orderLineParser = new OrderLineParser(orders.get(groupPosition).getOrders().get(childPosition).getOrderLine(),hand);
					orders.get(groupPosition).getOrders().get(childPosition).setState(3);
					orderLineParser.setServed(token, 3);
					orders.get(groupPosition).getOrders().remove(childPosition);
					if (orders.get(groupPosition).getOrders().size()==0){
						orders.remove(groupPosition);
						strings.remove(groupPosition);
					}
					CustomorderLineAdapter.this.notifyDataSetChanged();
					break;

				default:
					break;
				}
				
				
			}
		});
		
		
			
			
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		
		return orders.get(groupPosition).getOrders().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return orders.get(groupPosition).getName();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.orders.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public class ItemViewHolder
	{
		TextView title;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		ItemViewHolder itemViewHolder;
		String name = (String) getGroup(groupPosition);
		if (convertView == null) 
    	{
            convertView = inflater.inflate(R.layout.orders_line_title, null);
            itemViewHolder = new ItemViewHolder();
            itemViewHolder.title =(TextView) convertView.findViewById(R.id.Names);
            
            convertView.setTag(itemViewHolder);
        }
    	else
		{
    		itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		itemViewHolder.title.setText(orders.get(groupPosition).getOrders().size()+name);
		//ArrayList<Orderline> a = (ArrayList<Orderline>) this.list.get(groupPosition);
		
      
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
