package ascm;

public class ProcDefinition extends Definition {
  private SymExpr name;
  private LstExpr params;
  private ProcBody body;
  private Environment env;
  
  
  public ProcDefinition(SymExpr n, LstExpr p, ProcBody b, Environment e) {
    super(LineType.PROCDFN);
    
    name = n;
    params = p;
    body = b;
    env = e;
  }
  
  public SymExpr name() {
    return name;
  }
  
  public LstExpr params() {
    return params;
  }
  
  public ProcBody body() {
    return body;
  }
  
  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }

  public void visit(PrintVisitor p) {  // never used
    p.print(null);
  }
}