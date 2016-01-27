package org.seven.data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

import org.seven.utils.GlobalParam;

public class BillInfo implements Serializable ,Comparator<BillInfo>,Comparable<BillInfo> {

	private String billno;
	
	public static final String BILLNO = "BILLNO";
	
	private String s_store;
	
	public static final String S_STORE = "S_STORE";
	
	private String s_date;
	
	public static final String S_DATE = "S_DATE";
	
	private String packagecnt;
	
	public static final String PACKAGECNT = "PACKAGECNT";
	
	private String o_num;
	
	public static final String O_NUM = "O_NUM";
	
	private String a_num;
	
	public static final String A_NUM = "A_NUM";
	
	private String s_num;
	
	public static final String S_NUM = "S_NUM";
	
	private String skucount;
	
	public static final String SKUCOUNT = "SKUCOUNT";
	
	private String remark;
	
	public static final String REMARK = "REMARK";
	
	private String satisfied;
	
	public static final String SATISFIED = "SATISFIED";
	
	public String getSatisfied() {
		return satisfied;
	}


	public void setSatisfied(String satisfied) {
		this.satisfied = satisfied;
	}


	public String getBillno() {
		return billno;
	}


	public void setBillno(String billno) {
		this.billno = billno;
	}


	public String getS_store() {
		return s_store;
	}


	public void setS_store(String s_store) {
		this.s_store = s_store;
	}


	public String getS_date() {
		return s_date;
	}


	public void setS_date(String s_date) {
		this.s_date = s_date;
	}


	public String getPackagecnt() {
		return packagecnt;
	}


