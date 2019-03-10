<?php

require 'dbconnection.php';

$q_prev_order="SELECT * from orders,user where is_completed='N' and status!='COMPLETED' and user_id=user.id";
$r_prev_order=mysqli_query($connect,$q_prev_order);
$final_arr=array();

while($res_prev=mysqli_fetch_assoc($r_prev_order)){
	array_push($final_arr, $res_prev);
}

echo json_encode(array("data"=>$final_arr));
?>