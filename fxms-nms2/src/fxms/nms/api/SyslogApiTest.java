package fxms.nms.api;

import java.util.ArrayList;
import java.util.List;

import fxms.nms.co.syslog.mo.SyslogNode;
import fxms.nms.co.syslog.vo.SyslogEventLog;
import fxms.nms.co.syslog.vo.SyslogPattern;

public class SyslogApiTest extends SyslogApi {

	@Override
	protected void doDeleteLogExpired() throws Exception {

	}

	@Override
	protected void doInsertLog(List<SyslogEventLog> logList) throws Exception {

	}

	@Override
	protected List<SyslogNode> doSelectSyslogNode() throws Exception {
		return new ArrayList<SyslogNode>();
	}

	@Override
	protected List<SyslogPattern> doSelectSyslogPattern() throws Exception {
		return new ArrayList<SyslogPattern>();
	}

}
