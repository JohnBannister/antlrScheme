package ascm;

public class Proc_Cons extends ProcExpr {
  public Expr call(LstExpr ops, Environment e) throws ArityException {
    Environment env = new Environment(e);
    Eval eval = new Eval();
    int arity = 2, args = 0;
    Expr arg1 = null, arg2 = null;

    // pull arguments out of the list and check for correct number
    if(ops != Lsts.nil()) {
      arg1 = eval.eval(ops.car(), env);
      ++args;
      ops = (LstExpr)(ops.cdr());
      
      if(ops != Lsts.nil()) {
        arg2 = eval.eval(ops.car(), env);
        ++args;
        ops = (LstExpr)(ops.cdr());
        
        if(ops != Lsts.nil())
          ++args;
      }
    }
    else
      return this;
    
    if (args != arity)
      throw new ArityException(arity);
    else
      return Lsts.cons(arg1, arg2, env); 
  }
}