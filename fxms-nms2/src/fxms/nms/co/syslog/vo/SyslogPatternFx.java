package fxms.nms.co.syslog.vo;

import fxms.nms.mo.property.ModelNoable;
import fxms.nms.mo.property.Modelable;

public class SyslogPatternFx extends SyslogPattern implements ModelNoable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 767686799193620932L;

	public static void main(String[] args) {
		// String targetStr =
		// "Jun 6 00:34:26.965 KST: %LINK-4-ERROR: FastEthernet0/14 is experiencing
		// errors";
		// // pattern에 해당하는 string이 없기 때문에 nothing이 출력.
		//
		// // targetStr =
		// // "Jun 6 00:34:22.569 KST: %CDP-4-NATIVE_VLAN_MISMATCH: Native VLAN
		// // mismatch discovered on FastEthernet0/12
		// // (1), with NGN_FS3 FastEthernet9/7 (2)";
		// targetStr =
		// "%RPM0-P:RP1 %OSPF-5-ADJCHG: OSPF Process 100, Nbr 58.229.16.29 on interface
		// Te 2/2 change state from DOWN to INIT";
		// targetStr =
		// "Nov 13 18:49:29 devser25 sshd[21826]: pam_unix(sshd:session): session opened
		// for user ami by (uid=0)";
		// // ocrpattern string에 해당하는 값이 있기 때문에 occur

		// targetStr =
		// "%RPM0-P:RP1 %OSPF-5-ADJCHG: OSPF Process 100, Nbr 221.139.106.202 on
		// interface Te 3/2 change state from
		// LOADING to FULL";

		SyslogPatternFx st = new SyslogPatternFx();
		// st.setOcrPatternStr("Neighbor Down;EXCHANGE;EXSTART;INIT;2WAY;to DOWN;");
		// st.setOcrPatternStr("link off(operational)");
		// Neighbor Down EXCHANGE EXSTART INIT 2WAY to DOWN 이 string 패턴이 된다.
		// st.setClrPatternStr("to FULL;");
		// st.setClrPatternStr("to FULL;");

		// st.setIfDescrPatternStr("FastEthernet;GigabitEthernet;TenGigabitEthernet;Port-channel;Te;Gi;Fa;Po;Vlan;");
		// System.out.println(st.check(targetStr));
		// System.out.println(st.getAlarmLevel());
		// System.out.println(st.getIfDescr(targetStr));

		// System.out.println("AAA&bbb".indexOf('&'));
		// String test = "AAA|BBB;CCC&DDD";
		// String ss[] = test.split(";|&|\\|");
		// for (String s : ss) {
		// System.out.println(s);
		// }
		String s;

		st.setOcrPatternStr("link off(operational)");
		st.setClrPatternStr("link on(operational)");
		st.setInstanceBaseStr(" link ");
		st.setInstanceStart(-2);
		st.setInstanceLength(2);
		s = "Apr 19 14:15:45 167.1.21.95 system: port 20 link on(operational) ";
		System.out.println(st.getInstance(s));

		st.setOcrPatternStr("link off(operational)");
		st.setClrPatternStr("link on(operational)");
		st.setInstanceBaseStr(" session opened ");
		st.setInstanceStart(-2);
		st.setInstanceLength(1);
		s = "Apr 19 17:16:52 devser25 sshd[7669]: pam_unix(sshd:session): session opened for user nprism by (uid=0)";
		System.out.println(st.getInstance(s));

		st.setInstanceBaseStr(" port ");
		st.setInstanceStart(0);
		st.setInstanceLength(2);
		System.out.println(st.getInstance("Apr 19 14:15:45 167.1.21.95 system: port 20 link on(operational) "));

	}

	/** 발생위치검색필드 */
	private String fieldMo;

	/** 관리대상 종류 */
	private String moClass;

	/** 장비모델번호(적용할) */
	private int modelNo;

	public SyslogPatternFx() {

	}

	public SyslogPatternFx(int alarmCode, int modelNo, String alarmName, int alarmLevel, String triggerStr,
			String ocrPatternStr, String clrPatternStr, String moClass, int secRelease) {

		super(alarmCode, alarmName, alarmLevel, triggerStr, ocrPatternStr, clrPatternStr, secRelease);

		setFieldMo("moAlias");
		setMoClass(moClass);
		this.modelNo = modelNo;
	}

	@Override
	public LogStatus check(String syslog) {

		// targetString 을 체크하여 Logstaus 나옴

		if (isMatch(syslog, clrPatternStrArray, isClrAnd)) {
			return isSetMo() ? LogStatus.clearOne : LogStatus.clearAll;
		}

		// isDefined(ifDescrPatternStr)하여 참이면 LogStatus.clearOne, 아니면
		// LogStatus.clearAll
		// ifDescrPatternStr가 있으면 clearOne, 없으면 clearAll이다.

		// targetStr과 ocrPatternStr[]을 비교하여 ocrPatternStr[]에 targetStr가 있을
		// 경우 아래 return 실행
		if (isMatch(syslog, ocrPatternStrArray, isOcrAnd)) {
			if (isFault()) {
				return isClearDefined() && isSetMo() ? LogStatus.occurIfNotExist : LogStatus.occur;
			} else {
				return LogStatus.notTrigger;
			}
		}

		return LogStatus.nothing;
	}

	@Override
	public boolean equalModel(Modelable o) {
		if (o instanceof ModelNoable) {
			return ((ModelNoable) o).getModelNo() == modelNo;
		}
		return false;
	}

	public String getFieldMo() {
		return fieldMo;
	}

	public String getMoClass() {
		return moClass;
	}

	public int getModelNo() {
		return modelNo;
	}

	public boolean isSetMo() {
		return moClass != null && moClass.length() > 0 //
				&& fieldMo != null && fieldMo.length() > 0;
	}

	public void setFieldMo(String fieldMo) {
		this.fieldMo = fieldMo;
	}

	public void setMoClass(String moClass) {
		this.moClass = moClass;
	}

	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

}
