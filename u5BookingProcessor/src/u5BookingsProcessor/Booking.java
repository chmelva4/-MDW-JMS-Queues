package u5BookingsProcessor;

import java.io.Serializable;
import java.util.Date;

public class Booking implements Serializable{
	
	private static final long serialVersionUID=1l;
	
	private Date date;
	private String name;
	private int capacity;
	private int id; 
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Booking(Date date, String name, int capacity, int id) {
		super();
		this.date = date;
		this.name = name;
		this.capacity = capacity;
		this.id = id;
	}
	@Override
	public String toString() {
		return "Booking [date=" + date + ", name=" + name + ", capacity=" + capacity + ", id=" + id + "]";
	}
	
}
