<?php

$base = $_REQUEST['image'];
$name = uniqid() . ".png";

$binary=base64_decode($base);
header('Content-Type: bitmap; charset=utf-8');
$file = fopen('uploadedimages/'.$name, 'wb');

if(fwrite($file, $binary)) echo $name;
else echo "error writing img on server";

fclose($file);
