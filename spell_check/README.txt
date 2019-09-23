Software/Project Guide
_________________________________________

Sections
_________________________________________
I. Description of Project
II. File Guide (./mebrain/project directory)
III. Description of Functions in spellCheck.php
IV. Analysis of Project


I. Description of Project
_________________________________________
For a long time, I've desired to develop a spell check system and figured that it would make
a great final project.  In order to interact with it, you will need to first refer to spellCheckPrelim.html .
It's a form where you will be requested to enter the name of the file to submit to the spell check
component.  For the sake of this project, make sure to either upload new files to the server before
referencing them, or reference ones that have already been upload to the server, by me 
(refer to the File Guide for file names).  The spell check compenent's interface contains three 
sections: file output section (output.html); suggested spellings lists (suggestions.html);
word selection/rejection section ('select', 'ignore', 'and finish' buttons).  

A.  File Output Section
________________________________
Incorrectly spelled entries are to appear in bold and red-colored font (the ones flagged for correction).  
And the entry underlined in blue font, is the one in question -- in other words, it's the "current" word
being analyzed.  

B.  Suggested Spellings Section
________________________________
Fifteen words are chosen for every incorrectly spelled entry, as they serve alternative spelling options.
Theses words appear of the right-side of the interface.

C. Word Selection Section
________________________________
Users have the option of either selecting any of the alternative spellings (correction) or ignoring all of them.
Words that appear in the "Suggested Spellings" section appear in a drop-down menu for selection.
There is an additional button 'finish' that serves essentially as a program termination option.  Furthermore,
the current state of the document in question is preserved and contents are passed on (printed) to the
original document.


II. File Guide
_________________________________________

A. Test Input
________________________________

1. blank.txt (an empty file)
2. sentence.txt (a sentence containing a few errors)
3. students.txt (a document containing two correctly spelled words)
4. tchaikovsky.txt (a document containg a paragraph with numerous errors)

B. Lexical Documents
________________________________
(Words in these document can comprise the lexicon)
1. words2.txt (contains roughly 250,000 entries; 2mb)
2. words3.txt (contains 3000 entries; used in project)

C. Database Input Script
________________________________
lexicon.php

D. Experimental/Test Files
________________________________
1. experimentation_and_planning.txt (shows my thought processes and code development)
2. panel.html (development of user interface)

E. CSS File
________________________________
styles.css (used to format user interfact -- panel.html)


III. Functions in Main Program
_________________________________________

function ignoreWord($tokens, $misspelled_words, $html_output, $index, $indices, $fileName, $suggestions)
	// Purpose: select spelling suggestion to update list of tokens with
	// Purpose (cont.): update both output.html and suggestions.html (subsequent entry)
	// Input: list of tokens from input file; misspelled words list; html (output) token list; index; 
	// Input (cont.): list of array indices from misspelled_words list
	// Output: array containing updated token (user input) and html (output.html) lists
	
function selectWord($tokens, $misspelled_words, $html_output, $index, $indices, $fileName, $suggestions){
	// Purpose: select spelling suggestion to update list of tokens with
	// Purpose (cont.): update both output.html and suggestions.html (subsequent entry)
	// Input: list of tokens from input file; misspelled words list; html (output) token list; index; 
	// Input (cont.): list of array indices from misspelled_words list
	// Output: array containing updated token (user input) and html (output.html) lists
	
function general_outputUserText($html_out){
	// Purpose: to write user input file to html file for viewing (in output.html) -- general function (excluding instantiation)
	// Input: array of tokens containing html tags (viz. <b>, <br>)
	// Output: array of tokens containing html tags (viz. <b>, <br>)
	
function outputSuggestedSpellings($asl){ // asl stands for "a spelling list"
	// Purpose: to pretty-print and suggested spellings list and output it to suggestions.html
	// Input: a list (array) of spelling suggestions
	// Output: none (no values returned) 
	
function createLexicon($connection){
	// Purpose: to create data structures for lexicon and corresponding "word" counts
	// Input: database connection command (database: 'mebrain_project', table: 'lexicon')
	// Output: an array containing lexicon ($lexicon) and character counts of lexical items ($char_count)
	
function tokenization(){
	// Purpose: to create tokens from user input file
	// Input: none
	// Output: array of tokens ($tokens) and name of input file
	
function createMisspelledWordsList($token_list, $lex){
	// Purpose: to create misspelled word list and output-fo- display data structure (arrays) 
	// Input: tokens from user input file ($tokens) and mysqli database/table connection command
	// Output: list (array) containing misspelled words from user input and array of html treated/tagged tokens
	
function outputUserText($html_out){
	// Purpose: to write user input file to html file for viewing (in output.html) -- initial output
	// Input: array of tokens containing html tags (viz. <b>, <br>)
	// Output: array of tokens containing html tags (viz. <b>, <br>)
	
function compileSpellingList($a_lexicon, $misspelled_words_list, $chars){
	// Purpose: to compile a suggested spellings lists for all misspelled words
	// Input: lexicon (array) and misspelled words list (array)
	// Output: array of arrays of suggested spellings lists
	
function levenshteinDistance($string1, $string2){
	// Purpose: compute edit distance (similarity/dissimilarity) between two words
	// Input: two strings
	// Output: integer (edit distance)
	
function spellingList($a_word, $a_lexicon, $char_count_list){
	// Purpose: to generate a complete spelling list (suggestions) for eventual output to suggestions.html
	// Input: word from user input document; lexicon; character count array (of lexical items)
	// Output: array of words to be printed to suggestions.html (suggested spellings)
	
function initialize(){
	// Purpose: initialize program
	// Input: none
	// Output: array containing: lexicon, tokens from user input, character count (lexical items from lexicon),  
	// Output (cont): misspelled words list, tokens for output to output.html, suggested spellings list 
	// (for all misspelled words)
	
	
IV. Project Analysis
_________________________________________
Overall, I think my effort in implementing a spell check was relatively successful. The underlying logic
is solid; however, I was unsuccessful in getting the functions that are supposed to output content to
both the file output section (output.html) and suggested spellings list (suggestions.html) to work. It turns
out that spellCheck.php does not have the required permissions to open/write to files. I tried a few methods
to overcome this obstacle, but was still unsuccessful. FYI, the contents that in the file output and
suggested spellings section is to illustrate what the program should do -- the contents are immutable. I
was also unsuccessful in getting the buttons appearing in the word selection/rejection/program termination
section to work. In doing extensive research on this matter, I discovered that PHP, per se, is not designed to
support 'onclick' events: javascript is designed to handle that kind of stuff. I was successful in populating the
drop-down list that appears in the word selection/rejection/... section with recommended spellings; 
however, it's only good for one instance because I was unsuccessful in getting the button-click events to 
work. In short, even though I dedicated way more than the required time to complete/(work on) this project,
I had a blast doing so, and look forward to learning javascript (hint, hint =]...) so that I may complete it.
