package fxms.nms.api;

import java.util.ArrayList;
import java.util.List;

import fxms.nms.co.snmp.mo.TrapNode;
import fxms.nms.co.snmp.trap.vo.TrapEventLog;
import fxms.nms.co.snmp.trap.vo.TrapThr;

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
	protected List<TrapThr> doSelectTrapThr() throws Exception {
		// TODO Auto-generated method stub
		return new ArrayList<TrapThr>();
	}
}
