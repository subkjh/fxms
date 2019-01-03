package com.fxms.agent;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.management.MBeanServer;

public class test {

	public static void main(String[] args) {
		test t = new test();
		t.t1();
	}

	@SuppressWarnings("unused")
	public void t1() {

		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		for (Method m : osBean.getClass().getMethods()) {
			if ( m.getName().startsWith("get")) {
				try {
					m.setAccessible(true);
					System.out.println(m.getName() + "=" + m.invoke(osBean));
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

		System.out.println(osBean.getClass());

		int workersCount = Runtime.getRuntime().availableProcessors();

		System.out.println(osBean.getArch());
		System.out.println(osBean.getVersion());
		System.out.println(osBean.getAvailableProcessors());
		System.out.println(osBean.getSystemLoadAverage());
		System.out.println(osBean.getObjectName());
		System.out.println(workersCount);
		//
		// System.out.println(OperatingSystemMXBean.getTotalPhysicalMemorySize());

		MBeanServer s = ManagementFactory.getPlatformMBeanServer();

	}
}
