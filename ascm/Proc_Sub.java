package ascm;

import java.util.Set;

public class Proc_Sub extends ProcExpr {
  public Expr call(LstExpr ops, Environment e) {
    Eval eval = new Eval();

    if(ops == Lsts.nil())
      return this;

    long difference = 0;
    int operands = 0;
    long first_operand = 0;

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
      
      operands++;
      
      if (operands == 1) {
        difference = ((NumExpr) op).value() * (-1L);
        first_operand = ((NumExpr) op).value();
      }
      else if (operands == 2) {
        difference = first_operand - ((NumExpr) op).value();
      }
      else {
        difference -= ((NumExpr) op).value();
      }
      ops = (LstExpr)(ops.cdr());
    }

    return new NumExpr(new Long(difference).toString(), e);
  }
}