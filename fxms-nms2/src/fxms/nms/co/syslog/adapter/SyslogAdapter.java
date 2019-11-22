package fxms.nms.co.syslog.adapter;

import fxms.bas.fxo.FxActor;
import fxms.nms.co.syslog.mo.SyslogNode;
import fxms.nms.co.syslog.vo.SyslogVo;

public interface SyslogAdapter extends FxActor {

	public SyslogVo parse(SyslogNode node, SyslogVo vo) throws Exception;
}
