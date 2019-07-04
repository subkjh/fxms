package fxms.bas.po;

import java.util.ArrayList;
import java.util.List;

import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxActorParser;
import fxms.bas.fxo.thread.FXTHREAD_STATUS;
import fxms.bas.po.filter.UpdateFilter;
import fxms.bas.po.item.PsItem;
import fxms.bas.po.vo.UpdateDataVo;
import subkjh.bas.co.log.Logger;

/**
 * 수집된 값 중에서 저장소에 수정되어야 할 경우 처리합니다.
 * 
 * @author subkjh
 * 
 */
public class VoSubUpdater extends VoSub {

	/**
	 * 
	 * @param api
	 */
	public VoSubUpdater() {
		super(VoSubUpdater.class.getSimpleName());
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(VoList voList) throws Exception {

		PsItem poItem;
		String valueCur, valuePrev;
		Number valPrev;
		UpdateDataVo data;
		List<UpdateDataVo> updateList = new ArrayList<UpdateDataVo>();

		for (PsVo value : voList) {
			// 항목을 수정할 필요가 있을 경우 처리
			poItem = PsApi.getApi().getItem(value.getPsCode());

			if (poItem != null && poItem.isUpdate()) {

				valPrev = getPrevValue(value.getMoNo(), value.getMoInstance(), value.getPsCode());

				valuePrev = (valPrev == null ? null : poItem.toStringConvert(valPrev));
				valueCur = poItem.toStringConvert(value.getValue());

				if (valuePrev == null || (valuePrev.equals(valueCur) == false)) {

					Logger.logger.trace("mo={}, column={}, value {} -> {}", value.getMoNo(), poItem.getMoColumn(),
							valuePrev, valueCur);

					data = new UpdateDataVo(value.getMoNo(), poItem.getUpdateFilter(), valueCur, poItem.getMoTable(),
							poItem.getMoColumn(), poItem.getMoDateColumn());

					updateList.add(data);

					count++;

				}
			}

			// 캐슁하기
			setPrevValue(value);

		}

		if (updateList.size() > 0) {

			try {
				updateColumn(updateList);
			} catch (Exception e) {
				Logger.logger.error(e);
			}
			updateList.clear();
		}

	}

	@Override
	protected void onNoDatas(long index) {

	}

	private void updateColumn(List<UpdateDataVo> updateList) throws Exception {

		long ptime = System.currentTimeMillis();
		counter.setStatus(FXTHREAD_STATUS.Running);

		ValueApi.getApi().doUpdateColumn(updateList);

		UpdateFilter filter;

		for (UpdateDataVo bean : updateList) {

			if (bean.getUpdateFilter() != null) {
				filter = FxActorParser.getParser().getActor(UpdateFilter.class, bean.getUpdateFilter());
				if (filter != null) {
					try {
						filter.updated(bean.getMoNo());

					} catch (Exception e) {
						Logger.logger.error(e);
					}
				}
			}

		}

		counter.addOk(System.currentTimeMillis() - ptime);

	}
}
