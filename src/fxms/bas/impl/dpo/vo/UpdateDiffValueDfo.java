package fxms.bas.impl.dpo.vo;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.MoApi;
import fxms.bas.api.PsApi;
import fxms.bas.co.CoCode.MO_STATUS;
import fxms.bas.fxo.FxCfg;
import fxms.bas.fxo.service.FxServiceImpl;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.mo.Mo;
import fxms.bas.vo.MoStateEvt;
import fxms.bas.vo.PsValueUpdateVo;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.log.Logger;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * 수집 데이터값이 특정 테이블에 기록해야 할 때 변경된 부분이 있으면 업데이트 한다.
 * 
 * @author subkjh
 *
 */
public class UpdateDiffValueDfo implements FxDfo<PsVoList, Boolean> {

	@Override
	public Boolean call(FxFact fact, PsVoList voList) throws Exception {
		return update(voList);
	}

	/**
	 * 
	 * @param voList
	 * @throws Exception
	 */
	public boolean update(PsVoList voList) {

		List<PsValueUpdateVo> list = make(voList);

		if (list.size() > 0) {
			try {
				update(list);
				checkOnOff(list);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
		}

		return true;
	}

	/**
	 * 변경 내역 추출하기
	 * 
	 * @param voList
	 * @return
	 * @throws Exception
	 */
	private List<PsValueUpdateVo> make(PsVoList voList) {

		ValuePrevMap prevMap = ValuePrevMap.getInstance(this.getClass().getName());

		String valueCur, valuePrev;
		Number valPrev = null;
		PsValueUpdateVo data;
		List<PsValueUpdateVo> updateList = new ArrayList<PsValueUpdateVo>();

		for (PsVo value : voList) {

			if (value.getPsItem().isUpdate()) {

				valPrev = prevMap.getValue(value);
				prevMap.setValue(value);

				valuePrev = (valPrev == null ? null : value.getPsItem().toStringConvert(valPrev));
				valueCur = value.getPsItem().toStringConvert(value.getValue());

				if (valPrev == null || valPrev.floatValue() != value.getValue().floatValue()) {

					Logger.logger.debug("mo={}, column={}, value {}:{} -> {}:{}", value.getMo().getMoNo(),
							value.getPsItem().getMoColumn(), valPrev, valuePrev, value.getValue(), valueCur);

					data = new PsValueUpdateVo(value.getMo().getMoNo(), value.getPsItem().getUpdateFilter(),
							value.getPsItem().getPsId(), valueCur, value.getPsItem().getMoTable(),
							value.getPsItem().getMoColumn(), value.getPsItem().getMoDateColumn());

					updateList.add(data);

				}
			}

		}

		return updateList;
	}

	/**
	 * 저장소에 기록하기
	 * 
	 * @param updateList
	 * @throws Exception
	 */
	private void update(List<PsValueUpdateVo> updateList) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();

			for (PsValueUpdateVo updateColumn : updateList) {
				try {
					tran.executeSql(updateColumn.getSqlUpdate());
				} catch (Exception e) {
					Logger.logger.error(e);
				}
			}

			tran.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			tran.stop();
		}
	}

	private void checkOnOff(List<PsValueUpdateVo> list) {
		for (PsValueUpdateVo vo : list) {
			if (PsApi.isMoStatusPsId(vo.getPsId())) {
				try {

					Mo mo = MoApi.getApi().getMo(vo.getMoNo());

					int value = Integer.parseInt(vo.getValue().toString());

					MO_STATUS status = MO_STATUS.get(value);
					MoStateEvt event = new MoStateEvt(mo, status);

					FxServiceImpl.fxService.sendEvent(event, true, true);

				} catch (Exception e) {
					Logger.logger.error(e);
					Logger.logger.fail("Failed to broadcast data. {}.{}", vo.getMoNo(), vo.getPsId());
				}
			}
		}
	}

}