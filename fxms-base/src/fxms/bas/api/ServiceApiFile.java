package fxms.bas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fxms.bas.api.dbo.VarDbo;
import fxms.bas.define.PS_TYPE;
import fxms.bas.mo.FxServiceMo;
import subkjh.bas.fao.FxmsFao;
import subkjh.bas.log.Logger;

public class ServiceApiFile extends ServiceApi {

	public static void main(String[] args) {
		ServiceApiFile api = new ServiceApiFile();
		try {
			api.setVarValue("TEST", 2, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(api.getVarValue("TEST", 1));
	}

	private Map<String, FxServiceMo> serviceMap;
	private Map<String, VarDbo> varMap;

	private FxmsFao fao;

	public ServiceApiFile() {
		fao = new FxmsFao();

		try {
			// serviceMap = fao.get(ServiceDbo.class);
			// varMap = fao.get(VarDbo.class);
		} catch (Exception e) {
			Logger.logger.error(e);
		}

	}

	@Override
	protected VarDbo doSelectVar(String varName) throws Exception {
		return varMap.get(varName);
	}

	@Override
	protected List<VarDbo> doSelectVarAll() throws Exception {
		return new ArrayList<VarDbo>(varMap.values());

	}

	@Override
	protected void doSetServiceStatus(FxServiceMo service, long startDate, String serviceStatus) throws Exception {
		service.setServiceStatus(serviceStatus);
		service.setStatusChgDate(FxApi.getDate(0));

	}

	@Override
	protected void doUpdateVarValue(String varName, Object varValue) throws Exception {
		VarDbo var = varMap.get(varName);
		if (var == null) {
			var = new VarDbo();
			var.setVarName(varName);
			varMap.put(var.getVarName(), var);
		}
		var.setVarValue(varValue + "");
		var.setChgDate(PS_TYPE.getHstimeByMstime(System.currentTimeMillis()));

		fao.set(VarDbo.class, varMap);
	}

	@Override
	public void doSetServiceStatus(String serviceStatus) throws Exception {
		// TODO Auto-generated method stub

	}

}
