package ascm;

public class UserProcExpr extends Expr {
  private LstExpr params;
  private ProcBody body;
  private Environment env;
  
  public UserProcExpr(LstExpr p, ProcBody b, Environment e) {
    super(ExprType.USERPROC);
    
    params = p;
    body = b;
    env = e;
  }
  
  public LstExpr params() {
    return params;
  }
  
  public ProcBody body() {
    return body;
  }
  
  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}