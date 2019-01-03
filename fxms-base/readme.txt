2019.01.03 by subkjh

1. subkjh.module.excel 사용법
1.1 SQL 결과를 바로 Excel로 넣기
	예)	
		MakeExcelByQid c = new MakeExcelByQid();
		c.makeExcel(tran, "QID_SELECT_EXCEL_LIST", null, "test", "tmp/excel-test-qid.xlsx");
		
		/**
		 * 
		 * @param tran 트랜잭션
		 * @param qid QID
		 * @param para QID 조건
		 * @param title 엑셀 타이틀
		 * @param filename 엑셀 파일명
		 * @throws Exception
		 */
		public void makeExcel(DbTrans tran, String qid, Object para, String title, String filename) throws Exception; 
	
		QID : QID_SELECT_EXCEL_LIST
		
			select	c1		as GSC100장비ID
					, c2 	as GSL200장비명
					, c3 	as GSL200장비TID
					, c4	as DSC120포트ID
					, c5	as DSL150포트설명					
					, c6	as GSL200카드명
			from 	table
	
	 	컬럼 1번째 의미
	 		G|K|D|S = G(그룹), K(키로 사용), D(단순 데이터), S(소계가 생김)
	 	컬럼 2번째 의미
	 		S|I|P|F = S(문자열), I(정수), F(실수), P(백분율)
	 	컬럼 3번째 의미
	 		C|L|R = C(중앙정렬), R(오른쪽정렬), L(왼쪽정렬)
	 	컬럼 4~6번째 의미
	 		숫자3자리 = 컬럼 넓이
		컬럼 7번째부터
	 		컬럼명 = 컬럼내용