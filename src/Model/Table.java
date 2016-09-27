package Model;

public class Table {

	private int table_id;
	private String name;
	private int isOpen;
	private int area_id;
	
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	public int getTable_id()
	{
		return table_id;
	}
	public void setTable_id(int table_id)
	{
		this.table_id = table_id;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public int getIsOpen() 
	{
		return isOpen;
	}
	public void setIsOpen(int isOpen)
	{
		this.isOpen = isOpen;
	}
	
	
}
