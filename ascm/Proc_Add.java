package ascm;

import java.util.Set;

public class Proc_Add extends ProcExpr {
  public Expr call(LstExpr ops, Environment e) {
    Eval eval = new Eval();

    if(ops == Lsts.nil())
      return this;

    long sum = 0;

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

      sum += ((NumExpr)op).value();
      ops = (LstExpr)(ops.cdr());
    }

    return new NumExpr(new Long(sum).toString(), e);
  }
}