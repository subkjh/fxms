package fxms.bas.impl.dpo.vo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.impl.dpo.ps.PsValMakeGroupDfo;
import fxms.bas.impl.vo.PsValGroup;
import fxms.bas.impl.vo.PsValGroup.Data;
import fxms.bas.impl.vo.PsValGroup.MoData;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;

/**
 * 수집데이터를 influxDB에 기록한다.
 * 
 * @author subkjh
 *
 */
public class ValueAddInfluxDfo implements FxDfo<PsVoList, Void> {

	public static void main(String[] args) {

		MoApi.api = new MoApiDfo();

		ValueAddInfluxDfo dfo = new ValueAddInfluxDfo();
		PsVoList datas = new PsVoList("test", System.currentTimeMillis(), null);

		try {
			datas.add(1, MoApi.getApi().getMo(1000), PsApi.getApi().getPsItem("MoStatus"), null);
			dfo.add(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Void call(FxFact fact, PsVoList datas) throws Exception {

		add(datas);

		return null;
	}

	/**
	 * 
	 * @param datas
	 * @throws Exception
	 */
	public void add(PsVoList datas) throws Exception {

		DataBase database = DBManager.getMgr().getDataBase("INFLUXDB");

		final InfluxDB influxDB = InfluxDBFactory.connect(database.getUrl(), database.getUser(),
				database.getPassword());
		influxDB.setDatabase(database.getDbName());

		// Enable batch writes to get better performance.
		influxDB.enableBatch(BatchOptions.DEFAULTS.threadFactory(runnable -> {
			Thread thread = new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		}));

		try {
			List<PsValGroup> tables = new PsValMakeGroupDfo().make(datas);

			for (PsValGroup tab : tables) {
				for (MoData mo : tab.values.values()) {
					Builder builder = Point.measurement(tab.tabName).time(tab.mstime, TimeUnit.MILLISECONDS);
					builder.tag("moNo", String.valueOf(mo.moNo));
					for (Data data : mo.values) {
						builder.addField(data.colName, data.value);
					}
					influxDB.write(builder.build());
				}

				Logger.logger.debug("measurement={}, hstime={}, size={}", tab.tabName, DateUtil.toHstime(tab.mstime),
						tab.values.values().size());
			}
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			influxDB.close();

		}

	}
}
