package fj;

/**
 * Functions that convert between Java primitive types.
 *
 * @version %build.number%
 */
public final class Primitive {
  private Primitive() {
    throw new UnsupportedOperationException();
  }

  // BEGIN Boolean ->

  /**
   * A function that converts booleans to bytes.
   */
  public static final Func<Boolean, Byte> Boolean_Byte = new Func<Boolean, Byte>() {
    public Byte f(final Boolean b) {
      return (byte) (b ? 1 : 0);
    }
  };

  /**
   * A function that converts booleans to characters.
   */
  public static final Func<Boolean, Character> Boolean_Character = new Func<Boolean, Character>() {
    public Character f(final Boolean b) {
      return (char) (b ? 1 : 0);
    }
  };

  /**
   * A function that converts booleans to doubles.
   */
  public static final Func<Boolean, Double> Boolean_Double = new Func<Boolean, Double>() {
    public Double f(final Boolean b) {
      return b ? 1D : 0D;
    }
  };

  /**
   * A function that converts booleans to floats.
   */
  public static final Func<Boolean, Float> Boolean_Float = new Func<Boolean, Float>() {
    public Float f(final Boolean b) {
      return b ? 1F : 0F;
    }
  };

  /**
   * A function that converts booleans to integers.
   */
  public static final Func<Boolean, Integer> Boolean_Integer = new Func<Boolean, Integer>() {
    public Integer f(final Boolean b) {
      return b ? 1 : 0;
    }
  };

  /**
   * A function that converts booleans to longs.
   */
  public static final Func<Boolean, Long> Boolean_Long = new Func<Boolean, Long>() {
    public Long f(final Boolean b) {
      return b ? 1L : 0L;
    }
  };

  /**
   * A function that converts booleans to shorts.
   */
  public static final Func<Boolean, Short> Boolean_Short = new Func<Boolean, Short>() {
    public Short f(final Boolean b) {
      return (short) (b ? 1 : 0);
    }
  };

  // END Boolean ->

  // BEGIN Byte ->

  /**
   * A function that converts bytes to booleans.
   */
  public static final Func<Byte, Boolean> Byte_Boolean = new Func<Byte, Boolean>() {
    public Boolean f(final Byte b) {
      return b != 0;
    }
  };

  /**
   * A function that converts bytes to characters.
   */
  public static final Func<Byte, Character> Byte_Character = new Func<Byte, Character>() {
    public Character f(final Byte b) {
      return (char) (byte) b;
    }
  };

  /**
   * A function that converts bytes to doubles.
   */
  public static final Func<Byte, Double> Byte_Double = new Func<Byte, Double>() {
    public Double f(final Byte b) {
      return (double) b;
    }
  };

  /**
   * A function that converts bytes to floats.
   */
  public static final Func<Byte, Float> Byte_Float = new Func<Byte, Float>() {
    public Float f(final Byte b) {
      return (float) b;
    }
  };

  /**
   * A function that converts bytes to integers.
   */
  public static final Func<Byte, Integer> Byte_Integer = new Func<Byte, Integer>() {
    public Integer f(final Byte b) {
      return (int) b;
    }
  };

  /**
   * A function that converts bytes to longs.
   */
  public static final Func<Byte, Long> Byte_Long = new Func<Byte, Long>() {
    public Long f(final Byte b) {
      return (long) b;
    }
  };

  /**
   * A function that converts bytes to shorts.
   */
  public static final Func<Byte, Short> Byte_Short = new Func<Byte, Short>() {
    public Short f(final Byte b) {
      return (short) b;
    }
  };

  // END Byte ->

  // BEGIN Character ->

  /**
   * A function that converts characters to booleans.
   */
  public static final Func<Character, Boolean> Character_Boolean = new Func<Character, Boolean>() {
    public Boolean f(final Character c) {
      return c != 0;
    }
  };

  /**
   * A function that converts characters to bytes.
   */
  public static final Func<Character, Byte> Character_Byte = new Func<Character, Byte>() {
    public Byte f(final Character c) {
      return (byte) (char) c;
    }
  };

  /**
   * A function that converts characters to doubles.
   */
  public static final Func<Character, Double> Character_Double = new Func<Character, Double>() {
    public Double f(final Character c) {
      return (double) (char) c;
    }
  };

