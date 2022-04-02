import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Main class
public class Main {
    public static void main(String[] args) {
        ArrayList<String[]> kb = new ArrayList<>();
        String path1 = args[0];
        String[] testClause;

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

        // assign the clause to prove
        testClause = (kb.get(kb.size()-1));
        // remove the last line from the kb
        kb.remove(kb.size()-1);

        // loop to negate the clause to prove
        for(int i = 0; i < testClause.length; i++) {
            // if ~ is included, remove it
            if (testClause[i].contains("~")) {
                testClause[i] = testClause[i].replace("~", "");
            }
        }


    }


}
