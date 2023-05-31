package fxms.bas.impl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.AdapterApi;
import fxms.bas.api.FxApi;
import fxms.bas.exp.NotInstanceException;
import fxms.bas.fxo.FxAttrApi;
import fxms.bas.fxo.FxAttrVo;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.FxmsUtil;
import fxms.bas.fxo.adapter.FxAdapter;
import fxms.bas.fxo.adapter.FxAdapterInfo;
import fxms.bas.fxo.adapter.FxGetValueAdapter;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.impl.dbo.all.FX_CF_ADAPT;
import fxms.bas.impl.dpo.co.SelectAdapterDfo;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

public class AdapterApiDfo extends AdapterApi {

	/**
	 * 개발한 아답터를 저장소에 기록한다.
	 * 
	 * @param classOf
	 * @throws Exception
	 */
	public void insert(Class<?> classOf) throws Exception {

		FX_CF_ADAPT data = makeAdapterInfo(classOf);

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		try {
			tran.start();

			FxTableMaker.initRegChg(0, data);

			FX_CF_ADAPT old = tran.selectOne(FX_CF_ADAPT.class, makePara("adaptName", data.getAdaptName()));
			if (old != null) {
				tran.updateOfClass(data.getClass(), data);
			} else {
				tran.insertOfClass(data.getClass(), data);
			}
			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

	/**
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private FxAdapter makeAdapter(FX_CF_ADAPT data) throws Exception {

		Object obj = Class.forName(data.getAdaptName()).newInstance();

		if (obj instanceof FxAdapter) {

			FxAdapter adapter = (FxAdapter) obj;
			adapter.setPara(FxmsUtil.toMapFromJson(data.getParaJson()));

			if (adapter instanceof FxGetValueAdapter) {
				FxGetValueAdapter a = (FxGetValueAdapter) adapter;
				a.setMoPara(FxmsUtil.toMapFromJson(data.getMoJson()));
				a.setPollCycle(data.getPollCycle());
			}

			Logger.logger.info("java.class={}, para={}, mo={}, cycle={}", obj.getClass().getName(), data.getParaJson(),
					data.getMoJson(), data.getPollCycle());

			adapter.onCreated();

			return adapter;
		}

		throw new NotInstanceException(FxAdapter.class, data.getAdaptName());
	}

	private FX_CF_ADAPT makeAdapterInfo(Class<?> classOf) throws Exception {
		FxAdapterInfo info = classOf.getAnnotation(FxAdapterInfo.class);
		if (info == null) {
			return null;
		}

		FX_CF_ADAPT a = new FX_CF_ADAPT();
		a.setAdaptDescr(info.descr());
		a.setAdaptName(classOf.getCanonicalName());
		a.setFxsvcName(info.service());
		a.setMoJson(info.moJson());
		a.setPollCycle(-1);
		a.setParaJson("");
		a.setUseYn("Y");

		if (FxGetValueAdapter.class.isAssignableFrom(classOf)) {
			a.setPollCycle(info.pollCycle());
		}

		List<FxAttrVo> fields = FxAttrApi.getFxAttrField(classOf);
		Map<String, Object> map = new HashMap<String, Object>();
		for (FxAttrVo vo : fields) {
			if (FxApi.isNotEmpty(vo.attr.value())) {
				map.put(vo.getName(), vo.attr.value());
			}
		}
		a.setParaJson(FxmsUtil.toJson(map));

		return a;
	}

	@Override
	protected List<FxAdapter> getFxAdapters() throws Exception {

		List<FX_CF_ADAPT> list = new SelectAdapterDfo().selectAdapters();
		
		List<FxAdapter> tmpList = new ArrayList<FxAdapter>();
		StringBuffer sb = new StringBuffer();

		for (FX_CF_ADAPT data : list) {
			try {
				tmpList.add(makeAdapter(data));
				sb.append(Logger.makeSubString(data.getAdaptName(), "ok"));
			} catch (Exception e) {
				Logger.logger.error(e);
				sb.append(Logger.makeSubString(data.getAdaptName(), "error"));
			}
		}

		FxServiceImpl.logger.info(Logger.makeString("Adapters", tmpList.size(), sb.toString()));

		return tmpList;

	}
}
