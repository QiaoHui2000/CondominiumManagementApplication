<?php
error_reporting(0);

include 'DatabaseConfig.php' ;


header('Content-Type: application/json');
$data = array();

// Create connection
$conn = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);

if($conn) {
  $sql = "SELECT *from booking";
  $result = mysqli_query($conn, $sql);
  if(mysqli_num_rows($result) != 0) {
    $i = 0;
    while ($row = mysqli_fetch_assoc($result)){
      $data[$i] = $row;
      $i++;
    }
    echo json_encode($data, JSON_PRETTY_PRINT);
  }
}
else echo "Failed to connect to database";

?>