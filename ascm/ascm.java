package ascm;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import org.antlr.stringtemplate.*;
import java.util.Scanner;
import java.util.HashMap;

public class ascm {
  public static void main(String[] args) {
    Scanner kbd = new Scanner(System.in);
    Environment env = new Environment();
    Eval eval = new Eval();
    Print print = new Print();
    scmParser parser = new scmParser(env);

    // add primitive procedures to the symbol table
    env = new PrimitiveProcessCompiler(env).compile();
    

    System.out.println("Welcome to AntlrScheme; 'quit' to exit.");
    while(true) {
      System.out.print("\nascm> ");
      String input = kbd.nextLine();

      if(input.equals("quit")) break;

      AST ast = null;
      try {
        ast = parser.run(input);
      }
      catch(Exception e) {

      }
      if(ast != null)
        (ast.visit(eval)).visit(print);
    }

  }
}