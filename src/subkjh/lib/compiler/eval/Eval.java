package subkjh.lib.compiler.eval;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author subkjh
 * 
 */
public class Eval {

	public enum Func {
		sum, min, max, avg, count;

		public static Func fromName(String s) {
			for (Func f : Func.values()) {
				if (f.name().equalsIgnoreCase(s)) return f;
			}
			return null;
		}

		public static EvalFunc makeFunc(Func f, Object item) {
			EvalFunc ef;
			if (f == sum) ef = new Sum();
			else if (f == min) ef = new Min();
			else if (f == max) ef = new Max();
			else if (f == avg) ef = new Avg();
			else if (f == count) ef = new Count();
			else {
				return null;
			}

			ef.setItem(item);
			return ef;
		}
	}

	public enum Oper {

		PLUS('+', true, true) //
		, MINUS('-', true, true)//
		, DIVIDE('/', true, false) //
		, MULTIPLY('*', true, false) //
		, POW('^', true, false) //

		, L_PARENS('(', false, true), R_PARENS(')', false, true), BLANK(' ', false, true), TAB('\t', false, true);

		public static Oper getOperator(char ch) {
			for (Oper o : Oper.values()) {
				if (o.symbol == ch) return o;
			}
			return null;
		}

		private char symbol;
		private boolean op;
		private boolean number;

		Oper(char symbol, boolean op, boolean number) {
			this.symbol = symbol;
			this.op = op;
			this.number = number;
		}
	}

	public static final Pattern ID = Pattern.compile("[a-zA-z]+[a-zA-z0-9_]*");
	public static final Pattern Num = Pattern.compile("[0-9.]+");
	public static final Pattern Str = Pattern.compile("'[0-9.]+'");

	public static Number getNumber(Object obj) throws Exception {

		if (obj instanceof Number) {
			return (Number) obj;
		}
		else if (obj instanceof String) {
			try {
				return Long.parseLong(obj.toString());
			}
			catch (Exception e) {
				return Double.parseDouble(obj.toString());
			}
		}
		else {
			throw new Exception("Not Number or String - " + obj.getClass().getName());
		}
	}

	public static void main(String[] args) throws Exception {
		Eval t = new Eval();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("x", 3);
		map.put("y", 5);
		List<Number> num = new ArrayList<Number>();
		num.add(10);
		num.add(20);
		num.add(30);
		map.put("z", num);
		System.out.println(map);

		String s = " y-(2.5*10) ";
		System.out.println(s + " = " + (t.compute(s, map).doubleValue() == -20D));
		s = "1 + 2";
		System.out.println(s + " = " + (t.compute(s, map).doubleValue() == 3D));
		s = "( x * 10 ) + ( y + 2.5 + 10 )";
		System.out.println(s + " = " + (t.compute(s, map).doubleValue() == 47.5D));
		s = s + "+" + "sum(z)";
		System.out.println(s + " = " + (t.compute(s, map).doubleValue() == ( 47.5 + 60)));
		s = "x * ( 100  / y  ) ";
		System.out.println(s + " = " + (t.compute(s, map).doubleValue() == 60D));
		s = "((( ( x * 100 ) / y )))";
		System.out.println(s + " = " + (t.compute(s, map).doubleValue() == 60D));
		s = "( x * 100 ) / y + ( 2 ^ 10)";
		System.out.println(s + " = " + t.compute(s, map).doubleValue());
		s = " 200 / ( -10 )";
		System.out.println(s + " = " + (t.compute(s, map).doubleValue() == -20D));

		s = " 1000000 / ( -1000 * -1000*1000 )";
		System.out.println(s + " = " + t.compute(s, map).doubleValue());


		s = " .1 * .2";
		System.out.println(s + " = " + t.compute(s, map).doubleValue());

		
	}

