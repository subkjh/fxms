package fxms.bas.api.thread;

import fxms.bas.impl.dpo.vo.UpdateDiffValueDfo;
import fxms.bas.vo.PsVoList;

/**
 * 수집된 값 중에서 저장소에 수정되어야 할 경우 처리합니다.
 * 
 * @author subkjh
 * 
 */
public class ValueApiUpdateThread extends ValueApiBasThread {

	/**
	 * 
	 * @param api
	 */
	public ValueApiUpdateThread() {
		super(ValueApiUpdateThread.class.getSimpleName());
	}

	@Override
	protected void doInit() {

	}

	@Override
	protected void doWork(PsVoList voList) throws Exception {
		count++;
		new UpdateDiffValueDfo().update(voList);
	}

	@Override
	protected void onNoDatas(long index) {

	}

}
