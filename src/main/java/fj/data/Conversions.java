package fj.data;

import fj.Effect;
import fj.Func;
import fj.P1;
import static fj.data.List.asString;
import static fj.data.List.fromString;

/**
 * Functions that convert between data structure types.
 *
 * @version %build.number%
 */
public final class Conversions {
  private Conversions() {
    throw new UnsupportedOperationException();
  }

  // BEGIN List ->

  /**
   * A function that converts lists to arrays.
   *
   * @return A function that converts lists to arrays.
   */
  public static <A> Func<List<A>, Array<A>> List_Array() {
    return new Func<List<A>, Array<A>>() {
      public Array<A> f(final List<A> as) {
        return as.toArray();
      }
    };
  }

  /**
   * A function that converts lists to streams.
   *
   * @return A function that converts lists to streams.
   */
  public static <A> Func<List<A>, Stream<A>> List_Stream() {
    return new Func<List<A>, Stream<A>>() {
      public Stream<A> f(final List<A> as) {
        return as.toStream();
      }
    };
  }

  /**
   * A function that converts lists to options.
   *
   * @return A function that converts lists to options.
   */
  public static <A> Func<List<A>, Option<A>> List_Option() {
    return new Func<List<A>, Option<A>>() {
      public Option<A> f(final List<A> as) {
        return as.toOption();
      }
    };
  }

  /**
   * A function that converts lists to eithers.
   *
   * @return A function that converts lists to eithers.
   */
  public static <A, B> Func<P1<A>, Func<List<B>, Either<A, B>>> List_Either() {
    return new Func<P1<A>, Func<List<B>, Either<A, B>>>() {
      public Func<List<B>, Either<A, B>> f(final P1<A> a) {
        return new Func<List<B>, Either<A, B>>() {
          public Either<A, B> f(final List<B> bs) {
            return bs.toEither(a);
          }
        };
      }
    };
  }

  /**
   * A function that converts lists to strings.
   */
  public static final Func<List<Character>, String> List_String = new Func<List<Character>, String>() {
    public String f(final List<Character> cs) {
      return asString(cs);
    }
  };

  /**
   * A function that converts lists to string buffers.
   */
  public static final Func<List<Character>, StringBuffer> List_StringBuffer = new Func<List<Character>, StringBuffer>() {
    public StringBuffer f(final List<Character> cs) {
      return new StringBuffer(asString(cs));
    }
  };

  /**
   * A function that converts lists to string builders.
   */
  public static final Func<List<Character>, StringBuilder> List_StringBuilder = new Func<List<Character>, StringBuilder>() {
    public StringBuilder f(final List<Character> cs) {
      return new StringBuilder(asString(cs));
    }
  };

  // END List ->

  // BEGIN Array ->

  /**
   * A function that converts arrays to lists.
   *
   * @return A function that converts arrays to lists.
   */
  public static <A> Func<Array<A>, List<A>> Array_List() {
    return new Func<Array<A>, List<A>>() {
      public List<A> f(final Array<A> as) {
        return as.toList();
      }
    };
  }

  /**
   * A function that converts arrays to streams.
   *
   * @return A function that converts arrays to streams.
   */
  public static <A> Func<Array<A>, Stream<A>> Array_Stream() {
    return new Func<Array<A>, Stream<A>>() {
      public Stream<A> f(final Array<A> as) {
        return as.toStream();
      }
    };
  }

  /**
   * A function that converts arrays to options.
   *
   * @return A function that converts arrays to options.
   */
  public static <A> Func<Array<A>, Option<A>> Array_Option() {
    return new Func<Array<A>, Option<A>>() {
      public Option<A> f(final Array<A> as) {
        return as.toOption();
      }
    };
  }

  /**
   * A function that converts arrays to eithers.
   *
   * @return A function that converts arrays to eithers.
   */
  public static <A, B> Func<P1<A>, Func<Array<B>, Either<A, B>>> Array_Either() {
    return new Func<P1<A>, Func<Array<B>, Either<A, B>>>() {
      public Func<Array<B>, Either<A, B>> f(final P1<A> a) {
        return new Func<Array<B>, Either<A, B>>() {
          public Either<A, B> f(final Array<B> bs) {
            return bs.toEither(a);
          }
        };
      }
    };
  }

  /**
   * A function that converts arrays to strings.
   */
  public static final Func<Array<Character>, String> Array_String = new Func<Array<Character>, String>() {
    public String f(final Array<Character> cs) {
      final StringBuilder sb = new StringBuilder();
      cs.foreach(new Effect<Character>() {
        public void e(final Character c) {
          sb.append(c);
        }
      });
      return sb.toString();
    }
  };

