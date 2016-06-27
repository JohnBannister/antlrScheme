package ascm;

public abstract class ProcExpr extends Expr {
  protected Environment env;
  
  public ProcExpr() {
    super(ExprType.PRIMPROC);
  }

  public abstract Expr call(LstExpr ops, Environment e) throws Exception;

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}