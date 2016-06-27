package ascm;

public class Proc_Cdr extends ProcExpr {
  public Expr call(LstExpr ops, Environment e) throws ArityException {
    Environment env = new Environment(e);
    Eval eval = new Eval();
    int arity = 1, args = 0;
    Expr arg1 = null;

    // pull arguments out of the list and check for correct number
    if(ops != Lsts.nil()) {
      arg1 = eval.eval(ops.car(), env);
      ++args;
      ops = (LstExpr)(ops.cdr());
      
      if(ops != Lsts.nil()) {
        ++args;
      }
    }
    else
      return this;
    
    if (args != arity)
      throw new ArityException(arity);
    else if (arg1.exprType() != ExprType.LST) {
      return new ErrorExpr("cdr takes a list", e);
    }
    else {
      if (arg1 != Lsts.nil())
        return ((LstExpr) arg1).cdr();
      else
        return new ErrorExpr("cannot cdr NIL", e);
    }
  }
}