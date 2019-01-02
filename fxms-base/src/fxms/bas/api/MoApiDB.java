package fxms.bas.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.dbo.FX_CF_MO_ATTR;
import fxms.bas.dbo.FX_HST_MO;
import fxms.bas.fxo.FxCfg;
import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLocation;
import fxms.bas.mo.attr.Model;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exception.MoNotFoundException;
import fxms.bas.mo.property.HasAddableData;
import fxms.bas.noti.FxEvent;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.diff.DiffData;
import subkjh.bas.utils.diff.Differ;

public class MoApiDB extends MoApi {

	private Map<String, String> moAttrKeyMap;

	@Override
	public void onNotify(FxEvent noti) throws Exception {
		super.onNotify(noti);

		moAttrKeyMap = null;
	}

	private synchronized Map<String, String> getAttrKeyMap() {

		if (moAttrKeyMap != null) {
			return moAttrKeyMap;
		}

		FxDaoExecutor tran = null;
		try {
			tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();
		} catch (Exception e1) {
			Logger.logger.error(e1);
			return null;
		}

		try {
			tran.start();
			Map<String, String> keyMap = new HashMap<String, String>();

			List<FX_CF_MO_ATTR> list = tran.select(FX_CF_MO_ATTR.class, null);
			for (FX_CF_MO_ATTR attr : list) {
				keyMap.put(attr.getAttrCd(), attr.getAttrNm());
			}

			moAttrKeyMap = keyMap;
			return keyMap;

		} catch (Exception e) {
			Logger.logger.error(e);
			return null;
		} finally {
			tran.stop();
		}

	}

	private List<FX_HST_MO> getHstMo(Map<String, String> attrKeyMap, Mo mo, List<DiffData> diffList) {

		List<FX_HST_MO> list = new ArrayList<FX_HST_MO>();

		if (diffList == null) {
			return list;
		}

		FX_HST_MO hst;
		for (DiffData data : diffList) {
			hst = new FX_HST_MO();
			hst.setAttrAfVal(String.valueOf(data.getAfData()));
			hst.setAttrBfVal(String.valueOf(data.getBfData()));
			hst.setAttrCd(data.getName());
			hst.setAttrNm(attrKeyMap != null ? attrKeyMap.get(data.getName()) : data.getName());
			hst.setMoHstCd("U");
			hst.setMoHstNm("속성변경");
			hst.setMoName(mo.getMoName());
			hst.setMoNo(mo.getMoNo());
			hst.setRegDate(mo.getChgDate());
			hst.setRegUserNo(mo.getChgUserNo());

			list.add(hst);
		}

		return list;
	}

	private FX_HST_MO getHstMo(Mo mo, boolean added, int userNo, String reason) {

		FX_HST_MO hst;
		hst = new FX_HST_MO();
		hst.setAttrAfVal(reason);
		hst.setAttrBfVal(null);
		hst.setAttrCd("MO");
		hst.setAttrNm(mo.getMoClass());
		hst.setMoHstCd(added ? "A" : "D");
		hst.setMoHstNm(added ? "추가" : "삭제");
		hst.setMoName(mo.getMoName());
		hst.setMoNo(mo.getMoNo());
		hst.setRegDate(FxApi.getDate(0));
		hst.setRegUserNo(userNo);

		return hst;
	}

	@Override
	protected Mo doAdd(Mo mo, MoConfig children, String reason) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			if (mo.getMoNo() <= 0) {
				mo.setMoNo(tran.getNextVal(Mo.FX_SEQ_MONO, Long.class));
			}
			mo.setRegDate(FxApi.getDate(0));

			tran.insert(mo);

			if (mo instanceof HasAddableData) {
				List<Object> objList = ((HasAddableData) mo).getAddableDatas();
				if (objList != null) {
					for (Object obj : objList) {
						if (obj != null) {
							try {
								tran.insert(obj);
							} catch (Exception e) {
								Logger.logger.error(e);
							}
						}
					}
				}
			}

			tran.insert(getHstMo(mo, true, mo.getRegUserNo(), reason));

			if (children != null && children.sizeAll() > 0) {
				for (Mo child : children.getMoListAll()) {
					child.setMoNo(tran.getNextVal(Mo.FX_SEQ_MONO, Long.class));
					child.setUpperMoNo(mo.getMoNo());
					child.setRegDate(FxApi.getDate(0));
					child.setRegUserNo(mo.getRegUserNo());
					tran.insert(child);
					tran.insert(getHstMo(child, true, mo.getRegUserNo(), "add-upper"));
				}
			}

			tran.commit();

