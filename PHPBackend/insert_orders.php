<?php

require 'dbconnection.php';

$latitude=$_POST['latitude'];
$longitude=$_POST['longitude'];
$address=$_POST['address'];
$from_time=$_POST['from_time'];
$to_time=$_POST['to_time'];
$increment_by=$_POST['increment_by'];
$user=$_POST['user_id'];
$waste_type=$_POST['waste_type'];

$check="SELECT * from orders where user_id='$user' and status!='COMPLETED' order by id limit 1";
$r_prev_order=mysqli_query($connect,$check);
if(mysqli_num_rows($r_prev_order)==0){
	$q_insert="INSERT INTO `orders`(`latitude`, `longitude`, `freeformaddress`, `from_date_time`,`to_date_time`, `increment_by`,`user_id`,`waste_type`) VALUES ('$latitude','$longitude','$address','$from_time','$to_time','$increment_by','$user','$waste_type')";
    $res=mysqli_query($connect,$q_insert);
    echo json_encode(array("message"=>"Successfully inserted"));

}
else{
    $q_update="UPDATE `orders` SET `latitude`='$latitude',`longitude`='$longitude',`freeformaddress`='$address',`from_date_time`='$from_time',`increment_by`='$increment_by',`to_date_time`='$to_time',`waste_type`='$waste_type' where `user_id`='$user'";
    $res=mysqli_query($connect,$q_update);
    echo json_encode(array("message"=>"Successfully updated"));

    
}

?>