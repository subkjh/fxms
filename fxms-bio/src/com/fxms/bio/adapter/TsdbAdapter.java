package com.fxms.bio.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fxms.bio.mo.DeviceMo;
import com.fxms.bio.mo.GwMo;

import fxms.bas.api.EventApi;
import fxms.bas.api.MoApi;
import fxms.bas.define.ALARM_CODE;
import fxms.bas.exception.FxTimeoutException;
import fxms.bas.fxo.adapter.Adapter;
import fxms.bas.mo.child.MoConfig;
import fxms.bas.pso.PsVo;
import subkjh.bas.log.Logger;
import subkjh.bas.net.url.UrlClientGet;

/**
 * 
 * @author SUBKJH-DEV
 *
 *         <pre>
 * http://125.7.128.54:2580/q?start=2017/09/18-10:00:00&end=2017/09/18-10:00:59&m=sum:rc01.co2.ppm%7Bid=2000%7D&ascii
 * 
 * result
 * 
 * rc01.co2.ppm 1505696400 153 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696401 156 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696402 152 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696403 154 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696404 151 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696405 154 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696406 153 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696407 153 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696409 152 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
rc01.co2.ppm 1505696410 153 owner=kang country=kor sensor=co2 id=2000 building=SG floor_room=SG_office<br>
 * 
 *         </pre>
 */
public class TsdbAdapter extends Adapter<DeviceMo> {

	private static final SimpleDateFormat TM = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");

	@Override
	public List<PsVo> getValue(DeviceMo device, String psCodes[]) throws FxTimeoutException, Exception {

		GwMo gw = (GwMo) MoApi.getApi().getMo(device.getGwMoNo());
		if (gw == null) {
			throw new Exception("Not Found Gateway " + device.getGwMoNo());
		}

		long start = System.currentTimeMillis() - 65000L;
		long end = System.currentTimeMillis() - 5000L;
		String method3 = getMethod(device.getDeviceType());
		StringBuffer site = new StringBuffer();

		site.append("http://");
		site.append(gw.getGwIpaddr());
		site.append(":");
		site.append(gw.getGwPort());
		site.append("/q?");
		site.append("start=" + TM.format(new Date(start)));
		site.append("&end=" + TM.format(new Date(end)));
		site.append("&m=sum:");
		site.append(method3);
		site.append("%7Bid=");
		site.append(device.getDeviceIdInGw());
		site.append("%7D&ascii");

		UrlClientGet client = new UrlClientGet();

		Logger.logger.debug("{}-{} {}", device.getDeviceType(), device.getDeviceIdInGw(), site);

		// site = new StringBuffer();
		// site.append(
		// "http://125.7.128.54:2580/q?start=" + TM.format(new Date(start)) +
		// "&end=2017/09/18-10:00:59&m=sum:rc01.co2.ppm%7Bid=2000%7D&ascii");
		String contents = null;
		try {
			contents = client.get(site.toString());
		} catch (Exception e) {
			EventApi.getApi().check(gw, null, ALARM_CODE.NOT_RESPONSE_ON_PS_COLLECT, null, null);
			Logger.logger.error(site.toString() + " : " + e.getMessage());
			return null;
		}

		Logger.logger.trace("\n-------------------------------------------------------------------------------\n"
				+ contents + "\n-------------------------------------------------------------------------------");

		int count = 0;
		long sum = 0;
		long tm;
		int val;
		String ss[];
		String lineArray[] = contents.split(method3);
		for (String line : lineArray) {
			ss = line.trim().split(" ");
			if (ss.length < 2)
				continue;
			// System.out.println(Arrays.toString(ss));
			// System.out.println(ss[0] + ", " + ss[1]);
			tm = Long.valueOf(ss[0]) * 1000L;
			val = Double.valueOf(ss[1]).intValue();
			if (tm >= start && tm <= end) {
				//
				count++;
				sum += val;
			}
		}

		String psCode = getPsCode(device.getDeviceType());

		if (count > 0) {
			if (psCode != null) {
				List<PsVo> voList = new ArrayList<PsVo>();
				voList.add(new PsVo(device, null, psCode, sum / count));
				return voList;
			}
		}

		EventApi.getApi().check(device, psCode, ALARM_CODE.NO_DATA_ON_PS_COLLECT, null, null);

		return null;
		// TEMP 온도
		// PH pH
		// CO2 CO2 주입량
		// PUMP_ONOFF 펌프 On/Off
		// PUMP_HZ 펌프 Hz
		// LED Light Intensity
		// DOOR_STATE Door State
		// CO2_REMAIN CO2 잔유량
		// BIOMASS_OUT 바이오매스 측정량
		// ASTAZAN 아스타잔틴 함유율
		// BIOMASS_IN 바이오매스 접종량
		// COST 매지비용

	}

	@Override
	public void setValue(DeviceMo mo, String method, Map<String, Object> para) throws Exception {
		String msg = "Not Implements : " + mo;
		Logger.logger.fail(msg);
		throw new Exception(msg);
	}

	@Override
	public void getConfigChildren(MoConfig children, String... moClasses) throws FxTimeoutException, Exception {

		// String msg = "Not Implements : ";
		// Logger.logger.fail(msg);
		// throw new Exception(msg);

		return;
	}

	private String getPsCode(String method) {
		if ("CO2".equals(method)) {
			return "CO2";
		}
		if ("온도".equals(method)) {
			return "TEMP";
		}
		if ("습도".equals(method)) {
			return "HUMI";
		}
		if ("조도".equals(method)) {
			return "ILLUMI";
		}
		if ("PH".equals(method)) {
			return "PH";
		}
		return null;
	}

	private String getMethod(String deviceType) {
		if ("CO2".equals(deviceType)) {
			return "rc01.co2.ppm";
		}
		if ("온도".equals(deviceType)) {
			return "rc01.temp.degree";
		}
		if ("습도".equals(deviceType)) {
			return "rc01.humi.persent";
		}
		if ("조도".equals(deviceType)) {
			return "rc01.illumi.lux";
		}
		if ("PH".equals(deviceType)) {
			return "rc01.pH.H";
		}
		return null;
	}

}
