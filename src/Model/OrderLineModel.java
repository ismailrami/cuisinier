package Model;

import java.util.ArrayList;

public class OrderLineModel {
	
	ArrayList<Orderline> orders;
	String name;
	
	public ArrayList<Orderline> getOrders() {
		return orders;
	}
	public void setOrders(ArrayList<Orderline> orders) {
		this.orders = orders;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
