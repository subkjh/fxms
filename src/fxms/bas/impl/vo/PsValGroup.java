package fxms.bas.impl.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 유효한 PsVoRaw 목록
 * 
 * @author subkjh
 * @since 2013.05.25
 */
public class PsValGroup {

	public class Data {
		public final String colName;
		public final Number value;

		Data(String colName, Number value) {
			this.colName = colName;
			this.value = value;
		}
	}

	public class MoData {
		public final long moNo;
		public final List<Data> values = new ArrayList<>();

		MoData(long moNo) {
			this.moNo = moNo;
		}
	}

	public final String tabName;
	public final long mstime;
	public final Map<Long, MoData> values;

	public PsValGroup(String tabName, long mstime) {
		this.tabName = tabName;
		this.mstime = mstime;
		this.values = new HashMap<>();
	}

	public void add(long moNo, String psCol, Number value) {
		MoData datas = values.get(moNo);
		if (datas == null) {
			datas = new MoData(moNo);
			values.put(moNo, datas);
		}
		datas.values.add(new Data(psCol, value));
	}

}
