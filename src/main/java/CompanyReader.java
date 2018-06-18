import java.io.*;
import java.util.ArrayList;

class CompanyReader {
    private File file;
    private boolean AMBest;
    CompanyReader(String fp, boolean AMBest) throws IOException{
        this.file = new File(fp);
        file.createNewFile();
        this.AMBest = AMBest;
    }
    public ArrayList<TreeNode> getCompanies(){
        ArrayList<TreeNode> comps = new ArrayList<TreeNode>();
        try {

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null){
                //System.out.println(line);
                String[] lineParts = line.split(",");
                if(AMBest){
                    comps.add(new TreeNode(lineParts[3],lineParts[7],lineParts[9]));
                }
                else{
                    comps.add(new TreeNode(lineParts[0],lineParts[4],lineParts[6]));
                }
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
            System.out.print("");
        }
        return comps;
    }
}
