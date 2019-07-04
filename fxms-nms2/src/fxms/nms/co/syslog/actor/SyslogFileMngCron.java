package fxms.nms.co.syslog.actor;

import java.io.File;

import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;
import fxms.bas.api.FxApi;
import fxms.bas.co.cron.Crontab;
import fxms.nms.api.SyslogApi;

/**
 * TRAP Log의 파일을 관리한다.
 * 
 * @author subkjh(김종훈)
 *
 */
public class SyslogFileMngCron extends Crontab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3375987691470608116L;

	@Override
	public void cron() throws Exception {

		File folder = new File(SyslogApi.getApi().getSyslogFile());

		if (folder.exists() == false) {
			return;
		}

		int count;
		int ret;
		int trapLogFileTermDays = getFxPara().getInt("syslogFileKeepDays", 0);
		int zipAfterDays = getFxPara().getInt("zipAfterDays", 0);
		boolean zipOneFile = getFxPara().getBoolean("zipOneFile", true);

		String delYmd = FxApi.getYmd(System.currentTimeMillis() - (86400000 * trapLogFileTermDays)) + "";
		String zipYmd = FxApi.getYmd(System.currentTimeMillis() - (86400000 * zipAfterDays)) + "";

		Logger.logger.info("paramters : folder={}, del={}, zip={}", folder.getPath(), delYmd, zipYmd);

		// 보관 기간이 지난 자료 삭제
		if (trapLogFileTermDays > 0) {
			count = 0;
			for (File e : folder.listFiles()) {
				if (e.getName().compareTo(delYmd) < 0) {
					ret = FileUtil.delete(e);
					if (ret > 0) {
						Logger.logger.info("delete={}", e.getPath());
						count += ret;
					} else {
						Logger.logger.fail("cannot delete={}", e.getPath());
					}
				}
			}

			Logger.logger.info("delete files={}", count);
		}

		// log -> zip
		if (zipAfterDays > 0) {
			count = 0;
			File dst;
			for (File e : folder.listFiles()) {
				if (e.getName().compareTo(zipYmd) < 0) {

					if (zipOneFile) {
						dst = new File(e.getPath() + ".zip");
						FileUtil.zip(e, dst.getPath());
						FileUtil.delete(e);
						Logger.logger.info("zip {}->{}, delete={}", e.getPath(), dst.getPath(), e.getPath());
						count++;
					} else {
						for (File e1 : e.listFiles()) {
							if (e1.getName().endsWith("zip") == false) {
								dst = new File(e1.getPath() + ".zip");
								FileUtil.zipFile(e1, dst);
								count++;
								e1.delete();
								Logger.logger.info("zip {}->{}, delete={}", e1.getPath(), dst.getPath(), e1.getPath());
							}
						}
					}
				}
			}

			Logger.logger.info("zip files={}", count);
		}
	}

	@Override
	public String getGroup() {
		return "syslog";
	}

	@Override
	public String getLog() {
		return null;
	}

	@Override
	public int getOpcode() {
		return 0;
	}

}
