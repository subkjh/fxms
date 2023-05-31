package test.bas.api;

import fxms.bas.api.AdapterApi;
import fxms.bas.fxo.adapter.PsStatAfterAdapter;

public class AdapterApiTest {

	public static void main(String[] args) {

		try {
//			FxCfg.getCfg().setFxServiceName(VupService.class.getSimpleName());
			System.out.println(AdapterApi.getApi().getAdapters(PsStatAfterAdapter.class));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