	/**
	 * 계산식을 처리합니다. 계산식에 변수를 지정한 경우 변수집에 해당 변수의 값이 존재해야 합니다.
	 * 
	 * @param content
	 *            계산식
	 * @param varMap
	 *            변수집
	 * @return 계산된 값
	 * @throws Exception
	 */
	public Number compute(String content, Map<String, Object> varMap) throws Exception {

		Deque<Object> stack = parse(content);

		return compute(stack, varMap);

	}

	private Number compute(Deque<Object> stack, Map<String, Object> varMap) throws Exception {

		resort(stack);

		Number x, y = null;
		Object obj;
		x = getValue(stack.pop(), varMap);

		while (stack.size() > 0) {

			obj = stack.pop();
			if (obj instanceof Oper) {
				if (y == null) throw new Exception("it's not x,y,operation ");
				x = compute((Oper) obj, x, y);
			}
			else {
				y = getValue(obj, varMap);
			}
		}

		return x;
	}

	private Number compute(Oper oper, Number x, Number y) {

		Number ret;

		if (oper == Oper.PLUS) ret = x.doubleValue() + y.doubleValue();
		else if (oper == Oper.MINUS) ret = x.doubleValue() - y.doubleValue();
		else if (oper == Oper.DIVIDE) ret = x.doubleValue() / y.doubleValue();
		else if (oper == Oper.MULTIPLY) ret = x.doubleValue() * y.doubleValue();
		else if (oper == Oper.POW) ret = Math.pow(x.doubleValue(), y.doubleValue());
		else ret = null;

		// System.out.println(">>> " + x + " " + oper.symbol + " " + y + " = " +
		// ret);

		return ret;
	}

	@SuppressWarnings("unchecked")
	private Number getValue(Object obj, Map<String, Object> varMap) throws Exception {

		//
		if (obj instanceof Deque) {
			Deque<Object> entry = (Deque<Object>) obj;
			return compute(entry, varMap);
		}
		else if (obj instanceof Number) {
			return (Number) obj;
		}
		else if (obj instanceof EvalFunc) {
			EvalFunc func = (EvalFunc) obj;
			return func.compute(varMap);
		}
		else {
			Object val = varMap.get(obj + "");
			if (val == null) throw new Exception("The Variable " + obj + " is not defined");
			try {
				return getNumber(val);
			}
			catch (Exception e) {
				throw new Exception(obj + "'s Value is invalid : " + e.getMessage());
			}

		}
	}

	private Object makeToken(String s) throws Exception {

		Func f = Func.fromName(s);

		if (f != null) {
			return f;
		}
		else {

			Matcher m = ID.matcher(s);
			if (m.matches()) {
				return s;
			}
			else {
				try {
					return Long.parseLong(s);
				}
				catch (Exception e) {
					return Double.parseDouble(s);
				}
			}
		}
	}

	private Deque<Object> parse(String content) throws Exception {

		char chArr[] = content.toCharArray();
		// int i = 0;
		ArrayDeque<Object> stack = new ArrayDeque<Object>();
		parse(chArr, 0, stack);
		// ArrayDeque<Object> stack = mainStack;
		// ArrayDeque<Object> stackPrev = null;
		// Object item;
		// Object oPrev = null;
		//
		// MAIN: while (i < chArr.length) {
		//
		// ch = chArr[i++];
		//
		// op = Oper.getOperator(ch);
		// oPrev = (stack.size() > 0 ? stack.getLast() : null);
		//
		// System.out.println(op + ", " + oPrev);
		//
		// if (op != null && ((oPrev instanceof Oper) == false || (op !=
		// Oper.PLUS && op != Oper.MINUS))) {
		//
		// if (sb.toString().length() > 0) {
		//
		// item = makeToken(sb.toString());
		//
		// if (item instanceof Func) {
		// i--;
		// sb = new StringBuffer();
		// Func func = (Func) item;
		// while (i < chArr.length) {
		// ch = chArr[i++];
		// op = Oper.getOperator(ch);
		// if (op == Oper.L_PARENS) {
		// // nothing
		// }
		// else if (op == Oper.R_PARENS) {
		// Object funcPara = makeToken(sb.toString());
		// stack.add(Func.makeFunc(func, funcPara));
		// sb = new StringBuffer();
		// continue MAIN;
		// }
		// else if (op == null) {
		// sb.append(ch);
		// }
		// }
		// }
		// else {
		// stack.add(item);
		// }
		//
		// sb = new StringBuffer();
		// }
		// if (op == Oper.L_PARENS) {
		//
		// // 처음부터 괄호이면 무시함.
		// if (stack.size() > 0) {
		// stackPrev = stack;
		// stack = new ArrayDeque<Object>();
		// }
		//
		// }
		// else if (op == Oper.R_PARENS) {
		//
		// // 처음부터 괄호인 경우 무시함.
		// if (stackPrev != null) {
		// stackPrev.add(stack);
		// stack = stackPrev;
		// }
		// }
		// else if (op.op) {
		// stack.add(op);
		// }
		// continue;
		// }
		// else {
		// sb.append(ch);
		// }
		// }
		//
		// if (sb.toString().length() > 0) {
		// stack.add(makeToken(sb.toString()));
		// }

		// System.out.println(stack);

		return stack;
	}

