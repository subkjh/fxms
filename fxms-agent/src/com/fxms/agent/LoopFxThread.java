package com.fxms.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import fxms.bas.fxo.thread.FxThread;
import subkjh.bas.log.Logger;

/**
 * FxAgent에서 반복적으로 작업하는 스레드
 * 
 * @author SUBKJH-DEV
 *
 */
public class LoopFxThread extends FxThread {

	private LinkedBlockingQueue<List<String>> queue;

	public static void main(String[] args) {
		LoopFxThread looper = new LoopFxThread();
		looper.start();

		List<String> script = new ArrayList<String>();
		script.add("loop");
		script.add("PUMP.0 0");
		script.add("sleep 1");
		script.add("PUMP.1 1");
		script.add("sleep 5");
		script.add("PUMP.1 0");
		script.add("sleep 1");
		script.add("PUMP.0 1");
		script.add("sleep 100");

		looper.setScript(script);

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		script = new ArrayList<String>();
		script.add("once");
		script.add("PUMP.0 0");
		script.add("PUMP.1 1");

		looper.setScript(script);

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		script = new ArrayList<String>();
		script.add("loop");
		script.add("PUMP.0 0");
		script.add("sleep 1");
		script.add("PUMP.1 1");
		script.add("sleep 5");
		script.add("PUMP.1 0");
		script.add("sleep 1");
		script.add("PUMP.0 1");
		script.add("sleep 100");

		looper.setScript(script);

	}

	public void setScript(List<String> script) {

		Logger.logger.info("{}", script);

		try {
			queue.put(script);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public LoopFxThread() {
		super("LOOPER");

		queue = new LinkedBlockingQueue<List<String>>();
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork() {

		while (isContinue()) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			runScript();

		}

	}

	final String CMD_LOOP = "loop";
	final String CMD_ONCE = "once";
	final String CMD_SLEEP = "sleep";

	private void runScript() {

		List<String> list = getScript();

		if (list == null || list.size() < 2) {
			return;
		}

		boolean loop = list.get(0).equalsIgnoreCase(CMD_LOOP);
		String cmd;

		LOOP: while (true) {

			for (int i = 1; i < list.size(); i++) {

				cmd = list.get(i);

				Logger.logger.info(i + ") " + cmd);

				if (cmd.startsWith(CMD_SLEEP)) {
					sleep(cmd);
				} else {
					setValue(cmd);
				}

				if (queue.size() > 0)
					break LOOP;
			}

			if (loop == false)
				break;
		}

	}

	private void setValue(String cmd) {

	}

	private void sleep(String cmd) {

		String ss[] = cmd.split(" ");
		int seconds = 0;
		try {
			seconds = Integer.parseInt(ss[1]);
		} catch (Exception e) {
			return;
		}

		long ptime = System.currentTimeMillis() + (seconds * 1000L);

		while (System.currentTimeMillis() < ptime) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (queue.size() > 0)
				break;
		}

	}

	public List<String> getScript() {
		return queue.poll();
	}

}
