
<?php
// ini_set('display_errors', 1);
// error_reporting(E_ALL);

function ignoreWord($tokens, $misspelled_words, $html_output, $index, $indices, $fileName, $suggestions){
	// Purpose: select spelling suggestion to update list of tokens with
	// Purpose (cont.): update both output.html and suggestions.html (subsequent entry)
	// Input: list of tokens from input file; misspelled words list; html (output) token list; index; 
	// Input (cont.): list of array indices from misspelled_words list
	// Output: array containing updated token (user input) and html (output.html) lists
	
	$selected_spelling = filter_input(INPUT_POST, "suggestions"); // input from form (toward end of file)
	//$final_selection = filter_input(INPUT_POST, "display");
	$new_index = $indices[$index];
	$tokens[$new_index] = $misspelled_words[$new_index]; // update
	$html_output[$index] = $misspelled_words[$new_index]; // update

	$next = $index++;

	if($next >= sizeof($misspelled_words)){
		finish($tokens, $fileName);
	}
	$next_index = $indices[$index++];
	$html_output[$next_index] = "<b style=\"color: blue;\">".$html_output[$next_index]."</u>";
	outputSuggestedSpellings($misspelled_words[$next_index]);

	// output text to output.html
	general_outputUserText($html_output);

	return array($tokens, $html_output);
}

function selectWord($tokens, $misspelled_words, $html_output, $index, $indices, $fileName, $suggestions){
	// Purpose: select spelling suggestion to update list of tokens with
	// Purpose (cont.): update both output.html and suggestions.html (subsequent entry)
	// Input: list of tokens from input file; misspelled words list; html (output) token list; index; 
	// Input (cont.): list of array indices from misspelled_words list
	// Output: array containing updated token (user input) and html (output.html) lists
	
	$selected_spelling = filter_input(INPUT_POST, "suggestions"); // input from form (toward end of file)
	//$final_selection = filter_input(INPUT_POST, "display");
	$new_index = $indices[$index];
	$tokens[$new_index] = $final_selection;
	$html_output[$index] = $final_selection;

	$next = $index++;

	if($next >= sizeof($misspelled_words)){
		finish($tokens, $fileName);
	}
	$next_index = $indices[$index++];
	$html_output[$next_index] = "<b style=\"color: blue;\">".$html_output[$next_index]."</u>";
	outputSuggestedSpellings($misspelled_words[$next_index]);

	// output text to output.html
	general_outputUserText($html_output);

	return array($tokens, $html_output);
}

function general_outputUserText($html_out){
	// Purpose: to write user input file to html file for viewing (in output.html) -- general function (excluding instantiation)
	// Input: array of tokens containing html tags (viz. <b>, <br>)
	// Output: array of tokens containing html tags (viz. <b>, <br>)

	$file = "output.html"; // output.html
	$html_output = $html_out; 

	$max = sizeof($html_output);

	$display_text = "<p>";

	for ($i = 0; $i < $max; $i++){
		$display_text .= $html_output[$i]." ";
	}

	$display_text .= $display_text."/<p>";

	// write to file (output.html)
	// file_put_contents($file, $display_text); // cannot write to file; permissions denied

	return $html_output; // return html_output to be used in future computations
}

function outputSuggestedSpellings($asl){ // asl stands for "a spelling list"
	// Purpose: to pretty-print and suggested spellings list and output it to suggestions.html
	// Input: a list (array) of spelling suggestions
	// Output: none (no values returned) 


	$file = "suggestions.html"; // "suggestions.html";
	$spelling_suggestions = $asl;
	
	//var_dump($spelling_suggestions);
	
	$ss_keys = array_keys($spelling_suggestions);  // spelling suggestions keys
	
	$max = sizeof($spelling_suggestions);
	// this string of html tags and text will be output to suggestions.html
	$suggestions_string = "<p><u><b>Suggested Spellings:</b></u></p><p>"; 

	for ($i = 0; $i < $max; $i++){
		$index = $ss_keys[$i];
		$suggestions_string .= $index."<br>";
		$suggestions_string .= $spelling_suggestions[$i]."<br>";
	}
	
	$suggestions_string .= $suggestions_string."</p>";
	
	// print("$suggestions_string");

	// file_put_contents($file, $suggestions_string); // output spelling suggestions to suggestions.html
	
	if(!file_exists("suggestions.html")) {
      die("File not found");
      }
	
	// $output = fopen($file, "a+") or die("Unable to open file!");
	// chmod("spellCheck.php", 0755);
	// chmod($file, 0755);
	// fwrite($file, $suggestion_string);
	// fclose($output);
	//file_put_contents($file, $suggestions_string);
}

