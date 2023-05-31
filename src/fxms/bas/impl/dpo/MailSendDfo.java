package fxms.bas.impl.dpo;

import java.util.Map;

public class MailSendDfo implements FxDfo<Map<String, Object>, Boolean> {

	public static void main(String[] args) {
		MailSendDfo dfo = new MailSendDfo();

		try {
			dfo.send("subkjh@thingspire.com", "test", "AAAAA");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Boolean call(FxFact fact, Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean send(String to, String subject, String text) throws Exception {
		new SendMail().sendmail(to, subject, text, null);
		return true;
	}

}
