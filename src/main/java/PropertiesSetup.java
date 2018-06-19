import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

public class PropertiesSetup {
    public static void main(String[] args){
        Properties prop = new Properties();
        OutputStream output = null;
        Scanner input = new Scanner(System.in);
        File file;
        try{
            file = new File("./Props/config.properties");
            file.createNewFile();
            output = new FileOutputStream(file);
            System.out.println("Enter your database username");
            String props = input.next();
            prop.setProperty("username", props);
            System.out.println("Enter your database password");
            props = input.next();
            prop.setProperty("password", props);

            prop.store(output,null);
        }catch(Exception e){
            e.printStackTrace();
        }   finally {
            if (output != null){
                try{
                    output.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);
    }

}
