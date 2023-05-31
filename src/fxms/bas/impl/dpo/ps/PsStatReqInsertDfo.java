package fxms.bas.impl.dpo.ps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.api.PsApi;
import fxms.bas.co.CoCode.CRE_ST_CD;
import fxms.bas.exp.NotFoundException;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.StatMakeReqDbo;
import fxms.bas.impl.dbo.all.FX_PS_STAT_CRE;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsKind;
import fxms.bas.vo.PsStatReqVo;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.util.FxTableMaker;

/**
 * 통계 생성 요청을 기록한다.
 * 
 * @author subkjh
 *
 */
public class PsStatReqInsertDfo implements FxDfo<List<PsStatReqVo>, Integer> {

	@Override
	public Integer call(FxFact fact, List<PsStatReqVo> list) throws Exception {
		// TODO Auto-generated method stub
		return addStatReq(list);
	}

	/**
	 * 통계 생성 요청을 기록한다.
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int addStatReq(List<PsStatReqVo> list) throws Exception {

		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();
		int ret = 0;

		try {
			tran.start();

			Map<String, Object> para = new HashMap<String, Object>();
			PsKind psKind;

			for (PsStatReqVo vo : list) {

				// 데이터 존재 확인
				try {
					psKind = PsApi.getApi().getPsKind(vo.getPsKindName());
				} catch (NotFoundException e) {
					continue;
				}

				para.put("psTbl", vo.getPsTbl());
				para.put("psDtm", vo.getPsDtm());
				para.put("psDataCd", vo.getPsKindName());

				FX_PS_STAT_CRE req;
				FX_PS_STAT_CRE oldReq = tran.selectOne(FX_PS_STAT_CRE.class, para);

				if (oldReq == null) {

					// 없으면 추가
					req = new FX_PS_STAT_CRE();
					req.setPsCreReqNo(tran.getNextVal(StatMakeReqDbo.FX_SEQ_PSCREREQNO, Long.class));
					req.setCreStCd(CRE_ST_CD.Ready.getCode());
					req.setPsDtm(vo.getPsDtm());
					req.setPsTbl(vo.getPsTbl());
					req.setPsDataCd(vo.getPsKindName());
					req.setPsCreReqDtm(DateUtil.getDtm());
					req.setReqCnt(1);

				} else {
					req = oldReq;
					req.setCreStCd(CRE_ST_CD.Ready.getCode());
					req.setCreStrtDtm(0);
					req.setCreEndDtm(0);
					req.setRetMsg("re-request");
					req.setReqCnt(req.getReqCnt() + 1);
				}

				// 작업 가능일시
				req.setCreblDtm(psKind.getHstimeNext(req.getPsDtm(), 1));

				FxTableMaker.initRegChg(0, req);

				if (oldReq == null) {
					tran.insertOfClass(FX_PS_STAT_CRE.class, req);
				} else {
					tran.updateOfClass(FX_PS_STAT_CRE.class, req);
				}

				tran.commit();
				ret++;
			}
			return ret;
		} catch (Exception e) {
			Logger.logger.error(e);
			throw e;
		} finally {
			tran.stop();
		}

	}

}
