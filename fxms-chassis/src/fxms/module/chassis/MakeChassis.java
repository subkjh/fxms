package fxms.module.chassis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.fxms.nms.mo.NeMo;

import fxms.bas.alarm.dbo.Alarm;
import fxms.bas.api.EventApi;
import fxms.bas.api.MoApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.mo.Mo;
import fxms.bas.mo.child.MoConfig;
import fxms.module.chassis.beans.Chassis;
import fxms.module.chassis.beans.ChassisDef;
import fxms.module.chassis.beans.ChassisItem;
import fxms.module.chassis.beans.MoAttr;
import fxms.module.chassis.beans._ChassisDef;
import subkjh.bas.lang.Lang;
import subkjh.bas.log.LOG_LEVEL;
import subkjh.bas.log.Logger;
import subkjh.bas.utils.FileUtil;

/**
 * 실장도 API<br>
 * deploy/conf/chassis/chassis.xml 정보를 읽어 관리대상에 맞는 실장도를 만들어 줍니다.
 * 
 * @author subkjh
 * 
 */
public class MakeChassis {

	/**
	 * 모델이 실장도를 가지고 있는지 여부를 조회합니다.
	 * 
	 * @param modelName
	 *            모델명
	 * @return 실장도 존재 여부
	 */
	public static boolean existChassis(String modelName) {
		File folderXml = getFolder();
		File file = new File(folderXml + File.separator + modelName + ".xml");
		return file.exists();
	}

	public static void main(String[] args) throws Exception {
		FxCfg.setHome("../fxms-all");
		Logger.logger.setLevel(LOG_LEVEL.debug);

		// CLogger.logger.setLevel(LEVEL.debug);
		MakeChassis api = new MakeChassis();

		/**
		 * <HP10508> 10.100.201.1 - 1002642, 10.100.201.2 - 1004079 <HP5500-24G-SFP-HI>
		 * 10.100.201.13 - 1005963, 10.100.201.14 - 1006046 <HP5900AF-48XG-4QSFP+>
		 * 10.100.201.3 - 1004847, 10.100.201.4 - 1005018 <HP5820AF-24XG> 10.100.201.9 -
		 * 1005188, 10.100.201.10 - 1005275 <HP4400-48G-4SFP-HI> 10.100.201.5 - 1005362,
		 * 10.100.201.6 - 1005524, 10.100.201.7 - 1005674, 10.100.201.8 - 1005819
		 * <HP7506> 10.100.201.113 - 1004438
		 * 
		 */
		// 1006251 B-102-1-6390 TFOM-FM700
		// 1001524 10-0-1-6390 TFOM-S5500
		// 1006281 20-2-1-6390 TFOM-S520
		// 1006280 20-3-1-6390 TFOM-S542
		// 1001515 20-3-3-6390 TFOM-S542

		long moNo = 1006280;

		// 1000005; // 1000149, 1000690, 1000691, 1000005, 1000149,
		// // 1000102, 1000205, 1000641,

		MoConfig configMo = MoApi.getApi().getMoConfig( MoApi.getApi().getMo(moNo));
		Chassis chassis = api.getChassis(configMo);
		chassis.print("");
		FileUtil.writeToFile("datas/images/chassis.HTML", chassis.getHtml(), false);
	}

	private static File getFolder() {
		return new File(FxCfg.getHomeDeployConf() + File.separator + "chassis");
	}

	private final String FILE_CHASSIS_DEFAULT = "_nprism_chassis.xml";
	//private final String FILE_CHASSIS_UNKNOWN = "_nprism_unknown.xml";

	private List<ChassisDef> chassisList;

	/**
	 * 
	 */
	public MakeChassis() {

	}

	/**
	 * 실장도 그릴 정보를 제공합니다.
	 * 
	 * @param configMo
	 *            구성정보
	 * @return 실장도 그릴 내용
	 */
	public Chassis getChassis(MoConfig configMo) {

		Chassis chassis;
		NeMo ne = (NeMo) configMo.getParent();

		try {
			setAlarmLevel(configMo);
//			chassisList = parse(ne.getModelName());
		} catch (Exception e) {
			FxServiceImpl.logger.error(e);
			return null;
		}

		ChassisDef def = findChassisDef(configMo.getParent());
		if (def == null)
			return null;

		chassis = new Chassis();
		chassis.setImg(def.getImg());
		chassis.setMo(configMo.getParent());
		chassis.setName(def.getName());
		chassis.setWidth(def.getWidth());
		chassis.setHeight(def.getHeight());

		List<Mo> moList = (List<Mo>) configMo.getMoListAll();
		if (moList == null) {
			moList = new ArrayList<Mo>();
		}

		makeChassis(def, chassis, configMo.getParent(), moList);

		replaceText(chassis, ne);

		// try {
		// FileUtil.writeObjectToFile(getFileName4Chassis(configMo.getNode().getMoNo()),
		// chassis);
		// } catch (Exception e) {
		// FxServiceImpl.logger.fail(e.getMessage());
		// }

		return chassis;
	}

