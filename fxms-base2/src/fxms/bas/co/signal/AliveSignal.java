package fxms.bas.co.signal;

public class AliveSignal extends Signal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2437251984343202456L;

	private String msIpaddr;
	private String serviceName;
	private long startTime;

	public AliveSignal() {

	}

	public AliveSignal(long startTime, String msIpaddr, String serviceName) {
		
		super(AliveSignal.class.getSimpleName(), msIpaddr + "/" + serviceName);
		
		this.startTime = startTime;
		this.msIpaddr = msIpaddr;
		this.serviceName = serviceName;
	}

	public long getStartTime() {
		return startTime;
	}

	public String getMsIpaddr() {
		return msIpaddr;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setMsIpaddr(String msIpaddr) {
		this.msIpaddr = msIpaddr;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
