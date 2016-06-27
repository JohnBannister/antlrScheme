JC = javac
JFLAGS = -d ./
JVM = java
ANTLR = java org.antlr.Tool
GRAMMAR = ascm/scm.g

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	ascm/AST.java \
	ascm/BoolExpr.java \
	ascm/BooleanFactory.java \
	ascm/CharExpr.java \
	ascm/Command.java \
	ascm/Eval.java \
	ascm/EvalVisitable.java \
	ascm/EvalVisitor.java \
	ascm/Expr.java \
	ascm/ExprType.java \
	ascm/LineType.java \
	ascm/LstExpr.java \
	ascm/Lsts.java \
	ascm/NumExpr.java \
	ascm/Print.java \
	ascm/PrintVisitable.java \
	ascm/PrintVisitor.java \
	ascm/QuoteExpr.java \
	ascm/SCMLine.java \
	ascm/StrExpr.java \
	ascm/SymExpr.java \
	ascm/ascm.java \
	ascm/scmLexer.java \
	ascm/scmParser.java \
	ascm/VarDefinition.java \
	ascm/ErrorExpr.java \
	ascm/OKExpr.java \
	ascm/Environment.java \
	ascm/AssignExpr.java \
	ascm/IfExpr.java \
	ascm/ProcExpr.java \
	ascm/ProcCallExpr.java \
	ascm/Proc_Add.java \
	ascm/PrimitiveProcessCompiler.java \
	ascm/Proc_IsNull.java \
	ascm/Proc_IsBoolean.java \
	ascm/Proc_IsSymbol.java \
	ascm/Proc_IsInteger.java \
	ascm/Proc_IsChar.java \
	ascm/Proc_IsString.java \
	ascm/Proc_IsPair.java \
	ascm/ArityException.java \
	ascm/ProcBody.java \
	ascm/ProcDefinition.java \
	ascm/Proc_Cons.java \
	ascm/LambdaExpr.java \
	ascm/Proc_Car.java \
	ascm/Proc_Cdr.java \
	ascm/Proc_Sub.java \
	ascm/Proc_Mult.java \
	ascm/Proc_Equal.java 
	
MAIN = ascm/ascm

default: classes
	
classes: $(CLASSES:.java=.class)
	
run: $(MAIN).class
	$(JVM) $(MAIN)

antlr: ascm/scm.g
	$(ANTLR) $(GRAMMAR)

clean:
	$(RM) ascm/*.class
	$(RM) ascm/scmParser.java
	$(RM) ascm/scmLexer.java
	$(RM) ascm/scm.tokens
	$(RM) ascm/*.java.orig

