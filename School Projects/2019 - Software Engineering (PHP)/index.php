<?php
	$weekdays = array(1 => 'Monday',2 => 'Tuesday',3 => 'Wednesday',4 => 'Thursday',5 => 'Friday',6 => 'Saturday',7 => 'Sunday');
	
	$routeNms = array("All Routes"); // Route names
	$routeIds = array("0"); // Route ids
	
	$stopIds = array("0"); // Stop ids
	$stopRts = array("0"); // Stop routes
	$stopNms = array("All Stops"); // Stop names
	$stopLat = array("0"); // Stop latitude locations
	$stopLon = array("0"); // Stop longitude locations
	
	$scheduleIds = []; // Schedule ids
	$scheduleRts = []; // Schedule routes
	$scheduleTms = []; // Schedule times
	$scheduleDys = []; // Schedule days

	$runningTotal = [];
	$numAdditions = [];
	
	$selectedRoutes = [];
	$selectedStops = [];
	
	// Server info
	$servername = "**********************";
	$username = "*************************";
	$password = "*************************";
	$dbname = "Centro";

	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	
	// Check connection
	if ($conn->connect_error) {
		echo "Error: Could not connect to database!<p><p>";
	}
	else {

		// Get a list of available routes
		$sql = "SELECT rt, rtnm FROM routes WHERE INSTR(rt, 'OSW') > 0 AND INSTR(rt, 'OSW09') = 0";
		$routesResult = $conn->query($sql);
		
		// Make sure we got the route data
		if ($routesResult->num_rows > 0) {
			// Save every route
			while($routeRow = $routesResult->fetch_assoc()) {
				array_push($routeNms, $routeRow["rt"] . ": " . $routeRow["rtnm"]);
				array_push($routeIds, $routeRow["rt"]);
			}
		}
		
		// Get a list of available stops
		$sql = "SELECT stopid, rtnm, stpnm, lat, lon, dir FROM stops";
		$stopsResult = $conn->query($sql);
		
		// Make sure we got the stop data
		if ($stopsResult->num_rows > 0) {
			// Save every stop
			while($stopRow = $stopsResult->fetch_assoc()) {
				// Ignore any duplicates
				if (!in_array($stopRow["stopid"], $stopIds)) {
					array_push($stopIds, $stopRow["stopid"]);
					array_push($stopRts, $stopRow["rtnm"]);
					array_push($stopNms, $stopRow["dir"] . ": " . $stopRow["stpnm"]);
					array_push($stopLat, $stopRow["lat"]);
					array_push($stopLon, $stopRow["lon"]);
				}
			}
		}
		
		
		// Get a list of expected schedule times
		$sql = "SELECT rt, stopid, time, day FROM schedule";
		$scheduleResult = $conn->query($sql);
		
		// Make sure we got the schedule data
		if ($scheduleResult->num_rows > 0) {
			// Save every stop
			$scIndex = 1;
			while($scheduleRow = $scheduleResult->fetch_assoc()) {
				array_push($scheduleIds, $scheduleRow["stopid"]);
				array_push($scheduleRts, $scheduleRow["rt"]);
				array_push($scheduleTms, $scheduleRow["time"]);
				array_push($scheduleDys, $scheduleRow["day"]);
				array_push($runningTotal, 0);
				array_push($numAdditions, 0);


				$scIndex++;
				
			}
		}
		
		
?>

<!DOCTYPE html>

<html>
	<head>
		<title>Centro Bus Performance</title>
		<link rel="stylesheet" type="text/css" href="./common.css?rnd=<?php echo rand(); ?>">
		<script type="text/javascript" src="func.js?rnd=<?php echo rand(); ?>"></script>
	</head>
	<body>
		<div class="header">
		<h1>Centro Bus Performance Monitor</h1>
		</div>
		<div class="filter">
			<h2>Search Filter</h2>
			<br>
			<form id="filter_form" action="./index.php">
				From Date: 
				<input class="date_input" id="date_from" type="date" name="date_from" min="2019-11-23" max="<?php echo date('Y-m-d'); ?>" value="<?php
		// Check the input date from field
		$date_from = filter_input(INPUT_GET, "date_from", FILTER_SANITIZE_STRING);
		if ($date_from != null && $date_from != false)
			echo $date_from;
?>">
				To Date: 
				<input class="date_input" id="date_to" type="date" name="date_to" min="2019-11-23" max="<?php echo date('Y-m-d'); ?>" value="<?php
		// Check the input date to field
		$date_to = filter_input(INPUT_GET, "date_to", FILTER_SANITIZE_STRING);
		if ($date_to != null && $date_to != false)
			echo $date_to;
?>">
				<hr>
				Selected Routes: 
				<p>
				<div id="selected_routes">
<?php
		// Check each route filter selection
		$rtIndex = 0;
		$routeName = filter_input(INPUT_GET, "route" . $rtIndex, FILTER_SANITIZE_STRING);
		while($routeName != null && ($routeName != false || $routeName == "0")) {
			
			// Find which route is selected
			$route = array_search($routeName, $routeIds);
			if ($route != false || $routeName == "0") {
				
				// Add the route selection to the selected list
				echo "<div class=\"route_selection\" onclick=\"deselect_route(this)\" value=\"" . $routeName . "\">" . $routeNms[$route] . "</div>";
				array_push($selectedRoutes, $routeName);
			}
			
			$rtIndex++;
			$routeName = filter_input(INPUT_GET, "route" . $rtIndex, FILTER_SANITIZE_STRING);
		}
?>				
				</div>
				<select id = "route_list" name="route" onchange="select_route()"
<?php
		if (in_array("0", $selectedRoutes))
			echo " style=\"visibility: hidden; display: none;\"";
?>
				>
					<option selected disabled value = "-1">Select a Route</option>
<?php
		// Add an option for every route
		$rtIndex = 0;
		foreach ($routeNms as $rtNm) {
			if (!in_array($routeIds[$rtIndex], $selectedRoutes))
				echo "<option value = \"" . $routeIds[$rtIndex] . "\">" . $rtNm . "</option>";
			else
				echo "<option value = \"" . $routeIds[$rtIndex] . "\" style=\"visibility: hidden; display: none;\">" . $rtNm . "</option>";
			$rtIndex++;
		}
?>

				</select>
				<hr>
				Selected Stops: 
				<p>
				<div id="selected_stops">
<?php
		// Check each stop filter selection
		$stIndex = 0;
		$stop = filter_input(INPUT_GET, "stop" . $stIndex, FILTER_SANITIZE_NUMBER_INT);
		while($stop != null && ($stop != false || $stop == "0")) {
			
			// Find the index of the stop id
			$stLoc = array_search($stop, $stopIds);
			
			// Add the stop selection to the selected list
			echo "<div class=\"stop_selection\" value=\"" . $stop . "\" data-rtid=\"" . $stopRts[$stLoc] . "\" onclick=\"deselect_stop(this)\">" . $stopNms[$stLoc] . "</div>";
			array_push($selectedStops, $stop);
			
			$stIndex++;
			$stop = filter_input(INPUT_GET, "stop" . $stIndex, FILTER_SANITIZE_NUMBER_INT);
		}
		
?>	
				</div>
				<select id = "stop_list" name="stop" onchange="select_stop()"
<?php
		if (in_array("0", $selectedStops))
			echo " style=\"visibility: hidden; display: none;\"";
?>
				>
					<option selected disabled value = "-1">Select a Stop</option>
					
<?php
		// Add an option for every stop
		$stIndex = 0;
		foreach ($stopNms as $stNm) {
			if ($stIndex == 0 || ((in_array("0", $selectedRoutes) || in_array($stopRts[$stIndex], $selectedRoutes)) && !in_array($stopIds[$stIndex], $selectedStops)))
				echo "<option value = \"" . $stopIds[$stIndex] . "\" data-rtid=\"" . $stopRts[$stIndex] . "\" style=\"visibility: visible; display: inline;\">" . $stNm . "</option>";
			else
				echo "<option value = \"" . $stopIds[$stIndex] . "\" data-rtid=\"" . $stopRts[$stIndex] . "\" style=\"visibility: hidden; display: none;\">" . $stNm . "</option>";
			$stIndex++;
		}
?>
				</select>
				<input id="btn_submit" type="button" onclick="submit_filter()" value="Submit Filter">
			</form>
		</div>
		<div class="data">
			<h2>Route Data</h2>
			<p>
			<button id="btn_hide" onclick="show_raw_data()">Hide Data</button>
			<div id="raw_data">
<?php
		
		$filterReady = true;
		
		// Make sure that a date range was given
		if ($date_from == null || $date_from == false || $date_to == null || $date_to == false || $date_from > $date_to) {
			echo "<div class=\"data_field0\">Please select a valid date range!</div>";
			$filterReady = false;
		}
		
		// Make sure that at least one route was selected
		if (count($selectedRoutes) < 1) {
			echo "<div class=\"data_field0\">Please select at least one route!</div>";
			$filterReady = false;
		}
		
		// Make sure that at least one stop was selected
		if (count($selectedStops) < 1) {
			echo "<div class=\"data_field0\">Please select at least one stop!</div>";
			$filterReady = false;
		}
		
		// Process data only if all filters were selected
		if ($filterReady) {
			// Deconstruct the input dates
			$df = date_parse_from_format("Y-m-d", $date_from);
			$dt = date_parse_from_format("Y-m-d", $date_to);
			
			if (strlen($df["day"]) == 1)
				$df["day"] = "0".$df["day"];
			if (strlen($df["month"]) == 1)
				$df["month"] = "0".$df["month"];
			if (strlen($dt["day"]) == 1)
				$dt["day"] = "0".$dt["day"];
			if (strlen($df["month"]) == 1)
				$dt["month"] = "0".$dt["month"];
			
			// Build a selection string for the selected routes
			if ($selectedRoutes[0] == "0")
				$rtSelect = "";
			else {
				$rtSelect = "rt IN ('" . $selectedRoutes[0] . "'";
				$rtSelectIndex = 1;
				while($rtSelectIndex < count($selectedRoutes)) {
					$rtSelect = $rtSelect . ",'" . $selectedRoutes[$rtSelectIndex] . "'";
					$rtSelectIndex++;
				}
				$rtSelect = $rtSelect . ") AND ";
			}
			
			// Get a list of history between the date range
			$sql = "SELECT DISTINCT tmstmp, lat, lon, rt, des FROM vehicles WHERE " . $rtSelect . "(tmstmp BETWEEN '" . $df["year"] . $df["month"] . $df["day"] . " 00:00:00' AND '" . $dt["year"] . $dt["month"] . $dt["day"] . " 23:59:00')";
			$historyResult = $conn->query($sql);
			
			// Make sure we got the history data
			if ($historyResult->num_rows > 0) {
				// Save every history snapshot
				$hsIndex = 1;
				$stLast = "";
				$stHighlight = true;
				$stFound = false;
				while($historyRow = $historyResult->fetch_assoc()) {
					
					// Search through all stops to see if the bus was close to any
					$stError = 0.00005; // within 5 meters
					$stIndex = 1;

					while($stIndex < count($stopIds)) {
						if ($stLast != $stopNms[$stIndex] && $historyRow["lat"] > $stopLat[$stIndex] - $stError && $historyRow["lat"] < $stopLat[$stIndex] + $stError && $historyRow["lon"] > $stopLon[$stIndex] - $stError && $historyRow["lon"] < $stopLon[$stIndex] + $stError) {
							if ($selectedStops[0] == "0" || in_array($stopIds[$stIndex], $selectedStops)) {
								// Format the timestamp
								$tmstmp = substr($historyRow["tmstmp"],0,4) . "-" . substr($historyRow["tmstmp"],4,2) . "-" . substr($historyRow["tmstmp"],6,2) . " @ " . substr($historyRow["tmstmp"],8,9);
								
								$weekday = date('w', strtotime(substr($historyRow["tmstmp"],0,4) . substr($historyRow["tmstmp"],4,2) . substr($historyRow["tmstmp"],6,2)));

								$timeStamp = substr($tmstmp,14,5); 
								$hours = substr($timeStamp,0,2);
								$minutes = substr($timeStamp,3,2);
								$totalMins = ($hours * 60) + $minutes;

								$scheduleIndex = 0;
								$lastTime = 0;
								$whileFlag = true;
								
								while($scheduleIndex < count($scheduleIds) && $whileFlag)
								{

									if($scheduleIds[$scheduleIndex] == $stopIds[$stIndex] )
									{

										if($scheduleDys[$scheduleIndex] == $weekday)
										{

											if(strlen($scheduleTms[$scheduleIndex]) == 4)
											{
												

												$scheduleHours = substr($scheduleTms[$scheduleIndex],0,1);
												$scheduleMinutes = substr($scheduleTms[$scheduleIndex],2,2);
												

											}
											else
											{
												

												$scheduleHours = substr($scheduleTms[$scheduleIndex],0,2);
												$scheduleMinutes = substr($scheduleTms[$scheduleIndex],3,2);
											}
											

											$scheduleTotalMinutes = ($scheduleHours * 60) + $scheduleMinutes; 

											$absValue = abs($totalMins - $scheduleTotalMinutes);

											if($absValue < 10)
											{
												$runningTotal [$scheduleIndex] += ($totalMins - $scheduleTotalMinutes);
												$numAdditions [$scheduleIndex]++;
												$whileFlag = false;
											}
											else
											{

												$scheduleIndex++;
	
											}

										}
										else
										{
											$scheduleIndex++;
										}


									}
									else
									{
										$scheduleIndex++;
									}

								}
								$stFound = true;
							}
							$stLast = $stopNms[$stIndex];
						}
						$stIndex++;
					}
				
					$hsIndex++;
				}
				
				if (!$stFound)
					echo "<div class=\"data_field0\">There is no data that matches your filter... Please choose another date range, route, or stop to filter!</div>";
			} else {
				echo "<div class=\"data_field0\">There is no data that matches your filter... Please choose another date range, route, or stop to filter!</div>";
			}
			
			$lastName = $scheduleIds[0];

			for ($x = 0; $x < count($runningTotal); $x++) 
			{
				if($runningTotal [$x] != 0)
				{
					$id = $scheduleIds[$x];
					$stLoc = array_search($id, $stopIds);

					
					$diff = $runningTotal [$x] / $numAdditions [$x];

					if($diff > 0)
					{
						$colorFlag = "#EB4034";
						if($diff ==1)
						{
							$minFlag = " minute late.";

						}
						else
						{
							$minFlag = " minutes late.";

						}

					}
					else 
					{
						$colorFlag = "#34EB64";
						if($diff == -1)
						{
							$minFlag = " minute early.";

						}
						else
						{
							$minFlag = " minutes early.";

						}
					}

					$diff = round(abs($diff),1);

					
					if($lastName !=  $scheduleIds[$x])
					{
						$stHighlight = !$stHighlight;
						if ($stHighlight)
						{
							echo "<div class=\"data_field1\">" . "Stop: " . $stopNms[$stLoc] . "</div>";
						}
						else
						{
							echo "<div class=\"data_field2\">" . "Stop: " . $stopNms[$stLoc] . "</div>";
							
						}
						
						$lastName = $scheduleIds[$x];



					}
					else
					{
						
						if ($stHighlight)
						{
							echo "<div class=\"data_field1\">" . "Scheduled time: " . $scheduleTms[$x] . " | Average difference: <span style =\"color: " . $colorFlag . "\">" . $diff . " </span>" . $minFlag . " </div>";

						}
						else
						{
							echo "<div class=\"data_field2\">" . "Scheduled time: " . $scheduleTms[$x] . " | Average difference: <span style =\"color: " . $colorFlag . "\">" . $diff . " </span>" . $minFlag . " </div>";
						}
					}

				}
	
				
			}

			
		}
		
		
?>
			</div>
		</div>
	</body>
</html>
<?php
	}
	
	$conn->close();
?>


