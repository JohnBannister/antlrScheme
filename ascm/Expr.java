package ascm;

public abstract class Expr extends AST {
  private final ExprType expType;

  public Expr(ExprType t) {
    expType = t;
  }

  public final ExprType exprType() {
    return expType;
  }
}