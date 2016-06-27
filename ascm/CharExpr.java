package ascm;

public class CharExpr extends Expr {
  private Character value;
  private Environment env;

  public CharExpr(String val, Environment e) {
    super(ExprType.CHAR);

    if(val.length() == 3 && val.indexOf("#\\") == 0)
      value = new Character(val.charAt(2));
    else if (val.equals("#\\newline"))
      value = new Character('\n');
    else if (val.equals("#\\space"))
      value = new Character(' ');
    else
      value = null; // this should never happen
    
    env = e;
  }

  public Character value() {
    return value;
  }

  public AST visit(EvalVisitor e) {
    return e.eval(this, env);
  }
  public void visit(PrintVisitor p) {
    p.print(this);
  }
}