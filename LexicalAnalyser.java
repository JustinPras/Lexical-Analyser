import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class LexicalAnalyser {

	private static List<Token> tokenList;
	private enum State {Q1, Q2, Q3, Q4, Q5, WHITEINT, WHITEDECIMAL, NUMEXCEPTION, EXPEXCEPTION};

	public static List<Token> analyse(String input) throws NumberException, ExpressionException {

		StringBuilder sb = new StringBuilder();
		tokenList = new ArrayList<Token>();

		State state = State.Q1;
		String numberString;
		Double numberDouble;
		int numberInt;

		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '.') {
				switch (state) {
					case Q1:
						state = State.NUMEXCEPTION;
						break;
					case Q2:                                                                                                                                                                                                                            
						sb.append(input.charAt(i));
						state = State.Q3;
						break;
					case Q3:
					case Q4:
					case Q5:
						state = State.NUMEXCEPTION;
						break;
					case WHITEINT:
					case WHITEDECIMAL:
						state = State.EXPEXCEPTION;
					case NUMEXCEPTION:
					case EXPEXCEPTION:
						break;
				}
			}
			else if (Character.isWhitespace(input.charAt(i))) {
				switch (state) {
					case Q1:
						break;
					case Q2:
						state = State.WHITEDECIMAL;
						break;
					case Q3:
						throw new ExpressionException();
					case Q4:
						state = State.WHITEDECIMAL;
						break;
					case Q5:
						state = State.WHITEINT;
						break;
					case WHITEINT:
					case WHITEDECIMAL:
					case NUMEXCEPTION:
					case EXPEXCEPTION:
						break;
				}
			}
			else if (input.charAt(i) == '0') {
				switch (state) {
					case Q1:
						sb.append(input.charAt(i));
						state = State.Q2;
						break;
					case Q2:
						state = State.NUMEXCEPTION;
					case Q3:
					case Q4:
						sb.append(input.charAt(i));
						state = State.Q4;
						break;
					case Q5:
						sb.append(input.charAt(i));
						state = State.Q4;
						break;
					case WHITEINT:
					case WHITEDECIMAL:
						state = State.EXPEXCEPTION;
					case NUMEXCEPTION:
					case EXPEXCEPTION:
						break;
				}
			}
			// if the letter is a number
			else if (Token.typeOf(input.charAt(i)).equals(Token.TokenType.NUMBER)) {
				switch (state) {
					case Q1:
						sb.append(input.charAt(i));
						state = State.Q5;
						break;
					case Q2:
						state = State.NUMEXCEPTION;
						break;
					case Q3:
					case Q4:
						sb.append(input.charAt(i));
						state = State.Q4;
						break;
					case Q5:
						sb.append(input.charAt(i));
						state = State.Q5;
						break;
					case WHITEINT:
					case WHITEDECIMAL:
						state = State.EXPEXCEPTION;
					case NUMEXCEPTION:
					case EXPEXCEPTION:
						break;
				}
			}
			//if the character isn't a number
			else if (!Token.typeOf(input.charAt(i)).equals(Token.TokenType.NONE)) {
				switch (state) {
					case Q1:
						state = State.EXPEXCEPTION;
						break;
					case Q2:
						numberString = sb.toString();
						numberInt = Integer.parseInt(numberString);
						tokenList.add(new Token(numberInt));
						sb.delete(0, sb.length());

						tokenList.add(new Token(Token.typeOf(input.charAt(i))));
						state = State.Q1;
						break;
					case Q3:
						state = State.NUMEXCEPTION;
						break;
					case Q4:
						numberString = sb.toString();
						numberDouble = Double.parseDouble(numberString);
						tokenList.add(new Token(numberDouble));
						sb.delete(0, sb.length());
						
						tokenList.add(new Token(Token.typeOf(input.charAt(i))));
						state = State.Q1;
						break;
					case Q5:
						numberString = sb.toString();
						numberInt = Integer.parseInt(numberString);
						tokenList.add(new Token(numberInt));
						sb.delete(0, sb.length());
						
						tokenList.add(new Token(Token.typeOf(input.charAt(i))));
						state = State.Q1;
						break;
					case WHITEINT:
						numberString = sb.toString();
						numberInt = Integer.parseInt(numberString);
						tokenList.add(new Token(numberInt));
						sb.delete(0, sb.length());

						tokenList.add(new Token(Token.typeOf(input.charAt(i))));
						state = State.Q1;
						break;
					case WHITEDECIMAL:
						numberString = sb.toString();
						numberDouble = Double.parseDouble(numberString);
						tokenList.add(new Token(numberDouble));
						sb.delete(0, sb.length());

						tokenList.add(new Token(Token.typeOf(input.charAt(i))));
						state = State.Q1;
						break;
					case NUMEXCEPTION:
					case EXPEXCEPTION:
						break;
				}
			}
			else {
				state = State.EXPEXCEPTION;
			}
		}

		switch (state) {
			case Q1:
				throw new ExpressionException();
			case Q2:
				numberString = sb.toString();
				numberInt = Integer.parseInt(numberString);
				tokenList.add(new Token(numberInt));
				return tokenList;
			case Q3:
				throw new NumberException();
			case Q4:
				numberString = sb.toString();
				numberDouble = Double.parseDouble(numberString);
				tokenList.add(new Token(numberDouble));
				return tokenList;
			case Q5:
				numberString = sb.toString();
				numberInt = Integer.parseInt(numberString);
				tokenList.add(new Token(numberInt));
				return tokenList;
			case EXPEXCEPTION:
				throw new ExpressionException();
			case NUMEXCEPTION:
				throw new NumberException();
			default:
				throw new ExpressionException();
		}
	}
}