function createLexicon($connection){
	// Purpose: to create data structures for lexicon and corresponding "word" counts
	// Input: database connection command (database: 'mebrain_project', table: 'lexicon')
	// Output: an array containing lexicon ($lexicon) and character counts of lexical items ($char_count)

	$conn = $connection;
	$lexicon = array(); 
	$char_count = array();

	$sql = "SELECT * FROM lexicon";
	$result = mysqli_query($conn, $sql) or die(mysqli_error());

	while ($row = mysqli_fetch_assoc($result)) { // the following code block creates lexicon and char_count arrays
					
		$id = $row["id"];
		$lexical_item = $row["lexical_item"];
		$no_of_chars = $row["no_of_chars"]; 
	
		$lexicon[$id] = $lexical_item;
		$char_count[$lexical_item] = $no_of_chars;
	
	}
	
	return array($lexicon, $char_count); // returns lexicon and and char_count arrays in an array
}

function tokenization(){
	// Purpose: to create tokens from user input file
	// Input: none
	// Output: array of tokens ($tokens) and name of input file
	
	$file_name = filter_input(INPUT_POST, "file_name"); // collect file name from (input) form

	$file = file_get_contents("$file_name"); // returns contents in a file as a string
	
	if ($file == FALSE){
    	header('Location: spellCheckPrelim.html');
		exit;
	}
	
	$file = str_replace(PHP_EOL, " ".PHP_EOL, $file); // separate end-of-sentence (viz. "\n") punctuation from tokens

	// format end-of-sentence punctuation: separate punctuation from end-of-sentence tokens
	$file = str_replace("\. ", " \. ", $file);
	$file = str_replace("? ", " ? ", $file);
	$file = str_replace("! ", " ! ", $file);

	$tokens = preg_split("/[\s]+/", $file); // final product; array of tokens
	
	return array($tokens, $file_name);
}

function createMisspelledWordsList($token_list, $lex){
	// Purpose: to create misspelled word list and output-fo- display data structure (arrays) 
	// Input: tokens from user input file ($tokens) and mysqli database/table connection command
	// Output: list (array) containing misspelled words from user input and array of html treated/tagged tokens
	
	$tokens = $token_list; // array of all words in input document

	$lexicon = $lex;

	$misspelled_word_list = array(); // array of mispelled words from input document
	$max = sizeof($tokens);
	$html_output = array(); // output for html page; erroneous input to appear in bold & red font
	
	for ($i = 0; $i < $max; $i++){
		$entry = $tokens[$i];
		
		if (preg_match("/^[a-zA-Z]+$/", $entry)){ // only tokens that do not contain special characters are processed
			
			if (!in_array($entry, $lexicon)){
				// generate suggested-spellings list
				// return an array of suggested spellings (viz. $suggested_spellings (array) )
				$misspelled_word_list[$i] = $entry;
		
				// indicate the incorrectly spelled word appear in bold and red font
				$html_output[$i] = "<strong style=\"color: red;\">$entry</strong>";
			}
			else{
				$html_output[$i] = $entry;
			}
		}
		elseif($entry == PHP_EOL){
			$html_output[$i] = "<br>";
		}
		else{
			$html_output[$i] = $entry;
		} 
	}
	
	if(sizeof($misspelled_word_list) == 0){ // no misspelled words
    	header('Location: spellCheckPrelim.html');
		exit;
	}
	
	return array($misspelled_word_list, $html_output);
	
}

