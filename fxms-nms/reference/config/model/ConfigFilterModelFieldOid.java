package com.daims.dfc.filter.config.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.exception.NotFoundException;
import subkjh.exception.TimeoutException;
import subkjh.log.Ret;
import subkjh.service.services.ServiceImpl;
import subkjh.utils.FileUtil;

import com.daims.dfc.PATH;
import com.daims.dfc.common.mo.MoNode;
import com.daims.dfc.filter.config.ConfigFilterSnmpNode;
import com.daims.dfc.filter.config.ConfigMo;
import com.daims.dfc.lib.snmp.beans.OidValue;
import com.daims.dfc.lib.snmp.exception.SnmpTimeoutException;
import com.daims.dfc.service.conf.ConfApi;
import com.daims.dfc.service.conf.beans.NodeFieldOid;

/**
 * 장비모델별 속성OID 설정내용으로 필터 설정
 * 
 * @author subkjh
 * 
 */
public class ConfigFilterModelFieldOid extends ConfigFilterSnmpNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4565050338017976099L;

	private final String PATTERN = "( |\t)+";

	/** key : 모델명, value : { OID, fieldName } */
	private static Map<String, List<String[]>> oidMap = null;

	// public static void main(String[] args) throws Exception {
	//
	// try {
	// DaoFactory.getInstance().addDataBase(new
	// File("deploy/conf/databases.xml"));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// CLogger.logger.setLevel(LEVEL.raw);
	// MoNodeSnmpImpl node = ConfApi.makeNodeSnmp("167.1.21.97", 161, 1,
	// "public", MoNodeSnmpImpl.class);
	//
	// node.setModelNo(1046);
	//
	// ConfigFilterModelFieldOid filter = new ConfigFilterModelFieldOid();
	// ConfigMo configMo = new ConfigMo(node);
	//
	// filter.filter(configMo);
	//
	// System.out.println(CClass.toString(node));
	// }

	@Override
	public Ret filter(ConfigMo configMo, String moClassArr[], String moName) throws TimeoutException,
			NotFoundException, Exception {

		MoNode node = getSnmpNode(configMo);
		if (node == null) return Ret.OK;

		try {
			getValueWithApi(node);
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getClass().getSimpleName(), e.getMessage());
		}

		try {
			getValueWithFile(node);
		}
		catch (Exception e) {
			ServiceImpl.logger.fail(e.getClass().getSimpleName(), e.getMessage());
		}

		return Ret.OK;
	}

	@Override
	public String[] getMoClassContains() {
		return null;
	}

	@Override
	protected String getOidToCheck() {
		return null;
	}

	private synchronized List<String[]> getOid(String modelName) throws Exception {

		if (oidMap == null) {

			oidMap = new HashMap<String, List<String[]>>();

			String filename = PATH.getFileFilterConfigMsf();
			
			File file = new File(filename);
			if (file.exists() == false) return null;

			List<String> lineList = FileUtil.getLines(file);
			String no[];
			List<String[]> e;

			for (String line : lineList) {
				if (line == null || line.trim().length() == 0) continue;
				if (line.charAt(0) == '#') continue;
				no = line.split(PATTERN);

				if (no.length >= 3) {
					if (no[1].charAt(0) != '.') no[1] = "." + no[1];
					e = oidMap.get(no[0]);
					if (e == null) {
						e = new ArrayList<String[]>();
						oidMap.put(no[0], e);
					}
					e.add(new String[] { no[1], no[2] });
					ServiceImpl.logger.debug("model oid add : " + no[0] + "." + no[2] + " (" + no[1] + ")");
				}

			}

		}

		return oidMap.get(modelName);
	}

	private String getSnmpValue(MoNode node, String oid) throws Exception {
		String[] ss = oid.split("\\.");
		if (ss[(ss.length - 1)].equals("0")) {
			try {
				OidValue ovArr[] = snmpget(node, new String[] { oid });
				if (ovArr.length > 0) return ovArr[0].getValue();
			}
			catch (SnmpTimeoutException e) {
				throw e;
			}
			catch (Exception e) {
				ServiceImpl.logger.debug("OID 확인 실패 " + e.getMessage(), new Class[0]);
				return null;
			}
		}
		else {
			try {
				List<OidValue> ovList = snmpwalk(node, oid);
				for (OidValue ov : ovList) {
					if (ov != null && ov.getValue() != null && ov.getValue().trim().length() > 0) {
						return ov.getValue().trim();
					}
				}
			}
			catch (SnmpTimeoutException e) {
				throw e;
			}
			catch (Exception e) {
				ServiceImpl.logger.debug("OID 확인 실패 " + e.getMessage(), new Class[0]);
				return null;
			}

		}

		return null;

	}

	private void getValueWithApi(MoNode node) throws Exception {

		List<NodeFieldOid> varList = ConfApi.getApi().getNodeModelFieldOid(node);

		if (ServiceImpl.logger.isTrace()) {
			ServiceImpl.logger.trace(node.getMoName() + "|size=" + varList.size());
		}

		if (varList == null || varList.size() == 0) return;

		List<String> oidList = new ArrayList<String>();
		for (NodeFieldOid oid : varList) {
			if (oid.isSnmpOid()) {
				oidList.add(oid.getSnmpOid());
			}
			else {
				// SNMP OID가 아닌 경우 해당 내용을 설정합니다.
				try {
					node.set(oid.getJavaFieldName(), oid.getSnmpOid());
				}
				catch (Exception e) {
					ServiceImpl.logger.error(e);
				}
			}
		}

		// 1. SNMP내용 설정
		if (oidList.size() > 0) {

			OidValue ovArr[] = snmpget(node, oidList.toArray(new String[oidList.size()]));
			OidValue value;

			for (NodeFieldOid var : varList) {
				value = null;
				for (OidValue oidValue : ovArr) {
					if (oidValue.getOid().equals(var.getSnmpOid())) {
						value = oidValue;
						break;
					}
				}

				if (value != null) {
					try {
						if (ServiceImpl.logger.isTrace()) {
							ServiceImpl.logger.trace(var.getJavaFieldName() + "=" + var.getValue(value.getValue()));
						}
						node.set(var.getJavaFieldName(), var.getValue(value.getValue()));
					}
					catch (Exception e) {
						ServiceImpl.logger.error(e);
					}
				}
			}
		}

	}

	private void getValueWithFile(MoNode node) throws Exception {
		try {
			String value;
			List<String[]> oidList = getOid(node.getModelName());
			if (oidList != null) {
				for (String[] of : oidList) {
					value = getSnmpValue(node, of[0]);
					if (value != null) {
						node.set(of[1], value);
					}
				}
			}
		}
		catch (Exception e) {
			ServiceImpl.logger.error(e);
		}
	}

}