  /**
   * A function that converts arrays to string buffers.
   */
  public static final Func<Array<Character>, StringBuffer> Array_StringBuffer = new Func<Array<Character>, StringBuffer>() {
    public StringBuffer f(final Array<Character> cs) {
      final StringBuffer sb = new StringBuffer();
      cs.foreach(new Effect<Character>() {
        public void e(final Character c) {
          sb.append(c);
        }
      });
      return sb;
    }
  };

  /**
   * A function that converts arrays to string builders.
   */
  public static final Func<Array<Character>, StringBuilder> Array_StringBuilder =
      new Func<Array<Character>, StringBuilder>() {
        public StringBuilder f(final Array<Character> cs) {
          final StringBuilder sb = new StringBuilder();
          cs.foreach(new Effect<Character>() {
            public void e(final Character c) {
              sb.append(c);
            }
          });
          return sb;
        }
      };

  // END Array ->

  // BEGIN Stream ->

  /**
   * A function that converts streams to lists.
   *
   * @return A function that converts streams to lists.
   */
  public static <A> Func<Stream<A>, List<A>> Stream_List() {
    return new Func<Stream<A>, List<A>>() {
      public List<A> f(final Stream<A> as) {
        return as.toList();
      }
    };
  }

  /**
   * A function that converts streams to arrays.
   *
   * @return A function that converts streams to arrays.
   */
  public static <A> Func<Stream<A>, Array<A>> Stream_Array() {
    return new Func<Stream<A>, Array<A>>() {
      public Array<A> f(final Stream<A> as) {
        return as.toArray();
      }
    };
  }

  /**
   * A function that converts streams to options.
   *
   * @return A function that converts streams to options.
   */
  public static <A> Func<Stream<A>, Option<A>> Stream_Option() {
    return new Func<Stream<A>, Option<A>>() {
      public Option<A> f(final Stream<A> as) {
        return as.toOption();
      }
    };
  }

  /**
   * A function that converts streams to eithers.
   *
   * @return A function that converts streams to eithers.
   */
  public static <A, B> Func<P1<A>, Func<Stream<B>, Either<A, B>>> Stream_Either() {
    return new Func<P1<A>, Func<Stream<B>, Either<A, B>>>() {
      public Func<Stream<B>, Either<A, B>> f(final P1<A> a) {
        return new Func<Stream<B>, Either<A, B>>() {
          public Either<A, B> f(final Stream<B> bs) {
            return bs.toEither(a);
          }
        };
      }
    };
  }

  /**
   * A function that converts streams to strings.
   */
  public static final Func<Stream<Character>, String> Stream_String = new Func<Stream<Character>, String>() {
    public String f(final Stream<Character> cs) {
      final StringBuilder sb = new StringBuilder();
      cs.foreach(new Effect<Character>() {
        public void e(final Character c) {
          sb.append(c);
        }
      });
      return sb.toString();
    }
  };

  /**
   * A function that converts streams to string buffers.
   */
  public static final Func<Stream<Character>, StringBuffer> Stream_StringBuffer =
      new Func<Stream<Character>, StringBuffer>() {
        public StringBuffer f(final Stream<Character> cs) {
          final StringBuffer sb = new StringBuffer();
          cs.foreach(new Effect<Character>() {
            public void e(final Character c) {
              sb.append(c);
            }
          });
          return sb;
        }
      };

  /**
   * A function that converts streams to string builders.
   */
  public static final Func<Stream<Character>, StringBuilder> Stream_StringBuilder =
      new Func<Stream<Character>, StringBuilder>() {
        public StringBuilder f(final Stream<Character> cs) {
          final StringBuilder sb = new StringBuilder();
          cs.foreach(new Effect<Character>() {
            public void e(final Character c) {
              sb.append(c);
            }
          });
          return sb;
        }
      };

  // END Stream ->

  // BEGIN Option ->

  /**
   * A function that converts options to lists.
   *
   * @return A function that converts options to lists.
   */
  public static <A> Func<Option<A>, List<A>> Option_List() {
    return new Func<Option<A>, List<A>>() {
      public List<A> f(final Option<A> o) {
        return o.toList();
      }
    };
  }

  /**
   * A function that converts options to arrays.
   *
   * @return A function that converts options to arrays.
   */
  public static <A> Func<Option<A>, Array<A>> Option_Array() {
    return new Func<Option<A>, Array<A>>() {
      public Array<A> f(final Option<A> o) {
        return o.toArray();
      }
    };
  }

