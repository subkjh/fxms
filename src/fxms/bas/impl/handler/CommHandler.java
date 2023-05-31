package fxms.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.fxo.FxCfg;
import fxms.bas.handler.BaseHandler;
import fxms.bas.handler.vo.SessionVo;
import fxms.bas.impl.dbo.SelectBoardGridListDbo;
import fxms.bas.impl.dbo.SelectQnaGridListDbo;
import fxms.bas.impl.dbo.UpdateBoardCloseDbo;
import fxms.bas.impl.dbo.UpdateQnaCloseDbo;
import fxms.bas.impl.dbo.all.FX_CM_BOARD;
import fxms.bas.impl.dbo.all.FX_CM_QA_ANSWR;
import fxms.bas.impl.dbo.all.FX_CM_QA_QUEST;
import fxms.bas.impl.handler.dto.CmSelectQnaGridListPara;
import subkjh.bas.co.utils.DateUtil;
import subkjh.dao.ClassDao;
import subkjh.dao.ClassDaoEx;
import subkjh.dao.QidDao;
import subkjh.dao.database.DBManager;
import subkjh.dao.def.FxDaoCallBefore;

/**
 * 소통관련 HANDLER
 * 
 * @author subkjh
 *
 */
public class CommHandler extends BaseHandler {

	/**
	 * 공지사항을 추가한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object insertBoard(SessionVo session, FX_CM_BOARD obj) throws Exception {
		obj.setInloNo(session.getInloNo());

		insert(obj, new FxDaoCallBefore<FX_CM_BOARD>() {
			@Override
			public void onCall(ClassDao tran, FX_CM_BOARD data) throws Exception {
				int boardNo = tran.getNextVal(FX_CM_BOARD.FX_SEQ_BOARD_NO, Integer.class);
				obj.setBoardNo(boardNo);
			}

		});

		return obj;
	}

	/**
	 * Qna 답변을 등록한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object insertQnaAnswer(SessionVo session, FX_CM_QA_ANSWR obj) throws Exception {

		obj.setAnswrName(session.getUserName());
		obj.setAnswrUserNo(session.getUserNo());
		obj.setAnswrDate(DateUtil.getYmd());

		insert(obj, new FxDaoCallBefore<FX_CM_QA_ANSWR>() {
			@Override
			public void onCall(ClassDao tran, FX_CM_QA_ANSWR data) throws Exception {
				int answrNo = tran.getNextVal(FX_CM_QA_ANSWR.FX_SEQ_ANSWRNO, Integer.class);
				obj.setAnswrNo(answrNo);
			}

		});

		return obj;
	}

	/**
	 * QnA 질문을 등록한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object insertQnaQuestion(SessionVo session, FX_CM_QA_QUEST obj) throws Exception {

		obj.setInloNo(session.getInloNo());
		obj.setQuestDate(DateUtil.getYmd());
		obj.setCloseYn(false);

		insert(obj, new FxDaoCallBefore<FX_CM_QA_QUEST>() {
			@Override
			public void onCall(ClassDao tran, FX_CM_QA_QUEST data) throws Exception {
				int boardNo = tran.getNextVal(FX_CM_QA_QUEST.FX_SEQ_QUESTNO, Integer.class);
				obj.setQuestNo(boardNo);
			}

		});

		return obj;
	}

	/**
	 * 공지사항 그리드용 목록을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectBoardGridList(SessionVo session, SelectBoardGridListDbo obj) throws Exception {
		Map<String, Object> wherePara = new HashMap<String, Object>();
		wherePara.put("POST_STRT_DATE >= ", obj.getPostStrtDate());
		wherePara.put("POST_STRT_DATE <= ", obj.getPostFnshDate());
		if (obj.getBoardCnts() != null && obj.getBoardCnts().length() > 0) {
			wherePara.put("BOARD_CNTS like ", "%" + obj.getBoardCnts() + "%");
		}

		return ClassDaoEx.SelectDatas(FX_CM_BOARD.class, makePara4Ownership(session, wherePara), FX_CM_BOARD.class);
	}

	/**
	 * QNA 그리드용 목록을 조회한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object selectQnaGridList(SessionVo session, CmSelectQnaGridListPara obj) throws Exception {

		Map<String, Object> wherePara = new HashMap<String, Object>();
		wherePara.put("QUEST_DATE >= ", obj.getStartDate());
		wherePara.put("QUEST_DATE <= ", obj.getEndDate());

		if (obj.getContents() != null && obj.getContents().length() > 0) {
			wherePara.put("QUEST_CNTS like ", "%" + obj.getContents() + "%");
		}

		return ClassDaoEx.SelectDatas(SelectQnaGridListDbo.class, makePara4Ownership(session, wherePara),
				SelectQnaGridListDbo.class);
	}

	/**
	 * 공지사항 게시종료한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateBoardClose(SessionVo session, UpdateBoardCloseDbo obj) throws Exception {

		obj.setPostFnshDate(String.valueOf(DateUtil.getYmd(System.currentTimeMillis() - 86400000L)));

		update(obj, null);

		return obj;
	}

	/**
	 * QnA를 마감한다.
	 * 
	 * @param session
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public Object updateQnaClose(SessionVo session, UpdateQnaCloseDbo obj) throws Exception {

		obj.setCloseYn(true);

		update(obj, null);

		return obj;
	}

	@Override
	protected QidDao getQidDao() throws Exception {
		return DBManager.getMgr().getDataBase(FxCfg.DB_CONFIG).createQidDao();
	}

}
