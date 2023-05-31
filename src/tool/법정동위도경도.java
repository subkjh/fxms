package tool;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.all.FX_CO_DONG;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;

/**
 * https://www.code.go.kr/stdcode/regCodeL.do<br>
 * 위 사이트에서 법정동 목록을 받는다.<br>
 * 
 * @author subkjh
 *
 */
public class 법정동위도경도 {

	public static void main(String[] args) {
		법정동위도경도 c = new 법정동위도경도();
		c.parse();
	}

	private void parse() {
		List<String> lineList;
		try {
			lineList = FileUtil.getLines(new File("datas/setup/법정동위경도.txt"), Charset.forName("utf-8"));

			String ss[];
			String v[];
			List<FX_CO_DONG> list = new ArrayList<FX_CO_DONG>();
			FX_CO_DONG dong;

			for (String data : lineList) {
				
				dong = new FX_CO_DONG() ;

				ss = data.split("\t");
				try {
					System.out.println(ss[0] + " : " + ss[ss.length-1]);
					dong.setAreaNum(ss[0]);
					v = ss[ss.length-1].split(",");
					dong.setLat(Double.parseDouble(v[0]));
					dong.setLng(Double.parseDouble(v[1]));
					list.add(dong);
					if ( list.size() > 10000 ) {
						update(list);
						list.clear();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			
			if ( list.size() > 0 ) {
				update(list);
			}			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	private void parse1() {
		List<String> lineList;
		try {
			lineList = FileUtil.getLines(new File("datas/setup/법정동위도경도.txt"), Charset.forName("utf-8"));

			String ss[];
			String v[];
			List<FX_CO_DONG> list = new ArrayList<FX_CO_DONG>();
			FX_CO_DONG dong;

			for (String data : lineList) {
				
				dong = new FX_CO_DONG() ;

				ss = data.split("\t");
				try {
					dong.setAreaNum(ss[0]);
					dong.setLat(Double.parseDouble(ss[5]));
					dong.setLng(Double.parseDouble(ss[6]));
					list.add(dong);
					if ( list.size() > 10000 ) {
						update(list);
						list.clear();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			
			if ( list.size() > 0 ) {
				update(list);
			}			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void update(List<FX_CO_DONG> list) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			
			for ( FX_CO_DONG o : list) {
				tran.executeSql("update FX_CO_DONG set lat=" + o.getLat() + ", lng=" + o.getLng() + " where area_num = '" + o.getAreaNum() + "'");
			}

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
		} finally {
			tran.stop();
		}
	}
}
