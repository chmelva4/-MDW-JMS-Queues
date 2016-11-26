package u5NewTripsProcessor;

import java.io.Serializable;

public class Trip implements Serializable{
	
	private static final long serialVersionUID = 1l;
	
	private int id;
	private String name;
	private  String location;
	public Trip(int id, String name, String location) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "Trip [id=" + id + ", name=" + name + ", location=" + location + "]";
	}
	
}
