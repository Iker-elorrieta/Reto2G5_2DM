package Modelo;

import java.io.Serializable;
import java.util.HashMap;

public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String header;
	private HashMap<String,Object> data;
	
    public Request() {
    }
	
	public Request(String header, HashMap<String, Object> data) {
		super();
		this.header = header;
		this.data = data;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	
	 public Object get(String key) {
	        return data != null ? data.get(key) : null;
	    }
	
	
}
