package fxms.bas.impl.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.FxApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.DeleteDiagramLineDbo;
import fxms.bas.impl.dbo.DeleteDiagramNodeDbo;
import fxms.bas.impl.dbo.all.FX_UI_DIAG_BAS;
import fxms.bas.impl.dbo.all.FX_UI_DIAG_LINE;
import fxms.bas.impl.dbo.all.FX_UI_DIAG_NODE;
import fxms.bas.impl.handler.dto.DeleteDiagramLinePara;
import fxms.bas.impl.handler.dto.DeleteDiagramNodePara;
import fxms.bas.impl.handler.dto.DeleteDiagramPara;
import fxms.bas.impl.handler.dto.SelectDiagramPara;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User.USER_TYPE_CD;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.def.FxDaoCallBefore;

/**
 * 
 * @author subkjh
 *
 */
public class DiagramHandler extends BaseHandler {

	public Object deleteDiagram(SessionVo session, DeleteDiagramPara obj) throws Exception {

		this.delete(obj, new FxDaoCallBefore<DeleteDiagramPara>() {
			@Override
			public void onCall(ClassDao tran, DeleteDiagramPara data) throws Exception {

				Map<String, Object> wherePara = FxApi.makePara("diagNo", data.getDiagNo());
				tran.deleteOfClass(FX_UI_DIAG_NODE.class, wherePara);
				tran.deleteOfClass(FX_UI_DIAG_LINE.class, wherePara);
			}
		});

		return obj;
	}

	public Object deleteDiagramLine(SessionVo session, DeleteDiagramLinePara obj) throws Exception {

		delete(obj, null);

		return obj;
	}

	public Object deleteDiagramNode(SessionVo session, DeleteDiagramNodePara obj) throws Exception {
		delete(obj, null);
		return obj;
	}

	public Object getDiagramConfig(SessionVo session, SelectDiagramPara obj) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("nodeList", tran.select(FX_UI_DIAG_NODE.class, obj));
			retMap.put("lineList", tran.select(FX_UI_DIAG_LINE.class, obj));

			return retMap;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}

	}

	public Object insertDiagram(SessionVo session, FX_UI_DIAG_BAS obj) throws Exception {

		obj.setOwnerUserNo(session.getUserNo());

		this.insert(obj, new FxDaoCallBefore<FX_UI_DIAG_BAS>() {
			@Override
			public void onCall(ClassDao tran, FX_UI_DIAG_BAS data) throws Exception {

				int diagNo = tran.getNextVal(FX_UI_DIAG_BAS.FX_SEQ_DIAGNO, Integer.class);

				data.setDiagNo(diagNo);

			}
		});

		return obj;
	}

	public Object insertDiagramLine(SessionVo session, FX_UI_DIAG_LINE obj) throws Exception {

		insert(obj, null);

		return obj;
	}

	public Object insertDiagramNode(SessionVo session, FX_UI_DIAG_NODE obj) throws Exception {

		insert(obj, null);

		return obj;
	}

	public Object selectDiagram(SessionVo session, SelectDiagramPara obj) throws Exception {
		return ClassDaoEx.SelectDatas(FX_UI_DIAG_BAS.class, obj, FX_UI_DIAG_BAS.class);
	}

	public Object selectDiagramGridList(SessionVo session, Map<String, Object> parameters) throws Exception {

		Map<String, Object> para = parameters;

		if (session.getUserTypeCd() != USER_TYPE_CD.Admin) {
			para.put("userNo", session.getUserNo());
		}

		return ClassDaoEx.SelectDatas(FX_UI_DIAG_BAS.class, para, FX_UI_DIAG_BAS.class);
	}

	public Object selectDiagramLineList(SessionVo session, SelectDiagramPara obj) throws Exception {
		return ClassDaoEx.SelectDatas(FX_UI_DIAG_LINE.class, obj, FX_UI_DIAG_LINE.class);
	}

	public Object selectDiagramNodeList(SessionVo session, SelectDiagramPara obj) throws Exception {
		return ClassDaoEx.SelectDatas(FX_UI_DIAG_NODE.class, obj, FX_UI_DIAG_NODE.class);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object setDiagramConfig(SessionVo session, SelectDiagramPara obj, Map<String, Object> parameters)
			throws Exception {

		Object val;
		List<Map> listObj;
		List<FX_UI_DIAG_NODE> nodeList = new ArrayList<FX_UI_DIAG_NODE>();
		List<FX_UI_DIAG_LINE> lineList = new ArrayList<FX_UI_DIAG_LINE>();

		val = parameters.get("nodeList");
		if (val instanceof List) {
			listObj = (List) val;
			for (Map map : listObj) {
				FX_UI_DIAG_NODE o = convert(session, map, FX_UI_DIAG_NODE.class, true);
				nodeList.add(o);
			}

		}

		val = parameters.get("lineList");
		if (val instanceof List) {
			listObj = (List) val;
			for (Map map : listObj) {
				FX_UI_DIAG_LINE o = convert(session, map, FX_UI_DIAG_LINE.class, true);
				lineList.add(o);
			}

		}

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			tran.deleteOfObject(DeleteDiagramNodeDbo.class, obj);
			tran.deleteOfObject(DeleteDiagramLineDbo.class, obj);

			tran.insertOfClass(FX_UI_DIAG_NODE.class, nodeList);
			tran.insertOfClass(FX_UI_DIAG_LINE.class, lineList);

			tran.commit();

			return parameters;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createQidDao();
	}

}