	/**
	 * 현재 가지고 있는 경보등급을 장비, 하위까지 설정합니다.
	 * 
	 * @param configMo
	 * @param escalate
	 *            하위 경보를 상위까지 반영할지 여부
	 */
	private void setAlarmLevel(MoConfig children) {

		List<Alarm> alarmList = EventApi.getApi().getAlarmList4Mo(children.getParent().getMoNo());

		if (alarmList != null) {

			Mo parent = children.getParent();
			Mo mo;

			for (Alarm alarm : alarmList) {

				// 노드 경보
				if (alarm.getMoNo() == parent.getMoNo()) {
					if (alarm.getAlarmLevel() < parent.getAlarmLevel()) {
						parent.setAlarmLevel(alarm.getAlarmLevel());
					}
				}

				// 하위 경보이면
				if (alarm.getMoNo() != parent.getMoNo()) {

					mo = children.getChild(alarm.getMoNo());
					if (mo != null) {

						if (alarm.getAlarmLevel() < mo.getAlarmLevel()) {
							mo.setAlarmLevel(alarm.getAlarmLevel());
						}

					}
				}
			}
		}
	}

	/**
	 * 관리대상에 해당되는 실장도정의정보를 찾습니다.
	 * 
	 * @param mo
	 *            관리대상번호
	 * @return 실장도정의정보
	 */
	private ChassisDef findChassisDef(Mo mo) {
		for (ChassisDef def : chassisList) {
			if (def.match(null, mo))
				return def;
		}
		return null;
	}

	/**
	 * MO에 해당되는 Chassis를 찾아 그 이름을 제공합니다.
	 * 
	 * @param moList
	 * @return 실장도정의명
	 */
	private String findItemName(ChassisItem item, Mo parent, List<Mo> moList) {

		if (moList == null)
			return null;

		for (Mo mo : moList) {

			if (item.match(parent, mo)) {

				if (item.getImgGroup() != null && item.getImgGroup().length() > 0) {
					mo.getProperties().put("imgGroup", item.getImgGroup());
				}

				for (ChassisDef def : chassisList) {
					if (def.match(null, mo))
						return def.getName();
				}

			}
		}
		return null;
	}

	private ChassisDef getChassisDef(String name) {
		for (ChassisDef def : chassisList) {
			if (def.getName().equals(name))
				return def;
		}

		return null;
	}

