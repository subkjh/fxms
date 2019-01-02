package com.daims.dfc.filter.config.std;

import java.util.List;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.notification.beans.NotiBean;
import subkjh.service.services.ServiceImpl;

import com.daims.dfc.common.mo.MoDataBase;
import com.daims.dfc.common.mo.MoDbSpace;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.module.db.DbInfoMgr;

/**
 * 데이터베이스의 테이블 스페이스를 가져오는 필터<br>
 * oracle, informix만 구현되어 있음.
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterDatabase extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7992045819214032909L;

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		ServiceImpl.logger.debug(configMo);

		List<MoDataBase> dbList = configMo.getMoList(MoDataBase.class);
		if (dbList == null || dbList.size() == 0) return Ret.OK;

		List<MoDbSpace> spaceList;
		int countTotal = 0;

		DbInfoMgr dbinfo = new DbInfoMgr();

		for (MoDataBase database : dbList) {

			spaceList = dbinfo.findTsList(configMo.getNode(), database);
			if (spaceList != null) {
				configMo.setStatusAllChildren(NotiBean.BEAN_STATUS_NOTHING, NotiBean.BEAN_STATUS_DELETE,
						MoDbSpace.MO_CLASS);
				configMo.addMoListDetected(spaceList);
			}

			if (spaceList != null) countTotal += spaceList.size();

		}

		return new Ret(countTotal);
	}

	@Override
	public String[] getMoClassContains() {
		return new String[] { MoDbSpace.MO_CLASS };
	}

	@Override
	protected String getOidToCheck() {
		return null;
	}

}
