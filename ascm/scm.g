grammar scm;

@lexer::header {
  package ascm;
}

@parser::header {
  package ascm;
  
  import java.util.ArrayList;
}

@parser::members {
  private Environment env;
  
  public scmParser(Environment e) {
    super(null);
    
    env = e;
  }
  
  public AST run(String input) throws Exception {
    ANTLRStringStream in = new ANTLRStringStream(input);
    scmLexer lex = new scmLexer(in);
    CommonTokenStream tokens = new CommonTokenStream(lex);
    super.setTokenStream(tokens);
    
    return this.program();
  }
  
  //FIXME: del this
  private void print(String s) {
    System.out.println(s);
  }
}

/******************************************************************************
 programs and definitions
 *****************************************************************************/
program returns [AST ast] :  cd=cmd_or_dfn* {$ast = $cd.scml; } ;

cmd_or_dfn returns [SCMLine scml] :
             c=command {$scml = $c.cmd; }
             | d=definition {$scml = $d.dfn; }
             ;

definition returns [Definition dfn] : '('
                                         'define'
                                                 ( (s1=symbol e=expression
                                                   {$dfn = new VarDefinition($s1.exp, $e.exp, env); })
                                                 | ('(' s2=symbol d=def_formals ')' b=body
                                                   {$dfn = new ProcDefinition((SymExpr)($s2.exp), (LstExpr)($d.exp), $b.bdy, env); })
                                                 )
                                      ')'
                                      ;

def_formals returns [Expr exp] : {ArrayList<Expr> lst = new ArrayList<Expr>(); }
                                 (s=symbol {lst.add($s.exp); })*
                                 {$exp = Lsts.make_list(lst, env); }
                                 ;


command returns [Command cmd] : e=expression {$cmd = new Command($e.exp, env); }  ;

/******************************************************************************
 expressions
 *****************************************************************************/
expression returns [Expr exp] :
             li=literal {$exp = $li.exp; }
             | as=assignment {$exp = $as.exp; }
             | co=conditional {$exp = $co.exp; }
             | pc=procedure_call {$exp = $pc.exp; }
             | la=lambda_expression {$exp = $la.exp; }
             ;
             
procedure_call returns [Expr exp] : '(' rat=operator
                                        {ProcCallExpr p = new ProcCallExpr($rat.exp, env);
                                         ArrayList<Expr> lst = new ArrayList<Expr>(); }
                                        (rand=operand
                                         {lst.add($rand.exp); } 
                                        )*
                                        {p.setArgs(Lsts.make_list(lst, env)); }
                                        ')' {$exp = p; }
                                        ;

operator returns [Expr exp] : e=expression {$exp = $e.exp; }  ;

operand returns [Expr exp] : e=expression {$exp = $e.exp; }  ;

conditional returns [Expr exp] : '(''if' t=test c=consequent
                                 {IfExpr i = new IfExpr($t.exp, $c.exp, env); }
                                 (a=alternate {i.addAlternate($a.exp); })?
                                 ')' {$exp = i; }
                                 ;

test returns [Expr exp] : e=expression {$exp = $e.exp; }  ;

consequent returns [Expr exp] : e=expression {$exp = $e.exp; }  ;

alternate returns [Expr exp] : e=expression {$exp = $e.exp; }  ;

assignment returns [Expr exp] : '(''set!' VARIABLE e1=expression ')' {$exp = new AssignExpr($VARIABLE.text, $e1.exp, env); } ;

literal returns [Expr exp] : q=quotation {$exp = $q.exp; }
                           | ss=self_evaluating {$exp = $ss.exp; }
                           /*| li=list {$exp = $li.exp; }*/
                           ;  // this is temporary

quotation returns [Expr exp] : ('\'' d1=datum {$exp = new QuoteExpr($d1.exp, env); })
                             | ('(quote' d2=datum ')' {$exp = new QuoteExpr($d2.exp, env); })  ;

self_evaluating returns [Expr exp] : NUMBER {$exp = new NumExpr($NUMBER.text, env); }
                | BOOLEAN {$exp = new BoolExpr($BOOLEAN.text, env); }
                | CHARACTER {$exp = new CharExpr($CHARACTER.text, env); }
                | STRING {$exp = new StrExpr($STRING.text, env); }
                | s=symbol {$exp = $s.exp; }
                ;
             