function outputUserText($html_out){
	// Purpose: to write user input file to html file for viewing (in output.html) -- initial output
	// Input: array of tokens containing html tags (viz. <b>, <br>)
	// Output: array of tokens containing html tags (viz. <b>, <br>)

	$file = "output.html"; // output.html
	$html_output = $html_out; // array of all tokens in file, with incorrectly spelled words in red and bold font
													  // the first incorrectly spelled word will be underlined
	$max = sizeof($html_output);

	for ($i = 0; $i < $max; $i++) {
		if (preg_match("/<\/strong>/", $html_output[$i])){ // underline the first misspelled word; underlined words == current word being processed
			$html_output[$i] = "<b style=\"color: blue;\">".html_output[$i]."</b>";
			break;
		}
	}

	$display_text = "<p>";

	for ($i = 0; $i < $max; $i++){
		$display_text .= $html_output[$i]." ";
	}

	$display_text .= $display_text."/<p>";

	// write to file (output.html)
	// file_put_contents($file, $display_text); cannot write to file; denied permission(s)

	return $html_output; // return html_output to be used in future computations
}

function compileSpellingList($a_lexicon, $misspelled_words_list, $chars){
	// Purpose: to compile a suggested spellings lists for all misspelled words
	// Input: lexicon (array) and misspelled words list (array)
	// Output: array of arrays of suggested spellings lists
	
	$lexicon = $a_lexicon; // array (list) of lexical items
	$misspelled_words = $misspelled_words_list;
	$char_count = $chars;

	$suggested_spellings = array(); // list of suggested spellings (array of arrays)

	$ms_keys = array_keys($misspelled_words); // $misspelled_words keys

	$ms_keys_max = sizeof($ms_keys);


	for($i = 0; $i < $ms_keys_max; $i++){
		$index = $ms_keys[$i];
		$word = $misspelled_words[$index];
		$sl = spellingList($word, $lexicon, $char_count);
		$suggested_spellings[$index] = $sl; // some function returning array
	}
	
	// var_dump($suggested_spellings);
	
	return $suggested_spellings; // $suggested_spellings is basically a copy of $misspelled_words, except that values are comprised of arrays of words
}

function levenshteinDistance($string1, $string2){
	// Purpose: compute edit distance (similarity/dissimilarity) between two words
	// Input: two strings
	// Output: integer (edit distance)
	
	$word1 = str_split($string1); // converts string to array of characters
	$word2 = str_split($string2);

	$max1 = count($word1);
	$max2 = count($word2);
	
	$d = array();

	for ($i = 0; $i <= $max1; $i++){
		for ($j = 0; $j <= $max2; $j++){
			if ($i == 0){
				$d[0][$j] = $j; // initialization step
			} 
			elseif ($j == 0){
				$d[$i][0] = $i; // initialization step
			}
			else{
				$substitutionCost = 1;
			
				if($word1[$i-1] == $word2[$j-1]){
					$substitutionCost = 0;
				}
				$a = $d[$i-1][$j] + 1; // deletion
				$b = $d[$i][$j-1] + 1; // insertion
				$c = $d[$i-1][$j-1] + $substitutionCost; // substitution
			
				$d[$i][$j] = min($a, $b, $c);
			}
		}
	}
	return $d[$max1][$max2]; // return edit distance (integer)
}

function spellingList($a_word, $a_lexicon, $char_count_list){
	// Purpose: to generate a complete spelling list (suggestions) for eventual output to suggestions.html
	// Input: word from user input document; lexicon; character count array (of lexical items)
	// Output: array of words to be printed to suggestions.html (suggested spellings)
	
	$word = $a_word;
	$lexicon = $a_lexicon;
	$char_count = $char_count_list;
	
	$spelling_list = array(); // suggested spellings list for word in question

	$lexicon_size = sizeof($lexicon);

	for ($j = 1; $j < $lexicon_size; $j++){ // $j starts at 1 because id key in lexicon table (in mebrain_project) database starts at 1
		$lexical_item = $lexicon[$j]; // retrieve lexical item
		$lcc = $char_count[$lexical_item]; // character count of lexical item
		$wordLen = strlen($word);
		$dof = log($wordLen,2); //dof stands for: degrees of freedom
		$min =  $wordLen - $dof;
		$max = $wordLen + $dof;
	
		if ($lcc >= $min && $lcc <= $max) {
			$levDis = levenshteinDistance($word, $lexical_item);
			//print("<p>word: $word; lex item: $lexical_item; lev dis: $levDis</p>");
			$spelling_list[$lexical_item] = $levDis; // what is same word was misspelled more than once
		
		}
	}
	
	asort($spelling_list); // sort $spelling list in ascending order
	$final_list = array_slice($spelling_list, 1, 15); // top 15 words make the final cut
	
	//var_dump($final_list);
	

	return $final_list;
}

