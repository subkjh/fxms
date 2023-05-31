package fxms.bas.impl.dpo.ps;

import java.util.List;

import fxms.bas.api.PsApi;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.StatMakeReqDbo;
import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.PsItem;
import fxms.bas.vo.PsKind;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDaoEx;

/**
 * 함수를 이용하여 통계 생성한다.
 * 
 * @author subkjh
 *
 */
public class PsStatMakeDfo extends PsDpo implements FxDfo<StatMakeReqDbo, Integer> {

	@Override
	public Integer call(FxFact fact, StatMakeReqDbo data) throws Exception {
		return generateStatistics(data);
	}

	public int generateStatistics(StatMakeReqDbo req) throws Exception {
		return generateStatistics(req.getPsTbl(), req.getPsDataCd(), req.getPsDtm());
	}

	public int generateStatistics(String psTbl, String psKindName, long psDtm) throws Exception {

		String insSql;
		String delSql;
		PsKind psKindDst, psKindSrc;

		psKindDst = PsApi.getApi().getPsKind(psKindName); // 대상
		psKindSrc = PsApi.getApi().getPsKind(psKindDst.getPsDataSrc()); // 원천

		insSql = getSqlMake(psTbl, psKindSrc, psKindDst, psDtm); // insert문
		delSql = getSqlDelete(psTbl, psKindDst, psDtm); // delete문

		ClassDaoEx dao = ClassDaoEx.open(FxCfg.DB_PSVALUE).executeSql(delSql).executeSql(insSql).close();
		return dao.getProcessedCount();
	}

	private String getSqlDelete(String psTable, PsKind psKind, long psDate) {

		StringBuffer sql = new StringBuffer();

		sql.append("delete from ");
		sql.append(psKind.getTableName(psTable, psDate));
		sql.append(" where " + PsDpo.PS_DATE.getName() + "=" + psKind.getHstimeStart(psDate));

		return sql.toString();

	}

	private String getSqlMake(String psTable, PsKind psKindSrc, PsKind psKindDst, long psDtm) throws Exception {

		List<PsItem> itemList = PsApi.getApi().getPsItemList(psTable);
		StringBuffer sql = new StringBuffer();
		StringBuffer dest = new StringBuffer();
		StringBuffer src = new StringBuffer();
		String destTable = psKindDst.getTableName(psTable, psDtm);
		String srcTable = psKindSrc.getTableName(psTable, psDtm);

		dest.append(PsDpo.MO_NO.getName());
		dest.append(", ").append(PsDpo.MO_INSTANCE.getName());
		dest.append(", ").append(PsDpo.PS_DATE.getName());
		dest.append(", ").append(PsDpo.DATA_COUNT.getName());
		dest.append(", ").append(PsDpo.INS_DATE.getName());

		src.append(PsDpo.MO_NO.getName());
		src.append(", ").append(PsDpo.MO_INSTANCE.getName());
		src.append(", ").append(psKindDst.getHstimeStart(psDtm));
		src.append(", count(1)");
		src.append(", ").append(DateUtil.getDtm());

		// 각 성능항목에 대한 통계 함수를 이용한 컬럼을 추가한다.
		for (PsItem item : itemList) {
			dest.append("\n ");
			src.append("\n ");

			for (String func : item.getPsKindCols()) {
				String colName = item.getPsColumn() + "_" + func;
				dest.append(", ").append(colName);
				src.append(", ").append(func).append("(").append(psKindSrc.isRaw() ? item.getPsColumn() : colName)
						.append(")");
			}
		}

		sql.append("insert into ").append(destTable).append(" ( ").append(dest).append(") \n");
		sql.append("select ").append(src).append(" \n");
		sql.append("from ").append(srcTable).append(" \n");
		sql.append("where ").append(PsDpo.PS_DATE.getName()).append(" >= ").append(psKindDst.getHstimeStart(psDtm))
				.append(" \n");
		sql.append("and ").append(PsDpo.PS_DATE.getName()).append(" <= ").append(psKindDst.getHstimeEnd(psDtm))
				.append(" \n");
		sql.append("group by ").append(PsDpo.MO_NO.getName()).append(", ").append(PsDpo.MO_INSTANCE.getName());

		return sql.toString();

	}

}
