/**
 * Autocompleter.java
 * @author Jessie Baskauf and Ellie Mamantov
 * @version 04/05/2018
 *
 * (Adapted from Jeff Ondich, 20 March 2018)
 *
 * This class exposes a very simple interface for generating auto-completions of search strings.
 *
 */
 
package edu.carleton.jbaskauf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Autocompleter {
    private List<Actor> dataList;

    // Private class for storing extracted information about each actor name.
    private class Actor {
        private int firstNameIndex;
        private int matchIndex;
        private String lowerCaseName;
        private String upperCaseName;

        private Actor(String name) {
            upperCaseName = name;
            lowerCaseName = name.toLowerCase();
            lowerCaseName = lowerCaseName.replace(" ", "");
            if (lowerCaseName.contains(",")) {
                firstNameIndex = lowerCaseName.indexOf(",") + 1;
            }
            else {
                // -2 indicates no first name
                firstNameIndex = -2;
            }
            // -1 indicates no match, can be updated with setMatchIndex
            matchIndex = -1;
        }

        /** 
        * Finds index where actor name matches the input search string.
        * @param searchString the string we wish to find autocompletions for
        */
        private void setMatchIndex(String searchString) {
            if (lowerCaseName.contains(searchString)) {
                matchIndex = lowerCaseName.indexOf(searchString);
            }
        }

    }

    /**
     * @param dataFilePath the path to the data file containing the set of items to
     * from which auto-completed results will be drawn
     */

    public Autocompleter(String dataFilePath) {
        File inputFile = new File(dataFilePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile);
        }
        catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        }
        dataList = new ArrayList<Actor>();
        
        // reads each name from the txt file, generates an Actor object for it,
        // and stores the Actor in dataList
        while (scanner.hasNextLine()) {
            String name = scanner.nextLine();
            Actor actor = new Actor(name);
            dataList.add(actor);
        }
        scanner.close();
    }

    /**
     * @param searchString the string whose autocompletions are sought
     * @return the list of potential completions that match the search string,
     *  sorted in decreasing order of quality of the match (that is, the matches
     *  are sorted from best match to weakest match)
     */
    public List<String> getCompletions(String searchString) {
        ArrayList<String> completions = new ArrayList<String>();
        ArrayList<Actor> completionsToSort = new ArrayList<Actor>();

        if (searchString == null) {
            return completions;
        }
        
        //remove spaces and upper case from searchString
        searchString = searchString.toLowerCase();
        searchString = searchString.replace(" ", "");

        if (searchString.equals("")) {
            return completions;
        }
        
        //puts any partial matches into an ArrayList to sort later
        for (Actor actor : dataList) {
            if (actor.lowerCaseName.contains(searchString)) {
                actor.setMatchIndex(searchString);
                completionsToSort.add(actor);
            }
        }

        //sort completionsToSort
        completions = sortCompletions(completionsToSort, completions, searchString);
        return completions;
    }

    /**
     * @param list a list of actors
     * @return the list of actors, sorted by earliest match (i.e. smallest match index)
     * with the search string
     */
    public ArrayList<Actor> sort(ArrayList<Actor> list) {
        Actor temp;
        for (int i = 1; i < list.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (list.get(j).matchIndex < list.get(j - 1).matchIndex) {
                    temp = list.get(j);
                    list.set(j, list.get(j - 1));
                    list.set(j - 1, temp);
                }
            }
        }
        return list;
    }

    /**
     * @param unsortedCompletions an unsorted ArrayList of Actors that have any match with the search string
     * @param sortedCompletions an ArrayList to which we will add Actors in order of relevance to the search string
     * @param searchString the string whose autocompletions are sought
     * @return sortedCompletions with all Actors from unsortedCompletions added in proper order
     */
    public ArrayList<String> sortCompletions(ArrayList<Actor> unsortedCompletions, ArrayList<String> sortedCompletions, String searchString) {
        //case where match is at the beginning of last name
        for (Actor actor : unsortedCompletions) {
            if (actor.matchIndex == 0) {
                sortedCompletions.add(actor.upperCaseName);
            }
        }
        //case where match is at the beginning of first name
        ArrayList<Actor> firstNameMatch = new ArrayList<Actor>();
        for (Actor actor : unsortedCompletions) {
            if (actor.matchIndex == actor.firstNameIndex) {
                firstNameMatch.add(actor);
            }
        }
        firstNameMatch = sort(firstNameMatch);
        for (Actor actor : firstNameMatch) {
            sortedCompletions.add(actor.upperCaseName);
        }
        //case where match is somewhere else in the last name
        ArrayList<Actor> midLastNameMatch = new ArrayList<Actor>();
        for (Actor actor : unsortedCompletions) {
            if (actor.matchIndex > 0 && actor.matchIndex < actor.firstNameIndex && ! searchString.contains(",")) {
                midLastNameMatch.add(actor);
            }
        }
        midLastNameMatch = sort(midLastNameMatch);
        for (Actor actor : midLastNameMatch) {
            sortedCompletions.add(actor.upperCaseName);
        }
        //case where match is somewhere else in the first name
        ArrayList<Actor> midFirstNameMatch = new ArrayList<Actor>();
        for (Actor actor : unsortedCompletions) {
            if (actor.matchIndex > actor.firstNameIndex && actor.firstNameIndex != -2) {
                midFirstNameMatch.add(actor);
            }
        }
        midFirstNameMatch = sort(midFirstNameMatch);
        for (Actor actor : midFirstNameMatch) {
            sortedCompletions.add(actor.upperCaseName);
        }
        //case where match is across comma boundary
        ArrayList<Actor> commaBoundaryMatch = new ArrayList<Actor>();
        for (Actor actor : unsortedCompletions) {
            if (searchString.contains(",") && ! actor.lowerCaseName.equals(searchString)) {
                commaBoundaryMatch.add(actor);
            }
        }
        commaBoundaryMatch = sort(commaBoundaryMatch);
        for (Actor actor : commaBoundaryMatch) {
            sortedCompletions.add(actor.upperCaseName);
        }

        return sortedCompletions;
    }

    // Extract data file path to list of actor names and user-inputted search string from command-line args,
    // then print the generated autocompletions.
    // Command-line syntax: java Autocompleter dataFilePath searchString
    public static void main(String[] args) {
        String dataFilePath = args[0];
        String searchString = args[1];
        Autocompleter autocomplete = new Autocompleter(dataFilePath);
        List<String> completions = autocomplete.getCompletions(searchString);
        for (int i = 0; i < completions.size(); i++) {
            System.out.println(completions.get(i));
        }
    }
}
