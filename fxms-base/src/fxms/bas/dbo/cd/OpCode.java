package fxms.bas.dbo.cd;

public class OpCode extends FX_CD_OP {

	@Override
	public String toString() {
		return "(" + getOpNo() + ")" + getOpName();
	}

}
