package ascm;

public class ErrorExpr extends Expr {
  private String msg;
  private Environment env;

  public ErrorExpr(String m, Environment e) {
    super(ExprType.ERR);
    
    msg = m;
    env = e;
  }

  public String msg() {
    return msg;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}