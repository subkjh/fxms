package tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_TBL_COL_DEF;
import fxms.bas.impl.dbo.all.FX_TBL_DEF;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;
import subkjh.dao.database.MySql;
import subkjh.dao.def.Column;
import subkjh.dao.def.Table;
import subkjh.dao.util.SqlUtil;

public class InsertFX_TBL {

	public static void main(String[] args) throws Exception {

		Column.JAVA_FIELD_STYLE_OBJECT = true;

		InsertFX_TBL tool = new InsertFX_TBL();

		tool.insert_FX_TBL(new MySql(), new File("datas/setup/tables.txt"));

	}

	public void insert_FX_TBL(DataBase database, File f) {
		List<Table> tableList;
		List<FX_TBL_DEF> defList = new ArrayList<FX_TBL_DEF>();
		List<FX_TBL_COL_DEF> colList = new ArrayList<FX_TBL_COL_DEF>();
		long dtm = DateUtil.getDtm();
		try {
			tableList = new SqlUtil().getTableList(f);

			for (Table tab : tableList) {
				FX_TBL_DEF def = new FX_TBL_DEF();
				def.setChgDtm(dtm);
				def.setRegDtm(dtm);
				def.setChgUserNo(0);
				def.setRegUserNo(0);
				def.setResevYn("Y");
				def.setTblCmnt(tab.getComment());
				def.setTblName(tab.getName());
				defList.add(def);
				
				for ( Column col : tab.getColumns()) {
					FX_TBL_COL_DEF colDef = new FX_TBL_COL_DEF();
					colDef.setColCmnt(col.getComments());
					colDef.setColName(col.getName());
					colDef.setColNo(col.getColumnNo());
					colDef.setColSize(col.getDataLength());
					colDef.setColTypeCd(col.getDataType());
					colDef.setDefVal(col.getDataDefault());
					colDef.setFieldName(col.getFieldName());
					colDef.setNulblYn(col.isNullable() ? "Y" : "N");
					colDef.setSeqName(col.getSequence());
					colDef.setTblName(tab.getName());
					colDef.setUpdblYn("Y");

					colDef.setChgDtm(dtm);
					colDef.setRegDtm(dtm);
					colDef.setChgUserNo(0);
					colDef.setRegUserNo(0);
					
					colList.add(colDef);
				}
			}

			insert(defList, FX_TBL_DEF.class);
			insert(colList, FX_TBL_COL_DEF.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void insert(List<?> list, Class<?> classOf) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			tran.insertOfClass(classOf, list);
			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
		} finally {
			tran.stop();
		}
	}
}
