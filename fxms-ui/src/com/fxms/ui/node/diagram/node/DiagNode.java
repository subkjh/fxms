package com.fxms.ui.node.diagram.node;

import java.util.Map;

import com.fxms.ui.OP_NAME;
import com.fxms.ui.node.diagram.event.DiagNodeBounds;
import com.fxms.ui.node.diagram.event.FxBounds;
import com.fxms.ui.node.diagram.vo.DiagNodeVo;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public interface DiagNode {

	public static enum NODE_TYPE {

		mo(DiagNodeMo.class)

		, box(DiagNodeBox.class)

		, text(DiagNodeText.class)

		, image(DiagNodeImage.class)

		, diagram(DiagNodeDiagram.class)

		, location(DiagNodeInlo.class)

		, status(DiagNodeStatus.class)

		, network(DiagNodeRing.class)

		;

		private Class<? extends DiagNode> classOf;

		private NODE_TYPE(Class<? extends DiagNode> classOf) {
			this.classOf = classOf;
		}

		public DiagNode makeNode(DiagNodeVo vo) throws Exception {
			DiagNode node = (DiagNode) classOf.newInstance();
			node.setDiagNodeVo(vo);
			return node;
		}

		public OP_NAME getOpName() {
			DiagNode node;
			try {
				node = (DiagNode) classOf.newInstance();
				return node.getOpName();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	public static final Border SELECTED = new Border(
			new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)));

	public static Button makeAlarmNode(Node target, FxBounds box, String text, int level) {
		Button node = new Button(text);
		node.getStyleClass().add("alarm-level-" + level);
		node.setOpacity(.9);
		node.setLayoutX(box.getX() + box.getWidth() / 2);
		node.setLayoutY(box.getY() + box.getHeight() / 2);

		node.setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));

		node.setMaxWidth(180);
		node.setPrefWidth(box.getWidth());

		return node;
	}

	public static DiagNode makeDiagNode(DiagNodeVo vo) {
		NODE_TYPE nodeType = NODE_TYPE.valueOf(vo.getDiagNodeType());
		try {
			return nodeType.makeNode(vo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Font makeFont(Map<String, Object> properties) {
		Object fontSize = properties.get("font-size");
		Object fontFamily = properties.get("font-family");
		Object fontStyle = properties.get("font-bold");

		Font font = new Font(10);
		try {
			font = Font.font(fontFamily.toString(),
					"y".equalsIgnoreCase(fontStyle.toString()) ? FontWeight.BOLD : FontWeight.NORMAL,
					Double.valueOf(fontSize.toString()));
		} catch (Exception e) {
			if (fontSize != null) {
				try {
					font = new Font(Double.valueOf(fontSize.toString()));
				} catch (Exception e2) {
				}
			}
		}

		return font;
	}

	public static Node makeSelection(FxBounds box) {
		Rectangle node = new Rectangle(box.getX() - 3, box.getY() - 3, box.getWidth() + 6, box.getHeight() + 6);
		node.setFill(Color.TRANSPARENT);
		node.setStrokeWidth(3);
		node.setStroke(Color.GOLD);
		node.setMouseTransparent(true);
		return node;
	}

	public static void setAttributes2Node(Map<String, Object> properties, Node node) {

		Object value;

		value = properties.get("opacity");
		if (value != null) {
			try {
				node.setOpacity(Double.valueOf(value.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (node instanceof Labeled) {

			((Labeled) node).setFont(makeFont(properties));

			value = properties.get("text-fill");
			if (value != null) {
				try {
					((Labeled) node).setTextFill(Color.valueOf(value.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		if (node instanceof Text) {

			((Text) node).setFont(makeFont(properties));

			value = properties.get("fill");
			if (value != null) {
				try {
					((Text) node).setFill(Color.valueOf(value.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static void setNode2Attributes(Node node, Map<String, Object> properties) {

		properties.put("opacity", node.getOpacity());

		if (node instanceof Labeled) {
			Labeled e = (Labeled) node;
			properties.put("font-size", e.getFont().getSize());
			properties.put("font-bold", e.getFont().getStyle().equalsIgnoreCase("bold") ? "Y" : "N");
			properties.put("font-family", e.getFont().getFamily());
			properties.put("text-fill", e.getTextFill().toString());
		}

		if (node instanceof Text) {
			Text e = (Text) node;
			properties.put("font-size", e.getFont().getSize());
			properties.put("font-bold", e.getFont().getStyle().equalsIgnoreCase("bold") ? "Y" : "N");
			properties.put("font-family", e.getFont().getFamily());
			properties.put("fill", e.getFill().toString());
		}

	}

	public FxBounds getBounds();

	public DiagNodeBounds getDiagNodeBounds();

	public DiagNodeVo getDiagNodeVo();

	public OP_NAME getOpName();

	public void setDiagNodeLocation(FxBounds box);

	public void setDiagNodeVo(DiagNodeVo vo);

	public void setProperties(Map<String, Object> properties);
}
