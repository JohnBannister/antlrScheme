package ascm;

import java.util.ArrayList;

public class Lsts {
  private static LstExpr nil = null;

  public static LstExpr nil() {
    if (nil == null)
      nil = new LstExpr(new Environment());  // env not needed, but this is consistent
    return nil;
  }

  public static LstExpr make_list(ArrayList<Expr> ar, Environment e) {
    if(ar.size() == 0)
      return nil();

    return make_list(ar, 0, e);
  }

  private static LstExpr make_list(ArrayList<Expr> ar, int idx, Environment e) {
    if(idx == ar.size())
      return nil();
    return cons(ar.get(idx), make_list(ar, idx + 1, e), e);
  }

  public static LstExpr make_pair(ArrayList<Expr> ar1, ArrayList<Expr> ar2, Environment e) {
    if (ar1.size() == 1 && ar2.size() == 1)
      return cons(ar1.get(0), ar2.get(0), e);
    else if (ar1.size() == 1)
      return cons(ar1.get(0), make_list(ar2, e), e);
    else if (ar2.size() == 1)
      return cons(make_list(ar1, e), ar2.get(0), e);
    else
      return cons(make_list(ar1, e), make_list(ar2, e), e);
  }

  public static LstExpr append(LstExpr li, Expr ex, Environment e) {
    if (li == nil()) {
      return cons(ex, li, e);
    }
    else
      return cons(li.car(), append((LstExpr)(li.cdr()), ex, e), e);
  }

  public static LstExpr cons(Expr ex, Expr li, Environment e) {
    return new LstExpr(ex, li, e);
  } 

  public static boolean isList(Expr ex) {
    return ex.getClass().getName().equals("LstExpr");
  }
  
  // only works on proper lists (i.e., terminated by NIL)
  public static int size(LstExpr li) {
    if(li == nil())
      return 0;
    else
      return 1 + size((LstExpr)(li.cdr()));
  }
}