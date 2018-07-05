import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class PropertiesSetup {

    static Properties createProperties(File f){
        Properties prop = new Properties();
        OutputStream output = null;
        Scanner input = new Scanner(System.in);
        try{
            //file = new File("./Props/config.properties");
            //file.createNewFile();
            output = new FileOutputStream(f/*ile*/);
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
        return prop;
    }
    static Properties readProperties(File f) throws IOException{
        Properties prop = new Properties();

        InputStream input = new FileInputStream(f);
        prop.load(input);
        return prop;
    }
}
