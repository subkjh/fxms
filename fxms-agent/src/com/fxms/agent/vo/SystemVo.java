package com.fxms.agent.vo;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SystemVo {

	private long freePhysicalMemorySize;
	private long freeSwapSpaceSize;
	private double systemCpuLoad;
	private double processCpuLoad;
	private long processCpuTime;
	private long totalPhysicalMemorySize;
	private long totalSwapSpaceSize;
	private String name;
	private String arch;
	private String version;

	public static void main(String[] args) {

		while (true) {

			System.out.println("--------------------------------------------------------------");

			new SystemVo();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public SystemVo() {
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		for (Method m : osBean.getClass().getMethods()) {
			if (m.getName().startsWith("get")) {
				try {
					m.setAccessible(true);
					setValue(m, m.invoke(osBean));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void setValue(Method m, Object value) {
		for (Field f : getClass().getDeclaredFields()) {
			if (f.getName().equalsIgnoreCase(m.getName().substring(3))) {
				try {
					f.set(this, value);

//					System.out.println(f.getName() + "=" + value);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}

	public long getFreeSwapSpaceSize() {
		return freeSwapSpaceSize;
	}

	public double getSystemCpuLoad() {
		return systemCpuLoad;
	}

	public double getProcessCpuLoad() {
		return processCpuLoad;
	}

	public long getProcessCpuTime() {
		return processCpuTime;
	}

	public long getTotalPhysicalMemorySize() {
		return totalPhysicalMemorySize;
	}

	public long getTotalSwapSpaceSize() {
		return totalSwapSpaceSize;
	}

	public String getOsName() {
		return name;
	}

	public String getCpuArch() {
		return arch;
	}

	public String getCpuVersion() {
		return version;
	}

	public double getSystemCpuUsage() {
		if (getSystemCpuLoad() >= 1) {
			return 100f;
		} else if (getSystemCpuLoad() > 0) {
			return getSystemCpuLoad() * 100;
		} else {
			return 0;
		}
	}
}
