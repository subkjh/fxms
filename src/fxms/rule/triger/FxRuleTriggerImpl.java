package fxms.rule.triger;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fxms.bas.fxo.FxAttrApi;
import fxms.rule.FxBusinessRuleEngine;

/**
 *
 * @author subkjh
 *
 */
public abstract class FxRuleTriggerImpl implements FxRuleTrigger {

	private FxBusinessRuleEngine bre;

	private final Map<String, Object> paraMap;

	public FxRuleTriggerImpl(Map<String, Object> datas) throws Exception {
		paraMap = FxAttrApi.toObject(datas, this);
	}

	@Override
	public FxBusinessRuleEngine getRuleEngine() {
		return bre;
	}

	@Override
	public void setRuleEngine(FxBusinessRuleEngine bre) {
		this.bre = bre;
	}

	protected void timeCheck() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				Thread.sleep(10000);
				return "AAA";
			}
		};

		Future<String> future = executor.submit(task);
		try {
			System.out.println(future.get(5000, TimeUnit.MILLISECONDS));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Map<String, Object> getParaMap() {
		return paraMap;
	}

}