	private int parse(char chArr[], int offset, Deque<Object> stack) throws Exception {

		char ch;
		int i = offset;
		StringBuffer sb = new StringBuffer();
		Oper op;
		Object item;
		Object itemPrev = null;

		// System.out.println("-------------------------");

		MAIN: while (i < chArr.length) {

			ch = chArr[i++];

			op = Oper.getOperator(ch);
			itemPrev = (stack.size() == 0 ? null : stack.getLast());

//			 System.out.println(op + ":" + itemPrev);

			if (op != null) {

				if (sb.toString().length() > 0) {

					item = makeToken(sb.toString());

					if (item instanceof Func) {
						i--;
						sb = new StringBuffer();
						Func func = (Func) item;
						while (i < chArr.length) {
							ch = chArr[i++];
							op = Oper.getOperator(ch);
							if (op == Oper.L_PARENS) {
								// nothing
							}
							else if (op == Oper.R_PARENS) {
								Object funcPara = makeToken(sb.toString());
								stack.add(Func.makeFunc(func, funcPara));
								sb = new StringBuffer();
								continue MAIN;
							}
							else if (op == null) {
								sb.append(ch);
							}
						}
					}
					else {
						stack.add(item);
						itemPrev = item;
					}

					sb = new StringBuffer();
				}
				
				if (op == Oper.L_PARENS) {

					Deque<Object> child = new ArrayDeque<Object>();
					i = parse(chArr, i, child);
					stack.add(child);

				}
				else if (op == Oper.R_PARENS) {

					if (sb.toString().length() > 0) {
						stack.add(makeToken(sb.toString()));
					}

					return i;
				}

				else if (op.op) {

					// 이전이 Oper이면 문자 처리함.
					if ((itemPrev instanceof Oper) == false && stack.size() > 0) {
						stack.add(op);
					}
					else {
						if (op.number) {
							sb.append(ch);
						}
						else {
							throw new Exception(i + " position is invalid " + ch);
						}
					}
				}

				continue;
			}
			else {
				sb.append(ch);
			}
		}

		if (sb.toString().length() > 0) {
			stack.add(makeToken(sb.toString()));
		}

		return chArr.length;

	}

	private void resort(Deque<Object> stack) throws Exception {

		// System.out.println("--- resort " + stack);

		Object o, op = null;
		List<Object> list = new ArrayList<Object>();

		while (stack.size() > 0) {

			o = stack.pop();

			if (o instanceof Oper) {
				op = o;
				continue;
			}
			else {
				list.add(0, o);
				if (op != null) {
					list.add(0, op);
					op = null;
				}
			}
		}

		for (Object obj : list) {
			stack.push(obj);
		}

		// System.out.println("--- resort result : " + stack);

	}

}