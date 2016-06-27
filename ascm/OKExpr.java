package ascm;

public class OKExpr extends Expr {
  private Environment env;
  public OKExpr(Environment e) {
    super(ExprType.OK);
    
    env = e;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}