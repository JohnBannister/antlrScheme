package ascm;

public class Proc_Equal extends ProcExpr {
  public Expr call(LstExpr ops, Environment e) {
    Eval eval = new Eval();

    if(ops == Lsts.nil())
      return this;

    Expr operand = null;
    long firstOperand = 0L;
    boolean result = true;
    int count = 0;

    while(ops != Lsts.nil() && result) {
      operand = eval.eval((Expr)(ops.car()), e);
      ++count;
      
      if (count == 1)
        firstOperand = ((NumExpr) operand).value();
    
      while (operand.exprType() == ExprType.PROCCALL) {
        operand = eval.eval(operand, e);
      }

      if (operand.exprType() == ExprType.ERR) {
        return operand;
      }
      else if (operand.exprType() != ExprType.NUM) {
        return new ErrorExpr("bad operand to =", e);
      }
      
      if (count > 1) {
        if (((NumExpr) operand).value() != firstOperand)
          result = false;
      }
      
      ops = (LstExpr)(ops.cdr());
    }

    if (result)
      return new BoolExpr("#t", e);
    else
      return new BoolExpr("#f", e);
  }
  
}