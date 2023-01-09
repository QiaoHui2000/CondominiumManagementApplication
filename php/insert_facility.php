<?php
error_reporting(0);

include 'DatabaseConfig.php' ;

// Create connection
$conn = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);


$facilityName = $_POST['facilityName'];
$location = $_POST['location'];
$capacity = $_POST['capacity'];

$checkQuery = "SELECT * FROM facility WHERE facilityName = '$facilityName'";
$result = mysqli_query($conn,$checkQuery);
$resq = "";

if (mysqli_num_rows($result) > 0) {
    $resp['code'] = "200";
    $resp["msg"] = "This facility name is existed.";
}else{
	
$Sql_Query = "INSERT INTO facility (facilityName,location,capacity) VALUES ('$facilityName', '$location', '$capacity')";

$insert = mysqli_query($conn,$Sql_Query);
    if ($insert) {
        $resp['code'] = "201";
        $resp["msg"] = "Facility Recorded!";
    }else{
        $resp['code'] = "202";
        $resp["msg"] = "Someting Went Wrong!";
    }

}
echo json_encode($resp);
/*if(mysqli_query($conn,$Sql_Query)){
	echo 'Data Submit Successfully';
	}
	else{
		echo 'Try Again';
		}*/
mysqli_close($conn);


?>
