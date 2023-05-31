package subkjh.bas.net.soproth;

public class EchoSoproth  extends Soproth {

	@Override
	protected void initProcess() throws Exception {
	}

	@Override
	protected void process(byte[] bytes) throws Exception {
		
		System.out.println(new String(bytes));
		send(bytes);
	}

}