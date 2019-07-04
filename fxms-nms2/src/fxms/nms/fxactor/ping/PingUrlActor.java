package fxms.nms.fxactor.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import subkjh.bas.co.log.Logger;
import fxms.bas.mo.Mo;
import fxms.bas.po.PsVo;
import fxms.bas.poller.exp.PollingTimeoutException;
import fxms.nms.NmsCodes;
import fxms.nms.mo.UrlMo;

public class PingUrlActor extends PingFxActor {

	@Override
	public List<PsVo> getValues(long pollMsdate, Mo mo, String... psCodes) throws PollingTimeoutException, Exception {

		if ((mo instanceof UrlMo) == false) {
			return null;
		}

		UrlMo node = (UrlMo) mo;

		long mstime = System.currentTimeMillis();

		int index = node.getUrl().indexOf('?');
		String para = (index > 0 ? node.getUrl().substring(index + 1) : null);
		String urlStr = (index > 0 ? node.getUrl().substring(0, index) : node.getUrl());

		String contents = "";
		int status = UrlMo.STATUS_ERROR;
		int rtt;

		try {
			if (UrlMo.METHOD_GET.equalsIgnoreCase(node.getMethod())) {
				contents = methodGet(node.getUrl());
			} else {
				contents = methodPost(urlStr, para);
			}
		} catch (Exception e) {
			Logger.logger.fail(e.getMessage());
			status = UrlMo.STATUS_DOWN;
		}

		rtt = (int) (System.currentTimeMillis() - mstime);

		if (node.getStrOk() != null && node.getStrOk().length() > 0 && contents.indexOf(node.getStrOk()) >= 0) {
			status = UrlMo.STATUS_OK;
		} else if (node.getStrError() != null && node.getStrError().length() > 0 && contents.indexOf(node.getStrError()) >= 0) {
			status = UrlMo.STATUS_ERROR;
		} else {
			status = UrlMo.STATUS_OK;
		}

		List<PsVo> valueList = new ArrayList<PsVo>();

		valueList.add(new PsVo(node, null, NmsCodes.PsItem.UrlStatus, status));

		if (status == UrlMo.STATUS_OK) {
			valueList.add(new PsVo(node, null, NmsCodes.PsItem.UrlResponseTime, rtt));
		}

		return valueList;
	}

	public String methodGet(String site) throws Exception {
		try {

			URL url = new URL(site);
			URLConnection conn = url.openConnection();

			// Get the response
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}
			reader.close();

			return answer.toString();

		} catch (MalformedURLException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		}

	}

	public String methodPost(String site, String para) throws Exception {
		try {

			URL url = new URL(site);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			// write parameters
			writer.write(para);
			writer.flush();

			// Get the response
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}
			writer.close();
			reader.close();

			return answer.toString();

		} catch (MalformedURLException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		}

	}
}
