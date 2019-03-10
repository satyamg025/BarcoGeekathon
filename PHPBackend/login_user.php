<?php

require 'dbconnection.php';

$mobile=$_POST['mobile'];
$password=$_POST['password'];

$q_check = "SELECT * from user where mobile='$mobile' and password='$password'";
$res_duplicate=mysqli_query($connect,$q_check);
if(mysqli_num_rows($res_duplicate)>0){
	$r=mysqli_fetch_assoc($res_duplicate);

	$q_prev_order="SELECT * from orders where user_id='$r[id]' and status!='COMPLETED' order by id limit 1";
	$r_prev_order=mysqli_query($connect,$q_prev_order);
	if(mysqli_num_rows($r_prev_order)==0){
		$res_prev=array('id'=>-1);
	}
	else{
		$res_prev=mysqli_fetch_assoc($r_prev_order);
	}
	$res_prev['name']=$r['name'];
	echo json_encode(array("message"=>"Login successfull",'error'=>False,'user_id'=>$r['id'],'prev_order'=>$res_prev));	
}
else{
	echo json_encode(array("message"=>"Invalid credentials",'error'=>True,'user_id'=>-1,'prev_order'=>array('freeformaddress'=>"")));		
}

?>