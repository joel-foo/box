/**
 * A generic box storing an item.
 * CS2030S Lab 4
 * AY22/23 Semester 2
 *
 * @author Joel Foo (Group 12D)
 */
class Box<T> {
  private final T content;
  
  private static final Box<?> EMPTY_BOX = new Box<Object>(null);

  private Box(T content) {
    this.content = content;
  }
 
  public static <T> Box<T> empty() {
    @SuppressWarnings("unchecked")
    //It is always safe to cast EMPTY_BOX from Box<?> to Box<T>
    Box<T> b = (Box<T>) EMPTY_BOX;
    return b;
  }

  public static <T> Box<T> of(T content) {
    if (content == null) {
      return null;
    } 
    return new Box<T>(content);
  }

  public static <T> Box<T> ofNullable(T content) {
    if (content == null) {
      return empty();
    }
    return new Box<T>(content);
  }
    
  public boolean isPresent() {
    return this.content != null;
  }

  public Box<T> filter(BooleanCondition<? super T> b) {
    if (this == Box.EMPTY_BOX || b.test(this.content)) {
      return this;
    }
    return empty();
  }

  public <U> Box<U> map(Transformer<? super T, ? extends U> transformer) {
    if (this == Box.EMPTY_BOX) {
      return empty();
    } 
    return new Box<U>(transformer.transform(this.content));
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Box<?>) {
      Box<?> b = (Box<?>) obj;
      if (this.content == b.content) {
        return true;
      }
      if (this.content == null || b.content == null) {
        return false;
      }
      return this.content.equals(b.content);
    }
    return false;
  } 

  @Override
  public String toString() {
    if (content == null) {
      return "[]";
    }
    return "[" + content.toString() + "]";
  }
}

