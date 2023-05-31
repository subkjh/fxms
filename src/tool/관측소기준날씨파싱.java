package tool;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.FileUtil;
import subkjh.bas.co.utils.ObjectUtil;
import subkjh.bas.co.utils.StringUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.database.DBManager;
import test.dbo.FX_ZA_DAILY_WEATHER;

/**
 * https://www.code.go.kr/stdcode/regCodeL.do<br>
 * 위 사이트에서 법정동 목록을 받는다.<br>
 * 
 * @author subkjh
 *
 */
public class 관측소기준날씨파싱 {

	public static void main(String[] args) {
		관측소기준날씨파싱 c = new 관측소기준날씨파싱();
		c.parse();
	}

	private void parse() {
		List<String> lineList;
		try {

			String v[];
			String key;
			Map<String, String> map = new HashMap<String, String>();
			StringBuffer sb = new StringBuffer();

			FileUtil.read(new File("datas/setup/관측소기준날씨.txt"), Charset.forName("utf-8"), new FileUtil.StringListener() {

				Field fields[] = FX_ZA_DAILY_WEATHER.class.getDeclaredFields();
				List<FX_ZA_DAILY_WEATHER> list = new ArrayList<FX_ZA_DAILY_WEATHER>();

				@Override
				public void onLine(String line) {
					if ( line == null) {
						try {
							insert(list);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return;
					}
					
					// FX_ZA_DAILY_WEATHER 클래스 *** 필드 순서 변경하면 안 됨 ***
					
					FX_ZA_DAILY_WEATHER obj = new FX_ZA_DAILY_WEATHER() ;				
					List<String> ss = StringUtil.split(line, '\t');
					
					for ( int i = 0; i < fields.length; i++) {
						try {
							fields[i].setAccessible(true);
							ObjectUtil.setField(obj, fields[i], ss.get(i));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					list.add(obj);
					
					System.out.println(line);
					System.out.println(ss);
					System.out.println(fields.length + ", " + ss.size());
				}
				
			});



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void insert(List<FX_ZA_DAILY_WEATHER> list) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();
			tran.insertOfClass(FX_ZA_DAILY_WEATHER.class, list);
			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
		} finally {
			tran.stop();
		}
	}
}
