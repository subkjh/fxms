package com.fxms.bio.agent;

import com.fxms.agent.FxAgent;
import com.fxms.agent.LoopFxThread;
import com.fxms.bio.agent.method.GetGwConfig;
import com.fxms.bio.agent.method.GetMethodList;
import com.fxms.bio.agent.method.GetSensorList;
import com.fxms.bio.agent.method.GetSensorValue;
import com.fxms.bio.agent.method.NotifySensorValues;
import com.fxms.bio.agent.method.SetCycleJob;
import com.fxms.bio.agent.method.SetSendorValue;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.log.Logger;

public class BioFxAgent {

	public static void main(String[] args) {

		String remoteHost = "125.7.128.42";
		// String remoteHost = "localhost";
		int localPort = 8120;
		int remotePort = 8110;
		String containerId = "CON082TEST";

		if (args.length >= 1) {
			localPort = Integer.valueOf(args[0]);
		}
		if (args.length >= 2) {
			remoteHost = args[1];
		}
		if (args.length >= 3) {
			remotePort = Integer.valueOf(args[2]);
		}
		if (args.length >= 4) {
			containerId = args[3];
		}

		BioFxAgent agent = new BioFxAgent();
		agent.run(containerId, localPort, remoteHost, remotePort);
	}

	public void run(String containerId, int localPort, String remoteHost, int remotePort) {

		Logger.logger = Logger.createLogger(FxCfg.getHomeLogs(), "agent");

		FxAgent agent = new FxAgent(localPort, remoteHost, remotePort, new LoopFxThread());
		agent.addMethod(new GetSensorList());
		agent.addMethod(new GetSensorValue());
		agent.addMethod(new SetSendorValue());
		agent.addMethod(new SetCycleJob());
		agent.addMethod(new GetMethodList());
		agent.addMethod(new GetGwConfig());

		agent.start();

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		agent.setCycleMethod(new NotifySensorValues(containerId));

	}

}
