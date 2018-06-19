//import java.io.*;
//import java.util.ArrayList;
//
//class CompanyReader {
//    private File file;
//    private boolean AMBest;
//    private int ultimateParent;
//    CompanyReader(String fp, boolean AMBest) throws IOException{
//        this.file = new File(fp);
//        file.createNewFile();
//        this.AMBest = AMBest;
//        this.ultimateParent = -1;
//    }
//    public ArrayList<TreeNode> getCompanies(){
//        ArrayList<TreeNode> comps = new ArrayList<TreeNode>();
//        try {
//
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String line;
//            reader.readLine();
//            int count=0;
//            while((line = reader.readLine()) != null){
//                //System.out.println(line);
//
//                String[] lineParts = line.split(",");
//                if(lineParts.length >= 18) {
//                    if (AMBest) {
//                        this.ultimateParent = Integer.parseInt(lineParts[17]);
//                        comps.add(new TreeNode(lineParts[3], lineParts[7], lineParts[9]));
//                    } else {
//                        System.out.println(line);
//                        this.ultimateParent = Integer.parseInt(lineParts[17]);
//                        comps.add(new TreeNode(lineParts[0], lineParts[5], lineParts[6]));
//                    }
//                }
//            }
//        } catch(FileNotFoundException e){
//            e.printStackTrace();
//        } catch(IOException e){
//            e.printStackTrace();
//            System.out.print("");
//        }
//        return comps;
//    }
//
//    public int getUltimateParent() {
//        return ultimateParent;
//    }
//}
