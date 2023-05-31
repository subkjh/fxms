package fxms.bas.signal;

public class AliveSignal extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2437251984343202456L;

	private final String msIpaddr;
	private final String serviceName;
	private final long startTime;
	private final int rmiPort;
	private final int servicePort;

	public AliveSignal(long startTime, String msIpaddr, String serviceName, int rmiPort, int servicePort) {

		super(AliveSignal.class.getSimpleName(), msIpaddr + "/" + serviceName);

		this.startTime = startTime;
		this.msIpaddr = msIpaddr;
		this.serviceName = serviceName;
		this.rmiPort = rmiPort;
		this.servicePort = servicePort;
	}

	public String getMsIpaddr() {
		return msIpaddr;
	}

	public int getRmiPort() {
		return rmiPort;
	}

	public String getServiceName() {
		return serviceName;
	}

	public int getServicePort() {
		return servicePort;
	}

	public long getStartTime() {
		return startTime;
	}

}
