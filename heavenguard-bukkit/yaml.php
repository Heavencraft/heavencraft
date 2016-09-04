<?php
error_reporting(E_ALL);
	$regions = yaml_parse_file("regions.yml");
	
	//var_dump($regions);
	
	$world = "world_biome";
	
	$regions_query = "INSERT INTO regions (name, world, min_x, min_y, min_z, max_x, max_y, max_z) VALUES<br>";
	$members_query = "INSERT INTO regions_members (region_name, uuid, owner) VALUES<br>";
	$update_queries = "";

	foreach ($regions["regions"] as $regionName => $region)
	{
		$minX = $region["min"]["x"];
		$minY = $region["min"][1];
		$minZ = $region["min"]["z"];
		$maxX = $region["max"]["x"];
		$maxY = $region["max"][1];
		$maxZ = $region["max"]["z"];
		
		$regions_query .= "('$regionName', '$world', $minX, $minY, $minZ, $maxX, $maxY, $maxZ),<br>";

		foreach ($region["members"]["unique-ids"] as $uuid)
			$members_query .= "('$regionName', '$uuid', 0),<br>";

		foreach ($region["owners"]["unique-ids"] as $uuid)
			$members_query .= "('$regionName', '$uuid', 1),<br>";
			
			
		if (isset($region["parent"]))
		{
		$parent = $region["parent"];
		$update_queries .= "UPDATE regions SET parent_name = '$parent' WHERE name = '$regionName' LIMIT 1;<br>";
		}
	}

	echo $regions_query . "<br><br>";
	echo $members_query . "<br><br>";
	echo $update_queries . "<br><br>";
	
	
?>