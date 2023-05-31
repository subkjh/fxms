package fxms.bas.vo;

public class PollerCfg {
	
	/** 폴링 주기(초) */
	private int period = 60;

	/** 저장 주기(분) */
	private int cycleSavingMinutes = 5;

	/** poller간 수집 간격(초) */
	private int secDelayPoller = 3;

	/** 서비스 지연 시간(초) */
	private int secDelayServer = 0;

	/** 폴러당 관리 MO 수 */
	private int sizeMoPerPoller = 300;

	/** 폴러 수 */
	private int sizePoller = 0;

	/** Poller당 스레드 갯수 */
	private int sizeThreadInPoller = 30;

	private int countThreadMax = 10;
	private int countThreadMin = 3;

	/** 폴링 주기의 어느 정도 안에서 끝내지 나타내는 백분율 */
	private int limitRate = 80;

	/** 하나를 처리하는데 걸리는 시간(초) 기본값 */
	private int spp = 2;

	public PollerCfg() {

	}

	public PollerCfg(int countThreadMax, int countThreadMin) {
		this.countThreadMax = countThreadMax;
		this.countThreadMin = countThreadMin;
	}

	/**
	 * 
	 * @param var
	 *            폴링주기(초),저장주기(분),폴러수,폴러당스레드수,폴러당MO수,서비스지연시간(초),폴러지연시간(초)
	 * @throws Exception
	 */
	public PollerCfg(String var) throws Exception {
		if (var == null || var.trim().length() == 0) return;

		String ss[] = var.split(",");

		if (ss.length > 0) period = Integer.parseInt(ss[0].trim());
		if (ss.length > 1) cycleSavingMinutes = Integer.parseInt(ss[1].trim());
		if (ss.length > 2) sizePoller = Integer.parseInt(ss[2].trim());
		if (ss.length > 3) sizeThreadInPoller = Integer.parseInt(ss[3].trim());
		if (ss.length > 4) sizeMoPerPoller = Integer.parseInt(ss[4].trim());
		if (ss.length > 5) secDelayServer = Integer.parseInt(ss[5].trim());
		if (ss.length > 6) secDelayPoller = Integer.parseInt(ss[6].trim());

	}

	public int getCountThreadMax() {
		return countThreadMax;
	}

	public int getCountThreadMin() {
		return countThreadMin;
	}

	/**
	 * 
	 * @return 폴링 주기(초)
	 */
	public int getCyclePollingSeconds() {
		return period;
	}

	/**
	 * 
	 * @return 저장 주기(분)
	 */
	public int getCycleSavingMinutes() {
		return cycleSavingMinutes;
	}

	/**
	 * 폴링 주기의 어느 정도 안에서 끝내지 나타내는 백분율
	 * 
	 * @return 백분율
	 */
	public int getLimitRate() {
		return limitRate;
	}

	/**
	 * 
	 * @return poller간 수집 간격(초)
	 */
	public int getSecDelayPoller() {
		return secDelayPoller;
	}

	/**
	 * 
	 * @return 서비스 지연 시간(초)
	 */
	public int getSecDelayServer() {
		return secDelayServer;
	}

	/**
	 * 
	 * @return 폴러당 관리 MO 수
	 */
	public int getSizeMoPerPoller() {
		return sizeMoPerPoller;
	}

	/**
	 * 
	 * @return 폴러 수
	 */
	public int getSizePoller() {
		return sizePoller;
	}

	/**
	 * 
	 * @return Poller당 스레드 갯수
	 */
	public int getSizeThreadInPoller() {
		return sizeThreadInPoller;
	}

	public int getSpp() {
		return spp <= 0 ? 2 : spp;
	}

	public void setSpp(int spp) {
		this.spp = spp;
	}

	public void setVar(String var) {
		String ss[] = var.split(",|;");
		String s2[];
		for (String s : ss) {
			s2 = s.split("=");
			if (s2[0].equalsIgnoreCase("period")) {
				period = Integer.parseInt(s2[1].trim());
			} else if (s2[0].equalsIgnoreCase("countThreadMax")) {
				countThreadMax = Integer.parseInt(s2[1].trim());
			} else if (s2[0].equalsIgnoreCase("countThreadMin")) {
				countThreadMin = Integer.parseInt(s2[1].trim());
			} else if (s2[0].equalsIgnoreCase("limitRate")) {
				limitRate = Integer.parseInt(s2[1].trim());
			} else if (s2[0].equalsIgnoreCase("spp")) {
				spp = Integer.parseInt(s2[1].trim());
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CYCLE(" + period + ")");
		sb.append("THREAD-COUNT-MAX(" + countThreadMax + ")");
		sb.append("THREAD-COUNT-MAX(" + countThreadMin + ")");
		sb.append("LIMIT-RATE(" + limitRate + ")");
		sb.append("SECONDS-PER-POLLING(" + spp + ")");
		return sb.toString();
	}
}
