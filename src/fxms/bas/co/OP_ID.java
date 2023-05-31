package fxms.bas.co;

public enum OP_ID {

	/** 기본데이티관리 */
	base_data("base-data"),

	/** 설치위치 */
	menu_inlo("menu-inlo"),

	/** 관리대상 */
	menu_mo("menu-mo"),

	/** 관리모델 */
	menu_model("menu-model"),

	/** 알람조건 */
	menu_alarm_cfg("menu-alarm-cfg"),

	/** 관제 */
	menu_dashboard("menu-dashboard"),

	/** 알람실시간조회 */
	alarm_realtime("alarm-realtime"),

	/** 알람이력조회 */
	alarm_history("alarm-history"),

	/** 관리대상상태조회 */
	mo_status("mo-status"),

	/** 설치위치상태조회 */
	inlo_status("inlo-status"),

	/** 대쉬보드 */
	dashboard_fxms("dashboard-fxms"),

	/** 시스템관리 */
	system("system"),

	/** 사용자 */
	user("user"),

	/** 메뉴권한 */
	auth("auth"),

	/** 이력조회 */
	history("history"),

	/** 커뮤니티 */
	community("community"),

	/** 공지사항 */
	board("board"),

	/** Q&A */
	qna("qna"),

	/** 내 정보 관리 */
	my_info("my-info"),

	/** 암호변경 */
	change_my_pwd("change-my-pwd"),

	/** 정보수정 */
	change_my_info("change-my-info"),

	/** 경보인지 */
	ack_alarm("ack-alarm"),

	/** 경보수동해제 */
	clear_alarm("clear-alarm"),

	/** 경보생성 */
	fire_alarm("fire-alarm"),

	/** 알람조회 */
	get_alarm("get-alarm"),

	/** 경보발생조건목록 조회 */
	get_alarm_cfg_list("get-alarm-cfg-list"),

	/** 경보코드목록조회 */
	get_alarm_code_list("get-alarm-code-list"),

	/** 현재경보조회 */
	get_alarm_cur("get-alarm-cur"),

	/** 해제경보조회 */
	get_alarm_hst("get-alarm-hst"),

	/** 경보원인설정 */
	set_alarm_reason("set-alarm-reason"),

	/** 경보발생조건 추가 */
	add_alarm_cfg("add-alarm-cfg"),

	/** 경보발생조건 항목 추가 */
	add_alarm_cfg_mem("add-alarm-cfg-mem"),

	/** 경보발생조건 복사 */
	copy_alarm_cfg("copy-alarm-cfg"),

	/** 경보발생조건 항목 삭제 */
	delete_alarm_cfg_mem("delete-alarm-cfg-mem"),

	/** 경봉발생조건 삭제 */
	delete_alarm_cfg("delete-alarm-cfg"),

	/** 경보발생조건 수정 */
	update_alarm_cfg("update-alarm-cfg"),

	/** 설치위치 추가 */
	add_inlo("add-inlo"),

	/** 설치위치 삭제 */
	delete_inlo("delete-inlo"),

	/** 설치위치 조회 */
	get_inlo_list("get-inlo-list"),

	/** 설치위치 수정 */
	update_inlo("update-inlo"),

	/** 코드 삭제 */
	delete_code("delete-code"),

	/** 관리대상 추가 */
	add_mo("add-mo"),

	/** 관리대상 삭제 */
	delete_mo("delete-mo"),

	/** 구성정보 탐색 */
	detect_mo("detect-mo"),

	/** 관리대상 조회 */
	get_mo("get-mo"),

	/** 관리대상 건수 조회 */
	get_mo_count("get-mo-count"),

	/** 관리대상 목록 조회 */
	get_mo_list("get-mo-list"),

	/** 관리대상 명령 내리기 */
	setup_mo("setup-mo"),

	/** 구성정보 실시간 탐색 */
	test_mo("test-mo"),

	/** 관리대상 수정 */
	update_mo("update-mo"),

	/** 모델 추가 */
	add_model("add-model"),

	/** 모델 삭제 */
	delete_model("delete-model"),

	/** 모델 수정 */
	updae_model("updae-model"),

