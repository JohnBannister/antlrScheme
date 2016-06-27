package ascm;

public class Proc_IsString extends ProcExpr {
  public Expr call(LstExpr ops, Environment e) throws ArityException {
    Environment env = new Environment(e);
    Eval eval = new Eval();
    int arity = 1, args = 0;
    Expr result = null;
    
    if(ops != Lsts.nil()) {
      result = eval.eval(ops.car(), env);
      ops = (LstExpr)(ops.cdr());
      if(ops == Lsts.nil())
        args = 1;
      else
        args = 2;  // i.e., more than arity
    }
    
    if (args != arity) {
      throw new ArityException(arity);
    }
    else if(result == null) {
      return new ErrorExpr("unknown error", env);
    }
    else {
      if(result.exprType() == ExprType.STR)
        return new BoolExpr("#t", env);
      else
        return new BoolExpr("#f", env);
    }    
  }
}