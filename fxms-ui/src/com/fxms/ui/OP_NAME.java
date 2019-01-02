package com.fxms.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum OP_NAME {
	
	/** 1000) Managed Object */
	Mo("mo")

	/** 1001) Show Managed Objects */
	, MoList("mo-list")

	/** 1011) 컨테이너 목록 */
	, MoContainerList("mo-container-list")

	/** 1021) 게이트웨이 목록 */
	, MoGwList("mo-gw-list")

	/** 1031) PBR 목록 */
	, MoPbrList("mo-pbr-list")

	/** 1041) 센서 목록 */
	, MoSensorList("mo-sensor-list")

	/** 1051) 장비 목록 */
	, MoNeList("mo-ne-list")

	/** 1301) Show Mo Detail */
	, MoDetailShow("mo-detail-show")

	/** 1311) Show Container Detail */
	, MoContainerDetailShow("mo-container-detail-show")

	/** 1321) Show Gateway Detail */
	, MoGwDetailShow("mo-gw-detail-show")

	/** 1331) Show PBR Detail */
	, MoPbrDetailShow("mo-pbr-detail-show")

	/** 1341) 네트워크 장비 상태 조회 */
	, MoNeStatusShow("mo-ne-status-show")

	/** 1351) 관리대상 구성내역 조회 */
	, MoTreeShow("mo-tree-show")

	/** 1361) Show Status Chart */
	, MoChartShow("mo-chart-show")

	/** 1371) Show Network Interface */
	, MoIfDetailShow("mo-if-detail-show")

	/** 1501) 관리대상 운용이력 조회 */
	, MoOpHstList("mo-op-hst-list")

	/** 1003) 관리대상 등록 */
	, MoAdd("mo-add")

	/** 1013) 컨테이너 등록 */
	, MoContainerAdd("mo-container-add")

	/** 1023) 게이트웨이 등록 */
	, MoGwAdd("mo-gw-add")

	/** 1033) PBR 등록 */
	, MoPbrAdd("mo-pbr-add")

	/** 1043) 네트워크 장비 등록 */
	, MoNeAdd("mo-ne-add")

	, MoUpdate("mo-update")

	/** 1014) 관리여부 변경 */
	, MoAttrMngUpdate("mo-attr-mng-update")

	/** 1024) 경보조건 변경 */
	, MoAttrAlarmCfgUpdate("mo-attr-alarm-cfg-update")

	/** 1034) 화면표시명 변경 */
	, MoAttrAliasUpdate("mo-attr-alias-update")

	/** 1044) 컨테이너 수정 */
	, MoContainerUpdate("mo-container-update")

	/** 1054) 게이트웨이 수정 */
	, MoGwUpdate("mo-gw-update")

	/** 1064) PBR 수정 */
	, MoPbrUpdate("mo-pbr-update")

	/** 1074) 네트워크 장비 수정 */
	, MoNeUpdate("mo-ne-update")

	/** 1084) 네트워크 인터페이스 수정 */
	, MoIfUpdate("mo-if-update")

	/** 1094) 디바이스 수정 */
	, MoDeviceUpdate("mo-device-update")

	, MoDelete("mo-delete")

	/** 1015) 컨테이너 삭제 */
	, MoContainerDelete("mo-container-delete")

	/** 1025) 게이트웨이 삭제 */
	, MoGwDelete("mo-gw-delete")

	/** 1035) PBR 삭제 */
	, MoPbrDelete("mo-pbr-delete")

	/** 1045) 네트워크 장비 삭제 */
	, MoNeDelete("mo-ne-delete")

	/** 1010) 컨테이너 관리 */
	, MoContainer("mo-container")

	/** 1020) PBR 관리 */
	, MoPbr("mo-pbr")

	/** 1030) GW 관리 */
	, MoGw("mo-gw")

	/** 1040) 센서 관리 */
	, MoSensor("mo-sensor")

	/** 1050) 장비 관리 */
	, MoNe("mo-ne")

	/** 1401) 네트워크장비 구성 그리드 */
	, MoChlidGridNe("mo-chlid-grid-ne")

	/** 1411) 컨테이너 구성 그리드 */
	, MoChildGridContainer("mo-child-grid-container")

	/** 1421) MO 구성 그리드 */
	, MoChildGrid("mo-child-grid")

	/** 2000) 메뉴 */
	, Dx("dx")

	/** 2901) 경보 */
	, DxGroupAlarm("dx-group-alarm")

	/** 2902) 구성 */
	, DxGroupConfig("dx-group-config")

	/** 2903) 개인 */
	, DxGroupPrivate("dx-group-private")

	/** 2904) 운용 */
	, DxGroupOperation("dx-group-operation")

	/** 2905) 성능 */
	, DxGroupPs("dx-group-ps")

	/** 2906) 기타 */
	, DxGroupEtc("dx-group-etc")

	/** 2001) 화면 배치 편집 */
	, DxEditorUi("dx-editor-ui")

	/** 2101) 다이아그램 */
	, DxDiagram("dx-diagram")

	/** 2102) 알람카운트챠트 */
	, DxAlarmCountChart("dx-alarm-count-chart")

	/** 2103) 성능조회 */
	, DxViewPs("dx-view-ps")

	/** 2104) 현재경보리스트 */
	, DxAlarmCurList("dx-alarm-cur-list")

	/** 2105) 현재경보박스 */
	, DxAlarmCurBox("dx-alarm-cur-box")

	/** 2106) 경보건수 */
	, DxAlarmCountPie("dx-alarm-count-pie")

	/** 2107) 실시간연결상태 */
	, DxLinkStatus("dx-link-status")

	/** 2108) 상태조회 */
	, DxPsStatus("dx-ps-status")

	/** 2201) MO컨테이너관리 */
	, DxMenuContainers("dx-menu-containers")

	/** 2202) MO센서관리 */
	, DxMenuSensors("dx-menu-sensors")

	/** 2203) MO게이트웨이관리 */
	, DxMenuGateways("dx-menu-gateways")

	/** 2204) MOPBR관리 */
	, DxMenuPbr("dx-menu-pbr")

	/** 2205) 운용자로그인이력조회 */
	, DxMenuLoginHst("dx-menu-login-hst")

	/** 2206) 운용자운용이력조회 */
	, DxMenuUserOpHst("dx-menu-user-op-hst")

	/** 2207) 운용자관리 */
	, DxMenuUser("dx-menu-user")

	/** 2208) 웹 화면 열기 */
	, DxMenuWeb("dx-menu-web")

	/** 2209) 설치위치(Tree) */
	, DxMenuLocation("dx-menu-location")

	/** 2210) 암호변경 */
	, DxMenuMyAccount("dx-menu-my-account")

	/** 2211) 관리대상 트리 */
	, DxMenuMoTree("dx-menu-mo-tree")

	/** 2212) 경보조건관리 */
	, DxMenuAlarmCfg("dx-menu-alarm-cfg")

	/** 2213) 경보조회 */
	, DxMenuAlarmList("dx-menu-alarm-list")

	/** 2214) 상태이력챠트조회 */
	, DxMenuPsView("dx-menu-ps-view")

	/** 2215) 관리,대상,변경,이력 */
	, DxMenuMoOpHst("dx-menu-mo-op-hst")

	/** 2216) 다이아그램편집 */
	, DxMenuDiagramEditor("dx-menu-diagram-editor")

	/** 2217) 내정보 수정 */
	, DxMenuMyInfo("dx-menu-my-info")

	/** 2218) MO장비관리 */
	, DxMenuNe("dx-menu-ne")

	/** 2219) 시간 */
	, DxMenuClock("dx-menu-clock")

	/** 2220) 현재경보건수 */
	, DxMenuAlarmCount("dx-menu-alarm-count")

	/** 2221) 현재경보챠트 */
	, DxMenuAlarmChart("dx-menu-alarm-chart")

	/** 2222) 운용자트리 */
	, DxMenuFxTree("dx-menu-fx-tree")

	/** 2223) 데이터관리자 */
	, DxMenuDataManager("dx-menu-data-manager")

	/** 2224) 이벤트 발생 흐름 */
	, DxMenuEventMonitor("dx-menu-event-monitor")

	/** 2225) 이벤트 발생 표시 */
	, DxMenuEventMonitor2("dx-menu-event-monitor2")

	/** 2226) 내 트리 보기 */
	, DxMenuTreeShow("dx-menu-tree-show")

	/** 2227) 내 트리 편집 */
	, DxMenuTreeEdit("dx-menu-tree-edit")

	/** 2228) 단순 문자열 */
	, DxMenuText("dx-menu-text")

	/** 2229) 이미지 */
	, DxMenuImage("dx-menu-image")

	/** 2230) 설치위치(리스트) */
	, DxMenuLocationList("dx-menu-location-list")

	/** 2231) 코드리로딩 */
	, DxMenuReloadCode("dx-menu-reload-code")

	/** 2232) UI로그 */
	, DxMenuViewUiLog("dx-menu-view-ui-log")

	/** 2234) 관리대상 모델 관리 */
	, DxMenuModelList("dx-menu-model-list")

	/** 2235) 코드 관리 */
	, DxMenuCode("dx-menu-code")

	/** 2236) 실시간 성능 조회 */
	, DxMenuRtpsView("dx-menu-rtps-view")

	/** 2237) 기능권한관리 */
	, DxMenuOpTree("dx-menu-op-tree")

	/** 2238) 경보통계조회 */
	, DxMenuAl_st("dx-menu-al_st")

	/** 2239) 시스템시간 */
	, DxSystemTime("dx-system-time")

	/** 2240) 네트워크관리 */
	, DxMenuNetworkList("dx-menu-network-list")

	/** 3000) 경보관리 */
	, Alarm("alarm")

	/** 3001) 경보조건 관리 */
	, AlarmCfgList("alarm-cfg-list")

	/** 3011) 경보이력 조회 */
	, AlarmHstList("alarm-hst-list")

	/** 3021) 경보 상세 조회 */
	, AlarmDetailShow("alarm-detail-show")

	/** 3031) 경보조건 적용 관리대상 조회 */
	, AlarmCfgAppliedMoList("alarm-cfg-applied-mo-list")

	/** 3041) 경보조건 항목 조회 */
	, AlarmCfgMemList("alarm-cfg-mem-list")

	/** 3003) 새로운 경보조건 등록 */
	, AlarmCfgAdd("alarm-cfg-add")

	/** 3013) 경보조건 항목 추가 */
	, AlarmCfgMemAdd("alarm-cfg-mem-add")

	/** 3023) 경보조건 복사 */
	, AlarmCfgCopy("alarm-cfg-copy")

	/** 3033) 기본정보등록 */
	, AlarmCfgInfoAdd("alarm-cfg-info-add")

	/** 3034) 기본정보수정 */
	, AlarmCfgInfoUpdate("alarm-cfg-info-update")

	/** 3004) 경보조건 수정 */
	, AlarmCfgUpdate("alarm-cfg-update")

	/** 3014) 경보 인지 */
	, AlarmAck("alarm-ack")

	/** 3024) 경보 해제 */
	, AlarmClear("alarm-clear")

	/** 3005) 경보조건 삭제 */
	, AlarmCfgDelete("alarm-cfg-delete")

	/** 3015) 경보조건 항목 삭제 */
	, AlarmCfgMemDelete("alarm-cfg-mem-delete")

	/** 3051) 최근 3일간 경보 발생 이력 */
	, AlarmHstMoList("alarm-hst-mo-list")

	/** 3101) 경보 통계 조회 */
	, AlarmStatList("alarm-stat-list")

	/** 3061) 경보 내역 */
	, AlarmList("alarm-list")

	, Location("location")

	/** 4001) Show Location List */
	, LocationListView("location-list-view")

	/** 4002) 설치위치 관리 */
	, LocationList("location-list")

	/** 4003) 설치위치 등록 */
	, LocationAdd("location-add")

	/** 4004) 설치위치 수정 */
	, LocationUpdate("location-update")

	/** 4005) 설치위치 삭제 */
	, LocationDelete("location-delete")

	/** 4011) 설치위치 트리 조회 */
	, LocationTreeView("location-tree-view")

	/** 4013) 회사(설치위치) 등록 */
	, LocationCompanyAdd("location-company-add")

	/** 4023) 공장(설치위치) 등록 */
	, LocationPlantAdd("location-plant-add")

	, Model("model")

	, ModelListView("model-list-view")

	/** 4111) 선택한 모델의 관리대상 조회 */
	, ModelMoList("model-mo-list")

	/** 4102) 관리대상 모델 관리 */
	, ModelListSelect("model-list-select")

	, TestAlarmFire("test-alarm-fire")
	
	/** 4103) 관리대상 모델 등록 */
	, ModelAdd("model-add")

	/** 4104) 관리대상 모델 수정 */
	, ModelUpdate("model-update")

	/** 4105) 관리대상 모델 삭제 */
	, ModelDelete("model-delete")

	/** 4200) 네트워크관리 */
	, Network("network")

	/** 4201) 네트워크관리 */
	, NetworkListView("network-list-view")

	/** 4202) 네트워크조회 */
	, NetworkListSelect("network-list-select")

	/** 4203) 네트워크추가 */
	, NetworkAdd("network-add")

	/** 4204) 네트워크수정 */
	, NetworkUpdate("network-update")

	/** 4205) 네트워크삭제 */
	, NetworkDelete("network-delete")

	/** 4214) 네트워크구성설정 */
	, NetworkItemSet("network-item-set")

	/** 5000) 성능 */
	, Ps("ps")

	/** 5001) Show PS Items */
	, PsItemList("ps-item-list")

	/** 5002) Add New PS Item */
	, PsItemAdd("ps-item-add")

	/** 5003) Update PS Item */
	, PsItemUpdate("ps-item-update")

	/** 5004) Delete PS Item */
	, PsItemDelete("ps-item-delete")

	/** 5101) Show PS Datas */
	, PsView("ps-view")

	/** 5102) Show PS Datas */
	, PsMoView("ps-mo-view")

	/** 5103) Show PS Datas */
	, PsMoIfView("ps-mo-if-view")

	/** 5200) 네트워크 트래픽 조회 */
	, PsNms("ps-nms")

	/** 5201) 트래픽 조회 */
	, PsNmsTrafficView("ps-nms-traffic-view")

	/** 5206) 트래픽 조회 */
	, DxNmsTrafficView("dx-nms-traffic-view")

	/** 6000) Diagram */
	, Diagram("diagram")

	/** 6002) 다이아그램목록조회 */
	, DiagramList("diagram-list")

	/** 6012) 다이아그램조회 */
	, DiagramGet("diagram-get")

	/** 6004) 다이아그램설정 */
	, DiagramSet("diagram-set")

	/** 6001) 다이아그램박스편집 */
	, DiagramBoxEdit("diagram-box-edit")

	/** 6011) 다이아그램텍스트편집 */
	, DiagramTextEdit("diagram-text-edit")

	/** 6021) 다이아그램MO편집 */
	, DiagramMoEdit("diagram-mo-edit")

	/** 6031) 다이아그램라인편집 */
	, DiagramLineEdit("diagram-line-edit")

	/** 6041) 다이아그램서브편집 */
	, DiagramSubEdit("diagram-sub-edit")

	/** 6051) 다이아그램설치위치 */
	, DiagramInloEdit("diagram-inlo-edit")

	/** 6061) 다이아그램상태편집 */
	, DiagramStatusEdit("diagram-status-edit")

	/** 6071) 다이아그램편집 */
	, DiagramEdit("diagram-edit")

	/** 6081) 다이아그램이미지편집 */
	, DiagramImageEdit("diagram-image-edit")

	/** 6091) 다이아그램네트워크편집 */
	, DiagramNetworkEdit("diagram-network-edit")

	/** 8000) 운용 */
	, Operation("operation")

	/** 8100) Users */
	, User("user")

	/** 8101) 운용자 관리 */
	, UserList("user-list")

	/** 8103) 새로운 운용자 등록 */
	, UserAdd("user-add")

	/** 8104) 선택 운용자 수정 */
	, UserUpdate("user-update")

	/** 8105) 선택 운용자 삭제 */
	, UserDelete("user-delete")

	/** 8114) 운용자 암호 변경 */
	, UserPwdChange("user-pwd-change")

	/** 8124) 내 정보 변경 */
	, UserMyChange("user-my-change")

	/** 8102) 운용자 설정 화면 조회 */
	, UserUiGet("user-ui-get")

	/** 8134) 운용자 화면 설정 */
	, UserUiSet("user-ui-set")

	/** 8111) 운용 이력 조회 */
	, UserLogList("user-log-list")

	/** 8121) 로그인 이력 조회 */
	, UserAlogList("user-alog-list")

	/** 8141) 운용 이력 조회 */
	, UserLogASessionList("user-log-a-session-list")

	/** 8144) 계정 신청 */
	, UserNewApply("user-new-apply")

	/** 8131) 계정 신청 목록 */
	, UserNewList("user-new-list")

	/** 8154) 신청 계정 승인 */
	, UserNewOk("user-new-ok")

	/** 8164) 신청 계정 반려 */
	, UserNewReject("user-new-reject")

	, UserGroup("user-group")

	/** 8201) 운용자 그룹 관리 */
	, UserGroupList("user-group-list")

	/** 8203) 새로운 운용자그룹 등록 */
	, UserGroupAdd("user-group-add")

	/** 8204) 운용자그룹 수정 */
	, UserGroupUpdate("user-group-update")

	/** 8205) 운용자그룹 삭제 */
	, UserGroupDel("user-group-del")

	, Tree("tree")

	/** 8303) 세부트리 추가 */
	, TreeAdd("tree-add")

	/** 8304) 운용자 트리 수정 */
	, TreeUpdate("tree-update")

	/** 8305) 운용자 트리 삭제 */
	, TreeDelete("tree-delete")

	/** 8302) 운용자 트리 목록 조회 */
	, TreeGetList("tree-get-list")

	/** 8312) 운용자 트리 내용 조회 */
	, TreeGetItems("tree-get-items")

	/** 8301) 운용자 트리 조건 화면 */
	, TreeAttrList("tree-attr-list")

	/** 8313) 운용자 트리 조건 추가 */
	, TreeAttrAddMo("tree-attr-add-mo")

	/** 8323) 운용자 트리 조건 추가 */
	, TreeAttrAddLocation("tree-attr-add-location")

	/** 8315) 운용자 트리 조건 삭제 */
	, TreeAttrDel("tree-attr-del")

	/** 8333) 그룹트리 추가 */
	, TreeTopAdd("tree-top-add")

	/** 8400) 운용코드 */
	, UserOp("user-op")

	/** 8401) 운용코드 트리 */
	, UserOpTree("user-op-tree")

	/** 8404) 운용코드 권한 변경 */
	, UserOpChange("user-op-change")

	/** 9000) System Configuration */
	, Etc("etc")

	/** 9001) Edit Snmp Configuration */
	, EtcSnmpConfigEdit("etc-snmp-config-edit")

	/** 9002) Open Web */
	, EtcWebOpen("etc-web-open")

	/** 9100) FX TABLE */
	, EtcTable("etc-table")

	/** 9101) Show Repositories */
	, EtcTableUi("etc-table-ui")

	/** 9111) Show Repository Attributes */
	, EtcTableColumnUi("etc-table-column-ui")

	/** 9121) Show Repository Keys */
	, EtcTableIndexUi("etc-table-index-ui")

	/** 9102) Get Repositories */
	, EtcTableGet("etc-table-get")

	/** 9103) Add New Repository */
	, EtcTableAdd("etc-table-add")

	/** 9113) Add New Repository Attribute */
	, EtcTableColumnAdd("etc-table-column-add")

	/** 9123) Add New Repository Key */
	, EtcTableIndexAdd("etc-table-index-add")

	/** 9133) Create Repository */
	, EtcTableCreate("etc-table-create")

	/** 9104) Update Repository */
	, EtcTableUpdate("etc-table-update")

	/** 9114) Update Repository Attribute */
	, EtcTableColumnUpdate("etc-table-column-update")

	/** 9124) Update Repository Key */
	, EtcTableIndexUpdate("etc-table-index-update")

	/** 9105) Delete Repository */
	, EtcTableDelete("etc-table-delete")

	/** 9115) Delete Repository Attribute */
	, EtcTableColumnDelete("etc-table-column-delete")

	/** 9125) Delete Repository Key */
	, EtcTableIndexDelete("etc-table-index-delete")

	/** 9200) 코드 */
	, Code("code")

	/** 9202) 코드관리 */
	, CodeList("code-list")

	/** 9203) 코드 추가 */
	, CodeAdd("code-add")

	/** 9204) 코드 수정 */
	, CodeUpdate("code-update")

	/** 9205) 코드 삭제 */
	, CodeDelete("code-delete")

	/** 10000) 메인 화면 */
	, Main("main")



	
	;
	
	private String opName;

	private OP_NAME(String name) {
		this.opName = name;
	}

	public String getOpName() {
		return opName;
	}
	
	public static OP_NAME getOP_NAME(String opName)
	{
		for ( OP_NAME e : OP_NAME.values()) {
			if ( e.getOpName().equals(opName)) {
				return e;
			}
		}
		
		return null;
	}


	public static void main(String[] args) {
		List<String> lineList = getLines(new File("datas/opcode.txt"));
		
		String ss[];
		String nm[];
		String name;
		int index = 0;
		
		for ( String line : lineList) {
			if ( line.trim().length() == 0) {
				continue;
			}
			
			ss = line.split("\t");
			nm = ss[0].split("-");
			
			
			name = "";
			for ( String s : nm) {
				name += s.toUpperCase().substring(0, 1) + s.toLowerCase().substring(1);
			}
			
			if ( ss.length == 3) {
				System.out.println("/** " + ss[1] + ") " + ss[2] + " */");
			}
			
			System.out.println((index > 0 ? ", " : "" )  + name + "(\"" + ss[0] + "\")" + "\n");
			
			index++;
			
		}
	}
	
	public static List<String> getLines(File file) {
		List<String> ret = new ArrayList<String>();

		BufferedReader in = null;
		String val;

		try {
			in = new BufferedReader(new FileReader(file));

			while (true) {

				try {
					val = in.readLine();
					if (val == null) {
						break;
					}
					if (val.trim().length() == 0)
						continue;
					if (val.startsWith("#"))
						continue;
					ret.add(val.trim());

				} catch (IOException e) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return ret;

	}
}
