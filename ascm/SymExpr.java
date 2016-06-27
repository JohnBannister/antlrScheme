package ascm;

public class SymExpr extends Expr {
  private String value;
  private Environment env;

  public SymExpr(String val, Environment e) {
    super(ExprType.SYM);

    value = val;
    env = e;
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