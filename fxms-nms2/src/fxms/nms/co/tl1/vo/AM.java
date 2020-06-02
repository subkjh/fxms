package fxms.nms.co.tl1.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.nms.co.tl1.NetPduTL1;

/**
 * Autonomous Message<br>
 * 
 * format : <header><auto id>[<text block>]<terminator>
 * 
 * @author subkjh
 * 
 */
public class AM extends NetPduTL1 {

	private ORMF_HEADER header;
	private AM_AI ai;
	private List<String> dataList;

	public AM(ORMF_HEADER header, String s) throws Exception {
		
		this.header = header;
		
		String contents = s.trim();
		setContents(contents);

		String line[] = contents.split("\n");

		ai = new AM_AI(line[1]);

		dataList = new ArrayList<String>();

		for (int index = 2; index < line.length; index++) {
			dataList.add(line[index].trim());
		}
		
		
//		   JCN▒▒▒_▒▒▒▒COT1_▒▒õ1 2019-10-08 17:15:00
//		A  133851 REPT TCA
//		   /* AID,UNIT,SIGNAL,TIME,PM_ELEMENT,DATETIME */
//		   "M05-P1,MXG02UB,10GE,PM15M,UAS,2019-10-08 17:15:00"
//		;
	//
//		   TL1ADAPTER 2019-10-08 17:15:39
//		A  133852 REPT EVT SESSION-CHECK
//		;
	}
	
	

	public AM_AI getAi() {
		return ai;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setHeader(ORMF_HEADER header) {
		this.header = header;
	}

	public ORMF_HEADER getHeader() {
		return header;
	}

	public String toString() {
		return "AM " + header + "," + ai + "\n" + getContents();
	}
}
