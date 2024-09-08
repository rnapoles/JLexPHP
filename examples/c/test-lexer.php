<?php
require './vendor/autoload.php';

use Demo\CLexer;

error_reporting(E_ALL);

$lexer = new CLexer(fopen("php://stdin", "r"));

try {

  while ($t = $lexer->nextToken()) {
    print_r($t);
  }

} catch(\Exception $ex){
  echo $ex->getMessage() . "\n";
  dump($lexer);
}


