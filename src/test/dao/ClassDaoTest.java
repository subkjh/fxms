package test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fxms.bas.co.CoCode.ACCS_ST_CD;
import fxms.bas.fxo.FxCfg;
import fxms.bas.impl.dbo.AckAlarmHstDbo;
import fxms.bas.impl.dbo.DeleteAlarmHstExpireDbo;
import fxms.bas.impl.dbo.LogoutDbo;
import fxms.bas.impl.dbo.all.FX_CF_META;
import fxms.bas.impl.dbo.all.FX_MO;
import fxms.bas.impl.handler.dto.AckAlarmCurPara;
import subkjh.bas.BasCfg;
import subkjh.bas.co.log.LOG_LEVEL;
import subkjh.bas.co.log.Logger;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.database.DataBase;

public class ClassDaoTest {

	public static void main(String[] args) {
		FxCfg.DB_CONFIG = "VUPDB";
		FxCfg.DB_PSVALUE = FxCfg.DB_CONFIG;
		FxCfg.isTest = true;
		Logger.logger.setLevel(LOG_LEVEL.trace);

		ClassDaoTest c = new ClassDaoTest();

		try {
			c.testUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		StorageConfMo mo = new StorageConfMo();
//		mo.setMoNo(1000029L);
//		try {
//			c.testDelete(mo);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		try {
//			c.testDelete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param item INSERT할 DBO Java 클래스의 데이터
	 * @throws Exception
	 */
	public void insertSample(Object item) throws Exception {

		// 여러개이 DB를 동시에 접속가능하다

		// 1. Executor 가져오기
		// FxCfg.DB_CONFIG 데이터베이스를 사용하겠다는 의미
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {

			// 2. 사용을 시작하겠다는 의미
			tran.start();

			// 3. item Java Class에 속성을 이용하여 데이터베이스에 데이터를 넣는다.
			tran.insertOfClass(item.getClass(), item);

			// 3. item Java Class에 속성을 이용하여 데이터베이스에 업데이트 한다.
			tran.updateOfClass(item.getClass(), item);

			// 3. item java class에 속성을 이용하여 데이터베이스에서 삭제한다.
			tran.deleteOfObject(item.getClass(), item);

			// 4. 커밋
			tran.commit();

		} catch (Exception e) {

			// 오류 발생시 로그 기록
			Logger.logger.error(e);

			// 롤백
			tran.rollback();

			// Exception 보내기
			throw e;
		} finally {

			// 사용을 종료한다.
			tran.stop();
		}
	}

	public List selectSample(Map<String, Object> para) throws Exception {

		// 사용할 DB
		DataBase db = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG);
		// qid.xml 파일의 쿼리를 위한 트랜잭션
		QidDao tran = db.createQidDao(BasCfg.getHomeDeployConfSql("qid.xml"));

		try {
			// 사용하겠다는 의미
			tran.start();
			// QID에 해당되는 쿼리 실행, 결과는 QID의 ResultMap 형식
			return tran.selectQid2Res("SELECT_SAMPLE_DATA_QID", para);
		} catch (Exception e) {
			throw e;
		} finally {
			// 사용 종료
			tran.stop();
		}

	}

	public void testDelete() throws Exception {

		// 여러개이 DB를 동시에 접속가능하다

		// 1. Executor 가져오기
		// FxCfg.DB_CONFIG 데이터베이스를 사용하겠다는 의미
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {

			// 2. 사용을 시작하겠다는 의미
			tran.start();

			FX_CF_META obj = new FX_CF_META();
			obj.setMetaId("TEST");
			// 3. item java class에 속성을 이용하여 데이터베이스에서 삭제한다.
			tran.deleteOfObject(FX_CF_META.class, obj);
//			tran.deleteAllOfClass(FX_CF_META.class);

			Map<String, Object> para = new HashMap<String, Object>();
			para.put("regDtm >", "AAA");
			tran.deleteOfObject(DeleteAlarmHstExpireDbo.class, para);

			// 4. 커밋
			tran.commit();

		} catch (Exception e) {

			// 오류 발생시 로그 기록
			Logger.logger.error(e);

			// 롤백
			tran.rollback();

			// Exception 보내기
			throw e;
		} finally {

			// 사용을 종료한다.
			tran.stop();
		}
	}

	public void testUpdate() throws Exception {

		// 여러개이 DB를 동시에 접속가능하다

		// 1. Executor 가져오기
		// FxCfg.DB_CONFIG 데이터베이스를 사용하겠다는 의미
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {

			AckAlarmCurPara cur = new AckAlarmCurPara();
//			Map<String, Object> cur = new HashMap<String, Object>();
//			cur.put("alarmNo", 0);
//			cur.put("ackDtm", 0);

			// 2. 사용을 시작하겠다는 의미
			tran.start();

			LogoutDbo dbo = new LogoutDbo();
			dbo.setLogoutDtm(DateUtil.getDtm());
			dbo.setAccsStCd(ACCS_ST_CD.LOGOUT_OK.getCode());
			dbo.setSessionId("test");

			tran.updateOfClass(LogoutDbo.class, dbo);

			tran.updateOfClass(AckAlarmHstDbo.class, cur);

			// 4. 커밋
			tran.commit();

		} catch (Exception e) {

			// 오류 발생시 로그 기록
			Logger.logger.error(e);

			// 롤백
			tran.rollback();

			// Exception 보내기
			throw e;
		} finally {

			// 사용을 종료한다.
			tran.stop();
		}
	}

	public void testSelectCount() throws Exception {

		// 여러개이 DB를 동시에 접속가능하다

		// 1. Executor 가져오기
		// FxCfg.DB_CONFIG 데이터베이스를 사용하겠다는 의미
		ClassDao tran = DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createClassDao();

		try {

			// 2. 사용을 시작하겠다는 의미
			tran.start();

			// 3. item java class에 속성을 이용하여 데이터베이스에서 삭제한다.
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("mngYn", "Y");
			System.out.println(tran.selectCount(FX_MO.class, para));

		} catch (Exception e) {

			// 오류 발생시 로그 기록
			Logger.logger.error(e);

			// Exception 보내기
			throw e;
		} finally {

			// 사용을 종료한다.
			tran.stop();
		}
	}
}
