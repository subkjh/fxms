package subkjh.bas.net.soproth;

public abstract class FxJsonSoproth extends Soproth {

	public static void main(String[] args) {
		// byte pdu1[] = FxJsonSoproth.makePduByte("TEST1-lagoon");
		// byte pdu2[] = FxJsonSoproth.makePduByte("TEST2-강명중");
		String json = "{\"type\":2,\"currentTime\":\"1569472247054\",\"omn33810\":{\"trmsEquipDablNum\":321240,\"auditId\":\"ADAMS_TMN\",\"auditDtm\":\"Sep 26, 2019 10:06:35 AM\",\"dablGrCd\":\"01\",\"dablCd\":\"1030003\",\"dablMsgCtt\":\"Loss Of Signal\",\"occrDtm\":\"1569459994798\",\"occrRcvDtm\":\"Sep 26, 2019 10:06:34 AM\",\"equipId\":\"000000157687\",\"equipNm\":\"(철거-17.01.10)CWDM_DW_동작-인터넷진흥원\",\"equipTidVal\":\"(철거-17.01.10)인터넷진흥-동작\",\"equipIpAddr\":\"\",\"equipMdlCd\":\"0000190400\",\"tpoCd\":\"N44966\",\"scardDesc\":\"\",\"portDesc\":\"1-7-1\",\"dablOccrLocDesc\":\"LAGOON-LOC\",\"allMsgCtt\":\"by.lagoon\",\"trmsNetEquipMsgMgmtNum\":10942,\"cmprCharStrVal\":\"Loss Of Signal\",\"operDablYn\":false,\"emcyDablYn\":false,\"dmgLineCnt\":0,\"dablDupCnt\":0,\"emsId\":\"\",\"emsAlmVal\":\"\",\"rlseDtm\":\"1569460019001\",\"rlseRcvDtm\":\"Sep 26, 2019 10:06:59 AM\",\"dablSendYn\":true,\"dablSendDtm\":\"Sep 26, 2019 1:30:47 PM\",\"dablSendRsltVal\":\"\",\"dablRlseSendYn\":true,\"dablRlseSendDtm\":\"Sep 26, 2019 1:30:47 PM\",\"dablRlseSendRsltVal\":\"0\"},\"omn33830\":{\"trmsRingDablNum\":4041,\"trmsEquipDablNum\":321240,\"netNum\":\"551384\",\"nwNm\":\"CWDM_DW_동작-인터넷진흥원\",\"trmsNetTopoTypCd\":\"0013\",\"trmsNetUsgCd\":\"00\",\"trmsNetEquipCapaClCd\":\"0000\",\"dablSendYn\":false,\"netDablCd\":\"035001\"},\"eventType\":\"TeamsAlarmFxEvent\",\"status\":\"added\"}";
		byte pdu3[] = FxJsonSoproth.makePduByte(json);
		byte bytes[] = new byte[pdu3.length];
		System.arraycopy(pdu3, 0, bytes, 0, pdu3.length);

		// byte bytes[] = new byte[pdu1.length + pdu2.length];
		// System.arraycopy(pdu1, 0, bytes, 0, pdu1.length);
		// System.arraycopy(pdu2, 0, bytes, pdu1.length, pdu2.length);
		FxJsonSoproth s = new FxJsonSoproth() {

			@Override
			protected void processBody(int length, byte[] bytes) {
				System.out.println(length);
				System.out.println(new String(bytes));
			}

			@Override
			protected void initProcess() throws Exception {
				// TODO Auto-generated method stub

			}

		};

		try {
			s.process(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process(byte[] orgBytes) throws Exception {

		int length;
		byte bytes[] = orgBytes;
		byte body[];
		while (true) {

			if (bytes.length >= 10) {
				length = Integer.parseInt(new String(bytes, 0, 10));

				if (bytes.length >= (length + 10)) {
					body = new byte[length];
					System.arraycopy(bytes, 10, body, 0, length);
					processBody(length, body);

					byte tmp[] = new byte[bytes.length - (length + 10)];
					if (tmp.length == 0) {
						break;
					} else {
						System.arraycopy(bytes, (length + 10), tmp, 0,
								tmp.length);
						bytes = tmp;
						continue;
					}
				}
			}

			bytes = mergeNext(bytes);
		}
	}

	protected abstract void processBody(int length, byte bytes[]);

	public static byte[] makePduByte(String body) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("%010d", body.getBytes().length));
		sb.append(body);
		return sb.toString().getBytes();
	}
}
