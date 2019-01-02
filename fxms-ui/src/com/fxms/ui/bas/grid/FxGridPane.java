package com.fxms.ui.bas.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.fxms.ui.bas.property.FxNode;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

public class FxGridPane extends BorderPane {

	private GridCfg cfg;
	final protected GridPane grid;
	private LinkedBlockingQueue<Node> queue;
	private List<Node> nodeList = new ArrayList<Node>();
	private FxGridTitle title;
	private ScrollPane sp;

	public FxGridPane(String title, GridCfg cfg) {

		sp = new ScrollPane();
		sp.setId("fx-grid-scroll-pane");
		sp.setOpacity(0.8);
		
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(cfg.hgap);
		grid.setVgap(cfg.vgap);
		grid.setGridLinesVisible(cfg.gridLineVisible);
		grid.setPadding(cfg.padding);

		if (title != null) {
			this.title = new FxGridTitle(this, title);
			setTop(this.title);
		}

		setCenter(sp);

		sp.setContent(grid);
		sp.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);

		queue = new LinkedBlockingQueue<Node>();

		this.cfg = cfg;

	}

	public GridPane getGrid() {
		return grid;
	}

	public void add(String id, Node node) {
		add(id, node, new GridNode(null, 1, 1));
	}

	public void add(String id, Node node, GridNode gridNode) {

		if (gridNode == null) {
			node.getProperties().put(GridNode.class, new GridNode(null, 1, 1));
		} else {
			node.getProperties().put(GridNode.class, gridNode);
		}

		node.getProperties().put(FxGridPane.class, this);

		node.setId(id);

		try {
			queue.put(node);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		relocate(null);
	}

	public void remove(String id) {
		Node node = null;

		for (Node e : nodeList) {
			if (id.equals(e.getId())) {
				node = e;
				break;
			}
		}

		if (node != null) {
			relocate(node);
		}

	}

	private void relocate(Node toRemoveNode) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Node toAddNode;
				while (true) {
					toAddNode = queue.poll();
					if (toAddNode != null) {
						toAddNode.getProperties().put("is-new", true);
						nodeList.add(toAddNode);
					} else {
						break;
					}
				}

				for (Node node : nodeList) {
					grid.getChildren().remove(node);
				}

				if (toRemoveNode != null) {
					grid.getChildren().remove(toRemoveNode);
					nodeList.remove(toRemoveNode);

					if (toRemoveNode instanceof FxNode) {
						((FxNode) toRemoveNode).onRemovedFromParent();
					}
				}

				sort(nodeList);

				GridXy gridXy = GridXy.getNewGridXy(cfg);

				GridNode gridNode;
				GridNode.XY xy;

				for (Node node : nodeList) {

					gridNode = (GridNode) node.getProperties().get(GridNode.class);

					setSize(node, gridNode.size);

					if (gridNode.xy == null) {
						xy = gridXy.find(gridNode.size.getColSize(), gridNode.size.getRowSize());
					} else {
						xy = gridNode.xy;
					}

					checkConstraints((xy.x + gridNode.size.getColSize() - 1), (xy.y + gridNode.size.getRowSize() - 1));

					grid.add(node, xy.x, xy.y, gridNode.size.getColSize(), gridNode.size.getRowSize());

					if (node instanceof FxNode) {
						Object obj = node.getProperties().get("is-new");
						if (obj != null) {
							((FxNode) node).onAddedInParent();
						}
					}
					node.getProperties().remove("is-new");

				}
			}
		});
	}

	private void checkConstraints(int x, int y) {

		for (int i = grid.getColumnConstraints().size(); i < x; i++) {
			grid.getColumnConstraints().add(new ColumnConstraints(cfg.nodeWidth));
		}

		for (int i = grid.getRowConstraints().size(); i < y; i++) {
			grid.getRowConstraints().add(new RowConstraints(cfg.nodeHeight));
		}

	}

	private void setSize(Node node, GridNode.Size nodeSize) {

		if (node instanceof Region) {
			Region region = (Region) node;

			double width = cfg.getNodeWidth(nodeSize.getColSize());
			double height = cfg.getNodeHeight(nodeSize.getRowSize());

			// System.out.println(node.getId() + " : " + width + ", " + height);

			region.setPrefSize(width, height);

			region.setMinHeight(height);
			region.setMinWidth(width);
		}
	}

	protected void sort(List<Node> nodeList) {
	}

}