function initialize(){
	// Purpose: initialize program
	// Input: none
	// Output: array containing: lexicon, tokens from user input, character count (lexical items from lexicon),  
	// Output (cont): misspelled words list, tokens for output to output.html, suggested spellings list (for all misspelled words)

	// establish connection with mysqli database (database: 'mysqli_project', table: 'mebrain_project')
	$connection  = mysqli_connect("localhost", "mebrain", "psv0n6FrfS", "mebrain_project" );

	// Check connection
	if (!$connection) {
    	die("Connection failed: " . mysqli_connect_error());
	}

	$cl_output = createLexicon($connection); // "cl" stands for create lexicon (returns array: $lexicon, $char_count)
	
	mysqli_close($connection);
	
	$lexicon = $cl_output[0];
	$char_count = $cl_output[1];
	
	$to = tokenization(); // returns array of tokens from user-input file ("to" stands for tokenization output)
	$tokens = $to[0];
	$userInputFileName = $to[1];
	
	
	$misspelled_html = createMisspelledWordsList($tokens, $lexicon); // returns misspelled word list and html_output (array) //crash
	
	$mwl = $misspelled_html[0]; // mwl stands for misspelled word list
	$text_output = $misspelled_html[1]; // text output to output.html (from user input file)
	
	
	$suggested_spellings_list = compileSpellingList($lexicon, $mwl, $char_count); // returns list of misspelled words with 
																																								  	 // spelling suggestions
																																								
	// var_dump($suggested_spellings_list);
	
	$suggested_spellings_keys = array_keys($suggested_spellings_list); 
	$index = $suggested_spellings_keys[0];
	
	// var_dump($suggested_spellings_list[$index]);
	
	
	// output necessary information to output.html and suggestions.html
	outputSuggestedSpellings($suggested_spellings_list[$index]); // permissions denied
	
	
	//outputUserText($text_output); // permissions denied
	
	

	return array($lexicon, $tokens, $char_count, $mwl, $text_output, $suggested_spellings_list, $userInputFileName);
	
}

// main (section) of code

$initialization = initialize();

// $initialization[0] = lexicon
// $initialization[1] = tokens from user input
// $initialization[2] = character counts of lexical items from lexicon (array)
// $initialization[3] = misspelled words list (array) 
// $initialization[4] = array of lexical items to be output to output.html
// $initialization[5] = suggested_spellings_list // array of arrays of suggested spellings lists
// $initialization[6] = user input file name

$counter = 0;
$ms_keys = array_keys($initialization[3]);
$max_count = sizeof($ms_keys);

$temp = $initialization[5];

$temp_keys = array_keys($temp); // keys to array of arrays
$firstKey = $temp_keys[0];
$initialList = $temp[$firstKey]; // returns an array 
$dropDown = $initialList; // set up values for drop-down (word-selection) menu
$sl_keys = array_keys($dropDown);
//$index = $sl_keys[$counter];

//$max = sizeof();

//var_dump($dropDown);

// initializations

$word1 = "";
$word2 = "";
$word3 = "";
$word4 = "";
$word5 = "";
$word6 = "";
$word7 = "";
$word8 = "";
$word9 = "";
$word10 = "";
$word11 = "";
$word12 = "";
$word13 = "";
$word14 = "";
$word15 = "";

// instantiations

$word1 = $sl_keys[0];
$word2 = $sl_keys[1];
$word3 = $sl_keys[2];
$word4 = $sl_keys[3];
$word5 = $sl_keys[4];
$word6 = $sl_keys[5];
$word7 = $sl_keys[6];
$word8 = $sl_keys[7];
$word9 = $sl_keys[8];
$word10 = $sl_keys[9];
$word11 = $sl_keys[10];
$word12 = $sl_keys[11];
$word13 = $sl_keys[12];
$word14 = $sl_keys[13];
$word15 = $sl_keys[14];

