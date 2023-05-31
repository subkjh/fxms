package test.bas.impl.handler;

import java.util.HashMap;
import java.util.Map;

import subkjh.bas.co.user.User;

public class DiagramHandlerTest extends HandlerTest {

	private final int DIAG_NO = 50000;

	public static void main(String[] args) {

		DiagramHandlerTest c = null;

		try {
			c = new DiagramHandlerTest();
//			c.insertDiagram();
//			c.insertDiagramNode();
//			c.insertDiagramAttr();
//			c.insertDiagramLine();
//			c.selectDiagram();
//			c.selectDiagramGridList();
//			c.selectDiagramAttrList();
//			c.selectDiagramLineList();
//			c.selectDiagramNodeList();
			
//			c.deleteDiagramAttr();
//			c.deleteDiagramLine();
//			c.deleteDiagramNode();
			c.deleteDiagram();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			Thread.sleep(5000);
			c.logout();
			Thread.sleep(5000);

			System.exit(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public DiagramHandlerTest() throws Exception {
		super("localhost", "diagram");
	}

	public void deleteDiagram() throws Exception {

//		*** delete-diagram ***
//		diagNo 다이아그램번호 Y

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		
		call("delete-diagram", para);


	}

	public void deleteDiagramAttr() throws Exception {
//		*** delete-diagram-attr ***
//		diagNo 다이아그램번호 Y
//		diagNodeNo 다이아그램노드번호 Y
//		attrName 속성명 Y
		
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		para.put("diagNodeNo", 2);
		para.put("attrName", "moNo");


		call("delete-diagram-attr", para);

	}

	public void deleteDiagramLine() throws Exception {
//		*** delete-diagram-line ***
//		diagNo 다이아그램번호 Y
//		diagNodeNo 다이아그램노드번호 Y
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		para.put("diagNodeNo", 2);
		call("delete-diagram-line", para);

	}

	public void deleteDiagramNode() throws Exception {
//		*** delete-diagram-node ***
//		diagNo 다이아그램번호 Y
//		diagNodeNo 다이아그램노드번호 Y
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		para.put("diagNodeNo", 2);
		call("delete-diagram-node", para);

	}

	public void insertDiagram() throws Exception {

//		*** insert-diagram ***
//		diagNo 다이아그램번호 Y
//		diagTitle 다이아그램제목 Y
//		opId 기능ID Y
//		ownerUserNo 소유사용자번호 Y
//		shrUgrpNo 공유운영자그룹번호 Y
//		regUserNo 등록사용자번호 N
//		regDtm 등록일시 N
//		chgUserNo 수정사용자번호 N
//		chgDtm 수정일시 N

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", -1);
		para.put("diagTitle", "test222");
		para.put("opId", "test");
		para.put("ownerUserNo", -1);
		para.put("shrUgrpNo", User.USER_GROUP_EMPTY);

		call("insert-diagram", para);
	}

	public void insertDiagramAttr() throws Exception {

//		*** insert-diagram-attr ***
//		diagNo 다이아그램번호 Y
//		diagNodeNo 다이아그램노드번호 Y
//		attrName 속성명 Y
//		attrVal 속성값 Y
//		regUserNo 등록사용자번호 N
//		regDtm 등록일시 N
//		chgUserNo 수정사용자번호 N
//		chgDtm 수정일시 N

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		para.put("diagNodeNo", 2);
		para.put("attrName", "moNo");
		para.put("attrVal", 100001);

		call("insert-diagram-attr", para);

	}

	public void insertDiagramLine() throws Exception {

//		*** insert-diagram-line ***
//		diagNo 다이아그램번호 Y
//		diagNodeNo 다이아그램노드번호 Y
//		linkDiagNodeNo1 연결다이아그램노드번호1 Y
//		linkDiagNodeNo2 연결다이아그램노드번호2 Y
//		regUserNo 등록사용자번호 N
//		regDtm 등록일시 N
//		chgUserNo 수정사용자번호 N
//		chgDtm 수정일시 N

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		para.put("diagNodeNo", 3);
		para.put("linkDiagNodeNo1", 1);
		para.put("linkDiagNodeNo2", 2);

		call("insert-diagram-line", para);

	}

	public void insertDiagramNode() throws Exception {
//		*** insert-diagram-node ***
//		diagNo 다이아그램번호 Y
//		diagNodeNo 다이아그램노드번호 Y
//		diagNodeTypeCd 다이아그램노드유형 Y
//		diagNodeX 다이아그램노드X좌표 Y
//		diagNodeY 다이아그램노드Y좌표 Y
//		diagNodeWdth 다이아그램노드넓이 Y
//		diagNodeHght 다이아그램노드높이 Y
//		regUserNo 등록사용자번호 N
//		regDtm 등록일시 N
//		chgUserNo 수정사용자번호 N
//		chgDtm 수정일시 N

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		para.put("diagNodeNo", 2);
		para.put("diagNodeTypeCd", "test");
		para.put("diagNodeX", 10);
		para.put("diagNodeY", 12);
		para.put("diagNodeWdth", 13);
		para.put("diagNodeHght", 14);

		call("insert-diagram-node", para);

	}

	public void selectDiagram() throws Exception {
//		*** select-diagram ***
//		diagNo 다이아그램번호 Y

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);

		call("select-diagram", para);
	}

	public void selectDiagramAttrList() throws Exception {
//		*** select-diagram-attr-list ***
//		diagNo 다이아그램번호 Y

		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		call("select-diagram-attr-list", para);
	}

	public void selectDiagramGridList() throws Exception {

//		*** select-diagram-grid-list ***

		Map<String, Object> para = new HashMap<String, Object>();
		call("select-diagram-grid-list", para);
	}

	public void selectDiagramLineList() throws Exception {
//		*** select-diagram-line-list ***
//		diagNo 다이아그램번호 Y
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		call("select-diagram-line-list", para);
	}

	public void selectDiagramNodeList() throws Exception {
//		*** select-diagram-node-list ***
//		diagNo 다이아그램번호 Y
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("diagNo", DIAG_NO);
		call("select-diagram-node-list", para);
	}

}