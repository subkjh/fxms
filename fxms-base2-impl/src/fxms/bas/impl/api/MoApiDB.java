package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.user.User;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.co.utils.diff.DiffData;
import subkjh.bas.co.utils.diff.Differ;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.fxdao.control.FxDaoExecutor;
import fxms.bas.api.FxApi;
import fxms.bas.api.MoApi;
import fxms.bas.co.noti.FxEvent;
import fxms.bas.co.vo.IsRegChg;
import fxms.bas.co.vo.IsSync;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.FX_CF_INLO;
import fxms.bas.impl.dbo.FX_CF_MODEL;
import fxms.bas.impl.dbo.FX_CF_MO_ATTR;
import fxms.bas.impl.dbo.FX_HST_MO;
import fxms.bas.impl.dbo.FX_MO;
import fxms.bas.impl.mo.FxMo;
import fxms.bas.mo.Mo;
import fxms.bas.mo.attr.MoLocation;
import fxms.bas.mo.attr.Model;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.mo.exp.MoNotFoundException;
import fxms.bas.mo.property.HasAddableData;

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
			hst.setRegDate(FxApi.getDate(0));
			hst.setRegUserNo(0);

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
	protected Mo doAdd(Mo mo, MoConfig children, String reason, int userNo) throws Exception {

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			if (mo.getMoNo() <= 0) {
				mo.setMoNo(tran.getNextVal(FX_MO.FX_SEQ_MONO, Long.class));
			}

			if (mo instanceof IsRegChg) {
				IsRegChg v = (IsRegChg) mo;
				v.setChgUserNo(userNo);
				v.setRegUserNo(userNo);
			}

			tran.insertOfClass(toDboClass(mo.getMoClass()), mo);

			if (mo instanceof HasAddableData) {
				List<Object> objList = ((HasAddableData) mo).getAddableDatas();
				if (objList != null) {
					for (Object obj : objList) {
						if (obj != null) {
							try {
								tran.insertOfClass(obj.getClass(), obj);
							} catch (Exception e) {
								Logger.logger.error(e);
							}
						}
					}
				}
			}

			tran.insertOfClass(FX_HST_MO.class, getHstMo(mo, true, userNo, reason));

			if (children != null && children.sizeAll() > 0) {
				Mo child;
				for (Mo e : children.getMoListAll()) {
					child = (Mo) e;
					child.setMoNo(tran.getNextVal(FX_MO.FX_SEQ_MONO, Long.class));
					child.setUpperMoNo(mo.getMoNo());
					if (child instanceof IsRegChg) {
						IsRegChg v = (IsRegChg) child;
						v.setRegDate(FxApi.getDate(0));
						v.setRegUserNo(userNo);
					}
					tran.insertOfClass(toDboClass(child.getMoClass()), child);
					tran.insertOfClass(FX_HST_MO.class, getHstMo(child, true, userNo, "add-upper"));
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
			List<FxMo> moList = tran.select(FX_MO.class, para, FxMo.class);
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
				tran.deleteOfClass(toDboClass(e.getMoClass()), e, null);
				tran.insertOfClass(FX_HST_MO.class, getHstMo(e, false, userNo, "upper-delete"));
			}

			tran.deleteOfClass(toDboClass(mo.getMoClass()), mo, null);
			tran.insertOfClass(FX_HST_MO.class, getHstMo(mo, false, userNo, reason));

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
	protected List<MoLocation> doSelectLocationList() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			List<FX_CF_INLO> list = tran.select(FX_CF_INLO.class, null);
			List<MoLocation> ret = new ArrayList<MoLocation>();
			for (FX_CF_INLO a : list) {
				ret.add(new MoLocation(a.getInloNo(), a.getInloName(), a.getInloType(), a.getUpperInloNo(), a.getInloFname()));
			}
			return ret;

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Mo> doSelectMo(Map<String, Object> parameters, boolean isDeep) throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			List<FxMo> tmpList = tran.select(FX_MO.class, parameters, FxMo.class);
			List<Mo> moList = new ArrayList<Mo>();
			for (FxMo mo : tmpList) {
				moList.add(mo);
			}

			if (moList.size() == 0)
				return moList;

			if (isDeep == false) {
				return moList;
			}

			List<String> moClassList = new ArrayList<String>();
			for (Mo mo : moList) {
				if (moClassList.contains(mo.getMoClass()) == false) {
					moClassList.add(mo.getMoClass());
				}
			}

			parameters.remove("moClass");

			List<Mo> entry;
			moList.clear();
			for (String moClass : moClassList) {
				entry = (List<Mo>) tran.select(toDboClass(moClass), parameters, getMoJavaClass(moClass));
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

	@Override
	protected List<Model> doSelectModelList() throws Exception {
		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();
			List<FX_CF_MODEL> list = tran.select(FX_CF_MODEL.class, null);
			List<Model> ret = new ArrayList<Model>();
			for (FX_CF_MODEL a : list) {
				ret.add(new Model(a.getModelNo(), a.getModelName(), a.getDevType(), a.getVendorName()));
			}
			return ret;
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
		Mo parent = (Mo) children.getParent();

		try {
			tran.start();

			for (Mo mo : children.getMoListAll()) {
				if (mo.getStatus() == FxEvent.STATUS.deleted) {
					tran.deleteOfClass(toDboClass(mo.getMoClass()), mo, null);
				}
			}

			for (Mo mo : children.getMoListAll()) {
				if (mo.getStatus() == FxEvent.STATUS.changed) {
					tran.updateOfClass(toDboClass(mo.getMoClass()), mo, null);
				}
			}

			for (Mo mo : children.getMoListAll()) {

				if (mo.getStatus() == FxEvent.STATUS.added) {

					mo.setMoNo(tran.getNextVal(FX_MO.FX_SEQ_MONO, Long.class));

					if (parent instanceof IsSync && mo instanceof IsRegChg) {
						IsRegChg v = (IsRegChg) mo;
						v.setRegDate(((IsSync) parent).getSyncDate());
						v.setRegUserNo(((IsSync) parent).getSyncUserNo());
					}
					if (parent instanceof IsSync && mo instanceof IsSync) {
						IsSync v = (IsSync) mo;
						v.setSyncDate(((IsSync) parent).getSyncDate());
						v.setSyncUserNo(((IsSync) parent).getSyncUserNo());
					}

					tran.insertOfClass(toDboClass(mo.getMoClass()), mo);
					tran.insertOfClass(FX_HST_MO.class, getHstMo(mo, true, User.USER_NO_SYSTEM, "add-upper"));
				}
			}

			tran.updateOfClass(toDboClass(parent.getMoClass()), parent, null);

			if (children.getParent() instanceof HasAddableData) {
				List<Object> objList = ((HasAddableData) children.getParent()).getAddableDatas();
				if (objList != null) {
					for (Object obj : objList) {
						try {
							tran.updateOfClass(obj.getClass(), obj, null);
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
	protected Mo doUpdate(Mo mo, Map<String, Object> newAttr) throws Exception {

		Map<String, String> attrKeyMap = getAttrKeyMap();
		List<DiffData> diffList = new Differ(attrKeyMap == null ? null : attrKeyMap.keySet()).diff(mo, newAttr);

		FxDaoExecutor tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createFxDao();

		try {
			tran.start();

			tran.updateOfClass(toDboClass(mo.getMoClass()), newAttr);

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

			if (mo instanceof IsRegChg) {
				((IsRegChg) mo).setChgDate(FxApi.getDate(0));
			}

			tran.updateOfClass(toDboClass(mo.getMoClass()), mo, null);

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

	protected Class<?> toDboClass(String moClass) throws Exception {
		Class<?> ret = getDboJavaClass(moClass);
		if (ret == null) {
			throw new Exception("Not Found Dbo-java-class for " + moClass);
		}
		return ret;
	}
}
