package fxms.bas.co;

/**
 * 
 * @author subkjh<br>
 *         비교코드
 *
 */
public enum CMPR_CD {

	/** 감소 */
	DC,
	/** difference */
	DF,
	/** 같으면 */
	EQ,
	/** 보다 크거나 같으면 */
	GE,
	/** 보다 크면 */
	GT,
	/** 증가 */
	IC,
	/** 보다 작거나 같으면 */
	LE,
	/** 보다 작으면 */
	LT,
	/** 같지 않으면 */
	NE,
	/** 비교하지 않음 */
	NO,
	/** 무조건 */
	OK,
	/** rate */
	RT,
	/** IQR */
	IQR;

	public static CMPR_CD getCompare(String name) {
		for (CMPR_CD c : CMPR_CD.values()) {
			if (c.name().equalsIgnoreCase(name))
				return c;
		}

		return EQ;
	}

	private CMPR_CD() {
	}

}
