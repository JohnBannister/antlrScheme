package ascm;

public class LstExpr extends Expr {
  private Expr car;
  private Expr cdr;
  private Environment env;

  public LstExpr(Environment e) {
    super(ExprType.LST);
    car = null;
    cdr = null;
    
    env = e;
  }

  public LstExpr(Expr car, Expr cdr, Environment e) {
    this(e);
    this.car = car;
    this.cdr = cdr;
  }

  public Expr car() {
    return car;
  }

  public Expr cdr() {
    return cdr;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}