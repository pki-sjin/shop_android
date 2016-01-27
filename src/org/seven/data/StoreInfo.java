package org.seven.data;

import java.io.Serializable;
import java.util.HashMap;

public class StoreInfo implements Serializable{
	
	private String billcount;

	private String straddress;

	private String strname;

	private String strno;
	
	private String lat;
	
	private String lng;
	
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	private HashMap<String, Object> map = new HashMap<String, Object>();
	
	public HashMap<String, Object> getMap() {
		return map;
	}

	public String getBillcount() {
		return billcount;
	}

	public void setBillcount(String billcount) {
		this.billcount = billcount;
		map.put("billcount", this.billcount);
	}

	public String getStraddress() {
		return straddress;
	}

	public void setStraddress(String straddress) {
		this.straddress = straddress;
		map.put("straddress", this.straddress);
	}

	public String getStrname() {
		return strname;
	}

	public void setStrname(String strname) {
		this.strname = strname;
		map.put("strname", this.strname);
	}

	public String getStrno() {
		return strno;
	}

	public void setStrno(String strno) {
		this.strno = strno;
		map.put("strno", this.strno);
	}
}