/*
if(array_key_exists('select',$_POST)){
   $init_updates = selectWord($initialization[1], $initialization[3], $initialization[4], $counter, $ms_keys, $initialization[6], $initialization[5]); // add $initialization[5]
   $initialization[1] = $init_updates[0];
   $initialization[4] = $init_updates[1];
   $counter++;
   
	// initialize next instance of drop down menu
	$temp = $initialization[5];
	$temp_keys = array_keys($temp); // keys to array of arrays
	$firstKey = $temp_keys[$counter];
	$initialList = $temp[$firstKey]; // returns an array 
	$dropDown = $initialList; // set up values for drop-down (word-selection) menu
	$sl_keys = array_keys($dropDown);
	
	// instantiations
	$word1 = $sl_keys[0];
	$word2 = $sl_keys[1];
	$word3 = $sl_keys[2];
	$word4 = $sl_keys[3];
	$word5 = $sl_keys[4];
	$word6 = $sl_keys[5];
	$word7 = $sl_keys[6];
	$word8 = $sl_keys[7];
	$word9 = $sl_keys[8];
	$word10 = $sl_keys[9];
	$word11 = $sl_keys[10];
	$word12 = $sl_keys[11];
	$word13 = $sl_keys[12];
	$word14 = $sl_keys[13];
	$word15 = $sl_keys[14];  
}

if(array_key_exists('ignore',$_POST)){
   $init_updates = ignoreWord($initialization[1], $initialization[3], $initialization[4], $counter, $ms_keys, $initialization[6], initialization[5]);
   $initialization[1] = $init_updates[0];
   $initialization[4] = $init_updates[1];
   $counter++;
  
	// initialize next instance of drop down menu
	$temp = $initialization[5];
	$temp_keys = array_keys($temp); // keys to array of arrays
	$firstKey = $temp_keys[$counter];
	$initialList = $temp[$firstKey]; // returns an array 
	$dropDown = $initialList; // set up values for drop-down (word-selection) menu
	$sl_keys = array_keys($dropDown);
	
	// instantiations
	$word1 = $sl_keys[0];
	$word2 = $sl_keys[1];
	$word3 = $sl_keys[2];
	$word4 = $sl_keys[3];
	$word5 = $sl_keys[4];
	$word6 = $sl_keys[5];
	$word7 = $sl_keys[6];
	$word8 = $sl_keys[7];
	$word9 = $sl_keys[8];
	$word10 = $sl_keys[9];
	$word11 = $sl_keys[10];
	$word12 = $sl_keys[11];
	$word13 = $sl_keys[12];
	$word14 = $sl_keys[13];
	$word15 = $sl_keys[14];  
}
*/

if(array_key_exists('finish',$_POST)){
   finish($initialization[1], $initialization[6]);
}



print <<<HERE

<hmtl>
<title>Spell Check</title>
<link rel="stylesheet" type="text/css" href="styles.css">

<h2><center>Spell Check</center></h2>

<body>

<div id="wrapper">

<div id="menu">
	<object type="text/html" data="suggestions.html" height="375px" width="200px">
	</object>
</div>

<div id="content">
	<object type="text/html" data="output.html" height="375px" width="750px">
    </object>
</div>


<div id="footer">
	
<center>
<form method="post">
	<label><b>Suggested Spellings:</b></label>
		<select name="suggestions">
			<option value="$word1">$word1</option>
			<option value="$word2">$word2</option>
			<option value="$word3">$word3</option>
			<option value="$word4">$word4</option>
			<option value="$word5">$word5</option>
			<option value="$word6">$word6</option>
			<option value="$word7">$word7</option>
			<option value="$word8">$word8</option>
			<option value="$word9">$word9</option>
			<option value="$word10">$word10</option>
			<option value="$word11">$word11</option>
			<option value="$word12">$word12</option>
			<option value="$word13">$word13</option>
			<option value="$word14">$word14</option>
			<option value="$word15">$word15</option>	
	</select>
	<input type="submit" class="button" name="select" value="Select" />
	<input type="submit" class="button" name="ignore" value="Ignore" />
	<input type="submit" class="button" name="select" value="Select" />
</form>
</center>

</div>

</div>
</body>
</html>

HERE;



?>