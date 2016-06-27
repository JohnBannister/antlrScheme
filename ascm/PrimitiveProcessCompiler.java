package ascm;

public class PrimitiveProcessCompiler {
  private Environment env;

  public PrimitiveProcessCompiler(Environment env) {
    this.env = env;
  }

  public Environment compile() {
    System.out.println("compiling primitive procedures ...");
    env.set("+", new Proc_Add());
    env.set("null?", new Proc_IsNull());
    env.set("boolean?", new Proc_IsBoolean());
    env.set("symbol?", new Proc_IsSymbol());
    env.set("integer?", new Proc_IsInteger());
    env.set("char?", new Proc_IsChar());
    env.set("string?", new Proc_IsString());
    env.set("pair?", new Proc_IsPair());
    env.set("cons", new Proc_Cons());
    env.set("car", new Proc_Car());
    env.set("cdr", new Proc_Cdr());
    env.set("-", new Proc_Sub());
    env.set("*", new Proc_Mult());
    env.set("=", new Proc_Equal());
    

    return env;
  }

}