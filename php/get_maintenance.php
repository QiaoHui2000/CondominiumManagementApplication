<?php
error_reporting(0);

include 'DatabaseConfig.php' ;

// Create connection
$conn = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);


$result = array();
$result['item'] = array();
$select = "SELECT *from maintenance";
$response = mysqli_query($conn, $select);

while ($row = mysqli_fetch_array($response)){
	
	$index['maintenanceID'] = $row['0'];
	$index['facilityName'] = $row['1'];
	$index['maintenanceTime'] = $row['2'];
	$index['maintenanceDate'] = $row['3'];
	$index['maintenanceReason'] = $row['4'];
	
	array_push($result['item'], $index);
}

	$result["success"] = "1";
	echo json_encode($result);
	mysqli_close($conn);




?>