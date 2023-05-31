package tool;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class 법정동파싱 {

	public static void main(String[] args) {
		법정동파싱 c = new 법정동파싱();
		c.parse();
	}

	private void parse() {
		List<String> lineList;
		try {
			lineList = FileUtil.getLines(new File("datas/setup/법정동코드 전체자료.txt"), Charset.forName("euc-kr"));
			FileUtil.writeToFile("datas/setup/FX_CO_DONG_Created.sql", new byte[0], false);

			String ss[];
			String v[];
			String UPPR_AREA_NUM;
			String key;
			Map<String, String> map = new HashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			List<FX_CO_DONG> list = new ArrayList<FX_CO_DONG>();
			FX_CO_DONG dong;

			for (String data : lineList) {
				
				dong = new FX_CO_DONG() ;


				//

				ss = data.split("\t");
				dong.setAreaNum(ss[0]);
				dong.setAreaAllName(ss[1]);				
				if (ss[2].equals("폐지")) {
					dong.setUseYn(false);
				} else {
					dong.setUseYn(true);
				}
				v = ss[1].split(" ");

				// 충청북도 청주시 상당구 낭성면 귀래리
				// 경기도 시흥군 소래읍 옥길리
				// 위 2곳 모두 "리"로 분리한다.
//				AREA_CL_CD = v.length >= 4 ? 4 : v.length;
				dong.setAreaClCd(String.valueOf(v.length));
				dong.setAreaName(v[v.length - 1]);

//				if (AREA_CL_CD == 4 && (AREA_NM.endsWith("읍") || AREA_NM.endsWith("면"))) {
//					AREA_CL_CD = 3;
//				}

				key = "";
				for (int i = 0; i < v.length - 1; i++) {
					if (key.length() > 0) {
						key += " ";
					}
					key += v[i];
				}

				UPPR_AREA_NUM = map.get(key);
				dong.setUpperAreaNum(UPPR_AREA_NUM == null ? "0" : UPPR_AREA_NUM);

				map.put(dong.getAreaAllName(), dong.getAreaNum());
				
				list.add(dong);

				sb.append(dong.getAreaNum());
				sb.append("\t").append(dong.getAreaName());
				sb.append("\t").append(dong.getAreaAllName());
				sb.append("\t").append(dong.getAreaClCd());
				sb.append("\t").append(dong.getUpperAreaNum());
				sb.append("\t").append(dong.isUseYn());
				sb.append("\n");
				if ( sb.length() > 10000 ) {
					FileUtil.writeToFile("datas/setup/FX_CO_DONG_Created.sql", sb.toString().getBytes("utf-8"), true);
					sb = new StringBuffer();
				}
				
			}
			
			if ( sb.length() > 0) {
				FileUtil.writeToFile("datas/setup/FX_CO_DONG_Created.sql", sb.toString().getBytes("utf-8"), true);
			}
			
			insert(list);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void insert(List<FX_CO_DONG> list) throws Exception {
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {
			tran.start();


			tran.insertOfClass(FX_CO_DONG.class, list);

			tran.commit();

		} catch (Exception e) {
			Logger.logger.error(e);
			tran.rollback();
		} finally {
			tran.stop();
		}
	}
}
