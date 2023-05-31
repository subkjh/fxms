package fxms.bas.fxo;

/**
 * fxms.xml에 정의된 속성 목록
 * 
 * @author subkjh
 *
 */
public enum FX_PARA {	
	
	project("project", "프로젝트명을 나타낸다."),

	envFxmsHome("FXMS_HOME", "실행위치를 나타낸다."),

	fxmsHome("fxms.home", "실행위치를 나타낸다."),
	
	fxmsServiceName("fxms.service.name", "서비스명을 나타낸다."),
	
	fxmsStartTime("fxms.start.time", "실행시간을 나타낸다."),

	fxmsJava("fxms.java.home", "java위치를 나타낸다."),

	fxmsTimezone("timezone", "시간대를 나타낸다."),
	
	jwtSecret("jwt.secret", "JWT에 사용하는 키를 나타낸다."),

	charset("charset.default", "시스템에서 사용하는 문자 인코딩 문자집합을 나타낸다."),
	
	charsetSnmp("charset.snmp", "SNMP 명령에서 사용하는 문자 인코딩 문자집합을 나타낸다. 장비마다 다른 경우 각각 설정한다."),	

	fxsvrIpAddr("fxsvr.ip.addr", "시스템에서 사용하는 IP주소를 나타낸다."),
	
	logLevel("log.level", "로그 기록의 등급을 나타낸다. info,debug,trace중 하나이다."),	
	
	logMaxFileSize("log.file.size", "로그파일 최대 갯수를 나타낸다. 로그 파일은 일단위로 생성된다."),
	
	logPrintConsole("log.is.print.console", "로그를 파일에 기록하면서 콘솔에서 보일지를 나타낸다."),	
	
	fxmsAlivePort("fxms.alive.port", "서비스 상태를 확인하는 포트"),
	
	fxmsRmiPort("fxms.rmi.port", "FX.MS가 사용하는 RMI포트"),
	
	fxmsPort("fxms.port", "FX.MS 자신이 사용하는 서비스 포트"),
	
	fxservicePort("fxservice.port", "서비스가 사용하는 포트"),
	
	fxserviceRmiPort("fxservice.rmi.port", "FX서비스가 사용하는 RMI포트"),	
	
	threadSize("thread.size", "사용할 쓰레드 수를 나타낸다."),
	
	databaseConfigFile("database.config.file", "데이터베이스를 정의한 파일이다.");	

	private String key;
	private String descr;

	FX_PARA(String key, String descr) {
		this.key = key;
		this.descr = descr;
	}

	public String getKey() {
		return key;
	}

	public String getDescr() {
		return descr;
	}

}
