package ascm;

public class StrExpr extends Expr {
  private String value;
  private Environment env;

  public StrExpr(String val, Environment e) {
    super(ExprType.STR);

    value = val;
  }

  public String value() {
    return value;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}