			return mo;
			
		} catch (Exception e) {
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<Mo> doDelete(Mo mo, int userNo, String reason) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			// find children
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("upperMoNo", mo.getMoNo());
			List<Mo> moList = tran.select(Mo.class, para);
			List<Mo> children = new ArrayList<Mo>();
			Mo child;
			para.clear();

			for (Mo e : moList) {
				if (e.getMoClass().equals(Mo.MO_CLASS) == false) {
					para.put("moNo", e.getMoNo());
					child = tran.selectOne(getMoClass(e.getMoClass()), para);
					if (child != null) {
						children.add(child);
					}
				} else {
					children.add(e);
				}
			}

			for (Mo e : children) {
				tran.deleteOfObject(e);
				tran.insert(getHstMo(e, false, userNo, "upper-delete"));
			}

			tran.deleteOfObject(mo);
			tran.insert(getHstMo(mo, false, userNo, reason));

			tran.commit();

			children.add(0, mo);

			return children;

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected Mo doSelect(long moNo) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("moNo", moNo);

		try {
			tran.start();
			Mo mo = tran.selectOne(Mo.class, para);
			if (mo != null) {
				if (mo.getMoClass().equals(Mo.MO_CLASS) == false) {
					return tran.selectOne(getMoClass(mo.getMoClass()), para);
				}
			}

			return mo;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected Mo doSelect(long upperMoNo, String moClass, String moName) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			Map<String, Object> para = new HashMap<String, Object>();
			if (upperMoNo >= 0) {
				para.put("upperMoNo", upperMoNo);
			}
			para.put("moName", moName);
			para.put("moClass", moClass);

			Mo mo = tran.selectOne(Mo.class, para);
			if (mo != null) {
				return tran.selectOne(getMoClass(mo.getMoClass()), para);
			}

			return mo;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Mo> doSelect(Map<String, Object> parameters) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			List<Mo> moList = (List<Mo>) tran.select(Mo.class, parameters);
			if (moList.size() == 0)
				return moList;

			List<String> moClassList = new ArrayList<String>();
			for (Mo mo : moList) {
				if (moClassList.contains(mo.getMoClass()) == false) {
					moClassList.add(mo.getMoClass());
				}
			}

			List<Mo> entry;
			moList.clear();
			for (String moClass : moClassList) {
				entry = (List<Mo>) tran.select(getMoClass(moClass), parameters);
				moList.addAll(entry);
			}

			return moList;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Mo> doSelect(String moClass, Map<String, Object> para) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			return (List<Mo>) tran.select(getMoClass(moClass), para);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<Mo> doSelectChildren(long moNo, String moClass) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("upperMoNo", moNo);

		try {
			tran.start();

			List<Mo> moList = tran.select(Mo.class, para);
			Mo child;
			List<Mo> children = new ArrayList<Mo>();

			para.clear();
			for (Mo e : moList) {
				if (e.getMoClass().equals(Mo.MO_CLASS) == false) {
					para.put("moNo", e.getMoNo());
					child = tran.selectOne(getMoClass(e.getMoClass()), para);
					if (child != null) {
						children.add(child);
					}
				} else {
					children.add(e);
				}
			}

			return children;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<MoLocation> doSelectLocationList() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			return tran.select(MoLocation.class, null);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected List<Model> doSelectModelList() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			return tran.select(Model.class, null);
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected void doSetMoChildren(MoConfig children) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			for (Mo mo : children.getMoListAll()) {
				if (mo.getStatus() == FxEvent.STATUS.deleted) {
					tran.deleteOfObject(mo);
				}
			}

			for (Mo mo : children.getMoListAll()) {
				if (mo.getStatus() == FxEvent.STATUS.changed) {
					tran.update(mo, null);
				}
			}

			for (Mo mo : children.getMoListAll()) {

				if (mo.getStatus() == FxEvent.STATUS.added) {

					mo.setMoNo(tran.getNextVal(Mo.FX_SEQ_MONO, Long.class));

					((Mo) mo).setRegDate(((Mo) mo).getSyncDate());
					((Mo) mo).setRegUserNo(((Mo) mo).getSyncUserNo());

					tran.insert(mo);
					tran.insert(getHstMo(mo, true, children.getParent().getChgUserNo(), "add-upper"));
				}
			}

			tran.update(children.getParent(), null);

			if (children.getParent() instanceof HasAddableData) {
				List<Object> objList = ((HasAddableData) children.getParent()).getAddableDatas();
				if (objList != null) {
					for (Object obj : objList) {
						try {
							tran.update(obj, null);
						} catch (Exception e) {
							Logger.logger.error(e);
						}
					}
				}
			}

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected Mo doUpdate(Mo mo, Map<String, Object> parameters) throws Exception {

		Map<String, String> attrKeyMap = getAttrKeyMap();
		List<DiffData> diffList = new Differ(attrKeyMap == null ? null : attrKeyMap.keySet()).diff(mo, parameters);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			tran.updateOfClass(mo.getClass(), parameters);

			List<FX_HST_MO> list = getHstMo(attrKeyMap, mo, diffList);
			if (list.size() > 0) {
				tran.insertOfClass(FX_HST_MO.class, list);
			}

			tran.commit();
			return mo;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@Override
	protected Mo doUpdate(Mo mo, MoConfig children) throws Exception {

		Mo moOld = getMo(mo.getMoNo());
		if (moOld == null) {
			throw new MoNotFoundException(mo.getMoNo());
		}

		Map<String, String> attrKeyMap = getAttrKeyMap();
		List<DiffData> diffList = new Differ(attrKeyMap == null ? null : attrKeyMap.keySet()).diff(moOld, mo);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			mo.setChgDate(FxApi.getDate(0));
			tran.update(mo, null);

			List<FX_HST_MO> list = getHstMo(attrKeyMap, mo, diffList);
			if (list.size() > 0) {
				tran.insertOfClass(FX_HST_MO.class, list);
			}

			tran.commit();
			return mo;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

}
