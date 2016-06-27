package ascm;

import java.util.Set;

public class Proc_Mult extends ProcExpr {
  public Expr call(LstExpr ops, Environment e) {
    Eval eval = new Eval();

    if(ops == Lsts.nil())
      return this;

    long product = 1;

    while(ops != Lsts.nil()) {
      Expr op = eval.eval((Expr)(ops.car()), e);
      
      while (op.exprType() == ExprType.PROCCALL) {
        op = eval.eval(op, e);
      }

      if(op.exprType() != ExprType.NUM) {
        if(op.exprType() == ExprType.ERR)
          return op;
        else
          return new ErrorExpr("bad operand " + op.exprType(), e);
      }

      product *= ((NumExpr)op).value();
      ops = (LstExpr)(ops.cdr());
    }

    return new NumExpr(new Long(product).toString(), e);
  }
}