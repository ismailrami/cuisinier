package com.resto.cuisinier;

import java.util.ArrayList;

import Model.Order;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomOrderListAdapter extends BaseAdapter{
	
	ArrayList<Order> listOrdes;
	LayoutInflater layoutInflater;
	
	public CustomOrderListAdapter(Context context, ArrayList<Order> orders) {
		this.listOrdes=orders;
		layoutInflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		return listOrdes.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listOrdes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	static class ViewHolder{
		TextView nameView;
	
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(arg1 == null){
			arg1 = layoutInflater.inflate(R.layout.order_list_delev, null);
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) arg1.findViewById(R.id.orderId);
			
			arg1.setTag(viewHolder);
			
		}
		else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.nameView.setText(""+listOrdes.get(arg0).getId());
		
		
		return arg1;
	}

}

