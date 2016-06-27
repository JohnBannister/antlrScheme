package ascm;

public class BoolExpr extends Expr {
  private Boolean value;
  private Environment env;

  public BoolExpr(String val, Environment e) {
    super(ExprType.BOOL);
    if("#t".equals(val))
      value = BooleanFactory.t();
    else
      value = BooleanFactory.f();
    
    env = e;
  }

  public Boolean value() {
    return value;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}