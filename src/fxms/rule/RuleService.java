package fxms.rule;

import java.rmi.RemoteException;

import fxms.bas.fxo.service.FxService;

/**
 * 비즈니스 룰 엔진을 실행하는 서비스
 * 
 * @author subkjh
 * @since 2023.02
 */
public interface RuleService extends FxService {

	/**
	 * 룰을 실행하고 실행번호를 제공한다.
	 * 
	 * @param brRuleNo 비즈니스룰번호
	 * @return 비즈니스룰실행번호
	 * @throws RemoteException
	 * @throws Exception
	 */
	public long runRule(int brRuleNo) throws RemoteException, Exception;

	/**
	 * 테스트용
	 * 
	 * @param json
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String runRule(String json) throws RemoteException, Exception;

}
