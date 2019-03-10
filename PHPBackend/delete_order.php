<?php

require 'dbconnection.php';

$order_id=$_POST['id'];

$q_delete="UPDATE `orders` SET `status`='COMPLETED' WHERE id='$order_id'";
$res=mysqli_query($connect,$q_delete);

echo json_encode(array("message"=>"Successfully deleted"));
?>