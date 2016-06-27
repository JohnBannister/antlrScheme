package ascm;

public class QuoteExpr extends Expr {
  private Expr body;
  private Environment env;

  public QuoteExpr(Expr exp, Environment e) {
    super(ExprType.QUOT);

    body = exp;
    env = e;
  }

  public Expr body() {
    return body;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {  // never used
    p.print(this);
  }
}