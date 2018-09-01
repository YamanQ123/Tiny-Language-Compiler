import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String []args) {
        String c = "";
        String fileName = "scanner_input.txt";
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                c+=line+'\n';
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        Scanner scanner1 = new Scanner(c);
        scanner1.Scan();
        Parser parser = new Parser(scanner1);
        JFrame frame = new JFrame("Syntax Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(parser);
        frame.setSize(800,600);
        frame.setVisible(true);
        try {
            File file = new File("scanner_output.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(scanner1.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            File file = new File("parser_output.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(parser.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}

