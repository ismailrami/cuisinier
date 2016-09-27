package com.resto.cuisinier;

import java.util.ArrayList;

import Model.Table;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter{
	
	ArrayList<Table> listTables;
	LayoutInflater layoutInflater;
	
	public CustomListAdapter(Context context, ArrayList<Table> tables) {
		this.listTables=tables;
		layoutInflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		return listTables.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listTables.get(arg0);
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
			arg1 = layoutInflater.inflate(R.layout.order_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) arg1.findViewById(R.id.tableName);
			
			arg1.setTag(viewHolder);
			
		}
		else{
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.nameView.setText(listTables.get(arg0).getName());
		
		
		return arg1;
	}

}

