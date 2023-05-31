package fxms.bas.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 성능 관리대상 정보
 * 
 * @author subkjh
 *
 */
public class PsMsIpAddrVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5965088990433889655L;

	private List<String> ownerList = null;

	public PsMsIpAddrVo(String s) {
		if (s == null)
			return;
		if ("*".equals(s))
			return;

		ownerList = new ArrayList<String>();
		String ss[] = s.split("\\,");
		for (String s1 : ss) {
			if (s1.trim().length() > 0)
				ownerList.add(s1.trim());
		}
	}

	public void add(String ip) {

		if (ownerList == null) {
			ownerList = new ArrayList<String>();
		}

		if (ownerList.contains(ip) == false) {
			ownerList.add(ip);
		}
	}

	/**
	 * 관리서버 목록을 제공합니다.
	 * 
	 * @return 관리서버목록
	 */
	public List<String> getOwnerList() {
		return ownerList;
	}

	/**
	 * 
	 * @return '172.0.0.1', '172.0.0.2' 형식
	 */
	public String getSqlString() {

		if (isAll())
			return "";

		StringBuffer sb = new StringBuffer();
		for (String ip : ownerList) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append("'");
			sb.append(ip);
			sb.append("'");
		}

		if (sb.length() > 0)
			return sb.toString();

		return "'nothing'";
	}

	/**
	 * 모든 관제대상이 포함되는지 여부<br>
	 * 특정 IP가 지정되지 않으면 모든 MO가 대상이 됩니다.
	 * 
	 * @return 포함여부
	 */
	public boolean isAll() {
		return ownerList == null;
	}

	/**
	 * 입력된 수집서버 주소가 포함되어 있는지 확인
	 * 
	 * @param ip
	 * @return
	 */
	public boolean contains(String ip) {

		if (isAll())
			return true;

		return ownerList.contains(ip);
	}

	@Override
	public String toString() {

		if (isAll())
			return "OWNER(*)";

		StringBuffer sb = new StringBuffer();

		for (String ip : ownerList) {
			sb.append(ip);
			sb.append(" ");
		}

		if (sb.toString().length() > 0)
			return "OWNER(" + sb.toString().trim() + ")";
		return "OWNER(nothing)";
	}

}