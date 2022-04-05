import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*-----------------------Analysis--------------------------------*

Theorem prover for a clause logic using the resolution principle.

Implemented a Java program that proves whether a clause is entailed by a knowledge base.

Uses the resolution principle to prove a clause is valid by contradiction. Negates the clause

to be proved and add it to the knowledge base, then deduces new clauses until contradiction or

until no new clauses can be generated. The program takes exactly one argument from the command line:

A knowledge base file that contains the initial knowledge base and the clause whose validity we want

to test. The input file contains n lines organized as follows: the first n - 1 lines describe the

initial KB, while line n contains the (original) clause to test. The literals of each clause are

separated by a blank space, negated variables are indicated by the prefix ~.

 */

// Main class
public class Main {
    public static void main(String[] args) {
        // list to store the kb
        ArrayList<String[]> kb = new ArrayList<>();
        // path of the argument passed
        String path1 = args[0];
        // clause to be proved
        String[] testClause;
        // counter
        int clauseCount = 1;

        // open path1 file to get the knowledge base and the clause to test
        try {
            // open scanner on .kb file
            File file1 = new File(path1);
            Scanner scanner =  new Scanner(file1);

            // loop through the file adding elements to the kb list
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tempList = line.split(" ");

                kb.add(tempList);
            }

            // close scanner
            scanner.close();

         // print the error message if the file could not be opened
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred, File could not be opened.");
            e.printStackTrace();
        }

        // assign the clause needed to be proved
        testClause = (kb.get(kb.size()-1));
        // remove the last element (clause to be tested) from the kb list
        kb.remove(kb.size()-1);

        // loop to negate the clause to solve
        for(int i = 0; i < testClause.length; i++) {
            // if ~ is included, remove it
            if (testClause[i].contains("~")) {
                testClause[i] = testClause[i].replace("~", "");
            }
            // else add ~ to the element
            else {
                testClause[i] = testClause[i].replace(testClause[i], "~" + testClause[i]);
            }
        }

        // loop through the kb and print the clauses to the console
        for (String[] strings : kb) {
            System.out.println(clauseCount + ". " + String.join(" ", strings) + " {}");
            clauseCount++;
        }

        // loop through test clause, add to kb and print the clause to the console
        for (String s : testClause) {
            String[] temp = new String[1];
            temp[0] = s;
            kb.add(temp);
            System.out.println(clauseCount + ". " + String.join(" ", kb.get(kb.size() - 1)) + " {}");
            //update counter
            clauseCount++;
        }

        // assign clause1 = 1
        int clause1 = 1;

        // loop until clause 1 < clause counter - 1
        while (clause1 < clauseCount-1) {
            int clause2 = 0;
            // loop until clause 2 < clause 1
            while(clause2 < clause1) {
                // store the result returned from resolvedClause method
                ArrayList<String> result = resolveClause(kb.get(clause1), kb.get(clause2), kb);
                // if result is empty, update clause1 and clause2 and print the Contradiction to the console
                if (result.isEmpty()) {
                    int temp1 = clause1+1;
                    int temp2 = clause2+1;
                    System.out.println(clauseCount + ". " + "Contradiction" + " {" + temp1 + ", " + temp2 + "}" );
                    // update clause counter
                    clauseCount++;

                    System.out.println("Valid");
                    // exit program
                    System.exit(0);

                  // else if the results size is == 1 update clause2
                } else if (result.get(0).equals("1")) {
                    clause2 +=1;
                    continue;

                  // else update clause1 and clause2 and print the clause to the console
                } else {
                    String[] temp = result.toArray(new String[0]);
                    int temp1 = clause1+1;
                    int temp2 = clause2+1;
                    System.out.println(clauseCount + ". " + String.join(" ", result) + " {" + temp1 + ", " + temp2 +"}");
                    // update counter
                    clauseCount += 1;
                    // update the knowledge base
                    kb.add(temp);

                }
                // update clause 2
                clause2 += 1;
            }
            // update clause1
            clause1 +=1;
        }
        // Print not valid to the console
        System.out.println("Fail");
    }

    // method to resolve the clauses
    private static ArrayList<String> resolveClause(String[] clause1, String[] clause2, ArrayList<String[]> kb) {

        // string list declared to the size of the two clauses
        String[] combineClause = new String[clause1.length+ clause2.length];

        // concat the two string arrays (clause1 and clause2)
        System.arraycopy(clause1, 0, combineClause, 0, clause1.length );
        System.arraycopy(clause2, 0, combineClause, clause1.length, clause2.length );

        LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<>();

        // loop to store keys into the hashmap
        for (String s : combineClause) {
            if (!hashMap.containsKey(s)) {
                hashMap.put(s, 0);
            }
        }

        // set keys to resolved
        ArrayList<String> resolved = new ArrayList<>(hashMap.keySet());
        // clone the resolved key set
        ArrayList<String> resolved1 = new ArrayList<>(hashMap.keySet());

        // loop to see if the clauses are logically equivalent and removes it from the clauses
        for (String value : clause1) {
            for (String s : clause2) {
                if (isNegated(value, s)) {
                    resolved.remove(value);
                    resolved.remove(s);

                    // if resolve is empty, return an empty list
                    if (resolved.size() == 0) {
                        return new ArrayList<>();
                    }

                    // else if the clause evaluates to true, set the bool list size to 1
                    else if (evalTOTrue(resolved)) {
                        ArrayList<String> bool = new ArrayList<>();
                        bool.add("1");
                        return bool;
                    }

                    // else if difference of the clauses is empty,  set the bool list size to 1
                    else {
                        for (String[] strings : kb) {
                            if (getDifference(resolved, strings).isEmpty()) {
                                ArrayList<String> bool = new ArrayList<>();
                                bool.add("1");
                                return bool;
                            }
                        }
                        // return the list
                        return resolved;
                    }
                }
            }
        }

        ArrayList<String> s = new ArrayList<>();

        // return s with size == 1 if (resolve == resolve 1)
        if (resolved.equals(resolved1)) s.add("1");

        return s;
    }

    // gets the difference from list and returns that list
    private static ArrayList<String> getDifference(ArrayList<String> resolved, String[] clause1) {
        // list to store converted clause array
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(clause1));
        // temp list to store the resolved clause
        ArrayList<String> temp1 = new ArrayList<>(resolved);
        // loop to remove duplicate
        for(String s : resolved){
            if(temp.contains(s))
                temp1.remove(s);
        }

        // loop to add difference
        for(String s : temp){
            if(!resolved.contains(s))
                temp1.add(s);
        }

        // return the list
        return temp1;

    }


    // method to see if a clause evaluates to true
    private static boolean evalTOTrue(ArrayList<String> resolved) {
        for (int i = 0; i < resolved.size(); i++) {
            for (String s : resolved) {
                if (isNegated(resolved.get(i), s)) return true;
            }
        }
        return false;
    }

    // checks if the clauses are equivalent
    private static boolean isNegated(String i, String j) {
        return i.equals("~" + j) || j.equals("~" + i);
    }


}
