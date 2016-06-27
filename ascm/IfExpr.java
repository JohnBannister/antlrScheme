package ascm;

public class IfExpr extends Expr {
  private Expr test, consequent, alternate;
  private Environment env;

  public IfExpr(Expr t, Expr c, Environment e) {
    super(ExprType.IF);

    test = t;
    consequent = c;
    alternate = null;
    env = e;
  }

  public void addAlternate(Expr a) {
    alternate = a;
  }

  public Expr test() {
    return test;
  }

  public Expr conseq() {
    return consequent;
  }

  public Expr alter() {
    return alternate;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}