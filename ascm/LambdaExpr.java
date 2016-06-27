package ascm;

public class LambdaExpr extends Expr {
  private LstExpr params;
  private ProcBody body;
  private Environment env;
  
  public LambdaExpr(LstExpr p, ProcBody b, Environment e) {
    super(ExprType.LAMBDAPROC);
    
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
  
  public Environment envir() {
    return env;
  }
  
  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}