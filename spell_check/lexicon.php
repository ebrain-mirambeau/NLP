<?php
//Author: Ebrain Mirambeau
//Purpose: Input lexical items into a database
//make the database connection

// create connection
$conn  = mysqli_connect("localhost", "mebrain", "psv0n6FrfS", "mebrain_project" );

// check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

// create lexicon table
$sql = "DROP TABLE IF EXISTS lexicon";
$result = mysqli_query($conn, $sql) or die(mysqli_error());

$sql = "CREATE TABLE lexicon (id INT, lexical_item VARCHAR(30), no_of_chars INT, PRIMARY KEY (id))";
$result = mysqli_query($conn, $sql) or die(mysqli_error());

// read input from words2.txt and input lexical items (words) into lexicon
$counter = 1;
$file = fopen("words3.txt", "r");
if ($file) {
    while (($line = fgets($file)) !== false) {
        // process the line read.
        $line = str_replace(PHP_EOL, "", $line);
        $char_count = strlen($line);
        $sql = "INSERT INTO lexicon (id, lexical_item, no_of_chars) VALUES ('$counter', '$line', '$char_count')";
		$result = mysqli_query($conn, $sql) or die(mysqli_error());
		$counter++;
    }
    fclose($file); // close words2.txt
} else {
    echo "file cannot be opened";
}
mysqli_close($conn); // close mysqli connection
?>
