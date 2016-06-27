package ascm;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.ListIterator;
import static ascm.ExprType.*;
import static ascm.LineType.*;

public class Eval extends EvalVisitor {
  public Expr eval(SCMLine s, Environment e) {
    switch(s.lineType()) {
    case CMD:
      return eval(((Command) s).body(), e);

    case VARDFN:
      return eval((VarDefinition) s, e);
      
    case PROCDFN:
      return eval((ProcDefinition) s, e);

    default:
      return null;
    }
  }
  
  // dispatch a definition to the proper evaluator
  public Expr eval(Definition d, Environment e) {
    if(d.lineType() == VARDFN)
      return eval((VarDefinition) d, e);
    else if (d.lineType() == PROCDFN)
      return eval((ProcDefinition) d, e);
    else
      return new ErrorExpr("unknown error determining definition type", e);
  }
  
  public Expr eval(ProcDefinition p, Environment e) {
    String name = p.name().value();
    LstExpr params = p.params();
    ProcBody body = p.body();
    
    e.set(name, new UserProcExpr(params, body, e));
    
    return new OKExpr(e);
  }

  public Expr eval(VarDefinition d, Environment e) {
    SymExpr varName = (SymExpr)(d.varName());
    Expr dfn = d.dfn();

    e.set(varName.value(), eval(dfn, e));
    
    return new OKExpr(e);
  }
  
  /******************************************************************************************
   evaluate expressions - tailcall optimization thanks to SICP and Peter Michaux'a
   excellent blog:
   (http://peter.michaux.ca/articles/scheme-from-scratch-bootstrap-v0_13-lambda-the-ultimate)
   ******************************************************************************************/
  public Expr eval(Expr ex, Environment env) {
    tailcall:  // label
    while (true) {
      if (isSelfEvaluating(ex)) {
        return ex;
      }
      else if (ex.exprType() == SYM) {
        return eval_sym(ex, env);
      }
      else if (ex.exprType() == QUOT) {
        return eval_quote(ex, env);
      }
      else if (ex.exprType() == ASN) {
        return eval_assign(ex, env);
      }
      else if (ex.exprType() == IF) {
        ex = isTrue(eval(((IfExpr) ex).test(), env)) ? ((IfExpr) ex).conseq() : ((IfExpr) ex).alter();
        continue tailcall;
      }
      else if (ex.exprType() == LAMBDAPROC) {
        // preserve the environment by returning a new LambdaExpr
        return new LambdaExpr(((LambdaExpr) ex).params(), ((LambdaExpr) ex).body(), env);
      }
      else if (ex.exprType() == PROCCALL) {
        Expr procedure = eval(((ProcCallExpr) ex).operator(), env);
        
        if (procedure.exprType() == ERR)
          return procedure;
        
        LstExpr arguments = ((ProcCallExpr) ex).operands();
        
        if (procedure.exprType() == LAMBDAPROC) {  // there's environment in there we need
          env = new Environment(env);
          env = new Environment(((LambdaExpr) procedure).envir());
        }
        
        if (procedure.exprType() == PRIMPROC) {
          Expr result = null;

          try {
            result = ((ProcExpr) procedure).call(arguments, env);
          }
          catch(ArityException exc) {
            int num = exc.count;
            result = new ErrorExpr("procedure requires " + num + " argument" + (num == 1 ? "" : "s"), env);
          }
          catch(Exception exc) {
            result = new ErrorExpr("unknown error for primitive procedure", env);
          }
    
          return result;  // will not be null
        }
        else if (procedure.exprType() == USERPROC) {
          return evalUserProc((ProcCallExpr) ex, env);
        }
        else if (procedure.exprType() == LAMBDAPROC) {
          Environment thisEnv = new Environment(env);
          LambdaExpr le = (LambdaExpr) procedure;
          LstExpr params = le.params();
          ProcBody body = le.body();
    
          // check sizes of the lists
          int arity = Lsts.size(params);
          if(Lsts.size(arguments) != arity)
            return new ErrorExpr("wrong number of arguments to lambda expression", thisEnv);
    
          // insert arguments into environment
          for (int i = 0; i < arity; ++i) {
            String var = ((SymExpr) params.car()).value();
            Expr val = eval(arguments.car(), env);
            thisEnv.set(var, val);
      
            params = ((LstExpr) params.cdr());
            arguments = ((LstExpr) arguments.cdr());
          }
    
          // if the lambda has definitions, insert them into the environment
          ListIterator<Definition> dfns = body.definitions().listIterator();
          while(dfns.hasNext()) {
            Definition dfn = dfns.next();
      
            eval(dfn, thisEnv);
          }
    
          // evaluate the expressions of the lambda
          ListIterator<Expr> exprs = body.expressions().listIterator();
          while(exprs.hasNext()) {
            Expr thisExp = exprs.next();
      
            if(!exprs.hasNext()) {
              env = thisEnv;
              ex = thisExp;
              
              continue tailcall;
            }
            else
              eval(thisExp, thisEnv);
          }
        }
      }
    }
  }
  