list returns [Expr exp] :
  '(' {ArrayList<Expr> lst1 = new ArrayList<Expr>(); ArrayList<Expr> lst2 = null;}
  ( ')'
    | (d1=datum {lst1.add($d1.exp); }
        (d2=datum {lst1.add($d2.exp); })*
        ('.' d3=datum {lst2 = new ArrayList<Expr>(); lst2.add($d3.exp); })?
      ')'
      )
  ) {if (lst2 == null) $exp = Lsts.make_list(lst1, env); else $exp = Lsts.make_pair(lst1, lst2, env); }
  ;

datum returns [Expr exp] : s=simple_datum {$exp = $s.exp; }
                         | c=compound_datum {$exp = $c.exp; }
                         ;

simple_datum returns [Expr exp] : BOOLEAN {$exp = new BoolExpr($BOOLEAN.text, env); }
                                | NUMBER {$exp = new NumExpr($NUMBER.text, env); }
                                | CHARACTER {$exp = new CharExpr($CHARACTER.text, env); }
                                | STRING {$exp = new StrExpr($STRING.text, env); }
                                | s=symbol {$exp = $s.exp; }
                                ;
                                
symbol returns [Expr exp] : VARIABLE {$exp = new SymExpr($VARIABLE.text, env); }  ;

compound_datum returns [Expr exp] : li=list {$exp = $li.exp; }
                                 /*| vector */  // not implemented
                                 ;

body returns [ProcBody bdy] : {ArrayList<Definition> df = new ArrayList<Definition>();}
                            (d=definition {df.add($d.dfn); })*
                            seq=sequence
                            {$bdy = new ProcBody(df, $seq.pexp, env); }
                            ;

sequence returns [ArrayList<Expr> pexp] : {ArrayList<Expr> ex = new ArrayList<Expr>(); }
                                          (e=expression {ex.add($e.exp); })+
                                          {$pexp = ex; }
                                          ;

lambda_expression returns [Expr exp] : '(''lambda' f=formals b=body ')'
                                       {$exp = new LambdaExpr((LstExpr)($f.exp), $b.bdy, env); }
                                       ;

formals returns [Expr exp] :
                             {ArrayList<Expr> list = new ArrayList<Expr>(); }
                             '('
                             (s1=symbol {list.add($s1.exp); })*
                             ')' {$exp = Lsts.make_list(list, env); }
                             | {ArrayList<Expr> list = new ArrayList<Expr>(); }
                             (s2=symbol {list.add($s2.exp); })
                             {$exp = Lsts.make_list(list, env); }
                             ;


/******************************************************************************
 lexical items
 *****************************************************************************/
VARIABLE : IDENTIFIER  ;

fragment IDENTIFIER : (INITIAL)(SUBSEQUENT)*
                    | PECULIAR_IDENTIFIER  ;
           
fragment INITIAL : LETTER | SPECIAL_INITIAL  ;

fragment LETTER : 'a'..'z'|'A'..'Z'  ;

fragment SPECIAL_INITIAL : '!' | '$' | '%' | '&' | '*' | '/' | ':' | '<'
                | '=' | '>' | '?' | '^' | '_' | '~'  ;
                
fragment SUBSEQUENT : INITIAL | DIGIT | SPECIAL_SUBSEQUENT  ;

fragment DIGIT : '0'..'9'  ;
      
fragment SPECIAL_SUBSEQUENT : '+' | '-' | '.' | '@'  ;

fragment PECULIAR_IDENTIFIER : '+' | '-' | '...'  ;

BOOLEAN : '#t' | '#f'  ;

CHARACTER : '#\\'(.) | '#\\newline' |'#\\space' ;

STRING : '"' STRING_ELEMENT* '"'  ;

fragment STRING_ELEMENT : ~('"' | '\\') | '\\"' | '\\\\'  ;

NUMBER : NUM_10  ;

fragment NUM_10 : COMPLEX_10  ;

fragment COMPLEX_10 : REAL_10  ;

fragment REAL_10 : SIGN? UREAL_10  ;

fragment SIGN : '-' | '+'  ;

fragment UREAL_10 : UINTEGER_10  ;

fragment UINTEGER_10 : DIGIT_10+  ;

fragment DIGIT_10 : '0'..'9'  ;

// ignore on the stream
WS : ( ' ' | '\t' | '\r' | '\n' ) 	{ $channel=HIDDEN; };

COMMENT : ';' ~('\n')*  	{ $channel=HIDDEN; };