  /**
   * A function that converts options to streams.
   *
   * @return A function that converts options to streams.
   */
  public static <A> Func<Option<A>, Stream<A>> Option_Stream() {
    return new Func<Option<A>, Stream<A>>() {
      public Stream<A> f(final Option<A> o) {
        return o.toStream();
      }
    };
  }

  /**
   * A function that converts options to eithers.
   *
   * @return A function that converts options to eithers.
   */
  public static <A, B> Func<P1<A>, Func<Option<B>, Either<A, B>>> Option_Either() {
    return new Func<P1<A>, Func<Option<B>, Either<A, B>>>() {
      public Func<Option<B>, Either<A, B>> f(final P1<A> a) {
        return new Func<Option<B>, Either<A, B>>() {
          public Either<A, B> f(final Option<B> o) {
            return o.toEither(a);
          }
        };
      }
    };
  }

  /**
   * A function that converts options to strings.
   */
  public static final Func<Option<Character>, String> Option_String = new Func<Option<Character>, String>() {
    public String f(final Option<Character> o) {
      return asString(o.toList());
    }
  };

  /**
   * A function that converts options to string buffers.
   */
  public static final Func<Option<Character>, StringBuffer> Option_StringBuffer =
      new Func<Option<Character>, StringBuffer>() {
        public StringBuffer f(final Option<Character> o) {
          return new StringBuffer(asString(o.toList()));
        }
      };

  /**
   * A function that converts options to string builders.
   */
  public static final Func<Option<Character>, StringBuilder> Option_StringBuilder =
      new Func<Option<Character>, StringBuilder>() {
        public StringBuilder f(final Option<Character> o) {
          return new StringBuilder(asString(o.toList()));
        }
      };

  // END Option ->

  // BEGIN Either ->

  /**
   * A function that converts eithers to lists.
   *
   * @return A function that converts eithers to lists.
   */
  public static <A, B> Func<Either<A, B>, List<A>> Either_ListA() {
    return new Func<Either<A, B>, List<A>>() {
      public List<A> f(final Either<A, B> e) {
        return e.left().toList();
      }
    };
  }

  /**
   * A function that converts eithers to lists.
   *
   * @return A function that converts eithers to lists.
   */
  public static <A, B> Func<Either<A, B>, List<B>> Either_ListB() {
    return new Func<Either<A, B>, List<B>>() {
      public List<B> f(final Either<A, B> e) {
        return e.right().toList();
      }
    };
  }

  /**
   * A function that converts eithers to arrays.
   *
   * @return A function that converts eithers to arrays.
   */
  public static <A, B> Func<Either<A, B>, Array<A>> Either_ArrayA() {
    return new Func<Either<A, B>, Array<A>>() {
      public Array<A> f(final Either<A, B> e) {
        return e.left().toArray();
      }
    };
  }

  /**
   * A function that converts eithers to arrays.
   *
   * @return A function that converts eithers to arrays.
   */
  public static <A, B> Func<Either<A, B>, Array<B>> Either_ArrayB() {
    return new Func<Either<A, B>, Array<B>>() {
      public Array<B> f(final Either<A, B> e) {
        return e.right().toArray();
      }
    };
  }

  /**
   * A function that converts eithers to streams.
   *
   * @return A function that converts eithers to streams.
   */
  public static <A, B> Func<Either<A, B>, Stream<A>> Either_StreamA() {
    return new Func<Either<A, B>, Stream<A>>() {
      public Stream<A> f(final Either<A, B> e) {
        return e.left().toStream();
      }
    };
  }

  /**
   * A function that converts eithers to streams.
   *
   * @return A function that converts eithers to streams.
   */
  public static <A, B> Func<Either<A, B>, Stream<B>> Either_StreamB() {
    return new Func<Either<A, B>, Stream<B>>() {
      public Stream<B> f(final Either<A, B> e) {
        return e.right().toStream();
      }
    };
  }

  /**
   * A function that converts eithers to options.
   *
   * @return A function that converts eithers to options.
   */
  public static <A, B> Func<Either<A, B>, Option<A>> Either_OptionA() {
    return new Func<Either<A, B>, Option<A>>() {
      public Option<A> f(final Either<A, B> e) {
        return e.left().toOption();
      }
    };
  }

  /**
   * A function that converts eithers to options.
   *
   * @return A function that converts eithers to options.
   */
  public static <A, B> Func<Either<A, B>, Option<B>> Either_OptionB() {
    return new Func<Either<A, B>, Option<B>>() {
      public Option<B> f(final Either<A, B> e) {
        return e.right().toOption();
      }
    };
  }

