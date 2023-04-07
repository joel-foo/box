package cs2030s.fp;

/**
 * CS2030S PE2 Question 1.
 * AY20/21 Semester 2
 *
 * @author A0252784W
 */

public abstract class Try<T> {

  /**
   * Creates a success or failure depending on the producer.
   *
   * @param <T> The type of value in Try
   * @param p The producer
   * @return A Try instance
   *
   */
  public static <T> Try<T> of(Producer<? extends T> p) {
    try {
      return success(p.produce());
    } catch (Throwable t) {
      return failure(t);
    }
  }


  /**
   * Creates a success.
   *
   * @param <T> The type of value in Success
   * @param t the value in Success
   * @return a Success instance
   */
  public static <T> Success<T> success(T t) {
    return new Success<T>(t);
  }

  /**
   * Creates a failure.
   *
   * @param <T> The type of value in Failure
   * @param t the value in Failure
   * @return a Failure instance
   */
  public static <T> Try<T> failure(Throwable t) {
    return new Failure<T>(t);
  }

  public abstract <U> Try<U> map(Transformer<? super T, ? extends U> t);

  public abstract <U> Try<U> flatMap(Transformer<? super T, ? extends Try<? extends U>> t);

  /**
   * Attempts to consume the throwable if calling Try is a failure.
   * @param c The consumer of the Throwable
   * @return a Try instance
   */
  public abstract Try<T> onFailure(Consumer<? super Throwable> c);


  /**
   * Attempts to transform the Throwable if calling Try is a failure.
   * @param t The transformer for the Throwable
   * @return a Try instance
   */
  public abstract Try<T> recover(Transformer<? super Throwable, ? extends T> t); 

  public abstract T get() throws Throwable;

  public static final class Failure<T> extends Try<T> {
    
    private final Throwable t;

    Failure(Throwable t) {
      this.t = t;
    }

    @Override
    public T get() throws Throwable {
      throw this.t;
    }

    @Override
    public <U> Try<U> map(Transformer<? super T, ? extends U> tf) {
      @SuppressWarnings("unchecked")
      // This is safe.
      Try<U> x = (Try<U>) this;
      return x;
    }

    @Override
    public <U> Try<U> flatMap(Transformer<? super T, ? extends Try<? extends U>> tf) {
      @SuppressWarnings("unchecked")
      // This is safe.
      Try<U> x = (Try<U>) this;
      return x;
    }

    @Override
    public Try<T> onFailure(Consumer<? super Throwable> c) {
      try {
        c.consume(this.t);
        return this;
      } catch (Throwable throwable) {
        return failure(throwable);
      }
    }

    @Override
    public Try<T> recover(Transformer<? super Throwable, ? extends T> tf) {
      try {
        return success(tf.transform(this.t));
      } catch (Throwable throwable) {
        return failure(throwable);
      }
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Failure<?>) {
        Failure<?> f = (Failure<?>) o;
        if (f.t == null && this.t == null) {
          return true;
        }
        if (f.t == null || this.t == null) {
          return false;
        }
        return this.t.toString() == f.t.toString();
      }
      return false;
    }
  }

  public static final class Success<T> extends Try<T> {
    
    private final T t;

    Success(T t) {
      this.t = t;
    }

    @Override
    public T get() {
      return this.t;
    }

    @Override
    public <U> Try<U> map(Transformer<? super T, ? extends U> tf) {
      try {
        return success(tf.transform(this.t));
      } catch (Throwable throwable) {
        return failure(throwable);
      }
    }

    @Override
    public <U> Try<U> flatMap(Transformer<? super T, ? extends Try<? extends U>> tf) {
      Try<? extends U> x = null;
      try {
        x = tf.transform(this.t);
      } catch (Throwable t) {
        // Do nothing.
      }
      try {
        return success(x.get());
      } catch (Throwable throwable) {
        return failure(throwable);
      }
    }

    @Override
    public Try<T> onFailure(Consumer<? super Throwable> c) {
      return this;
    }

    @Override
    public Try<T> recover(Transformer<? super Throwable, ? extends T> t) {
      return this;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Success<?>) {
        Success<?> s = (Success<?>) o;
        if (s.t == null && this.t == null) {
          return true;
        } 
        if (s.t == null || this.t == null) {
          return false;
        }
        return this.t.equals(s.t);
      }
      return false;
    }
  }

}
