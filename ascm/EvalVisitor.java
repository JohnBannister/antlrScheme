package ascm;

public abstract class EvalVisitor {
  public abstract AST eval(SCMLine s, Environment env);
  public abstract AST eval(Expr e, Environment env);
}