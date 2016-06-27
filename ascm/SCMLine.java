package ascm;

public abstract class SCMLine extends AST {
  private LineType lineType;

  public SCMLine(LineType lt) {
    lineType = lt;
  }

  public final LineType lineType() {
    return lineType;
  }
}