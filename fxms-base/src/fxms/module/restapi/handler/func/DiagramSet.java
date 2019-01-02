package fxms.module.restapi.handler.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.dbo.diag.FX_DI_LINE;
import fxms.bas.dbo.diag.FX_DI_MAIN;
import fxms.bas.dbo.diag.FX_DI_NODE;
import fxms.bas.dbo.diag.FX_DI_PROPERTY;
import fxms.bas.fxo.FxCfg;
import fxms.module.restapi.vo.SessionVo;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.ObjectUtil;

public class DiagramSet {

	@SuppressWarnings("unchecked")
	public Object set(SessionVo session, Map<String, Object> parameters) throws Exception {

		Map<String, Object> mainMap = (Map<String, Object>) parameters.get("main");
		List<Map<String, Object>> nodeMapList = (List<Map<String, Object>>) parameters.get("node");
		List<Map<String, Object>> lineMapList = (List<Map<String, Object>>) parameters.get("line");
		List<Map<String, Object>> propertyMapList = (List<Map<String, Object>>) parameters.get("property");

		FX_DI_MAIN main = new FX_DI_MAIN();
		ObjectUtil.toObject(mainMap, main);

		Logger.logger.info(String.valueOf(main));

		List<FX_DI_NODE> nodeList = null;
		List<FX_DI_LINE> lineList = null;
		List<FX_DI_PROPERTY> propertyList = null;

		if (nodeMapList != null && nodeMapList.size() > 0) {
			nodeList = new ArrayList<FX_DI_NODE>();
			FX_DI_NODE node;
			for (Map<String, Object> map : nodeMapList) {
				node = new FX_DI_NODE();
				ObjectUtil.toObject(map, node);
				nodeList.add(node);
			}
		}

		if (lineMapList != null && lineMapList.size() > 0) {
			lineList = new ArrayList<FX_DI_LINE>();
			FX_DI_LINE line;
			for (Map<String, Object> map : lineMapList) {
				line = new FX_DI_LINE();
				ObjectUtil.toObject(map, line);
				lineList.add(line);
			}
		}

		if (propertyMapList != null && propertyMapList.size() > 0) {
			propertyList = new ArrayList<FX_DI_PROPERTY>();
			FX_DI_PROPERTY property;
			for (Map<String, Object> map : propertyMapList) {
				property = new FX_DI_PROPERTY();
				ObjectUtil.toObject(map, property);
				if (property.getPropertyValue() != null && property.getPropertyValue().length() > 0) {
					propertyList.add(property);
				}
			}
		}

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		try {
			tran.start();

			main.setChgDate(FxApi.getDate(0));
			main.setUserNo(session.getUserNo());
			main.setOpNo(0);

			if (main.getDiagNo() == null || main.getDiagNo() <= 0) {

				Integer diagNo = tran.getNextVal(FX_DI_MAIN.FX_SEQ_DIAG_NO, Integer.class);
				if (diagNo == null) {
					throw new Exception(FX_DI_MAIN.FX_SEQ_DIAG_NO + " error");
				}

				main.setDiagNo(diagNo);
				main.setRegDate(FxApi.getDate(0));
				tran.insert(main);

			} else {
				Map<String, Object> deletePara = new HashMap<String, Object>();
				deletePara.put("diagNo", main.getDiagNo());
				tran.update(main, null);
				tran.deleteOfClass(FX_DI_NODE.class, deletePara);
				tran.deleteOfClass(FX_DI_LINE.class, deletePara);
				tran.deleteOfClass(FX_DI_PROPERTY.class, deletePara);
			}

			if (nodeList != null) {
				for (FX_DI_NODE node : nodeList) {
					node.setDiagNo(main.getDiagNo());
				}
			}
			if (lineList != null) {
				for (FX_DI_LINE node : lineList) {
					node.setDiagNo(main.getDiagNo());
				}
			}
			if (propertyList != null) {
				for (FX_DI_PROPERTY node : propertyList) {
					node.setDiagNo(main.getDiagNo());
				}
			}

			tran.insertOfClass(FX_DI_NODE.class, nodeList);
			tran.insertOfClass(FX_DI_LINE.class, lineList);
			tran.insertOfClass(FX_DI_PROPERTY.class, propertyList);

			return main;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}
