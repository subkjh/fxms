package com.fxms.ui.bas.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListData {

	private String columns[];
	private List<Object[]> dataList = new ArrayList<Object[]>();
	private String qid;
	private String errmsg;
	
	public ListData()
	{
		
	}
	
	public ListData(String errmsg) {
		columns = new String[] {};
		this.errmsg = errmsg;
	}

	@SuppressWarnings("unchecked")
	public ListData(String qid, Map<String, Object> map) {

		this.qid = qid;

		List<String> colList = (List<String>) map.get("columns");
		columns = colList.toArray(new String[colList.size()]);

		List<List<Object>> datas = (List<List<Object>>) map.get("datas");
		if (datas != null) {
			for (List<Object> data : datas) {
				dataList.add(data.toArray(new Object[data.size()]));
			}
		}

	}

	public String[] getColumns() {
		return columns;
	}

	public List<Object[]> getDataList() {
		return dataList;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public String getQid() {
		return qid;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

}
