<?php

require 'dbconnection.php';

$order_id=$_POST['id'];

$q_prev_order="SELECT * from orders where id='$order_id'";
$r_prev_order=mysqli_query($connect,$q_prev_order);
$res_prev=mysqli_fetch_assoc($r_prev_order);
extract($res_prev);

$q_update="UPDATE `orders` SET `is_completed`='Y' where id='$order_id'";
$r_update=mysqli_query($connect,$q_update);

$new_arrival = date('Y-m-d H:i:s',strtotime('+'.(string)($increment_by).' day',strtotime($arrival_date_time)));

$q_insert="INSERT INTO `orders`(`latitude`, `longitude`, `freeformaddress`, `arrival_date_time`, `increment_by`,`user_id`,`waste_type`) VALUES ('$latitude','$longitude','$freeformaddress','$arrival_date_time','$increment_by','$user_id','$waste_type')";
$res=mysqli_query($connect,$q_insert);

echo json_encode(array("message"=>"Successfully updated"));
?>