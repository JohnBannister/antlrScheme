package ascm;

public class Print extends PrintVisitor {
  private final static LstExpr NIL = Lsts.nil();

  public void print(Expr e) {
    if (e == NIL) {
      printVal("()");
      return;
    }

    if (e != null) {
      switch(e.exprType()) {
      case NUM:
        printVal(((NumExpr) e).value().toString());
        break;

      case BOOL:
        printVal(((BoolExpr) e).value() ? "#t" : "#f");
        break;

      case CHAR:
        String prn;
        Character c = ((CharExpr) e).value();
        if (c.equals('\n'))
          prn = "newline";
        else if (c.equals(' '))
          prn = "space";
        else
          prn = c.toString();
        printVal("#\\" + prn);
        break;

      case STR:
        printVal(((StrExpr) e).value());
        break;

      case LST:
        printList((LstExpr) e);
        break;

      case SYM:
        printVal(((SymExpr) e).value());
        break;

      case OK:
        printVal("ok");
        break;

      case ERR:
        printVal(((ErrorExpr) e).msg());
        break;

      case PRIMPROC:
      case USERPROC:
      case LAMBDAPROC:
        printVal("#[procedure]");

      default:
        printVal("");
      }
    }
  }

  private void printList(LstExpr li) {
    printVal("(");
    printPair(li);
    printVal(")");
  }

  private void printPair(LstExpr li) {
    Expr car = li.car();
    Expr cdr = li.cdr();

    print(car);

    if (cdr == NIL) {
      return;
    }
    else if (cdr.exprType() == ExprType.LST) {
      printVal(" ");
      printPair((LstExpr)cdr);
    }
    else {
      printVal(" . ");
      print(cdr);
    }
  }

  private void printCar(LstExpr li) {
    print(li.car());
    if(li.cdr() != Lsts.nil())
      printVal(" ");
  }

  private void printCdr(LstExpr li) {
    printCar(li);
    if(li.cdr() != Lsts.nil()) {
      if(Lsts.isList(li.cdr()))
        printCdr((LstExpr)(li.cdr()));
      else
        print(li.cdr());
    }
  }

  private void printVal(String s) {
    System.out.print(s);
  }
}