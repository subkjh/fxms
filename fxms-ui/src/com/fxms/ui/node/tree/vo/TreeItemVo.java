package com.fxms.ui.node.tree.vo;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.LocationVo;
import com.fxms.ui.bas.vo.folder.UiUserTreeVo;
import com.fxms.ui.css.image.ImagePointer;
import com.fxms.ui.dx.DxAlarmReceiver;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

public class TreeItemVo {

	private int topAlarmLevel = UiAlarm.AlarmLevel.unknown.getLevel();
	private List<TreeItem<TreeItemVo>> treeItemList;
	private final Object source;
	private Object state = null;

	public TreeItemVo(Object source) {
		this.source = source;
	}

	public Object getSource() {
		return source;
	}

	public int getTopAlarmLevel() {
		return topAlarmLevel;
	}

	public List<TreeItem<TreeItemVo>> getTreeItemList() {
		if (treeItemList == null) {
			treeItemList = new ArrayList<TreeItem<TreeItemVo>>();
		}

		return treeItemList;
	}

	public void setState(Object state) {
		this.state = state;
	}

	/**
	 * 
	 * @param topAlarmLevel
	 *            최상위 경보 등급
	 */
	public void setTopAlarmLevel(int topAlarmLevel) {

		this.topAlarmLevel = topAlarmLevel;

		// 1. 자신이 속한 TreeItem 아이콘 변경
		for (TreeItem<TreeItemVo> treeItem : getTreeItemList()) {
			treeItem.setGraphic(
					new ImageView(ImagePointer.getAlarmImage(treeItem.getValue().getSource(), topAlarmLevel)));
		}

		// 2. 상위 TreeItem 아이콘 변경

		TreeItem<TreeItemVo> parent;
		int alarmLevel;

		if (this instanceof TreeMoVo) {

			// 자신이 MO인 경우

			List<Long> moNoList = new ArrayList<Long>();
			for (TreeItem<TreeItemVo> treeItem : getTreeItemList()) {
				parent = treeItem.getParent();
				moNoList.clear();
				for (TreeItem<TreeItemVo> child : parent.getChildren()) {
					if (child.getValue() instanceof TreeMoVo) {
						moNoList.add(((TreeMoVo) child.getValue()).getMo().getMoNo());
					} else {

					}
				}
				alarmLevel = DxAlarmReceiver.getBorader().getTopAlarmLevel(moNoList);
				parent.getValue().setTopAlarmLevel(alarmLevel);
			}

		} else {

			// 자신이 그룹인 경우

			for (TreeItem<TreeItemVo> treeItem : getTreeItemList()) {
				parent = treeItem.getParent();
				alarmLevel = 999;

				for (TreeItem<TreeItemVo> child : parent.getChildren()) {
					if (child.getValue().getTopAlarmLevel() > 0 && child.getValue().getTopAlarmLevel() < alarmLevel) {
						alarmLevel = child.getValue().getTopAlarmLevel();
					}
				}
				parent.getValue().setTopAlarmLevel(alarmLevel);
			}
		}

	}

	@Override
	public String toString() {

		String ret;

		if (source instanceof LocationVo) {
			ret = ((LocationVo) source).getInloName();
		} else if (source instanceof Mo) {
			ret = ((Mo) source).getMoName();
		} else if (source instanceof UiUserTreeVo) {
			UiUserTreeVo vo = (UiUserTreeVo) source;
			ret = vo.getTreeName();
		} else {
			ret = source.toString();
		}
		if (state == null) {
			return ret;
		} else {
			return ret + " ( " + state + " )";
		}
	}

}
