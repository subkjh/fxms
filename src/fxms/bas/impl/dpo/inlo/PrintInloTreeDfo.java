package fxms.bas.impl.dpo.inlo;

import java.util.List;

import fxms.bas.impl.dpo.FxDfo;
import fxms.bas.impl.dpo.FxFact;
import fxms.bas.vo.InloExt;

public class PrintInloTreeDfo implements FxDfo<List<InloExt>, Void> {

	@Override
	public Void call(FxFact fact, List<InloExt> data) throws Exception {
		printInlo(data);
		return null;
	}

	public void printInlo(List<InloExt> locList) throws Exception {

		for (InloExt inlo : locList) {
			printInlo("", inlo);
		}
	}

	private void printInlo(String prev, InloExt inlo) throws Exception {

		System.out.println(prev + inlo);

		for (InloExt child : inlo.getChildren()) {
			printInlo(prev + "  ", child);
		}

	}

}
