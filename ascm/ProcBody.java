package ascm;

import java.util.ArrayList;

public class ProcBody {
  private ArrayList<Definition> definitions;
  private ArrayList<Expr> expressions;
  private Environment env;
  
  public ProcBody(ArrayList<Definition> df, ArrayList<Expr> ex, Environment e) {
    definitions = df;
    expressions = ex;
    env = e;
  }
  
  public ArrayList<Definition> definitions() {
    return definitions;
  }
  
  public ArrayList<Expr> expressions() {
    return expressions;
  }
}