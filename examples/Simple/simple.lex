<?php # vim:ft=php
namespace Demo;

%%

%{

%}


%class SimpleLexer
%function nextToken
%line
%char
%state COMMENTS

ALPHA=[A-Za-z_]
DIGIT=[0-9]
ALPHA_NUMERIC={ALPHA}|{DIGIT}
IDENT={ALPHA}({ALPHA_NUMERIC})*
NUMBER=({DIGIT})+
WHITE_SPACE=([\ \n\r\t\f])+

%%

<YYINITIAL> {ALPHA_NUMERIC} { 
    return $this->createToken();//ALPHA_NUMERIC
}

<YYINITIAL> {IDENT} { 
    return $this->createToken();//IDENT
}

<YYINITIAL> {NUMBER} { 
    return $this->createToken();//number
}
<YYINITIAL> {WHITE_SPACE} { 
    //WHITE_SPACE
}

<YYINITIAL> "+" { 
    return $this->createToken();//+
} 
<YYINITIAL> "-" { 
    return $this->createToken();//-
} 
<YYINITIAL> "*" { 
    return $this->createToken();//*
} 
<YYINITIAL> "/" { 
    return $this->createToken();//\/
} 
<YYINITIAL> ";" { 
    return $this->createToken();//;
} 
<YYINITIAL> "//" {
    $this->yybegin(self::COMMENTS);//\/\/
}
<COMMENTS> [^\n] {
}
<COMMENTS> [\n] {
    $this->yybegin(self::YYINITIAL);
}
<YYINITIAL> . {
    throw new Exception("bah!");
}
