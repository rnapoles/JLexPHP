<?php # vim:ft=php
namespace Demo;

use \Exception;

/* This is a lexer for the C language */
%%

%{

  //Tokens
  const TK_ADD_ASSIGN = 1;
  const TK_AMP = 2;
  const TK_AND_ASSIGN = 3;
  const TK_AND_OP = 4;
  const TK_AUTO = 5;
  const TK_BREAK = 6;
  const TK_CARET = 7;
  const TK_CASE = 8;
  const TK_CHAR = 9;
  const TK_COLON = 10;
  const TK_COMMA = 11;
  const TK_COMMENT = 12;
  const TK_CONST = 13;
  const TK_CONSTANT = 14;
  const TK_CONTINUE = 15;
  const TK_DEC_OP = 16;
  const TK_DEFAULT = 17;
  const TK_DIV_ASSIGN = 18;
  const TK_DO = 19;
  const TK_DOUBLE = 20;
  const TK_ELLIPSIS = 21;
  const TK_ELSE = 22;
  const TK_ENUM = 23;
  const TK_EQUALS = 24;
  const TK_EQ_OP = 25;
  const TK_EXCLAM = 26;
  const TK_EXTERN = 27;
  const TK_FLOAT = 28;
  const TK_FOR = 29;
  const TK_GE_OP = 30;
  const TK_GOTO = 31;
  const TK_IDENTIFIER = 32;
  const TK_IF = 33;
  const TK_INC_OP = 34;
  const TK_INT = 35;
  const TK_LANGLE = 36;
  const TK_LCURLY = 37;
  const TK_LEFT_ASSIGN = 38;
  const TK_LEFT_OP = 39;
  const TK_LE_OP = 40;
  const TK_LONG = 41;
  const TK_LPAREN = 42;
  const TK_LSQUARE = 43;
  const TK_MINUS = 44;
  const TK_MOD_ASSIGN = 45;
  const TK_MUL_ASSIGN = 46;
  const TK_NE_OP = 47;
  const TK_OR_ASSIGN = 48;
  const TK_OR_OP = 49;
  const TK_PERCENT = 50;
  const TK_PERIOD = 51;
  const TK_PIPE = 52;
  const TK_PLUS = 53;
  const TK_PRAGMA = 54;
  const TK_PTR_OP = 55;
  const TK_QUESTION = 56;
  const TK_RANGLE = 57;
  const TK_RCURLY = 58;
  const TK_REGISTER = 59;
  const TK_RETURN = 60;
  const TK_RIGHT_ASSIGN = 61;
  const TK_RIGHT_OP = 62;
  const TK_RPAREN = 63;
  const TK_RSQUARE = 64;
  const TK_SEMIC = 65;
  const TK_SHORT = 66;
  const TK_SIGNED = 67;
  const TK_SIZEOF = 68;
  const TK_SLASH = 69;
  const TK_STAR = 70;
  const TK_STATIC = 71;
  const TK_STRING_LITERAL = 72;
  const TK_STRUCT = 73;
  const TK_SUB_ASSIGN = 74;
  const TK_SWITCH = 75;
  const TK_TILDE = 76;
  const TK_TYPEDEF = 77;
  const TK_UNION = 78;
  const TK_UNSIGNED = 79;
  const TK_VOID = 80;
  const TK_VOLATILE = 81;
  const TK_WHILE = 82;
  const TK_XOR_ASSIGN = 83;
  // End Tokens

%}

%function nextToken
%line
%char
%state COMMENT
%class CLexer

D	=	[0-9]
L	=	[a-zA-Z_]
H	=	[a-fA-F0-9]
E	=	[Ee][+-]?{D}+
FS	=	(f|F|l|L)
IS	=	(u|U|l|L)

%%

<YYINITIAL> "/*"			{ 
								$this->commentTok = $this->createToken(CLexer::TK_COMMENT);
								$this->yybegin(self::COMMENT);
						    }
<YYINITIAL> //[^\r\n]*      { return $this->createToken(CLexer::TK_COMMENT); }

<COMMENT>   "*/"            { 
								$this->commentTok->value .= $this->yytext();
							    $this->yybegin(self::YYINITIAL); 
							    return $this->commentTok;
							}
<COMMENT>   (.|[\r\n])      { $this->commentTok->value .= $this->yytext(); }

<YYINITIAL> #[^\r\n]*       { return $this->createToken(CLexer::TK_PRAGMA); }

