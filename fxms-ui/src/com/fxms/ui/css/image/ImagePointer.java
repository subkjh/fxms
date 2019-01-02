package com.fxms.ui.css.image;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.fxms.ui.bas.mo.Mo;
import com.fxms.ui.bas.vo.LocationVo;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

public class ImagePointer {

	public static final Image managedIcon = getImage("s16x16/mo-managed.png");
	public static final Image unmanagedIcon = getImage("s16x16/mo-unmanaged.png");
	private static final Image location_type_country = getImage("s16x16/location-country.jpg");
	private static final Image location_type_company = getImage("s16x16/location-company.jpg");
	private static final Image location_type_plant = getImage("s16x16/location-plant.jpg");
	private static final Image location_type_other = getImage("s16x16/location-other.png");
	private static final Image cursor_line = getImage("s16x16/line.png");
	private static ImageCursor lineCursor = null;

	public static Image getAlarmGoodGroupImage() {
		return getImage("s16x16/alarm-level-0-group.jpg");
	}

	public static Image getAlarmGoodMoImage() {
		return getImage("s16x16/alarm-level-0-mo.jpg");
	}

	public static Image getAlarmGoodTopImage() {
		return getImage("s16x16/alarm-level-0-top.jpg");
	}

	public static Image getAlarmImage(Object target, int alarmLevel) {
		if (target instanceof Mo) {
			if (alarmLevel >= 1 && alarmLevel <= 4) {
				return getImage("s16x16/alarm-level-" + alarmLevel + ".jpg");
			}
			return getAlarmGoodMoImage();
		} else {
			if (alarmLevel >= 1 && alarmLevel <= 4) {
				return getImage("s16x16/alarm-level-" + alarmLevel + "-group.jpg");
			}
			return getAlarmGoodGroupImage();
		}
	}

	public static Image getImage(String name) {
		
		InputStream inputStream = ImagePointer.class.getResourceAsStream(name);
		if (inputStream != null) {
			return new Image(inputStream);
		}
		
		return null;
	}

	public static List<String> getImageList() {

		List<String> result = new ArrayList<String>();
		URLClassLoader cld = (URLClassLoader) Thread.currentThread().getContextClassLoader();
		String packageName = ImagePointer.class.getPackage().getName().replaceAll("\\.", "/");
		try {
			for (URL jarURL : cld.getURLs()) {
				// System.out.println("JAR: " + jarURL.getPath());
				getClassesInSamePackageFromJar(result, packageName, jarURL.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> ret = new ArrayList<String>();
		for (String name : result) {
			if (name.indexOf(".class") >= 0) {
				continue;
			} else {
				name = name.replace(packageName + "/", "");
				ret.add(name);
			}
		}

		return ret;
	}

	public static ImageCursor getLineCursor() {

		if (lineCursor == null) {
			lineCursor = new ImageCursor(ImagePointer.cursor_line);
		}

		return lineCursor;
	}

	public static Image getLocationTypeImage(LocationVo location) {

		if (location.getInloType().equalsIgnoreCase("country")) {
			return location_type_country;
		} else if (location.getInloType().equalsIgnoreCase("company")) {
			return location_type_company;
		} else if (location.getInloType().equalsIgnoreCase("plant")) {
			return location_type_plant;
		}

		return location_type_other;
	}

	public static Image getMoImage(Mo mo) {
		try {
			return getImage("s16x16/mo-" + mo.getMoClass() + ".jpg");
		} catch (Exception e) {
			return getImage("s16x16/mo-managed.png");
		}
	}

	public static Image getMoImage(String moClass) {
		return getImage("s16x16/mo-" + moClass + ".jpg");
	}

	public static void main(String[] args) {
		// ImagePointer.getImageList();
		ImagePointer.getImage("aa");
	}

	private static void getClassesInSamePackageFromFolder(List<String> result, String packageName, String jarPath,
			File file) {

		if (file.exists() == false) {
			return;
		}

		String entryName;

		for (File e : file.listFiles()) {
			if (e.isDirectory()) {
				getClassesInSamePackageFromFolder(result, packageName, jarPath, e);
			} else if (e.isFile()) {
				entryName = e.getPath().substring(jarPath.length() - 1);
				entryName = entryName.replace(File.separatorChar, '/');
				if (entryName.startsWith(packageName)) {
					result.add(entryName);
				}
			}
		}

	}

	private static void getClassesInSamePackageFromJar(List<String> result, String packageName, String jarPath) {

		try {
			File file = new File(jarPath);
			if (file.isDirectory()) {
				getClassesInSamePackageFromFolder(result, packageName, jarPath, file);
				return;
			}
		} catch (Exception e) {
			return;
		}

		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarPath);
			Enumeration<JarEntry> en = jarFile.entries();
			while (en.hasMoreElements()) {
				JarEntry entry = en.nextElement();
				String entryName = entry.getName();

				if (entryName != null && entryName.startsWith(packageName)) {
					result.add(entryName);
				}

			}
		} catch (Exception e) {

		} finally {
			try {
				if (jarFile != null) {
					jarFile.close();
				}
			} catch (Exception e) {
			}
		}

	}

}