  private Expr eval_sym(Expr ex, Environment env) {
    Expr result = env.lookup(((SymExpr) ex).value());
    
    if (result == null)
      return new ErrorExpr("symbol " + ((SymExpr)ex).value() + " not defined", env);
    
    ExprType t = result.exprType();
      if (t != SYM)
        return result;
    else
      return eval(result, env);
  }
  
  private Expr eval_quote(Expr ex, Environment env) {
    return ((QuoteExpr) ex).body();
  }
  
  private Expr eval_assign(Expr ex, Environment env) {
    String name = ((AssignExpr) ex).varName();
    Expr result = env.lookup(name);
    
    if (result == null)
      return new ErrorExpr("symbol " + name + " not defined", env);
    
    env.set(name, ((AssignExpr) ex).value());
    
    return new OKExpr(env);
  }
  
  // evaluate a call to a user-defined procedure
  private Expr evalUserProc(ProcCallExpr p, Environment e) {
    Environment thisEnv = new Environment(e);
    
    // the caller
    String name = ((SymExpr) p.operator()).value();
    LstExpr args = p.operands();
    UserProcExpr procedure = ((UserProcExpr) e.lookup(name));
    
    if (procedure == null)  // this should never happen
      return new ErrorExpr(procedure + " not defined", thisEnv);
    
    // the called
    LstExpr params = procedure.params();
    ProcBody body = procedure.body();
    
    // check sizes of the lists
    int arity = Lsts.size(params);
    if(Lsts.size(args) != arity)
      return new ErrorExpr(name + " takes exactly " + arity + " argument" + ((arity == 1) ? "" : "s"), thisEnv);
    
    // insert arguments into environment
    for (int i = 0; i < arity; ++i) {
      String var = ((SymExpr) params.car()).value();
      Expr val = eval(args.car(), e);  // eval argument in parent scope
      thisEnv.set(var, val);
      params = ((LstExpr) params.cdr());
      args = ((LstExpr) args.cdr());
    }
    
    // if the procedure has definitions, insert them into the environment
    ListIterator<Definition> dfns = body.definitions().listIterator();
    while(dfns.hasNext()) {
      Definition dfn = dfns.next();
      
      eval(dfn, thisEnv);
    }
    
    // evaluate the expressions of the procedure
    ListIterator<Expr> exprs = body.expressions().listIterator();
    while(exprs.hasNext()) {
      Expr thisExp = exprs.next();
      
      // return the result of the final expression
      if(!exprs.hasNext())
        return eval(thisExp, thisEnv);
      else
        eval(thisExp, thisEnv);
    }
    
    return new ErrorExpr("unknown error evaluating user procedure", thisEnv);
  }

  /******************************************************************************
   helper functions
   *****************************************************************************/
  private boolean isSelfEvaluating(Expr e) {
    ExprType t = e.exprType();
    return (t == NUM || t == BOOL || t == CHAR || t == STR);
  }

  // everything is true except for '#f'
  private boolean isTrue(Expr e) {
    if(e.exprType() == BOOL)
      return ((BoolExpr)e).value() == BooleanFactory.t();
    else
      return true;
  }
}