package ascm;

public class NumExpr extends Expr {
  private Long value;
  private Environment env;

  public NumExpr(String val, Environment e) {
    super(ExprType.NUM);
    value = new Long(val);
  }

  public Long value() {
    return value;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}