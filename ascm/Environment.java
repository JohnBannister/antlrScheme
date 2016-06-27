package ascm;

import java.util.HashMap;
import java.util.Map;
import static ascm.ExprType.*;

public class Environment {
  private HashMap<String, Expr> env;
  private Environment parent;

  public Environment() {
    this(null);
  }

  public Environment(Environment par) {
    parent = par;
    env = new HashMap<String, Expr>();
  }

  public Environment parent() {
    return parent;
  }

  public Expr lookup(String lu) {
    boolean found = false;
    Environment theEnv = this;
    Expr result = null;

    while(!found && theEnv != null) {
      if(!theEnv.getMap().containsKey(lu)) {
        theEnv = theEnv.parent();
      }
      else {
        result = theEnv.getMap().get(lu);
        found = true;
      }
    }

    return result;
  }

  public void set(String key, Expr val) {
    env.put(key, val);
  }

  // dump an environment to the console -- aids in debugging
  public static void dumpEnv(Environment env) {
    Environment test = env;
    
    System.out.println("\tDumping environment:");
    
    while (test != null) {
      for(Map.Entry<String, Expr> entry : test.getMap().entrySet()) {
        String val = null;
        Expr value = entry.getValue();
        
        if (value.exprType() != PRIMPROC)
          System.out.print("\t" + entry.getKey() + " : ");
        
        switch(value.exprType()) {
          case NUM:
          val = ((NumExpr) value).value().toString();
          break;
          
          case BOOL:
          val = ((BoolExpr) value).value().toString();
          break;
          
          case CHAR:
          val = ((CharExpr) value).value().toString();
          break;
          
          case STR:
          val = ((StrExpr) value).value();
          break;
          
          case LST:
          val = "List";
          break;
          
          case SYM:
          val = ((SymExpr) value).value();
          break;
          
          case PROC:
          case USERPROC:
          case PRIMPROC:
          case LAMBDAPROC:
          val = "#[process]";
          
        }
        
        if (value.exprType() != PRIMPROC)
          System.out.println(val);
      }
      
      test = test.parent();
    }
  }

  private HashMap<String, Expr> getMap() {
    return env;
  }

}