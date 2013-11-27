import java.sql.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JoeExtractData {
    
    private String tableName = "";
    
    // Method to establish database connection 
    private static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");  // load driver
        }
        catch (Exception e) {
            System.err.println("ERROR: CANNOT LOAD DRIVER");
            System.out.println(e.toString());
            e.printStackTrace();
        }
        try {
            c = DriverManager.getConnection("jdbc:mysql://XXXXXXXXXXXXX","username","password");   // establish connection
        }
        catch (SQLException sqle) {
            System.out.println("ERROR: CANNOT CONNECT TO DATABASE: " + sqle.getMessage());
            System.out.println(sqle.toString());
        }
        return c;
    }
    
    // Method that generates XML file
    private String getXml() {
        StringBuilder sb = new StringBuilder("");
        try {
            Connection c = getConnection();             // get connection
            Statement stmt = c.createStatement();       // statement creation
            String query = ("select * from "+tableName);    // create query string
            ResultSet rs = stmt.executeQuery(query);        // get result set based on query
            ResultSetMetaData md = rs.getMetaData();        // generate metadata
            int numColumns = md.getColumnCount();           // get column count for use in for loop
            sb.append("<?xml version="+"\""+"1.0"+"\""+" ?>\n");        // beginning of xml document
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zZ yyyy");  // proper date format
            String date = sdf.format(new Date());           // creates new date string based on given format
            sb.append("<!-- Table: "+tableName+" Date- "+date+" -->\n");    // table name and date header
            sb.append("<"+tableName+">\n");     // open tag containing table name
            
            while (rs.next()) {
                sb.append("\t<rowset>\n");      // row open tag
                for (int i = 1; i<=numColumns; i++) {       // retrieve data from table
                    String type = md.getColumnTypeName(i);
                    sb.append("\t\t<"+md.getColumnName(i)+" type="+"\""+type.toLowerCase()+"\""+">"+rs.getString(i)+"</"+md.getColumnName(i)+">"+"\n");
                }
                sb.append("\t</rowset>\n");     // row close tag
            }
            sb.append("</"+tableName+">");      // closing tag containing table name
            
            // result set, statement, and connection close
            rs.close();
            stmt.close();
            c.close();
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.println(sqle.toString());
        }
        return sb.toString();
    }
    
    // Method that writes XML document to file
    public void createXmlFile() {
        try {
            String xmlFileName = "joe_"+tableName+".xml";       // proper file name
            FileWriter fstream = new FileWriter(xmlFileName);
            BufferedWriter out = new BufferedWriter(fstream);
            String str = getXml();
            out.write(str);         // writes XML to file
            out.close();
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println(e.toString());
        }
    }
    
    // Method to display the XML file on screen
    public void displayXmlFile() {
        String str = getXml();
        System.out.println(str);
    }
    
    // Method that generates raw data file
    private String getRawData() {
        StringBuilder sb = new StringBuilder("");
        try {
            Connection c = getConnection();         // get connection
            Statement stmt = c.createStatement();       // create statement
            String query = ("select * from "+tableName);        // create query
            ResultSet rs = stmt.executeQuery(query);        // generate result set based on query
            ResultSetMetaData md = rs.getMetaData();        // generate metadata
            int numColumns = md.getColumnCount();           // get column count
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss zZ yyyy");  // proper data format
            String date = sdf.format(new Date());
            sb.append("Table- "+tableName+" Date: "+date+"\n");     // table name and date header
            
            while (rs.next()) {
                for (int i = 1; i<=numColumns; i++) {       // get table data
                    if (i<numColumns) {
                        sb.append(rs.getString(i) + "|");
                    }
                    else {
                        sb.append(rs.getString(i)+"\n");    // makes sure no pipes are added to end of row
                    }
                }
            }
            // result set, statement, and connection close
            rs.close();
            stmt.close();
            c.close();
        }
        catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
            System.out.println(sqle.toString());
        }
        return sb.toString();
    }
    
    // Method that writes raw data to file
    public void createRawDataFile() {
        try {
            String rawDataFileName = "joe_"+tableName+".txt";
            FileWriter fstream = new FileWriter(rawDataFileName);
            BufferedWriter out = new BufferedWriter(fstream);
            String str = getRawData();
            out.write(str);         // writes raw data to file
            out.close();
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println(e.toString());
        }
    }
    
    // Method that displays raw data on screen
    public void displayRawDataFile() {
        String str = getRawData();
        System.out.println(str);
    }
    
    // Method that gives program instructions
    public static String programInstructions() {
        StringBuilder sb = new StringBuilder("");
        sb.append("This program takes two command line arguments. Table name followed by a flag.\n");
        sb.append("Ex. java (program name) (table name) (flag)\n");
        sb.append("Flags:   -f  converts table to xml format and stores it in present working directory with file name joe_(table name).xml\n");
        sb.append("         -s  converts table to xml format and displays it on screen\n");
        sb.append("         -fr converts table to pipe separated values and stores it in present working directory with file name joe_(table name).txt\n");
        sb.append("         -sr converts table to pipe separated values and displays it on screen\n");
        return sb.toString();
    }
    
    // Table verification method
    public static boolean checkTable(String s) {
        String tableName1 = s;
        int z = 0;
        try {
            Connection c = getConnection();     // get connection
            Statement stmt = c.createStatement();       // statement creation
            String query = ("show tables like "+"\""+tableName1+"\"");      // query creation
            ResultSet rs = stmt.executeQuery(query);        // result set based on query
            ResultSetMetaData md = rs.getMetaData();        // generate metadata
            int numColumns = md.getColumnCount();       // get column count
            
            while (rs.next()) {                             // searches for specific table name
                for (int i = 1; i<=numColumns; i++) {
                    String a = rs.getString(i);
                    if (a.equals(tableName1)) {
                        z=1;
                    }
                    else {
                        z=2;
                    }
                }
            }
            // result set, statement, and connection close
            rs.close();
            stmt.close();
            c.close();
        }
        catch (SQLException sqle) {
            System.out.println("ERROR: " + sqle.getMessage());
            System.out.println(sqle.toString());
        }
        catch (Exception e) {
           System.out.println(e.getMessage());
           System.out.println(e.toString());
        }
        if (z==1) {
            return true;
        }
        else {
            return false;
        }
    }
    
    // Constructor
    JoeExtractData(String s) {
        tableName = s;
    }
}
