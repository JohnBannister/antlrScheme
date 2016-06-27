package ascm;

public class AssignExpr extends Expr {
  private String var;
  private Expr val;
  private Environment env;

  public AssignExpr(String var, Expr val, Environment e) {
    super(ExprType.ASN);

    this.var = var;
    this.val = val;
    env = e;
  }

  public String varName() {
    return var;
  }

  public Expr value() {
    return val;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}