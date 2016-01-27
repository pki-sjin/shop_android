package org.seven.data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

public class BillDetailInfo implements Serializable ,Comparator<BillDetailInfo>,Comparable<BillDetailInfo> {

	private String plu_no;
	
	public static final String PLU_NO = "PLU_NO";
	
	private String plu_name;
	
	public static final String PLU_NAME = "PLU_NAME";
	
	private String Mainbarcode;
	
	public static final String MAINBARCODE = "MAINBARCODE";
	
	private String plu_style;
	
	public static final String PLU_STYLE = "PLU_STYLE";
	
	private String unit;
	
	public static final String UNIT = "UNIT";
	
	private String stan_pack;
	
	public static final String STAN_PACK = "STAN_PACK";
	
	private String o_num;
	
	public static final String O_NUM = "O_NUM";
	
	private String a_num;
	
	public static final String A_NUM = "A_NUM";
	
	private String s_num;
	
	public static final String S_NUM = "S_NUM";
	
	private String wh_num;
	
	public static final String WH_NUM = "WH_NUM";
	
	public String getWh_num() {
		return wh_num;
	}

	public void setWh_num(String wh_num) {
		this.wh_num = wh_num;
	}

	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}

	public String getPlu_no() {
		return plu_no;
	}


	public void setPlu_no(String plu_no) {
		this.plu_no = plu_no;
	}


	public String getPlu_name() {
		return plu_name;
	}


	public void setPlu_name(String plu_name) {
		this.plu_name = plu_name;
	}


	public String getMainbarcode() {
		return Mainbarcode;
	}


	public void setMainbarcode(String mainbarcode) {
		Mainbarcode = mainbarcode;
	}


	public String getPlu_style() {
		return plu_style;
	}


	public void setPlu_style(String plu_style) {
		this.plu_style = plu_style;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getStan_pack() {
		return stan_pack;
	}


	public void setStan_pack(String stan_pack) {
		this.stan_pack = stan_pack;
	}


	public String getO_num() {
		return o_num;
	}


	public void setO_num(String o_num) {
		this.o_num = o_num;
	}


	public String getA_num() {
		return a_num;
	}


	public void setA_num(String a_num) {
		this.a_num = a_num;
	}


	public String getS_num() {
		return s_num;
	}


	public void setS_num(String s_num) {
		this.s_num = s_num;
	}



	private HashMap<String, Object> map = new HashMap<String, Object>();
	
	public BillDetailInfo(String plu_no, String plu_name, String Mainbarcode, String plu_style, String unit, String stan_pack, String o_num, String a_num, String s_num, String wh_num){
		this.plu_no = plu_no;
		this.plu_name = plu_name;
		this.Mainbarcode = Mainbarcode;
		this.plu_style = plu_style;
		this.unit = unit;
		this.stan_pack = stan_pack;
		this.o_num = o_num;
		this.a_num = a_num;
		this.s_num = s_num;
		this.wh_num = wh_num;
		map.put(PLU_NO, this.plu_no);
		map.put(PLU_NAME, this.plu_name);
		map.put(MAINBARCODE, this.Mainbarcode);
		map.put(PLU_STYLE, this.plu_style);
		map.put(UNIT, this.unit);
		map.put(STAN_PACK, this.stan_pack);
		map.put(O_NUM, this.o_num);
		map.put(A_NUM, this.a_num);
		map.put(S_NUM, this.s_num);
		map.put(WH_NUM, this.wh_num);
	}
	
	
	public HashMap<String, Object> getMap() {
		return map;
	}


	@Override
	public int compare(BillDetailInfo src, BillDetailInfo dst) {
		return 0;
	}
	
	@Override
	public int compareTo(BillDetailInfo another) {
		return 0;
	}
}
