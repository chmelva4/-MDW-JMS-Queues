package u5OrderClient;

import java.io.Serializable;

public class Order implements Serializable {
	
	private static final long serialVersionUID=1L;
	
	private String type;
	private String data;
	
	public Order(String type, String data) {
		super();
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