  /**
   * A function that converts characters to floats.
   */
  public static final Func<Character, Float> Character_Float = new Func<Character, Float>() {
    public Float f(final Character c) {
      return (float) (char) c;
    }
  };

  /**
   * A function that converts characters to integers.
   */
  public static final Func<Character, Integer> Character_Integer = new Func<Character, Integer>() {
    public Integer f(final Character c) {
      return (int) (char) c;
    }
  };

  /**
   * A function that converts characters to longs.
   */
  public static final Func<Character, Long> Character_Long = new Func<Character, Long>() {
    public Long f(final Character c) {
      return (long) (char) c;
    }
  };

  /**
   * A function that converts characters to shorts.
   */
  public static final Func<Character, Short> Character_Short = new Func<Character, Short>() {
    public Short f(final Character c) {
      return (short) (char) c;
    }
  };

  // END Character ->

  // BEGIN Double ->

  /**
   * A function that converts doubles to booleans.
   */
  public static final Func<Double, Boolean> Double_Boolean = new Func<Double, Boolean>() {
    public Boolean f(final Double d) {
      return d != 0D;
    }
  };

  /**
   * A function that converts doubles to bytes.
   */
  public static final Func<Double, Byte> Double_Byte = new Func<Double, Byte>() {
    public Byte f(final Double d) {
      return (byte) (double) d;
    }
  };

  /**
   * A function that converts doubles to characters.
   */
  public static final Func<Double, Character> Double_Character = new Func<Double, Character>() {
    public Character f(final Double d) {
      return (char) (double) d;
    }
  };

  /**
   * A function that converts doubles to floats.
   */
  public static final Func<Double, Float> Double_Float = new Func<Double, Float>() {
    public Float f(final Double d) {
      return (float) (double) d;
    }
  };

  /**
   * A function that converts doubles to integers.
   */
  public static final Func<Double, Integer> Double_Integer = new Func<Double, Integer>() {
    public Integer f(final Double d) {
      return (int) (double) d;
    }
  };

  /**
   * A function that converts doubles to longs.
   */
  public static final Func<Double, Long> Double_Long = new Func<Double, Long>() {
    public Long f(final Double d) {
      return (long) (double) d;
    }
  };

  /**
   * A function that converts doubles to shorts.
   */
  public static final Func<Double, Short> Double_Short = new Func<Double, Short>() {
    public Short f(final Double d) {
      return (short) (double) d;
    }
  };

  // END Double ->

  // BEGIN Float ->

  /**
   * A function that converts floats to booleans.
   */
  public static final Func<Float, Boolean> Float_Boolean = new Func<Float, Boolean>() {
    public Boolean f(final Float f) {
      return f != 0F;
    }
  };

  /**
   * A function that converts floats to bytes.
   */
  public static final Func<Float, Byte> Float_Byte = new Func<Float, Byte>() {
    public Byte f(final Float f) {
      return (byte) (float) f;
    }
  };

  /**
   * A function that converts floats to characters.
   */
  public static final Func<Float, Character> Float_Character = new Func<Float, Character>() {
    public Character f(final Float f) {
      return (char) (float) f;
    }
  };

  /**
   * A function that converts floats to doubles.
   */
  public static final Func<Float, Double> Float_Double = new Func<Float, Double>() {
    public Double f(final Float f) {
      return (double) (float) f;
    }
  };

  /**
   * A function that converts floats to integers.
   */
  public static final Func<Float, Integer> Float_Integer = new Func<Float, Integer>() {
    public Integer f(final Float f) {
      return (int) (float) f;
    }
  };

  /**
   * A function that converts floats to longs.
   */
  public static final Func<Float, Long> Float_Long = new Func<Float, Long>() {
    public Long f(final Float f) {
      return (long) (float) f;
    }
  };

  /**
   * A function that converts floats to shorts.
   */
  public static final Func<Float, Short> Float_Short = new Func<Float, Short>() {
    public Short f(final Float f) {
      return (short) (float) f;
    }
  };

  // END Float ->

  // BEGIN Integer ->

  /**
   * A function that converts integers to booleans.
   */
  public static final Func<Integer, Boolean> Integer_Boolean = new Func<Integer, Boolean>() {
    public Boolean f(final Integer i) {
      return i != 0;
    }
  };

  /**
   * A function that converts integers to bytes.
   */
  public static final Func<Integer, Byte> Integer_Byte = new Func<Integer, Byte>() {
    public Byte f(final Integer i) {
      return (byte) (int) i;
    }
  };

