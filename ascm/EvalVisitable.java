package ascm;

public interface EvalVisitable {
  public AST visit(EvalVisitor e);
}