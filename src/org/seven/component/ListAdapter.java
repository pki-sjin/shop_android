package org.seven.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seven.activity.R;
import org.seven.data.BillDetailInfo;
import org.seven.data.BillInfo;
import org.seven.data.StoreInfo;

import android.content.Context;
import android.widget.SimpleAdapter;

public class ListAdapter extends SimpleAdapter implements Serializable {

	private static final long serialVersionUID = 5417130025663063540L;

	ArrayList<StoreInfo> storeList;

	ArrayList<BillInfo> billList;
	
	ArrayList<BillDetailInfo> billDetailList;

	public ListAdapter(Context context, List<? extends Map<String, ?>> list,
			ArrayList<StoreInfo> storeList) {
		super(context, list,// 数据源
				R.layout.liststore,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "strname", "billcount" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.strname, R.id.billcount });
		this.storeList = storeList;
	}
	
	public ListAdapter(Context context, List<? extends Map<String, ?>> list,
			ArrayList<StoreInfo> storeList, boolean containsGeo) {
		super(context, list,// 数据源
				R.layout.liststoregeo,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "strname"},
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.strname });
		this.storeList = storeList;
	}

	public ListAdapter(Context context, List<? extends Map<String, ?>> list,
			ArrayList<BillInfo> billList, int isBill) {
		super(context, list,// 数据源
				R.layout.listbill,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] {
						BillInfo.PACKAGECNT,
						BillInfo.S_NUM, 
						BillInfo.SATISFIED, 
						BillInfo.O_NUM,
						BillInfo.S_STORE,
						BillInfo.BILLNO, 
						BillInfo.S_DATE, 
						BillInfo.A_NUM,  
						BillInfo.SKUCOUNT, 
						BillInfo.REMARK },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] {
						R.id.packagecnt,
						R.id.s_num,
						R.id.satisfied,
						R.id.o_num,
						R.id.s_store,
						R.id.billno, 
						R.id.s_date,  
						R.id.a_num, 
						R.id.skucount,
						R.id.remark });
		this.billList = billList;
	}
	
	public ListAdapter(Context context, List<? extends Map<String, ?>> list,
			ArrayList<BillDetailInfo> billDetailList, String isBillDetail) {
		super(context, list,// 数据源
				R.layout.listdetail,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { 
				BillDetailInfo.MAINBARCODE, 
				BillDetailInfo.PLU_NAME,
				BillDetailInfo.S_NUM,
				BillDetailInfo.O_NUM, 
				BillDetailInfo.PLU_NO, 
				BillDetailInfo.PLU_STYLE, 
				BillDetailInfo.UNIT, 
				BillDetailInfo.STAN_PACK ,
				BillDetailInfo.A_NUM,
				BillDetailInfo.WH_NUM},
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { 
						R.id.Mainbarcode,
						R.id.plu_name,
						R.id.s_num,
						R.id.o_num, 
						R.id.plu_no, 
						R.id.plu_style, 
						R.id.unit,
						R.id.stan_pack, 
						R.id.a_num,
						R.id.wh_num});
		this.billDetailList = billDetailList;
	}

	/**
	 * 从list中获得选中的Item
	 */
	public StoreInfo getStoreItem(int position) {
		return storeList.get(position);
	}

	public BillInfo getBillItem(int position) {
		return billList.get(position);
	}
	
	public BillDetailInfo getBillDetailItem(int position) {
		return billDetailList.get(position);
	}
}