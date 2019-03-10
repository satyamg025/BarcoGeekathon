<?php

require 'dbconnection.php';

$name=$_POST['name'];
$mobile=$_POST['mobile'];
$password=$_POST['password'];

$q_check_duplicate = "SELECT * from user where mobile='$mobile'";
$res_duplicate=mysqli_query($connect,$q_check_duplicate);
if(mysqli_num_rows($res_duplicate)>0){
	echo json_encode(array("message"=>"Mobile number already registered. Please login to continue",'error'=>True));	
}
else{
	$q_register="INSERT INTO `user`(`name`, `mobile`, `password`) VALUES ('$name','$mobile','$password')";
	$res=mysqli_query($connect,$q_register);
	echo json_encode(array("message"=>"User successfully registered. Please login to continue",'error'=>False));	
}

?>