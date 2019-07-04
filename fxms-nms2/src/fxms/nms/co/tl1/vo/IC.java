package fxms.nms.co.tl1.vo;

import fxms.nms.co.tl1.NetPduTL1;

public class IC extends NetPduTL1 {

	/** command code. * NE가 수행할 행위 */
	private String cc;

	/** staging parameter blocks. Target NE와 행위의 대상 */
	private String spb;

	/** message payload block(s). 행위에 필요한 파라메터 */
	private String mpb;

	public IC(String cc, String spb, String mpb) {

	}

	@Override
	public byte[] getBytes() throws Exception {
		return null;
	}

	@Override
	public String toString() {
		return cc + ":" + (spb == null ? "" : spb) + ":" + (mpb == null ? "" : mpb) + ";";
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getSpb() {
		return spb;
	}

	public void setSpb(String spb) {
		this.spb = spb;
	}

	public String getMpb() {
		return mpb;
	}

	public void setMpb(String mpb) {
		this.mpb = mpb;
	}

}