<YYINITIAL> "auto"			{ return $this->createToken(CLexer::TK_AUTO); }
<YYINITIAL> "break"			{ return $this->createToken(CLexer::TK_BREAK); }
<YYINITIAL> "case"			{ return $this->createToken(CLexer::TK_CASE); }
<YYINITIAL> "char"			{ return $this->createToken(CLexer::TK_CHAR); }
<YYINITIAL> "const"			{ return $this->createToken(CLexer::TK_CONST); }
<YYINITIAL> "continue"		{ return $this->createToken(CLexer::TK_CONTINUE); }
<YYINITIAL> "default"		{ return $this->createToken(CLexer::TK_DEFAULT); }
<YYINITIAL> "do"			{ return $this->createToken(CLexer::TK_DO); }
<YYINITIAL> "double"		{ return $this->createToken(CLexer::TK_DOUBLE); }
<YYINITIAL> "else"			{ return $this->createToken(CLexer::TK_ELSE); }
<YYINITIAL> "enum"			{ return $this->createToken(CLexer::TK_ENUM); }
<YYINITIAL> "extern"		{ return $this->createToken(CLexer::TK_EXTERN); }
<YYINITIAL> "float"			{ return $this->createToken(CLexer::TK_FLOAT); }
<YYINITIAL> "for"			{ return $this->createToken(CLexer::TK_FOR); }
<YYINITIAL> "goto"			{ return $this->createToken(CLexer::TK_GOTO); }
<YYINITIAL> "if"			{ return $this->createToken(CLexer::TK_IF); }
<YYINITIAL> "int"			{ return $this->createToken(CLexer::TK_INT); }
<YYINITIAL> "long"			{ return $this->createToken(CLexer::TK_LONG); }
<YYINITIAL> "register"		{ return $this->createToken(CLexer::TK_REGISTER); }
<YYINITIAL> "return"		{ return $this->createToken(CLexer::TK_RETURN); }
<YYINITIAL> "short"			{ return $this->createToken(CLexer::TK_SHORT); }
<YYINITIAL> "signed"		{ return $this->createToken(CLexer::TK_SIGNED); }
<YYINITIAL> "sizeof"		{ return $this->createToken(CLexer::TK_SIZEOF); }
<YYINITIAL> "static"		{ return $this->createToken(CLexer::TK_STATIC); }
<YYINITIAL> "struct"		{ return $this->createToken(CLexer::TK_STRUCT); }
<YYINITIAL> "switch"		{ return $this->createToken(CLexer::TK_SWITCH); }
<YYINITIAL> "typedef"		{ return $this->createToken(CLexer::TK_TYPEDEF); }
<YYINITIAL> "union"			{ return $this->createToken(CLexer::TK_UNION); }
<YYINITIAL> "unsigned"		{ return $this->createToken(CLexer::TK_UNSIGNED); }
<YYINITIAL> "void"			{ return $this->createToken(CLexer::TK_VOID); }
<YYINITIAL> "volatile"		{ return $this->createToken(CLexer::TK_VOLATILE); }
<YYINITIAL> "while"			{ return $this->createToken(CLexer::TK_WHILE); }

<YYINITIAL> {L}({L}|{D})*		{ return $this->createToken(CLexer::TK_IDENTIFIER); }

<YYINITIAL> 0[xX]{H}+{IS}?		{ return $this->createToken(CLexer::TK_CONSTANT); }
<YYINITIAL> 0{D}+{IS}?		{ return $this->createToken(CLexer::TK_CONSTANT); }
<YYINITIAL> {D}+{IS}?		{ return $this->createToken(CLexer::TK_CONSTANT); }
<YYINITIAL> L?\'(\\.|[^\\\'])+\'	{ return $this->createToken(CLexer::TK_CONSTANT); }

<YYINITIAL> {D}+{E}{FS}?		{ return $this->createToken(CLexer::TK_CONSTANT); }
<YYINITIAL> {D}*"."{D}+({E})?{FS}?	{ return $this->createToken(CLexer::TK_CONSTANT); }
<YYINITIAL> {D}+"."{D}*({E})?{FS}?	{ return $this->createToken(CLexer::TK_CONSTANT); }

<YYINITIAL> L?\"(\\.|[^\\\"])*\" 	{ return $this->createToken(CLexer::TK_STRING_LITERAL); }