	/** 수집값 항목 조회 */
	get_ps_item_list("get-ps-item-list"),

	/** 수집값 조회 */
	get_ps_list("get-ps-list"),

	/** 실시간 수집값 조회 */
	get_rs_real_list("get-rs-real-list"),

	/** 수집값 엿보기 등록 */
	register_ps_val_peek("register-ps-val-peek"),

	/** 수집값 엿보기 취소 */
	unregister_ps_val_peek("unregister-ps-val-peek"),

	/** 사용자 추가 */
	add_user("add-user"),

	/** 사용자그룹 추가 */
	add_user_group("add-user-group"),

	/** 신규 사용 신청 */
	apply_new_user("apply-new-user"),

	/** 암호 변경 */
	change_pwd("change-pwd"),

	/** 사용자 삭제 */
	delete_user("delete-user"),

	/** 사용자 그룹 삭제 */
	delete_user_group("delete-user-group"),

	/** 로그인 사용자 인원수 조회 */
	get_logined_user_count("get-logined-user-count"),

	/** 사용자 정보 조회 */
	get_user("get-user"),

	/** 사용자그룹 목록 조회 */
	get_user_group_list("get-user-group-list"),

	/** 사용자 목록 조회 */
	get_user_list("get-user-list"),

	/** 사용자 속성 조회 */
	get_user_properties("get-user-properties"),

	/** 사용자 화면 정보 조회 */
	get_user_ui("get-user-ui"),

	/** 신규 사용자 승인/반려 처리 */
	process_new_user("process-new-user"),

	/** 사용자 속성 설정 */
	set_user_property("set-user-property"),

	/** 사용자 화면 설정 */
	set_user_ui("set-user-ui"),

	/** 사용자 정보 수정 */
	update_user("update-user"),

	/** 사용자그룹 정보 수정 */
	update_user_group("update-user-group"),

	/** 코드 추가 */
	add_code("add-code"),

	/** 지정테이블 데이터 추가 */
	add_fxtable_data("add-fxtable-data"),

	/** 지정테이블 데이터 삭제 */
	delete_fxtable_data("delete-fxtable-data"),

	/** 속성 조회 */
	get_attrValue_list("get-attrValue-list"),

	/** 챠트 조회 */
	get_chart_list("get-chart-list"),

	/** 코드 조회 */
	get_code_list("get-code-list"),

	/** */
	get_conf_data_list("get-conf-data-list"),

	/** */
	get_data_list("get-data-list"),

	/** 특정 다이아그램 조회 */
	get_diagram("get-diagram"),

	/** 다이아그램 조회 */
	get_diagram_list("get-diagram-list"),

	/** 지정테이블 컬럼 조회 */
	get_fxtable_columns("get-fxtable-columns"),

	/** 지정테이블 인덱스 조회 */
	get_fxtable_indexes("get-fxtable-indexes"),

	/** 지정테이블 조회 */
	get_fxtables("get-fxtables"),

	/** 기능코드 조회 */
	get_op_list("get-op-list"),

	/** 운영시간 조회 */
	get_opTime_list("get-opTime-list"),

	/** QID 조회된 데이터 추출 */
	get_qid_select("get-qid-select"),

	/** 시스템 시간 조회 */
	get_system_time("get-system-time"),

	/** 환경변수값 조회 */
	get_var_list("get-var-list"),

	/** QID 조회된 데이터 추출 */
	select_qid("select-qid"),

	/** 이벤트 전달 */
	send_fxevent("send-fxevent"),

	/** 다이아그램 설정 */
	set_diagram("set-diagram"),

	/** 코드 수정 */
	update_code("update-code"),

	/** 지정테이블 데이터 수정 */
	update_fxtable_data("update-fxtable-data"),

	/** 기능코드 수정 */
	update_opcode("update-opcode"),

	/** 기능크기 수정 */
	update_op_size("update-op-size")

	;

	//
	//
	// tool.BaseDataMake 클래스 사용해서 base-data-opId.txt 내용으로 만들어짐.
	//
	//

	private String opId;

	OP_ID(String opId) {
		this.opId = opId;
	}

	public String getOpId() {
		return opId;
	}
}
