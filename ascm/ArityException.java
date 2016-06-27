package ascm;

public class ArityException extends Exception {
  public int count;

  public ArityException() {
    super();
  }

  public ArityException(int cnt) {
    count = cnt;
  }
}