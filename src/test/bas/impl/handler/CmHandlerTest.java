package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import fxms.bas.co.CoCode.QUEST_CL_CD;
import fxms.bas.ws.handler.client.FxHttpClient;

public class CmHandlerTest {

	public static void main(String[] args) {

		try {
			CmHandlerTest c = new CmHandlerTest();
//			c.addBoard();
//			c.updateBoardClose();
//			c.selectBoardGridList();
//			c.insertQnaQuestion();
//			c.insertQnaAnswer();
			c.selectQnaGridList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(50000);
			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	FxHttpClient c;
	Map<String, Object> para = new HashMap<String, Object>();

	public CmHandlerTest() throws Exception {
		c = FxHttpClient.getFxmsClient();
		c.login("subkjh", "1111");
	}

	void updateBoardClose() {
//		update-board-close
//		mandatory :
//		boardNo : 게시판번호
//		postFnshDate : 게시종료일자
//		optional :
//		chgUserNo : 수정사용자번호
//		chgDtm : 수정일시

		para.clear();
		para.put("boardNo", 1);

		try {
			c.call("cm/update-board-close", para);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void addBoard() {
//		insert-board
//		mandatory :
//		boardNo : 게시판번호
//		boardTitle : 게시판제목
//		boardClCd : 게시판구분코드
//		boardCnts : 게시내용
//		topDispYn : TOP표시여부
//		postStrtDate : 게시시작일자
//		postFnshDate : 게시종료일자
//		optional :
//		inloNo : 설치위치번호
//		regUserNo : 등록사용자번호
//		regDtm : 등록일시
//		chgUserNo : 수정사용자번호
//		chgDtm : 수정일시
//		
		para.clear();
		para.put("boardNo", 0);
		para.put("boardTitle", "테스트");
		para.put("boardClCd", "q");
		para.put("boardCnts", "adfafdasfasdfasdf");
		para.put("topDispYn", "N");
		para.put("postStrtDate", "20220501");
		para.put("postFnshDate", "20220531");
		

		try {
			c.call("cm/insert-board", para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void selectBoardGridList() {
//		mandatory :
//			postStrtDate : 조회시작일자
//			postFnshDate : 조회종료일자
//			optional :
//			boardCnts : 게시내용
//		
		para.clear();
		para.put("postStrtDate", "20220501");
		para.put("postFnshDate", "20220531");

		try {
			c.call("cm/select-board-grid-list", para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void insertQnaQuestion() {
//		mandatory :
//			questNo : 질문번호
//			questTitle : 질문제목
//			questClCd : 질문구분코드
//			questCnts : 질문내용
//			closeYn : 마감여부
//			questName : 질문자명
//			optional :
//			questUserNo : 질문사용자번호
//			inloNo : 설치위치번호
//			regUserNo : 등록사용자번호
//			regDtm : 등록일시
//			chgUserNo : 수정사용자번호
//			chgDtm : 수정일시
		para.clear();
		para.put("questNo", 0);
		para.put("questTitle", "테스트");
		para.put("questClCd", "q");
		para.put("questCnts", "adfafdasfasdfasdf");
		para.put("closeYn", "N");
		para.put("questName", "홍길동");

		try {
			c.call("cm/insert-qna-question", para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}

	void insertQnaAnswer() {
//		mandatory :
//			questNo : 질문번호
//			answrNo : 답변번호
//			answrCnts : 답변내용
//			answrName : 답변자명
//			optional :
//			answrUserNo : 답변사용자번호
//			regUserNo : 등록사용자번호
//			regDtm : 등록일시
//			chgUserNo : 수정사용자번호
//			chgDtm : 수정일시
		para.clear();
		para.put("questNo", 1);
		para.put("answrNo", 0);
		para.put("answrCnts", "답변이요");
		para.put("answrName", "도깨비");
		
		try {
			c.call("cm/insert-qna-answer", para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void selectQnaGridList() {
//		mandatory :
//			contents : Q&A 검색 내용
//			startDate : 검색시간시작일자
//			endDate : 검색시간종료일자
//			questClCd : 질문구분코드
//			optional :
		para.clear();
		para.put("questClCd", QUEST_CL_CD.Frequency.getCode());
		para.put("startDate", 20220501);
		para.put("endDate", 20220531);
//		para.put("contents", "답변");

		try {
			c.call("cm/select-qna-grid-list", para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
