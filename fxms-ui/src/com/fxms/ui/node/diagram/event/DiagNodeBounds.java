package com.fxms.ui.node.diagram.event;

import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class DiagNodeBounds {

	public static final int GRID_UNIT = 8;
	public final int CURSOR_WITDH = 8;

	double sceneX;
	double sceneY;
	double nodeX;
	double nodeY;
	double nodeWidth;
	double nodeHeight;
	public EventType<MouseEvent> mouseEvent;
	private boolean allowWidthOver;
	private boolean allowHeightOver;

	public DiagNodeBounds() {
		allowWidthOver = false;
		allowHeightOver = false;
	}

	public DiagNodeBounds(boolean allowWidthOver, boolean allowHeightOver) {
		this.allowWidthOver = allowWidthOver;
		this.allowHeightOver = allowHeightOver;
	}

	public void setPressedPointer(MouseEvent e, FxBounds box) {
		sceneX = e.getSceneX();
		sceneY = e.getSceneY();
		nodeX = box.getX();
		nodeY = box.getY();
		nodeWidth = ((int) (box.getWidth() / GRID_UNIT)) * GRID_UNIT;
		nodeHeight = ((int) (box.getHeight() / GRID_UNIT)) * GRID_UNIT;
		mouseEvent = MouseEvent.MOUSE_PRESSED;

		// System.out.println("set position : " + sceneX + ", " + sceneY + " : " + nodeX
		// + ", " + nodeY + " : " + nodeWidth
		// + ", " + nodeHeight);
	}

	public FxBounds resize(Node node, MouseEvent e) {

		Cursor cursor = node.getScene().getCursor();

		double changedX = (e.getSceneX() - sceneX);
		double changedY = (e.getSceneY() - sceneY);
		double x = -1;
		double y = -1;
		double width = -1;
		double height = -1;

		changedX = ((int) (changedX / GRID_UNIT)) * GRID_UNIT;
		changedY = ((int) (changedY / GRID_UNIT)) * GRID_UNIT;

		if (changedX == 0 && changedY == 0) {
			return null;
		}

		// System.out.println("changed : " + changedX + "," + changedY);

		if (cursor == Cursor.N_RESIZE || cursor == Cursor.NE_RESIZE || cursor == Cursor.NW_RESIZE) {
			if (nodeY + changedY > GRID_UNIT) {
				y = nodeY + changedY < GRID_UNIT ? GRID_UNIT : nodeY + changedY;
				if (nodeHeight + (-1 * changedY) >= GRID_UNIT) {
					height = nodeHeight + (-1 * changedY);
				}
				if (y >= nodeY + nodeHeight) {
					return null;
				}
			}
		}

		if (cursor == Cursor.S_RESIZE || cursor == Cursor.SE_RESIZE || cursor == Cursor.SW_RESIZE) {
			if (nodeHeight + changedY >= GRID_UNIT
					&& nodeY + nodeHeight + changedY <= node.getScene().getHeight() - GRID_UNIT) {
				height = nodeHeight + changedY;
			}
		}

		if (cursor == Cursor.E_RESIZE || cursor == Cursor.SE_RESIZE || cursor == Cursor.NE_RESIZE) {
			if (nodeWidth + changedX >= GRID_UNIT
					&& nodeX + nodeWidth + changedX <= node.getScene().getWidth() - GRID_UNIT) {
				width = nodeWidth + changedX;
			}
		}

		if (cursor == Cursor.W_RESIZE || cursor == Cursor.SW_RESIZE || cursor == Cursor.NW_RESIZE) {
			if (nodeX + changedX > GRID_UNIT) {
				x = nodeX + changedX < GRID_UNIT ? GRID_UNIT : nodeX + changedX;
				if (nodeWidth + (-1 * changedX) >= GRID_UNIT) {
					width = nodeWidth + (-1 * changedX);
				}
				if (x >= nodeX + nodeWidth) {
					return null;
				}
			}
		}

		// System.out.println("resize : " + x + ", " + y + " : " + width + ", " +
		// height);

		return new FxBounds(x, y, width, height);
	}

	public static int makeGridUnit(double x) {
		return ((int) (x / GRID_UNIT)) * GRID_UNIT;

	}

	public FxBounds move(Node node, MouseEvent e) {

		double x = nodeX + (e.getSceneX() - sceneX);
		double y = nodeY + (e.getSceneY() - sceneY);

		// System.out.println("org = " + x + "," + y + " size=" + nodeWidth + "," +
		// nodeHeight);

		if (x < GRID_UNIT) {
			x = GRID_UNIT;
		}

		if (y < GRID_UNIT) {
			y = GRID_UNIT;
		}

		if (allowWidthOver == false) {
			// 범위를 벗어나지 않게 조절한다.
			if ((x + nodeWidth > node.getScene().getWidth())) {
				x = node.getScene().getWidth() - nodeWidth - GRID_UNIT;
			}
		}

		if (allowHeightOver == false) {
			// 범위를 벗어나지 않게 조절한다.
			if ((y + nodeHeight > node.getScene().getHeight())) {
				y = node.getScene().getHeight() - nodeHeight - GRID_UNIT;
			}
		}
		x = ((int) (x / GRID_UNIT)) * GRID_UNIT;
		y = ((int) (y / GRID_UNIT)) * GRID_UNIT;

		// System.out.println("new = " + x + "," + y + " size=" + nodeWidth + "," +
		// nodeHeight);

		return new FxBounds(x, y, -1, -1);
	}

	public FxBounds move(MouseEvent e) {

		double x = nodeX + (e.getSceneX() - sceneX);
		double y = nodeY + (e.getSceneY() - sceneY);

		if (x < GRID_UNIT) {
			x = GRID_UNIT;
		}

		if (y < GRID_UNIT) {
			y = GRID_UNIT;
		}

		x = ((int) (x / GRID_UNIT)) * GRID_UNIT;
		y = ((int) (y / GRID_UNIT)) * GRID_UNIT;

		return new FxBounds(x, y, -1, -1);
	}

	public void checkResize(Node node, double width, double height, MouseEvent e) {

		double x = e.getX();
		double y = e.getY();

		if (y < CURSOR_WITDH && x < CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.NW_RESIZE);
		} else if (y > height - CURSOR_WITDH && x < CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.SW_RESIZE);
		} else if (y < CURSOR_WITDH && x > width - CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.NE_RESIZE);
		} else if (y > height - CURSOR_WITDH && x > width - CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.SE_RESIZE);
		} else if (y < CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.N_RESIZE);
		} else if (y > height - CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.S_RESIZE);
		} else if (x < CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.W_RESIZE);
		} else if (x > width - CURSOR_WITDH) {
			node.getScene().setCursor(Cursor.E_RESIZE);
		} else {
			node.getScene().setCursor(Cursor.HAND);
		}

		// System.out.println(node.getScene().getCursor());

	}

}
