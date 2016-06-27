package ascm;

public class VarDefinition extends Definition {
  private Expr variable;
  private Expr definition;
  private Environment env;

  public VarDefinition(Expr v, Expr d, Environment e) {
    super(LineType.VARDFN);
    
    variable = v;
    definition = d;
    env = e;
  }

  public Expr varName() {
    return variable;
  }

  public Expr dfn() {
    return definition;
  }


  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {  // never used
    p.print(null);
  }
}