	/**
	 * 실장도의 내부를 채웁니다.
	 * 
	 * @param def
	 *            실장도정의내역
	 * @param chassis
	 *            실장도그릴내역
	 * @param moList
	 *            관리대상목록
	 */
	private void makeChassis(ChassisDef def, Chassis chassis, Mo parent, List<Mo> moList) {

		if (def.getItemList() == null)
			return;

		ChassisDef defChild;
		String name;

		for (ChassisItem item : def.getItemList()) {

			if (item.isText()) {
				Chassis chassisChild = new Chassis();
				chassisChild.setImg(null);
				chassisChild.setName(null);
				chassisChild.setX(item.getX());
				chassisChild.setY(item.getY());
				chassisChild.setText(item.getText());
				chassis.getChildren().add(chassisChild);
			} else {

				name = findItemName(item, parent, moList);

				if (name == null) {
					name = item.getName();
				}

				if (name == null) {
					continue;
				}

				defChild = getChassisDef(name);
				if (defChild == null) {
					FxServiceImpl.logger.fail("ChassisDef not found : " + name);
					continue;
				}

				Chassis chassisChild = new Chassis();
				chassisChild.setImg(defChild.getImg());
				chassisChild.setName(defChild.getName());
				chassisChild.setX(item.getX());
				chassisChild.setY(item.getY());
				chassisChild.setText(item.getText());

				// 크기 지정 ( item -> def 순 )
				if (item.getWidth() > 0) {
					chassisChild.setWidth(item.getWidth());
					chassisChild.setHeight(item.getHeight());
				} else if (defChild.getWidth() > 0) {
					chassisChild.setWidth(defChild.getWidth());
					chassisChild.setHeight(defChild.getHeight());
				}

				chassisChild.setRotate(item.getRorate());
				chassisChild.setTag(item.getTag());

				if (item.hasAttr() == false) {
					chassis.getChildren().add(chassisChild);
					makeChassis(defChild, chassisChild, null, moList);
				} else {
					Mo parentNew = null;
					for (Mo mo : moList) {
						if (item.match(parent, (Mo) mo)) {
							chassisChild.setMo(mo);
							parentNew = mo;
							break;
						}
					}

					chassis.getChildren().add(chassisChild);

					makeChassis(defChild, chassisChild, parentNew, moList);

				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void parse(File fileXml, List<ChassisDef> list) throws Exception {

		if (fileXml.exists() == false) {
			throw new Exception("File Not Found : " + "(" + fileXml.getPath() + ")");
		}

		if (FxServiceImpl.logger.isTrace()) {
			FxServiceImpl.logger.trace(fileXml.getPath());
		}

		SAXBuilder builder = new SAXBuilder();

		Document document = null;
		try {
			document = builder.build(new FileInputStream(fileXml));
		} catch (Exception e) {
			throw e;
		}

		Element root = document.getRootElement();
		List<Element> children = root.getChildren();
		List<Element> grandChildren = root.getChildren();
		ChassisDef chassisDef;
		ChassisItem chassisItem;

		for (Element child : children) {

			if (child.getName().equals("chassis_def") == false)
				continue;

			chassisDef = new ChassisDef(child.getAttributeValue("name")//
					, child.getAttributeValue("img") //
					, child.getAttributeValue("width") //
					, child.getAttributeValue("height"));

			if (list.contains(chassisDef)) {
				FxServiceImpl.logger.fail("Chassis Def is dup [" + chassisDef.getName() + "]");
				continue;
			}

			list.add(chassisDef);

			setAttr(child, chassisDef);

			grandChildren = child.getChildren();

			for (Element grand : grandChildren) {

				if (grand.getName().equals("item")) {

					chassisItem = new ChassisItem(grand.getAttributeValue("name") //
							, grand.getAttributeValue("x") //
							, grand.getAttributeValue("y") //
							, grand.getAttributeValue("rotate")//
							, grand.getAttributeValue("tag") //
							, grand.getAttributeValue("width") //
							, grand.getAttributeValue("height") //
							, grand.getAttributeValue("text") //
							, grand.getAttributeValue("imgGroup") //
					);

					try {
						if (grand.getAttributeValue("moClass") != null) {
							MoAttr moAttr = new MoAttr(grand.getAttributeValue("moClass"),
									grand.getAttributeValue("field"), grand.getAttributeValue("method"),
									grand.getAttributeValue("value"), grand.getAttributeValue("result"));
							chassisItem.addMoAttr(moAttr);
						}
					} catch (Exception e) {
						FxServiceImpl.logger.error(e);
					}

					setAttr(grand, chassisItem);

					chassisDef.add(chassisItem);
				}
			}

			if (FxServiceImpl.logger.isTrace()) {
				FxServiceImpl.logger.trace(String.valueOf(chassisDef));
			}
		}
	}

	List<ChassisDef> parse(String modelName) throws Exception {

		File folderXml = getFolder();

		List<ChassisDef> list = new ArrayList<ChassisDef>();

		if (folderXml.exists() == false) {
			throw new Exception("File Not Found : " + "(" + folderXml.getPath() + ")");
		}

		File file = new File(folderXml + File.separator + modelName + ".xml");

		if (file.exists()) {
			parse(file, list);
		} else {
			throw new Exception(Lang.get("모델에 대한 실장도가 정의되지 않았습니다.") + " : " + modelName);
			// parse(new File(folderXml + File.separator +
			// FILE_CHASSIS_UNKNOWN), list);
		}

		parse(new File(folderXml + File.separator + FILE_CHASSIS_DEFAULT), list);

		return list;
	}

	/**
	 * 장비의 필드로 TEXT를 변경합니다.
	 * 
	 * @param chassis
	 * @param node
	 */
	private void replaceText(Chassis chassis, NeMo node) {

		if (chassis.isText()) {
			Object val;
			String s = chassis.getText();
			List<String> varList = getVarList4Percent(s);
			if (varList != null) {
				for (String var : varList) {
					try {
						val = Mo.getValue(node, var);
						if (val != null) {
							s = s.replaceAll("%" + var + "%", val.toString());
						}
					} catch (Exception e) {
					}
				}
			}
			chassis.setText(s);
		}

		if (chassis.getChildren() != null) {
			for (Chassis c : chassis.getChildren()) {
				replaceText(c, node);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void setAttr(Element element, _ChassisDef def) {
		List<Element> children = element.getChildren();

		for (Element child : children) {
			if (child.getName().equals("attr")) {
				try {
					MoAttr moAttr = new MoAttr(child.getAttributeValue("moClass"), child.getAttributeValue("field"),
							child.getAttributeValue("method"), child.getAttributeValue("value"),
							child.getAttributeValue("result"));
					def.addMoAttr(moAttr);
				} catch (Exception e) {
					FxServiceImpl.logger.error(e);
				}
			}
		}
	}

	private List<String> getVarList4Percent(String s) {
		char chs[] = s.toCharArray();
		String var = null;
		List<String> list = null;

		for (char ch : chs) {
			if (ch == '%') {
				if (var == null) var = "";
				else {
					if (var != null && var.trim().length() > 0) {
						if (list == null) {
							list = new ArrayList<String>();
						}
						list.add(var);
					}
					var = null;
				}
				continue;
			}
			if (var != null) var += ch;
		}

		return list;

	}
}
