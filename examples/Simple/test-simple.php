<?php

require './vendor/autoload.php';
use Demo\SimpleLexer;

error_reporting(E_ALL);

$l = new SimpleLexer(fopen("php://stdin", "r"));

while ($t = $l->nextToken()) {
	print_r($t);
}

