package subkjh.bas.net.soproth;

public abstract class JSonSoproth extends Soproth {

	public static String charset = "utf-8";

	@Override
	protected void process(byte[] bytesOrg) throws Exception {
		String msg;
		String json;
		int index;
		byte bytes[] = bytesOrg;

		if (logger.isTrace()) {
			logger.trace(new String(bytes));
		}

		while (true) {

			msg = new String(bytes, charset);

			try {
				index = getIndexEndJson(msg);
			} catch (Exception e) {
				index = -1;
			}

			if (index > 0) {
				json = msg.substring(0, index);
				processJSon(json);

				if (index == bytes.length)
					return;

				bytes = msg.substring(index).getBytes();
			} else {
				bytes = mergeNext(bytes);

				if (logger.isTrace()) {
					logger.trace("merged : " + new String(bytes));
				}

			}

			if (bytes == null || bytes.length == 0)
				break;
		}

	}

	/**
	 * 
	 * @param jsonMsg
	 * @throws Exception
	 */
	protected abstract void processJSon(String jsonMsg) throws Exception;

	private int getIndexEndJson(String msg) throws Exception {

		char ch;
		char chArray[] = msg.toCharArray();
		StringBuffer sb = new StringBuffer();
		String name = null;
		@SuppressWarnings("unused")
		String value = null;
		boolean inValue = false;
		boolean valueOk = false;
		boolean inName = false;
		boolean jsonStart = false;
		boolean forceCh = false;

		for (int index = 0; index < chArray.length; index++) {
			ch = chArray[index];

			if (forceCh) {
				forceCh = false;
				sb.append(ch);
				continue;
			}

			switch (ch) {

			case '{':
				if (jsonStart == false) {
					jsonStart = true;
					continue;
				}
				break;

			case '}':
				if (inValue == false && inName == false) {
					return index + 1;
				}
				break;

			case '"':
				if (inName == false) {
					inName = true;
					continue;
				} else if (inValue == false && inName) {
					name = sb.toString();
					sb = new StringBuffer();
					inName = false;
					continue;
				} else if (inValue) {
					valueOk = true;
				}
				break;

			case ':':
				if (name != null && inValue == false) {
					inValue = true;
					continue;
				}
				break;

			case ',': {
				if (inValue) {
					valueOk = true;
				}
				break;
			}

			case '\\':
				if (forceCh == false) {
					forceCh = true;
				}
				break;

			default:
				break;
			}

			if (valueOk) {
				valueOk = false;

				value = sb.toString();
				sb = new StringBuffer();
				inValue = false;

				if (chArray.length > index && chArray[index + 1] == ',') {
					index++;
				}

				// System.out.println(name + "=" + value);

				name = null;
				value = null;
				inName = false;
				inValue = false;
				continue;
			}

			sb.append(ch);

		}

		return -1;
	}

}
