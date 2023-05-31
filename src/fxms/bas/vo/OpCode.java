package fxms.bas.vo;

import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.user.User;

/**
 * 운용 코드
 * 
 * @author subkjh
 *
 */
public class OpCode {

	private String opId;

	private String opName;

	private List<Integer> userGroupNoList;

	private String dataType;

	public OpCode(String opId, String opName, String dataType) {
		this.opId = opId;
		this.opName = opName;
		this.dataType = dataType;
		this.userGroupNoList = new ArrayList<Integer>();
	}

	public String getDataType() {
		return dataType;
	}

	public String getOpName() {
		return opName;
	}

	public String getOpId() {
		return opId;
	}

	/**
	 * 사용자그룹이 접근 가능한지 판단한다.
	 * 
	 * @param userGroupNo
	 * @return
	 */
	public boolean isAcceesable(int userGroupNo) {
		return userGroupNoList.contains(User.USER_GROUP_ALL) || userGroupNoList.contains(userGroupNo);
	}

	public void addUserGroupNo(int userGroupNo) {
		userGroupNoList.add(userGroupNo);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(opId).append(")").append(opName);
		if (userGroupNoList != null) {
			sb.append(":").append(userGroupNoList);
		}

		return sb.toString();
	}
}
