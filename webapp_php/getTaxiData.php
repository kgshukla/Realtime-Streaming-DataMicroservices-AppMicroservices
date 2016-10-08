<?php 

$vcap_services2 =  $_ENV["VCAP_SERVICES"];
$aggservice_url=json_decode($vcap_services2)->{'user-provided'}[0]->{'credentials'}->{'AGGSERVICE_URL'};

if (isset($_POST["type"])) {
	if ($_POST["type"] == "TABLE") {

		$ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $aggservice_url."/top10routes");
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		$output = curl_exec($ch);
		curl_close($ch);
	     	
		$table = array();
		$table['cols'] = array(
				array('id' => "", 'label' => 'Route', 'pattern' => "", 'type' => 'string'),
				array('id' => "", 'label' => '#Trips', 'pattern' => "", 'type' => 'string')
				#array('id' => "", 'label' => 'Pickup Time', 'pattern' => "", 'type' => 'string'),
				#array('id' => "", 'label' => 'Dropoff Time', 'pattern' => "", 'type' => 'string')
				);

		$rows = array(); 
		foreach(json_decode($output) as $nt) {
			$temp = array();
			$temp[] = array('v' => $nt[0], 'f' =>NULL);
			$temp[] = array('v' => $nt[1], 'f' =>NULL);
			#$temp[] = array('v' => $nt[2], 'f' =>NULL);
			#$temp[] = array('v' => $nt[3], 'f' =>NULL);
			$rows[] = array('c' => $temp);
		}

		$table['rows']=$rows;
		syslog(LOG_WARNING, "data");
		echo json_encode($table);
	}  else if ($_POST["type"] == "PROCESSTABLE") {
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $gemfire_url."ProcessData/LATEST_DATA");
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		$output1 = curl_exec($ch);
		curl_close($ch);
	     	
		$table1 = array();
		$table1['cols'] = array(
				array('id' => "", 'label' => '#Events Processed', 'pattern' => "", 'type' => 'string'),
				array('id' => "", 'label' => '#Events Lacking Data', 'pattern' => "", 'type' => 'string'),
				array('id' => "", 'label' => 'Per Batch Spark Processing Time (ms)', 'pattern' => "", 'type' => 'string'),
				);

		$rows1 = array();
		$decodedOutput = json_decode($output1);
		$temp1 = array();
		$temp1[] = array('v' => $decodedOutput[0], 'f' => NULL); 
		$temp1[] = array('v' => $decodedOutput[1], 'f' => NULL); 
		$temp1[] = array('v' => $decodedOutput[2], 'f' => NULL);
		$rows1[] = array('c' => $temp1); 

		$table1['rows']=$rows1;
 
		echo json_encode($table1);
    } else if ($_POST["type"] == "STREAMSCHART") {
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $aggservice_url."/totalevents");
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
                $output1 = curl_exec($ch);
                curl_setopt($ch, CURLOPT_URL, $aggservice_url."/missedevents");
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
                $output2 = curl_exec($ch);
                curl_close($ch);

                $table1 = array();
                $table1['cols'] = array(
                                array('id' => "", 'label' => 'Time', 'pattern' => "", 'type' => 'string'),
                                array('id' => "", 'label' => '#Events Processed', 'pattern' => "", 'type' => 'number'),
                                array('id' => "", 'label' => '#Events Lacking Data', 'pattern' => "", 'type' => 'number')
                                );

                $rows1 = array();
                $temp1 = array();
                $temp1[] = array('v' => date("G:i:s"), 'f' => NULL);
                $temp1[] = array('v' => (int)$output1, 'f' => NULL);
                $temp1[] = array('v' => (int)$output2, 'f' => NULL);
                $rows1[] = array('c' => $temp1);

                $table1['rows']=$rows1;

                echo json_encode($table1);
        } else if ($_POST["type"] == "SPARKPROCCHART") {
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $aggservice_url."/processtime");
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
                $output1 = curl_exec($ch);
                curl_close($ch);

                $table1 = array();
                $table1['cols'] = array(
                                array('id' => "", 'label' => 'Time', 'pattern' => "", 'type' => 'string'),
                                array('id' => "", 'label' => 'Spark Processing Time (ms)', 'pattern' => "", 'type' => 'number'),
                                );

                $rows1 = array();
                $temp1 = array();
                $temp1[] = array('v' => date("G:i:s"), 'f' => NULL);
                $temp1[] = array('v' => (int)$output1, 'f' => NULL);
                $rows1[] = array('c' => $temp1);

                $table1['rows']=$rows1;

                echo json_encode($table1);
        } else if ($_POST["type"] == "DATAQUALITY") {
                $ch = curl_init();
                curl_setopt($ch, CURLOPT_URL, $aggservice_url."/correctevents");
                curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
                $output1 = curl_exec($ch);
                curl_close($ch);

                $ch2 = curl_init();
                curl_setopt($ch2, CURLOPT_URL, $aggservice_url."/missedevents");
                curl_setopt($ch2, CURLOPT_RETURNTRANSFER, 1);
                $output2 = curl_exec($ch2);
                curl_close($ch2);

                $table1 = array();
                $table1['cols'] = array(
                                array('id' => "", 'label' => 'Time', 'pattern' => "", 'type' => 'string'),
                                array('id' => "", 'label' => 'Correct Data', 'pattern' => "", 'type' => 'number'),
                                array('id' => "", 'label' => 'Missed Data', 'pattern' => "", 'type' => 'number'),
                                );

                $rows1 = array();
                $temp1 = array();
                $temp1[] = array('v' => date("G:i:s"), 'f' => NULL);
                $temp1[] = array('v' => (int)$output1, 'f' => NULL);
                $temp1[] = array('v' => (int)$output2, 'f' => NULL);
                $rows1[] = array('c' => $temp1);

                $table1['rows']=$rows1;

                echo json_encode($table1);
        } 
        else if ($_POST["type"] == "MAPDATA") {
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $gemfire_url."/RouteData/LATEST_DATA?limit=6");
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		$output2 = curl_exec($ch);
		curl_close($ch);

		$table2 = array();
		$table2['cols'] = array(
				array('id' => "", 'label' => 'Lat', 'pattern' => "", 'type' => 'number'),
				array('id' => "", 'label' => 'Long', 'pattern' => "", 'type' => 'number'),
				array('id' => "", 'label' => 'Location', 'pattern' => "", 'type' => 'string'),
				array('id' => "", 'label' => 'Marker', 'pattern' => "", 'type' => 'string'),
				);
		$rows2 = array();
		$color = array ('blue1', 'blue2', 'green1', 'green2', 'pink1', 'pink2');
		$i = 0; 
		foreach(json_decode($output2) as $nt2) {
		   if($i < 6) {
			$temp2 = array();
			$temp2[] = array('v' => $nt2[0], 'f' =>NULL);
			$temp2[] = array('v' => $nt2[1], 'f' =>NULL);
			$temp2[] = array('v' => $nt2[2], 'f' =>NULL);
			$temp2[] = array('v' => $color[$i], 'f' =>NULL);
			$rows2[] = array('c' => $temp2);
			$i = $i+1;
		   }
		}
		$table2['rows']=$rows2;
		echo json_encode($table2);
	}  else if ($_POST["type"] == "FREETAXIDATA") {
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $aggservice_url."/freetaxies");
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		$output3 = curl_exec($ch);
		curl_close($ch);

		$table3 = array();
		$table3['cols'] = array(
				array('id' => "", 'label' => 'Lat', 'pattern' => "", 'type' => 'number'),
				array('id' => "", 'label' => 'Long', 'pattern' => "", 'type' => 'number'),
				array('id' => "", 'label' => 'Taxi Number', 'pattern' => "", 'type' => 'string')
				);
		$rows3 = array();
		foreach(json_decode($output3) as $nt3) {
			$temp3 = array();
			$temp3[] = array('v' => $nt3[1], 'f' =>NULL);
			$temp3[] = array('v' => $nt3[2], 'f' =>NULL);
			$temp3[] = array('v' => $nt3[0], 'f' =>NULL);
			$rows3[] = array('c' => $temp3);
		}
		$table3['rows']=$rows3;
		echo json_encode($table3);
	}  else {
		echo "";
	}
}
?>
