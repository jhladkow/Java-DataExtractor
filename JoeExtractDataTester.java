public class JoeExtractDataTester {
    
    public static void main(String[] args) {
        
        String tableName = "";
        String flag = "";
        int length = args.length;
        
        if (length == 2) {
            tableName = args[0];
            flag = args[1];
            boolean t = JoeExtractData.checkTable(tableName);
            
            if (t) {
                if (flag.equals("-f")) {
                    JoeExtractData jed = new JoeExtractData(tableName);
                    jed.createXmlFile();
                    String msg = ("joe_"+tableName+".xml created in current directory");
                    System.out.println(msg);
                }
                else if (flag.equals("-s")) {
                    JoeExtractData jed = new JoeExtractData(tableName);
                    jed.displayXmlFile();
                }
                else if (flag.equals("-fr")) {
                    JoeExtractData jed = new JoeExtractData(tableName);
                    jed.createRawDataFile();
                    String msg = ("joe_"+tableName+".txt created in current directory");
                    System.out.println(msg);
                }
                else if (flag.equals("-sr")) {
                    JoeExtractData jed = new JoeExtractData(tableName);
                    jed.displayRawDataFile();
                }
                else {
                    // Message if flag is invalid
                    System.out.println("ERROR: INVALID FLAG");
                    System.out.println(JoeExtractData.programInstructions());
                }
            }
            else {
                // Message if table does not exist in database
                System.out.println("ERROR: TABLE DOES NOT EXIST");
                System.out.println(JoeExtractData.programInstructions());
            }
        }
        else {
            // Message if there is an incorrect number of arguments
            System.out.println("ERROR: INVALID NUMBER OF ARGUMENTS");
            System.out.println(JoeExtractData.programInstructions());
        }
    }
}