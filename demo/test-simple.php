<?php

include 'SimpleLexer.php';
error_reporting(E_ALL);

$L = new SimpleLexer(fopen("php://stdin", "r"));

while ($t = $L->nextToken()) {
	print_r($t);
}

