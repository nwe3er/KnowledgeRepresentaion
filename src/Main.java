import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Main class
public class Main {
    public static void main(String[] args) {
        // list to store the kb
        ArrayList<String[]> kb = new ArrayList<>();
        // path of the argument
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
            System.out.println(clauseCount + ". " + String.join(" ", strings) + " { }");
            clauseCount++;
        }

        // loop through test clause, add negation to kb and print the clause to the console
        for (String s : testClause) {
            String[] temp = new String[1];
            temp[0] = s;
            kb.add(temp);
            System.out.println(clauseCount + ". " + String.join(" ", kb.get(kb.size() - 1)) + " { }");
            clauseCount++;
        }

        int cli = 1;
        while (cli < clauseCount-1) {
            int clj = 0;
            while(clj < cli) {
                ArrayList<String> result = resolveClause(kb.get(cli), kb.get(clj), kb);
                if (result.isEmpty()) {
                    int temp1 = cli+1;
                    int temp2 = clj+1;
                    System.out.println(clauseCount + ". " + "Contradiction" + " {" + temp1 + ", " + temp2 + "}" );
                    clauseCount++;

                    System.out.print("Valid");
                    System.exit(0);

                } else if (result.get(0).equals("1")) {
                    clj +=1;
                    continue;
                } else {
                    String[] temp = result.toArray(new String[0]);
                    int temp1 = cli+1;
                    int temp2 = clj+1;
                    System.out.println(clauseCount + ". " + String.join(" ", result) + " {" + temp1 + ", " + temp2 +"}");
                    clauseCount += 1;
                    kb.add(temp);

                }
                clj += 1;
            }
            cli +=1;
        }
        // Print not valid to the console
        System.out.print("Not Valid");
    }

    private static ArrayList<String> resolveClause(String[] clause1, String[] clause2, ArrayList<String[]> kb) {

        // string list declared to the size of the two clauses
        String[] combineClause = new String[clause1.length+ clause2.length];

        // concat the two string arrays (clauses)
        System.arraycopy(clause1, 0, combineClause, 0, clause1.length );
        System.arraycopy(clause2, 0, combineClause, clause1.length, clause2.length );

         LinkedHashMap<String, Integer> hashMap = new LinkedHashMap<>();

        for (String s : combineClause) {
            if (!hashMap.containsKey(s)) {
                hashMap.put(s, 0);
            }
        }

         ArrayList<String> resolved = new ArrayList<>(hashMap.keySet());
         ArrayList<String> resolved1 = new ArrayList<>(hashMap.keySet());

        for (String value : clause1) {
            for (String s : clause2) {
                if (isNegated(value, s)) {
                    resolved.remove(value);
                    resolved.remove(s);

                    if (resolved.size() == 0) {
                        ArrayList<String> bool = new ArrayList<>();
                        return bool;
                    }

                    else if (evalTOTrue(resolved)) {
                        ArrayList<String> bool = new ArrayList<>();
                        bool.add("1");
                        return bool;
                    }

                    else {
                        for (String[] strings : kb) {
                            if (getDifference(resolved, strings).isEmpty()) {
                                ArrayList<String> bool = new ArrayList<>();
                                bool.add("1");
                                return bool;
                            }
                        }
                         return resolved;
                    }
                }
            }
        }

        ArrayList<String> s = new ArrayList<>();

        if (resolved.equals(resolved1)) s.add("1");

        return s;
    }

    // gets the difference from list and returns that list
    private static ArrayList<String> getDifference(ArrayList<String> resolved, String[] clause1) {
        // loop to convert the string array (clause1) and add to temp list
        ArrayList<String> temp = new ArrayList<String>(Arrays.asList(clause1));
        ArrayList<String> temp1 = new ArrayList<String>(resolved);
        for(String s : resolved){
            if(temp.contains(s))
                temp1.remove(s);
        }

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
