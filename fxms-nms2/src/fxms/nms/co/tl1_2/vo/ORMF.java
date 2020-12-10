package fxms.nms.co.tl1_2.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.nms.co.tl1_2.NetPduTL1;

/**
 * Output Response Message Format<br>
 * <header><response identification>[<text block>]<terminator>
 * 
 * @author subkjh
 * 
 */
public class ORMF extends NetPduTL1 {

	private ORMF_HEADER header;
	private ORMF_RI ri;
	private List<String> dataList;

	public ORMF(String s, int index) throws Exception {

		String contents = s.trim();
		setContents(contents);

		String line[] = contents.split("\n");

		// header = new ORMF_HEADER(line[0]);
		ri = new ORMF_RI(line[index + 1]);

		dataList = new ArrayList<String>();

		for (int i = index + 2; i < line.length; i++) {
			dataList.add(line[i].trim());
		}
	}

	@Override
	public byte[] getBytes() throws Exception {
		return null;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public ORMF_HEADER getHeader() {
		return header;
	}

	public ORMF_RI getRi() {
		return ri;
	}

	@Override
	public String getString() {
		return getContents();
	}

	public void setHeader(ORMF_HEADER header) {
		this.header = header;
	}

	@Override
	public String toString() {
		return header + "," + ri + "\n" + getContents();
	}

	public boolean isCompld() {
		return ri != null && ri.getCc().equals("COMPLD");
	}

	public boolean isDeny() {
		return ri != null && ri.getCc().equals("DENY");
	}

	public boolean isRtrv() {
		return ri != null && ri.getCc().equals("RTRV");
	}
}
