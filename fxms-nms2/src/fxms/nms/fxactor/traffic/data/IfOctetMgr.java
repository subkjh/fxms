package fxms.nms.fxactor.traffic.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subkjh.bas.co.log.Logger;
import fxms.bas.fxo.FxCfg;

public class IfOctetMgr {

	private static IfOctetMgr mgr = null;

	public static IfOctetMgr getMgr() {

		if (mgr == null) {
			mgr = new IfOctetMgr();
			mgr.initOctetLast();
		}
		return mgr;
	}

	private Map<Long, IfOctets> octetMap;

	private IfOctetMgr() {
		octetMap = new HashMap<Long, IfOctets>();
	}

	public IfOctets getOctetLast(long moNo) {
		return octetMap.get(moNo);
	}

	public void put(long moNo, IfOctets octets) {
		octetMap.put(moNo, octets);
	}

	public void initOctetLast() {

		octetMap.clear();

		if (getPathOctetLast() == null)
			return;

		// 최소 2시간 이내의 자료만 취함.
		long unixtime = System.currentTimeMillis() / 1000L;
		unixtime -= 7200;

		File folder = new File(getPathOctetLast());
		IfOctets octets;
		if (folder.exists() && folder.isDirectory()) {
			for (File f : folder.listFiles()) {
				try {
					BufferedReader in = null;
					String val;

					try {
						in = new BufferedReader(new FileReader(f));

						while (true) {

							try {
								val = in.readLine();
								if (val == null) {
									break;
								}
								try {
									octets = new IfOctets(val);
									if (octets.moNo > 0 && octets.unixtime >= unixtime)
										octetMap.put(octets.moNo, octets);
								} catch (Exception e2) {
									Logger.logger.error(e2);
								}

							} catch (IOException e) {
								break;
							}
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();

					} finally {
						if (in != null)
							try {
								in.close();
							} catch (IOException e) {
							}
					}

				} catch (Exception e1) {
					Logger.logger.error(e1);
				}
			}
		}

		Logger.logger.info("OCTET-SIZE(" + octetMap.size() + ") LOADED");

	}

	public void writeIfOctet2File(long moNoNode, long mstime, List<IfOctets> list) {

		SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

		String folder = FxCfg.getFile(getPathOctetFile(), YYYYMMDD.format(new Date(mstime)), moNoNode + "");

		if (new File(folder).exists() == false) {
			new File(folder).mkdirs();
		}

		String filename = FxCfg.getFile(folder, "snmp_" + moNoNode + "_" + YYYYMMDD.format(new Date(mstime)));

		File file = new File(filename);

		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, true);
			for (IfOctets e : list) {
				outStream.write(e.getString().getBytes());
				outStream.write("\n".getBytes());
			}
		} catch (IOException e) {
			Logger.logger.error(e);
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	/**
	 * 최근 Octets을 기록합니다.
	 * 
	 * @param moNoNode
	 * @param list
	 */
	public void writeIfOctetLast(long moNoNode, List<IfOctets> list) {

		String filename = getPathOctetLast() + File.separator + moNoNode + ".ifoctets";

		File file = new File(filename);

		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file, false);
			for (IfOctets e : list) {
				outStream.write(e.getString().getBytes());
				outStream.write("\n".getBytes());
			}
		} catch (IOException e) {
			Logger.logger.error(e);
		} finally {
			if (outStream != null)
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}

	public String getPathOctetFile() {
		return FxCfg.getCfg().getString(FxCfg.PATH_IF_OCTETS, FxCfg.getHomeDatas() + File.separator + "ifoctets");
	}

	public String getPathOctetLast() {
		return FxCfg.getCfg().getString(FxCfg.PATH_IF_OCTETS_LAST, null);
	}
}
