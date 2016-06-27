package ascm;

public class Command extends SCMLine {
  private Expr body;
  private Environment env;

  public Command(Expr exp, Environment e) {
    super(LineType.CMD);

    body = exp;
    env = e;
  }
  
  public Expr body() {
    return body;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(body);
  }
}