  /**
   * A function that converts integers to characters.
   */
  public static final Func<Integer, Character> Integer_Character = new Func<Integer, Character>() {
    public Character f(final Integer i) {
      return (char) (int) i;
    }
  };

  /**
   * A function that converts integers to doubles.
   */
  public static final Func<Integer, Double> Integer_Double = new Func<Integer, Double>() {
    public Double f(final Integer i) {
      return (double) i;
    }
  };

  /**
   * A function that converts integers to floats.
   */
  public static final Func<Integer, Float> Integer_Float = new Func<Integer, Float>() {
    public Float f(final Integer i) {
      return (float) i;
    }
  };

  /**
   * A function that converts integers to longs.
   */
  public static final Func<Integer, Long> Integer_Long = new Func<Integer, Long>() {
    public Long f(final Integer i) {
      return (long) i;
    }
  };

  /**
   * A function that converts integers to shorts.
   */
  public static final Func<Integer, Short> Integer_Short = new Func<Integer, Short>() {
    public Short f(final Integer i) {
      return (short) (int) i;
    }
  };

  // END Integer ->

  // BEGIN Long ->

  /**
   * A function that converts longs to booleans.
   */
  public static final Func<Long, Boolean> Long_Boolean = new Func<Long, Boolean>() {
    public Boolean f(final Long l) {
      return l != 0L;
    }
  };

  /**
   * A function that converts longs to bytes.
   */
  public static final Func<Long, Byte> Long_Byte = new Func<Long, Byte>() {
    public Byte f(final Long l) {
      return (byte) (long) l;
    }
  };

  /**
   * A function that converts longs to characters.
   */
  public static final Func<Long, Character> Long_Character = new Func<Long, Character>() {
    public Character f(final Long l) {
      return (char) (long) l;
    }
  };

  /**
   * A function that converts longs to doubles.
   */
  public static final Func<Long, Double> Long_Double = new Func<Long, Double>() {
    public Double f(final Long l) {
      return (double) (long) l;
    }
  };

  /**
   * A function that converts longs to floats.
   */
  public static final Func<Long, Float> Long_Float = new Func<Long, Float>() {
    public Float f(final Long l) {
      return (float) (long) l;
    }
  };

  /**
   * A function that converts longs to integers.
   */
  public static final Func<Long, Integer> Long_Integer = new Func<Long, Integer>() {
    public Integer f(final Long l) {
      return (int) (long) l;
    }
  };

  /**
   * A function that converts longs to shorts.
   */
  public static final Func<Long, Short> Long_Short = new Func<Long, Short>() {
    public Short f(final Long l) {
      return (short) (long) l;
    }
  };

  // END Long ->

  // BEGIN Short ->

  /**
   * A function that converts shorts to booleans.
   */
  public static final Func<Short, Boolean> Short_Boolean = new Func<Short, Boolean>() {
    public Boolean f(final Short s) {
      return s != 0;
    }
  };

  /**
   * A function that converts shorts to bytes.
   */
  public static final Func<Short, Byte> Short_Byte = new Func<Short, Byte>() {
    public Byte f(final Short s) {
      return (byte) (short) s;
    }
  };

  /**
   * A function that converts shorts to characters.
   */
  public static final Func<Short, Character> Short_Character = new Func<Short, Character>() {
    public Character f(final Short s) {
      return (char) (short) s;
    }
  };

  /**
   * A function that converts shorts to doubles.
   */
  public static final Func<Short, Double> Short_Double = new Func<Short, Double>() {
    public Double f(final Short s) {
      return (double) (short) s;
    }
  };

  /**
   * A function that converts shorts to floats.
   */
  public static final Func<Short, Float> Short_Float = new Func<Short, Float>() {
    public Float f(final Short s) {
      return (float) (short) s;
    }
  };

  /**
   * A function that converts shorts to integers.
   */
  public static final Func<Short, Integer> Short_Integer = new Func<Short, Integer>() {
    public Integer f(final Short s) {
      return (int) (short) s;
    }
  };

  /**
   * A function that converts shorts to longs.
   */
  public static final Func<Short, Long> Short_Long = new Func<Short, Long>() {
    public Long f(final Short s) {
      return (long) (short) s;
    }
  };

  // END Short
}
