import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Main class
public class Main {
    public static void main(String[] args) {
        int clauseInteger = 1;
        ArrayList<String[]> clauses = new ArrayList<>();
        String kb = args[0];
        ArrayList<String[]> testClause = new ArrayList<>();

        try {
            File file1 = new File(kb);
            Scanner scanner =  new Scanner(file1);

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tempList = line.split(" ");

                clauses.add(tempList);
            }

            scanner.close();

            testClause.add(clauses.get(clauses.size()-1));
            clauses.remove(clauses.size()-1);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred, File could not be opened.");
            e.printStackTrace();
        }


    }
}
