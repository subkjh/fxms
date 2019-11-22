package fxms.nms.co.syslog.vo;

import fxms.nms.co.syslog.vo.SyslogPattern.LogStatus;

/**
 * 시스로그 패턴을 분석한 결과 데이터
 * 
 * @author subkjh(Kim,JongHoon)
 *
 */
public class SyslogParsingResultVo {

	private LogStatus status;

	private SyslogPattern pattern;

	private String instance;

	public String getInstance() {
		return instance;
	}

	public SyslogPattern getPattern() {
		return pattern;
	}

	public LogStatus getStatus() {
		return status;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public void setPattern(SyslogPattern pattern) {
		this.pattern = pattern;
	}

	public void setStatus(LogStatus status) {
		this.status = status;
	}

}
