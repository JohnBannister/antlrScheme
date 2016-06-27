package ascm;

public class BooleanFactory {
  private static Boolean t = null;
  private static Boolean f = null;

  public static Boolean t() {
    if (t == null)
      t = new Boolean(true);

    return t;
  }

  public static Boolean f() {
    if (f == null)
      f = new Boolean(false);

    return f;
  }
}