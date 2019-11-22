package fxms.nms.api;

import java.util.ArrayList;
import java.util.List;

import fxms.nms.co.snmp.trap.TrapNode;
import fxms.nms.co.snmp.trap.vo.TrapEventLog;
import fxms.nms.co.snmp.trap.vo.TrapPattern;

public class TrapApiTest extends TrapApi {

	@Override
	protected void doDeleteLogExpired() throws Exception {

	}

	@Override
	protected void doInsertLog(List<TrapEventLog> logList) throws Exception {

	}

	@Override
	protected List<TrapNode> doSelectTrapNode() throws Exception {
		return new ArrayList<TrapNode>();
	}

	@Override
	protected List<TrapPattern> doSelectTrapPattern() throws Exception {
		// TODO Auto-generated method stub
		return new ArrayList<TrapPattern>();
	}
}
