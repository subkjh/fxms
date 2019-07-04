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

	public AM(String s) throws Exception {
		String contents = s.trim();
		setContents(contents);

		String line[] = contents.split("\n");

		// header = new ORMF_HEADER(line[0]);
		ai = new AM_AI(line[1]);

		dataList = new ArrayList<String>();

		for (int index = 2; index < line.length; index++) {
			dataList.add(line[index].trim());
		}
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
