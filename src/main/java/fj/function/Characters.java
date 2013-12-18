package fj.function;

import fj.Func;
import fj.Func2;

import static fj.Function.curry;

/**
 * First-class functions on Characters.
 */
public final class Characters {
  private Characters() {
    throw new UnsupportedOperationException();
  }
  public static final Func<Character, String> toString = new Func<Character, String>() {
    public String f(final Character c) {return Character.toString(c);}
  };
  public static final Func<Character, Boolean> isLowerCase = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isLowerCase(ch);}
  };
  public static final Func<Character, Boolean> isUpperCase = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isUpperCase(ch);}
  };
  public static final Func<Character, Boolean> isTitleCase = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isTitleCase(ch);}
  };
  public static final Func<Character, Boolean> isDigit = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isDigit(ch);}
  };
  public static final Func<Character, Boolean> isDefined = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isDefined(ch);}
  };
  public static final Func<Character, Boolean> isLetter = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isLetter(ch);}
  };
  public static final Func<Character, Boolean> isLetterOrDigit = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isLetterOrDigit(ch);}
  };
  public static final Func<Character, Boolean> isJavaIdentifierStart = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isJavaIdentifierStart(ch);}
  };
  public static final Func<Character, Boolean> isJavaIdentifierPart = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isJavaIdentifierPart(ch);}
  };
  public static final Func<Character, Boolean> isUnicodeIdentifierStart = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isUnicodeIdentifierStart(ch);}
  };
  public static final Func<Character, Boolean> isUnicodeIdentifierPart = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isUnicodeIdentifierPart(ch);}
  };
  public static final Func<Character, Boolean> isIdentifierIgnorable = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isIdentifierIgnorable(ch);}
  };
  public static final Func<Character, Character> toLowerCase = new Func<Character, Character>() {
    public Character f(final Character ch) {return Character.toLowerCase(ch);}
  };
  public static final Func<Character, Character> toUpperCase = new Func<Character, Character>() {
    public Character f(final Character ch) {return Character.toUpperCase(ch);}
  };
  public static final Func<Character, Character> toTitleCase = new Func<Character, Character>() {
    public Character f(final Character ch) {return Character.toTitleCase(ch);}
  };
  public static final Func<Character, Func<Integer, Integer>> digit = curry(new Func2<Character, Integer, Integer>() {
    public Integer f(final Character ch, final Integer radix) {return Character.digit(ch, radix);}
  });
  public static final Func<Character, Integer> getNumericValue = new Func<Character, Integer>() {
    public Integer f(final Character ch) {return Character.getNumericValue(ch);}
  };
  public static final Func<Character, Boolean> isSpaceChar = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isSpaceChar(ch);}
  };
  public static final Func<Character, Boolean> isWhitespace = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isWhitespace(ch);}
  };
  public static final Func<Character, Boolean> isISOControl = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isISOControl(ch);}
  };
  public static final Func<Character, Integer> getType = new Func<Character, Integer>() {
    public Integer f(final Character ch) {return Character.getType(ch);}
  };
  public static final Func<Character, Byte> getDirectionality = new Func<Character, Byte>() {
    public Byte f(final Character ch) {return Character.getDirectionality(ch);}
  };
  public static final Func<Character, Boolean> isMirrored = new Func<Character, Boolean>() {
    public Boolean f(final Character ch) {return Character.isMirrored(ch);}
  };
  public static final Func<Character, Character> reverseBytes = new Func<Character, Character>() {
    public Character f(final Character ch) {return Character.reverseBytes(ch);}
  };
  public static final Func<Character, Boolean> isNewLine = new Func<Character, Boolean>() {
    public Boolean f(final Character c) { return c == '\n'; }
  };  
}
