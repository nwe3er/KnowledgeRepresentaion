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

        // open path1 file to get the kb and the clause to test
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
        // remove the last element from the kb
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

        // loop the kb and print the clauses to the console
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



    }


}