	public void setPackagecnt(String packagecnt) {
		this.packagecnt = packagecnt;
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


	public String getSkucount() {
		return skucount;
	}


	public void setSkucount(String skucount) {
		this.skucount = skucount;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}

	private HashMap<String, Object> map = new HashMap<String, Object>();
	
	public BillInfo(String billno, String s_store, String s_date, String packagecnt, String o_num, String a_num, String s_num, String skucount, String remark){
		this.billno = billno;
		this.s_store = s_store;
		this.s_date = s_date;
		this.packagecnt = packagecnt;
		this.o_num = o_num;
		this.a_num = a_num;
		this.s_num = s_num;
		this.skucount = skucount;
		this.remark = remark;
		this.satisfied = Math.round(Float.parseFloat(s_num) / Float.parseFloat(o_num) * 10000)/100.0 + "%";
		map.put(BILLNO, this.billno);
		map.put(S_STORE, this.s_store);
		map.put(S_DATE, this.s_date);
		map.put(PACKAGECNT, this.packagecnt);
		map.put(O_NUM, this.o_num);
		map.put(A_NUM, this.a_num);
		map.put(S_NUM, this.s_num);
		map.put(SKUCOUNT, this.skucount);
		map.put(REMARK, this.remark);
		map.put(SATISFIED, satisfied);
	}
	
	
	public HashMap<String, Object> getMap() {
		return map;
	}


	@Override
	public int compare(BillInfo src, BillInfo dst) {
		int result = 0;
		switch(GlobalParam.getInstance().billSortType){
		case PACKAGECNT_SORT_UP:
			return Double.valueOf(Double.parseDouble(src.getPackagecnt()) - Double.parseDouble(dst.getPackagecnt())).intValue();
		case PACKAGECNT_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(dst.getPackagecnt()) - Double.parseDouble(src.getPackagecnt())).intValue();
		case S_NUM_SORT_UP:
			return Double.valueOf(Double.parseDouble(src.getS_num()) - Double.parseDouble(dst.getS_num())).intValue();
		case S_NUM_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(dst.getS_num()) - Double.parseDouble(src.getS_num())).intValue();
		case SATISFIED_SORT_UP:
		{
			String srcValue = src.getSatisfied().replace("%", "");
			String dstValue = dst.getSatisfied().replace("%", "");
			return Double.valueOf(Double.parseDouble(srcValue) - Double.parseDouble(dstValue)).intValue();
		}
		case SATISFIED_SORT_DOWN:
		{
			String srcValue = src.getSatisfied().replace("%", "");
			String dstValue = dst.getSatisfied().replace("%", "");
			return Double.valueOf(Double.parseDouble(dstValue) - Double.parseDouble(srcValue)).intValue();
		}
		case O_NUM_SORT_UP:
			return Double.valueOf(Double.parseDouble(src.getO_num()) - Double.parseDouble(dst.getO_num())).intValue();
		case O_NUM_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(dst.getO_num()) - Double.parseDouble(src.getO_num())).intValue();
		case S_STORE_SORT_UP:
			return src.getS_store().compareTo(dst.getS_store());
		case S_STORE_SORT_DOWN:
			return dst.getS_store().compareTo(src.getS_store());
		case S_DATE_SORT_UP:
			return src.getS_date().compareTo(dst.getS_date());
		case S_DATE_SORT_DOWN:
			return dst.getS_date().compareTo(src.getS_date());
		case BILLNO_SORT_UP:
			return src.getBillno().compareTo(dst.getBillno());
		case BILLNO_SORT_DOWN:
			return dst.getBillno().compareTo(src.getBillno());
		case A_NUM_SORT_UP:
			return Double.valueOf(Double.parseDouble(src.getA_num()) - Double.parseDouble(dst.getA_num())).intValue();
		case A_NUM_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(dst.getA_num()) - Double.parseDouble(src.getA_num())).intValue();
		case SKUCOUNT_SORT_UP:
			return Double.valueOf(Double.parseDouble(src.getSkucount()) - Double.parseDouble(dst.getSkucount())).intValue();
		case SKUCOUNT_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(dst.getSkucount()) - Double.parseDouble(src.getSkucount())).intValue();
		case REMARK_SORT_UP:
			return src.getRemark().compareTo(dst.getRemark());
		case REMARK_SORT_DOWN:
			return dst.getRemark().compareTo(src.getRemark());
		default:
			return result;
		}
	}
	
	@Override
	public int compareTo(BillInfo another) {
		int result = 0;
		switch(GlobalParam.getInstance().billSortType){
		case PACKAGECNT_SORT_UP:
			return Double.valueOf(Double.parseDouble(this.getPackagecnt()) - Double.parseDouble(another.getPackagecnt())).intValue();
		case PACKAGECNT_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(another.getPackagecnt()) - Double.parseDouble(this.getPackagecnt())).intValue();
		case S_NUM_SORT_UP:
			return Double.valueOf(Double.parseDouble(this.getS_num()) - Double.parseDouble(another.getS_num())).intValue();
		case S_NUM_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(another.getS_num()) - Double.parseDouble(this.getS_num())).intValue();
		case SATISFIED_SORT_UP:
		{
			String thisValue = this.getSatisfied().replace("%", "");
			String anotherValue = another.getSatisfied().replace("%", "");
			return Double.valueOf(Double.parseDouble(thisValue) - Double.parseDouble(anotherValue)).intValue();
		}	
		case SATISFIED_SORT_DOWN:
		{
			String thisValue = this.getSatisfied().replace("%", "");
			String anotherValue = another.getSatisfied().replace("%", "");
			return Double.valueOf(Double.parseDouble(anotherValue) - Double.parseDouble(thisValue)).intValue();
		}
		case O_NUM_SORT_UP:
			return Double.valueOf(Double.parseDouble(this.getO_num()) - Double.parseDouble(another.getO_num())).intValue();
		case O_NUM_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(another.getO_num()) - Double.parseDouble(this.getO_num())).intValue();
		case S_STORE_SORT_UP:
			return this.getS_store().compareTo(another.getS_store());
		case S_STORE_SORT_DOWN:
			return another.getS_store().compareTo(this.getS_store());
		case S_DATE_SORT_UP:
			return this.getS_date().compareTo(another.getS_date());
		case S_DATE_SORT_DOWN:
			return another.getS_date().compareTo(this.getS_date());
		case BILLNO_SORT_UP:
			return this.getBillno().compareTo(another.getBillno());
		case BILLNO_SORT_DOWN:
			return another.getBillno().compareTo(this.getBillno());
		case A_NUM_SORT_UP:
			return Double.valueOf(Double.parseDouble(this.getA_num()) - Double.parseDouble(another.getA_num())).intValue();
		case A_NUM_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(another.getA_num()) - Double.parseDouble(this.getA_num())).intValue();
		case SKUCOUNT_SORT_UP:
			return Double.valueOf(Double.parseDouble(this.getSkucount()) - Double.parseDouble(another.getSkucount())).intValue();
		case SKUCOUNT_SORT_DOWN:
			return Double.valueOf(Double.parseDouble(another.getSkucount()) - Double.parseDouble(this.getSkucount())).intValue();
		case REMARK_SORT_UP:
			return this.getRemark().compareTo(another.getRemark());
		case REMARK_SORT_DOWN:
			return another.getRemark().compareTo(this.getRemark());
		default:
			return result;
		}
	}
}