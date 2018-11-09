# Autocomplete
Autocomplete is a class project from CS257 Software Design (taught by Jeff Ondich at Carleton College), completed collaboratively with Ellie Mamantov. It includes a simple command-line impelementation of an autocompletion feature and JUnit unit tests for the primary method in this Autocompleter class, which returns all completions for a given search string.

## Features
The Autocompleter class is designed to search through a list of actor names (found in actors.txt) and return the best matches for a search string inputted by the user in the command line. It's most important method, getCompletions, finds all possible matches and sorts them based on a certain order of priority for "best matches." For a given search string S (converted to lower-case, with spaces removed), it finds all possible completions of S (completions are all actor names in lower case, listed as last_name,first_name, where S is a substring of the actor name). It then sorts for "best matches" based on this order of priority:
1. S matches at the beginning of the last name
2. S matches at the beginning of the first name
3. S matches anywhere else in the last name
4. S matches anywhere else in the first name
5. S matches C across the comma boundary between the last name and the first name
When two completions match in the same way, ties are broken by ordering first based on the index at which S occurs (earlier instances are prioritized), and then, if S occurs at the same index in both completions, based on the lexicographic order of the two completion strings.

The AutocompleterTest  class consists of unit tests for the getCompletions method of Autocompleter.java. It relies on the JUnit testing framework.

## Usage
Autocompleter can be used in the command line to find completions (actor names) of a search string inputted by the user. The command line syntax to accomplish this is:
`java Autocompleter dataFilePath searchString` 
where dataFilePath is the path to the actors.txt file (located in the top level Autocomplete project folder) and searchString is the string you would like to get autocompletions for.

### Contributors
Jessie Baskauf
Ellie Mamantov
Basic code stubs provided by Jeff Ondich
