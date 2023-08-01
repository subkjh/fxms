package fxms.bas.impl.dpo.vo;

import java.io.File;

import fxms.bas.api.MoApi;
import fxms.bas.api.ValueApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.api.MoApiDfo;
import fxms.bas.impl.api.ValueApiDfo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsVo;
import fxms.bas.vo.PsVoList;
import subkjh.bas.co.utils.DateUtil;
import subkjh.bas.co.utils.FileUtil;

/**
 * 수집 데이터를 관리대상 단위 파일에 기록한다.
 * 
 * @author subkjh
 *
 */
public class ValueWriteFileDfo implements FxDfo<PsVoList, Boolean> {

	public static void main(String[] args) {
//		ValueWriteFileDfo dfo = new ValueWriteFileDfo();
		MoApi.api = new MoApiDfo();
		ValueApi.api = new ValueApiDfo();

//		PsVoRawList list = new PsVoRawList("TEST", System.currentTimeMillis());
//		try {
//			for (Mo mo : MoApi.getApi().getMoList(FxApi.makePara("moClass", "SENSOR"))) {
//				List<PsVoRaw> datas = adapter.getValue(mo);
//				if (datas != null) {
//					list.addAll(datas);
//				}
//			}
//			System.out.println(dfo.write(new ValueExtractValidDfo().extractValidData(list)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		Thread ths[] = new Thread[Thread.activeCount()];
		Thread.enumerate(ths);
		for (Thread th : ths) {
			System.out.println(th.getName() + ";" + th.getClass().getName());
		}

	}

	@Override
	public Boolean call(FxFact fact, PsVoList data) throws Exception {
		return write(data);
	}

	/**
	 * 
	 * @param voList
	 * @return 데이터가 추가된 테이블 목록
	 * @throws Exception
	 */
	public boolean write(PsVoList voList) throws Exception {

		long hstime = DateUtil.getDtm(voList.getMstime());
		String month = String.valueOf(hstime).substring(0, 6);

		String filename;
		StringBuffer sb = new StringBuffer();
		for (PsVo vo : voList) {
			filename = getValueFile(vo, month);
			sb = new StringBuffer();
			sb.append(hstime).append("\t").append(vo.getValue()).append("\n");
			FileUtil.writeToFile(filename, sb.toString(), true);
		}

		return true;
	}

	private String getValueFile(PsVo vo, String month) {

		String filename = String.valueOf(vo.getMo().getMoNo());
		String f1 = filename.substring(filename.length() - 2);
		String f2 = filename.substring(filename.length() - 4);
		if (vo.getMoInstance() != null && vo.getMoInstance().length() > 0) {
			filename = filename + "_" + vo.getMoInstance();
		}

		File file = new File(
				FxCfg.getFile(FxCfg.getHomeDatas(), "collected", f1, f2, filename, vo.getPsItem().getPsId()));
		if (file.exists() == false) {
			file.mkdirs();
		}

		return file.getPath() + File.separator + month + ".dat";
	}

}