<YYINITIAL> "..."			{ return $this->createToken(CLexer::TK_ELLIPSIS); }
<YYINITIAL> ">>="			{ return $this->createToken(CLexer::TK_RIGHT_ASSIGN); }
<YYINITIAL> "<<="			{ return $this->createToken(CLexer::TK_LEFT_ASSIGN); }
<YYINITIAL> "+="			{ return $this->createToken(CLexer::TK_ADD_ASSIGN); }
<YYINITIAL> "-="			{ return $this->createToken(CLexer::TK_SUB_ASSIGN); }
<YYINITIAL> "*="			{ return $this->createToken(CLexer::TK_MUL_ASSIGN); }
<YYINITIAL> "/="			{ return $this->createToken(CLexer::TK_DIV_ASSIGN); }
<YYINITIAL> "%="			{ return $this->createToken(CLexer::TK_MOD_ASSIGN); }
<YYINITIAL> "&="			{ return $this->createToken(CLexer::TK_AND_ASSIGN); }
<YYINITIAL> "^="			{ return $this->createToken(CLexer::TK_XOR_ASSIGN); }
<YYINITIAL> "|="			{ return $this->createToken(CLexer::TK_OR_ASSIGN); }
<YYINITIAL> ">>"			{ return $this->createToken(CLexer::TK_RIGHT_OP); }
<YYINITIAL> "<<"			{ return $this->createToken(CLexer::TK_LEFT_OP); }
<YYINITIAL> "++"			{ return $this->createToken(CLexer::TK_INC_OP); }
<YYINITIAL> "--"			{ return $this->createToken(CLexer::TK_DEC_OP); }
<YYINITIAL> "->"			{ return $this->createToken(CLexer::TK_PTR_OP); }
<YYINITIAL> "&&"			{ return $this->createToken(CLexer::TK_AND_OP); }
<YYINITIAL> "||"			{ return $this->createToken(CLexer::TK_OR_OP); }
<YYINITIAL> "<="			{ return $this->createToken(CLexer::TK_LE_OP); }
<YYINITIAL> ">="			{ return $this->createToken(CLexer::TK_GE_OP); }
<YYINITIAL> "=="			{ return $this->createToken(CLexer::TK_EQ_OP); }
<YYINITIAL> "!="			{ return $this->createToken(CLexer::TK_NE_OP); }
<YYINITIAL> ";"			{ return $this->createToken(CLexer::TK_SEMIC); }
<YYINITIAL> ("{"|"<%")		{ return $this->createToken(CLexer::TK_LCURLY); }
<YYINITIAL> ("}"|"%>")		{ return $this->createToken(CLexer::TK_RCURLY); }
<YYINITIAL> ","			{ return $this->createToken(CLexer::TK_COMMA); }
<YYINITIAL> ":"			{ return $this->createToken(CLexer::TK_COLON); }
<YYINITIAL> "="			{ return $this->createToken(CLexer::TK_EQUALS); }
<YYINITIAL> "("			{ return $this->createToken(CLexer::TK_LPAREN); }
<YYINITIAL> ")"			{ return $this->createToken(CLexer::TK_RPAREN); }
<YYINITIAL> ("["|"<:")		{ return $this->createToken(CLexer::TK_LSQUARE); }
<YYINITIAL> ("]"|":>")		{ return $this->createToken(CLexer::TK_RSQUARE); }
<YYINITIAL> "."			{ return $this->createToken(CLexer::TK_PERIOD); }
<YYINITIAL> "&"			{ return $this->createToken(CLexer::TK_AMP); }
<YYINITIAL> "!"			{ return $this->createToken(CLexer::TK_EXCLAM); }
<YYINITIAL> "~"			{ return $this->createToken(CLexer::TK_TILDE); }
<YYINITIAL> "-"			{ return $this->createToken(CLexer::TK_MINUS); }
<YYINITIAL> "+"			{ return $this->createToken(CLexer::TK_PLUS); }
<YYINITIAL> "*"			{ return $this->createToken(CLexer::TK_STAR); }
<YYINITIAL> "/"			{ return $this->createToken(CLexer::TK_SLASH); }
<YYINITIAL> "%"			{ return $this->createToken(CLexer::TK_PERCENT); }
<YYINITIAL> "<"			{ return $this->createToken(CLexer::TK_LANGLE); }
<YYINITIAL> ">"			{ return $this->createToken(CLexer::TK_RANGLE); }
<YYINITIAL> "^"			{ return $this->createToken(CLexer::TK_CARET); }
<YYINITIAL> "|"			{ return $this->createToken(CLexer::TK_PIPE); }
<YYINITIAL> "?"			{ return $this->createToken(CLexer::TK_QUESTION); }

<YYINITIAL> [ \r\t\v\n\f] { /* skip spaces... */ }
.			{ throw new \Exception('Unexpected token:' . $this->yytext()); }

