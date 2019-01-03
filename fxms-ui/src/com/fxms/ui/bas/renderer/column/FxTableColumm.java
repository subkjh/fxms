package com.fxms.ui.bas.renderer.column;

import java.util.Calendar;

import com.fxms.ui.bas.lang.Lang;
import com.fxms.ui.bas.vo.UiAlarm;
import com.fxms.ui.bas.vo.UiAlarm.AlarmKind;
import com.fxms.ui.css.image.ImagePointer;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class FxTableColumm {

	public static abstract class AlarmStatus<DATA> extends TableColumn<DATA, ImageView> {

		public AlarmStatus(String text, int width) {
			super(text);

			setPrefWidth(width);

			setCellValueFactory(new Callback<CellDataFeatures<DATA, ImageView>, ObservableValue<ImageView>>() {
				public ObservableValue<ImageView> call(CellDataFeatures<DATA, ImageView> p) {

					Object value = getValue(p.getValue());

					ImageView imageView;

					if ("acked".equalsIgnoreCase(String.valueOf(value))) {
						imageView = new ImageView(
								new Image(ImagePointer.class.getResourceAsStream("s16x16/alarm-acked.png")));
					} else if ("cur".equalsIgnoreCase(String.valueOf(value))) {
						imageView = new ImageView(
								new Image(ImagePointer.class.getResourceAsStream("s16x16/alarm-on.png")));
					} else {
						imageView = new ImageView(
								new Image(ImagePointer.class.getResourceAsStream("s16x16/alarm-cleared.png")));
					}

					return new SimpleObjectProperty<ImageView>(imageView);
				}
			});
		}

		protected abstract Object getValue(DATA data);
	}

	public static abstract class AlarmLevel<DATA> extends TableColumn<DATA, String> {

		public AlarmLevel(String text, int width) {

			super(text);

			setPrefWidth(width);

			setStyle("-fx-alignment: center-right;");

			setCellValueFactory(new Callback<CellDataFeatures<DATA, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<DATA, String> p) {
					Object value = getValue(p.getValue());
					if (value == null) {
						return new SimpleStringProperty("");
					}
					return new SimpleStringProperty(value.toString());

				}
			});

			setCellFactory(e -> new TableCell<DATA, String>() {
				@Override
				public void updateItem(String item, boolean empty) {

					super.updateItem(item, empty);

					if (item == null || empty) {

						setText(null);

					} else {

						String ss[] = item.toString().split(",");
						AlarmKind alarmKind = AlarmKind.valueOf(ss[0]);
						int alarmLevel = Integer.valueOf(ss[1]);
						String styleClass = "alarm-" + UiAlarm.AlarmLevel.getAlarmLevel(alarmLevel).name() + "-"
								+ alarmKind;

//						setText(UiCode.alarmName[alarmLevel]);
						setText("");
						getStyleClass().clear();
						getStyleClass().add(styleClass);

						// if (alarmLevel == 1) {
						// this.setStyle("-fx-background-color: #f26a6a; -fx-alignment: center;");
						// } else if (alarmLevel == 2) {
						// this.setStyle("-fx-background-color: #f2c16a; -fx-alignment: center;
						// -fx-text-fill: black; -fx-opacity: 0.35");
						// } else if (item.intValue() == 3) {
						// this.setStyle("-fx-background-color: #f9f5ad; -fx-alignment: center;
						// -fx-text-fill: black;");
						// } else if (item.intValue() == 4) {
						// this.setStyle("-fx-background-color: #d9f9b9; -fx-alignment: center;");
						// }

					}
				}
			});
		}

		protected abstract Object getValue(DATA data);
	}

	public static abstract class StringData<DATA> extends TableColumn<DATA, String> {

		public StringData(String text, int width) {
			super(text);

			setPrefWidth(width);

			setCellValueFactory(new Callback<CellDataFeatures<DATA, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<DATA, String> p) {
					Object value = getValue(p.getValue());
					return new SimpleStringProperty(value == null ? "" : String.valueOf(value));
				}
			});
		}

		protected abstract Object getValue(DATA data);
	}

	public static abstract class DateData<DATA> extends TableColumn<DATA, Number> {

		public DateData(String text, int width) {
			super(text);

			setPrefWidth(width);

			setStyle("-fx-alignment: CENTER;");

			setCellValueFactory(new Callback<CellDataFeatures<DATA, Number>, ObservableValue<Number>>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ObservableValue<Number> call(CellDataFeatures<DATA, Number> p) {
					Object value = getValue(p.getValue());

					String ret = "";

					if (value instanceof Number) {
						ret = makeDate(String.valueOf(((Number) value).longValue()));
					} else if (value != null) {
						ret = makeDate(value.toString());
					}

					return new ReadOnlyObjectWrapper(ret);
				}
			});

		}

		private String makeDate(String s) {

			if (s == null || s.length() < 8) {
				return "";
			}

			StringBuffer sb = new StringBuffer();
			for (char ch : s.toCharArray()) {
				if (sb.length() == 4 || sb.length() == 7) {
					sb.append("-");
				} else if (sb.length() == 10) {
					sb.append(" ");
				} else if (sb.length() == 13 || sb.length() == 16) {
					sb.append(":");
				}

				sb.append(ch);
			}

			return sb.toString();
		}

		protected abstract Object getValue(DATA data);
	}

	public static abstract class UptimeData<DATA> extends TableColumn<DATA, Number> {

		public UptimeData(String text, int width) {

			super(text);

			setPrefWidth(width);

			setStyle("-fx-alignment: top-right;");

			setCellValueFactory(new Callback<CellDataFeatures<DATA, Number>, ObservableValue<Number>>() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public ObservableValue<Number> call(CellDataFeatures<DATA, Number> p) {
					Object value = getValue(p.getValue());

					String ret = "";

					if (value instanceof Number) {
						ret = makeDate(String.valueOf(((Number) value).longValue()));
					} else if (value != null) {
						ret = makeDate(value.toString());
					}

					return new ReadOnlyObjectWrapper(ret);
				}
			});

		}

		private String makeDate(String s) {

			if (s == null) {
				return "";
			}

			long uptime;

			if (s.length() == 14) {
				long mstime = getMstimeByHstime(s);
				uptime = (System.currentTimeMillis() - mstime) / 10;
			} else {
				try {
					uptime = Double.valueOf(s).longValue();
				} catch (Exception e) {
					return "-";
				}
			}

			uptime /= 100;

			if (uptime < 60) {
				return Lang.getText(Lang.Type.datetime, "방금");
			}

			int day = (int) (uptime / 86400);
			uptime = uptime % 86400;
			int hour = (int) (uptime / 3600);
			uptime = uptime % 3600;
			int min = (int) (uptime / 60);

			if (day > 0) {
				return day + Lang.getText(Lang.Type.datetime, "일 전");
			}
			if (hour > 0) {
				return hour + Lang.getText(Lang.Type.datetime, "시간 전");
			}
			if (min >= 1) {
				return min + Lang.getText(Lang.Type.datetime, "분 전");
			}

			return "";

		}

		protected abstract Object getValue(DATA data);
	}

	public static long getMstimeByHstime(String hstime) {

		if (hstime == null || hstime.length() < 8)
			return 0;

		Calendar c = Calendar.getInstance();

		int year = Integer.parseInt(hstime.substring(0, 4));
		int month = Integer.parseInt(hstime.substring(4, 6));
		int day = Integer.parseInt(hstime.substring(6, 8));

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);

		if (hstime.length() == 14) {
			int hh = Integer.parseInt(hstime.substring(8, 10));
			int mm = Integer.parseInt(hstime.substring(10, 12));
			int ss = Integer.parseInt(hstime.substring(12, 14));
			c.set(Calendar.HOUR_OF_DAY, hh);
			c.set(Calendar.MINUTE, mm);
			c.set(Calendar.SECOND, ss);
			c.set(Calendar.MILLISECOND, 0);
		} else {
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

		}

		return c.getTimeInMillis();

	}

}
