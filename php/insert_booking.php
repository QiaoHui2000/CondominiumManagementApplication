<?php
error_reporting(0);

include 'DatabaseConfig.php' ;

// Create connection
$conn = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);


$facilityName = $_POST['facilityName'];
$bookingDate = $_POST['bookingDate'];
$bookingTime = $_POST['bookingTime'];

$maintenanceTime =$_POST['$maintenanceTime'];
$$maintenanceDate =$_POST['maintenanceDate'];
//$$maintenanceReason =$_POST['maintenanceReason'];

$checkQuery = "SELECT * FROM booking WHERE facilityName = '$facilityName'&& bookingTime ='$bookingTime'&& bookingDate ='$bookingDate'";

$maintenanceQuery = "SELECT * FROM maintenance WHERE facilityName = '$facilityName'&& maintenanceTime ='$bookingTime'&& maintenanceDate ='$bookingDate'";

$result = mysqli_query($conn,$checkQuery);
$results = mysqli_query($conn,$maintenanceQuery);
$resq = "";

if (mysqli_num_rows($result) > 0 | mysqli_num_rows($results) > 0) {
    $resp['code'] = "200";
    $resp["msg"] = "This slot already booked. Please choose another slot.";
}else{
    $query = "INSERT INTO booking (facilityName, bookingTime, bookingDate) 
	VALUES ('$facilityName', '$bookingTime','$bookingDate')";
    $insert = mysqli_query($conn,$query);
    if ($insert) {
        $resp['code'] = "201";
        $resp["msg"] = "Facility Booked!";
    }else{
        $resp['code'] = "202";
        $resp["msg"] = "Someting Went Wrong!";
    }
    
}
echo json_encode($resp);

		
mysqli_close($conn);

/*$Sql_Query = "INSERT INTO booking (facilityName,bookingDate,bookingTime) VALUES ('$facilityName', '$bookingDate', '$bookingTime')";

if(mysqli_query($conn,$Sql_Query)){
	echo 'Data Submit Successfully';
	}
	else{
		echo 'Try Again';
		}
mysqli_close($conn);*/

//, bookingTime='$bookingTime', bookingDate='$bookingDate'

?>