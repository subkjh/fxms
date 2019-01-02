package com.fxms.bio.dao;

import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.database.DataBase;

public class BioDao {

	public static void main(String[] args) {
		BioDao dao = new BioDao();
		try {
			List<Map<String, Object>> mapList = dao.selectSensorList(null);
			for ( Map<String, Object> map : mapList) {
				System.out.println(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DbTrans tran;

	public BioDao() {

		DataBase database = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG);
		try {
			tran = database.createDbTrans("deploy/conf/sql/bio/bio-handler.xml");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectContainerList(Map<String, Object> para) throws Exception {
		return tran.selectQid2Res("SELECT_CONTAINER_LIST", para);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectSensorList(Map<String, Object> para) throws Exception {
		return tran.selectQid2Res("SELECT_SENSOR_LIST", para);
	}
}