  /**
   * A function that converts eithers to strings.
   *
   * @return A function that converts eithers to strings.
   */
  public static <B> Func<Either<Character, B>, String> Either_StringA() {
    return new Func<Either<Character, B>, String>() {
      public String f(final Either<Character, B> e) {
        return asString(e.left().toList());
      }
    };
  }

  /**
   * A function that converts eithers to strings.
   *
   * @return A function that converts eithers to strings.
   */
  public static <A> Func<Either<A, Character>, String> Either_StringB() {
    return new Func<Either<A, Character>, String>() {
      public String f(final Either<A, Character> e) {
        return asString(e.right().toList());
      }
    };
  }

  /**
   * A function that converts eithers to string buffers.
   *
   * @return A function that converts eithers to string buffers.
   */
  public static <B> Func<Either<Character, B>, StringBuffer> Either_StringBufferA() {
    return new Func<Either<Character, B>, StringBuffer>() {
      public StringBuffer f(final Either<Character, B> e) {
        return new StringBuffer(asString(e.left().toList()));
      }
    };
  }

  /**
   * A function that converts eithers to string buffers.
   *
   * @return A function that converts eithers to string buffers.
   */
  public static <A> Func<Either<A, Character>, StringBuffer> Either_StringBufferB() {
    return new Func<Either<A, Character>, StringBuffer>() {
      public StringBuffer f(final Either<A, Character> e) {
        return new StringBuffer(asString(e.right().toList()));
      }
    };
  }

  /**
   * A function that converts eithers to string builders.
   *
   * @return A function that converts eithers to string builders.
   */
  public static <B> Func<Either<Character, B>, StringBuilder> Either_StringBuilderA() {
    return new Func<Either<Character, B>, StringBuilder>() {
      public StringBuilder f(final Either<Character, B> e) {
        return new StringBuilder(asString(e.left().toList()));
      }
    };
  }

  /**
   * A function that converts eithers to string builders.
   *
   * @return A function that converts eithers to string builders.
   */
  public static <A> Func<Either<A, Character>, StringBuilder> Either_StringBuilderB() {
    return new Func<Either<A, Character>, StringBuilder>() {
      public StringBuilder f(final Either<A, Character> e) {
        return new StringBuilder(asString(e.right().toList()));
      }
    };
  }

  // END Either ->

  // BEGIN String ->

  /**
   * A function that converts strings to lists.
   */
  public static final Func<String, List<Character>> String_List = new Func<String, List<Character>>() {
    public List<Character> f(final String s) {
      return fromString(s);
    }
  };

  /**
   * A function that converts strings to arrays.
   */
  public static final Func<String, Array<Character>> String_Array = new Func<String, Array<Character>>() {
    public Array<Character> f(final String s) {
      return fromString(s).toArray();
    }
  };

  /**
   * A function that converts strings to options.
   */
  public static final Func<String, Option<Character>> String_Option = new Func<String, Option<Character>>() {
    public Option<Character> f(final String s) {
      return fromString(s).toOption();
    }
  };

  /**
   * A function that converts string to eithers.
   *
   * @return A function that converts string to eithers.
   */
  public static <A> Func<P1<A>, Func<String, Either<A, Character>>> String_Either() {
    return new Func<P1<A>, Func<String, Either<A, Character>>>() {
      public Func<String, Either<A, Character>> f(final P1<A> a) {
        return new Func<String, Either<A, Character>>() {
          public Either<A, Character> f(final String s) {
            return fromString(s).toEither(a);
          }
        };
      }
    };
  }

  /**
   * A function that converts strings to streams.
   */
  public static final Func<String, Stream<Character>> String_Stream = new Func<String, Stream<Character>>() {
    public Stream<Character> f(final String s) {
      return fromString(s).toStream();
    }
  };

  /**
   * A function that converts strings to string buffers.
   */
  public static final Func<String, StringBuffer> String_StringBuffer = new Func<String, StringBuffer>() {
    public StringBuffer f(final String s) {
      return new StringBuffer(s);
    }
  };

  /**
   * A function that converts strings to string builders.
   */
  public static final Func<String, StringBuilder> String_StringBuilder = new Func<String, StringBuilder>() {
    public StringBuilder f(final String s) {
      return new StringBuilder(s);
    }
  };

  // END String ->

  // BEGIN StringBuffer ->

  /**
   * A function that converts string buffers to lists.
   */
  public static final Func<StringBuffer, List<Character>> StringBuffer_List = new Func<StringBuffer, List<Character>>() {
    public List<Character> f(final StringBuffer s) {
      return fromString(s.toString());
    }
  };

