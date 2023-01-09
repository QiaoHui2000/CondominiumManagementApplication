<?php
error_reporting(0);

include 'DatabaseConfig.php' ;

// Create connection
$conn = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);

$maintenanceID = $_POST['maintenanceID'];
$facilityName = $_POST['facilityName'];
$maintenanceTime = $_POST['maintenanceTime'];
$maintenanceDate = $_POST['maintenanceDate'];
$maintenanceReason = $_POST['maintenanceReason'];

$sql = "UPDATE maintenance SET facilityName = '$facilityName',maintenanceTime = '$maintenanceTime',maintenanceDate = '$maintenanceDate', maintenanceReason = '$maintenanceReason' WHERE maintenanceID = '$maintenanceID'";

$result = mysqli_query($conn, $sql);

if($result){
	echo "Data Updated";
}
else{
	echo "Failed";
}
mysqli_close($conn)
?>