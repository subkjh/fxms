package com.fxms.ui.bas.vo;

import java.util.ArrayList;
import java.util.List;

import com.fxms.ui.bas.mo.Mo;

public class LocationTreeVo {

	public static List<LocationTreeVo> makeTreeLocation(List<LocationVo> pool) {

		List<LocationTreeVo> ret = new ArrayList<LocationTreeVo>();
		LocationVo location;

		for (int i = pool.size() - 1; i >= 0; i--) {
			location = pool.get(i);

			if (location.getUpperInloNo() == -1 && location.getInloNo() > 0) {
				ret.add(new LocationTreeVo(location));
				pool.remove(i);
			}
		}

		setChildren(ret, pool);

		return ret;
	}

	private static void setChildren(List<LocationTreeVo> ret, List<LocationVo> pool) {
		for (LocationTreeVo loc : ret) {

			setLocation(loc, pool);

			setChildren(loc.getChildren(), pool);

			if (pool.size() == 0) {
				return;
			}
		}
	}

	private static void setLocation(LocationTreeVo upper, List<LocationVo> pool) {

		LocationTreeVo me;

		for (int i = pool.size() - 1; i >= 0; i--) {
			if (pool.get(i).getUpperInloNo() == upper.me.getInloNo()) {

				me = new LocationTreeVo(pool.get(i));
				// add children
				upper.getChildren().add(me);

				// add parent
				me.getParents().add(upper);

				pool.remove(i);
			}
		}
	}

	private List<LocationTreeVo> children;

	private List<LocationTreeVo> parents;

	private List<Mo> moList;

	private LocationVo me;

	public LocationTreeVo(LocationVo me) {
		this.me = me;
	}

	public LocationTreeVo(String name) {
		me = new LocationVo();
		me.setInloName(name);
	}

	public List<LocationTreeVo> getChildren() {
		if (children == null) {
			children = new ArrayList<LocationTreeVo>();
		}
		return children;
	}

	public LocationVo getMe() {
		return me;
	}

	public List<Mo> getMoList() {
		if (moList == null) {
			moList = new ArrayList<Mo>();
		}
		return moList;
	}

	public List<LocationTreeVo> getParents() {
		if (parents == null) {
			parents = new ArrayList<LocationTreeVo>();
		}
		return parents;
	}

	@Override
	public String toString() {
		return me == null ? "" : me.getInloName();
	}

}