  /**
   * A function that converts string buffers to arrays.
   */
  public static final Func<StringBuffer, Array<Character>> StringBuffer_Array = new Func<StringBuffer, Array<Character>>() {
    public Array<Character> f(final StringBuffer s) {
      return fromString(s.toString()).toArray();
    }
  };

  /**
   * A function that converts string buffers to streams.
   */
  public static final Func<StringBuffer, Stream<Character>> StringBuffer_Stream =
      new Func<StringBuffer, Stream<Character>>() {
        public Stream<Character> f(final StringBuffer s) {
          return fromString(s.toString()).toStream();
        }
      };

  /**
   * A function that converts string buffers to options.
   */
  public static final Func<StringBuffer, Option<Character>> StringBuffer_Option =
      new Func<StringBuffer, Option<Character>>() {
        public Option<Character> f(final StringBuffer s) {
          return fromString(s.toString()).toOption();
        }
      };

  /**
   * A function that converts string buffers to eithers.
   *
   * @return A function that converts string buffers to eithers.
   */
  public static <A> Func<P1<A>, Func<StringBuffer, Either<A, Character>>> StringBuffer_Either() {
    return new Func<P1<A>, Func<StringBuffer, Either<A, Character>>>() {
      public Func<StringBuffer, Either<A, Character>> f(final P1<A> a) {
        return new Func<StringBuffer, Either<A, Character>>() {
          public Either<A, Character> f(final StringBuffer s) {
            return fromString(s.toString()).toEither(a);
          }
        };
      }
    };
  }

  /**
   * A function that converts string buffers to strings.
   */
  public static final Func<StringBuffer, String> StringBuffer_String = new Func<StringBuffer, String>() {
    public String f(final StringBuffer s) {
      return s.toString();
    }
  };

  /**
   * A function that converts string buffers to string builders.
   */
  public static final Func<StringBuffer, StringBuilder> StringBuffer_StringBuilder = new Func<StringBuffer, StringBuilder>() {
    public StringBuilder f(final StringBuffer s) {
      return new StringBuilder(s);
    }
  };

  // END StringBuffer ->

  // BEGIN StringBuilder ->

  /**
   * A function that converts string builders to lists.
   */
  public static final Func<StringBuilder, List<Character>> StringBuilder_List = new Func<StringBuilder, List<Character>>() {
    public List<Character> f(final StringBuilder s) {
      return fromString(s.toString());
    }
  };

  /**
   * A function that converts string builders to arrays.
   */
  public static final Func<StringBuilder, Array<Character>> StringBuilder_Array =
      new Func<StringBuilder, Array<Character>>() {
        public Array<Character> f(final StringBuilder s) {
          return fromString(s.toString()).toArray();
        }
      };

  /**
   * A function that converts string builders to streams.
   */
  public static final Func<StringBuilder, Stream<Character>> StringBuilder_Stream =
      new Func<StringBuilder, Stream<Character>>() {
        public Stream<Character> f(final StringBuilder s) {
          return fromString(s.toString()).toStream();
        }
      };

  /**
   * A function that converts string builders to options.
   */
  public static final Func<StringBuilder, Option<Character>> StringBuilder_Option =
      new Func<StringBuilder, Option<Character>>() {
        public Option<Character> f(final StringBuilder s) {
          return fromString(s.toString()).toOption();
        }
      };

  /**
   * A function that converts string builders to eithers.
   *
   * @return A function that converts string builders to eithers.
   */
  public static <A> Func<P1<A>, Func<StringBuilder, Either<A, Character>>> StringBuilder_Either() {
    return new Func<P1<A>, Func<StringBuilder, Either<A, Character>>>() {
      public Func<StringBuilder, Either<A, Character>> f(final P1<A> a) {
        return new Func<StringBuilder, Either<A, Character>>() {
          public Either<A, Character> f(final StringBuilder s) {
            return fromString(s.toString()).toEither(a);
          }
        };
      }
    };
  }

  /**
   * A function that converts string builders to strings.
   */
  public static final Func<StringBuilder, String> StringBuilder_String = new Func<StringBuilder, String>() {
    public String f(final StringBuilder s) {
      return s.toString();
    }
  };

  /**
   * A function that converts string builders to string buffers.
   */
  public static final Func<StringBuilder, StringBuffer> StringBuilder_StringBuffer = new Func<StringBuilder, StringBuffer>() {
    public StringBuffer f(final StringBuilder s) {
      return new StringBuffer(s);
    }
  };

  // END StringBuilder ->
}
