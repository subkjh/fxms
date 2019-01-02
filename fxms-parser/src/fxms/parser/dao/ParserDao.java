package fxms.parser.dao;

import java.util.List;
import java.util.Map;

import subkjh.bas.dao.control.DbTrans;
import subkjh.bas.dao.database.DBManager;
import subkjh.bas.dao.database.DataBase;

public class ParserDao {

	public static void main(String[] args) {
		try {
			List<Map<String, Object>> mapList;
			
//			mapList = new ParserDao().selectList("SELECT_SCREEN_LIST");
//			for ( Map<String, Object> map : mapList ) {
//				System.out.println("<screen id=\"" + map.get("ID") + "\" text=\"" + map.get("TEXT").toString().trim()+ "\" />");
//			}
//			
//			mapList = new ParserDao().selectList("SELECT_TPO_LIST");
//			for ( Map<String, Object> map : mapList ) {
//				System.out.println("<attr id=\"tpoCd\" value=\"" + map.get("ID") + "\" text=\"" + map.get("TEXT").toString().trim()+ "\" type=\"string\" />");
//				
//			}
			
			mapList = new ParserDao().selectList("SELECT_GUNGU_LIST");
			for ( Map<String, Object> map : mapList ) {
				System.out.println("<attr id=\"ldongCd\" value=\"" + map.get("ID") + "\" text=\"" + map.get("TEXT").toString().trim()+ "\" type=\"string\" />");
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private DbTrans tran;

	public ParserDao() {

		DataBase database = DBManager.getMgr().getDataBase("ADAMS_MICM");
		try {
			tran = database.createDbTrans("deploy/conf/sql/ai-screen/screen-qid.xml");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectList(String qid) throws Exception {
		return tran.selectQid2Res(qid, null);
	}
}
