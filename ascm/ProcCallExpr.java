package ascm;

public class ProcCallExpr extends Expr {
  private Expr operator;
  private LstExpr operands;
  private Environment env;

  public ProcCallExpr(Expr op, Environment e) {
    super(ExprType.PROCCALL);

    operator = op;
    env = e;
  }

  public void setArgs(LstExpr l) {
    operands = l;
  }

  public Expr operator() {
    return operator;
  }

  public LstExpr operands() {
    return operands;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}