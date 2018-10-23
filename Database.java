package com.core.framework.utils;
/**
 *To add JavaDB
 * Right Click on Your Project (DBConnectivity) -->Properties -->Java Build Path -->Libraries -->Click On "Add External Jars" -->derby.jar
 * Path to derby.jar could be C:\ProgramFiles\JDK64\1.8\db\lib
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import  java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.sql.DriverManager.*;

/**
 * Class Purpose: Database Connections
 * @author akongara (akongara@balglobal.com)
 */
@SuppressWarnings("unused")
public class Database {
    private String dbType;
    private String driver;
    private final String userName;
    private final String password;
    private final String tableSchema;
    private final String dbUrl;
    private Connection conn = null;
    private static final Logger Log = LogManager.getLogger(DriverFactory.class);

    /**
     * @param : dbType, dbUrl, tableSchema, userName, password
     * @Category : DB Related
     * @author akongara
     */
    public Database(String dbType, String dbUrl, String tableSchema, String userName, String password) throws Exception {
        this.dbUrl = dbUrl;
        this.userName = userName;
        this.password = password;
        this.dbType = dbType;
        this.tableSchema = tableSchema;
        switch (dbType.toLowerCase()) {
            case "db2":
                this.driver = "com.ibm.db2.jcc.DB2Driver";
                break;
            case "oracle":
                this.driver = "oracle.jdbc.driver.OracleDriver";
                break;
            case "mysql":
                this.driver = "com.mysql.jdbc.Driver";
                break;

            case "SQLServer":
                this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                        //"net.sourceforge.jtds.jdbc.Driver";
                break;
            default:
                try {
                    HtmlReporter.Log("Database", dbType, " is not handled in switch ot it is not given");
                } catch (IOException ex) {
                    Log.info("IOException occured" + ex.getMessage());
                }

        }
    }

    /**
     * @author akongara
     * Description: Get DB Connection
     * @throws: ClassNotFoundException
     */
    private void getDBConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver Got Loaded");
        } catch (ClassNotFoundException err) {
            Log.info("ClassNotFoundException occured " + err.getMessage());
            System.err.println("Failed to Load the " + dbType + " driver! Exiting the program.");
            err.printStackTrace(System.err);
        }
        try {
            System.out.println("Connecting to Database.....");
            conn = DriverManager.getConnection(dbUrl, userName, password);
            //conn = DriverManager.getConnection(dbUrl);
            System.out.println("Database Connected.");
        } catch (SQLException err) {
            Log.info("SQLException occured " + err.getMessage());
            System.err.println("SQL Error ");
            err.printStackTrace(System.err);

        } catch (Exception e) {
            Log.info("Exception occured " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Description: Execute Query
     */
    public List<HashMap<String, String>> executeQuery(String query) {
        ResultSet rs = null;
        Statement s = null;
        List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
        try {
            System.out.println("Connecting to Database....");
            getDBConnection();
            System.out.println("Database Connected... ");
            s = conn.createStatement();
            rs = s.executeQuery(query);
            System.out.println("Query Executed. ");
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> columns = new ArrayList<String>();
            int colCount = rsmd.getColumnCount();
            if (colCount > 0) {
                for (int i = 1; i <= colCount; i++) {
                    columns.add(rsmd.getColumnName(i));
                }
            }
            while (rs.next()) {
                HashMap<String, String> hm = new HashMap<String, String>();
                for (String colName : columns) {
                    hm.put(colName, rs.getString(colName));
                    System.out.println(hm);
                }
                results.add(hm);
            }
        } catch (SQLException err) {
            Log.info("SQLException occured " + err.getMessage());
            System.err.println("SQL Error");
            err.printStackTrace(System.err);
        } catch (Exception e) {
            Log.info("Exception occured " + e.getMessage());
            e.printStackTrace();
        }
        //Clean up the environment
        finally {
            try {
                rs.close();
            } catch (Exception e) {

            }
            try {
                s.close();
            } catch (Exception e) {

            }

            try {
                conn.close();
            } catch (Exception e) {

            }
        }
        return results;


    }

    /**
     * Get Column Names based on Query
     */
    public List<String> getColumnNames(String query){
        List<String> columns = new ArrayList<String>();
        ResultSet rs = null;
        Statement s = null;

        try{
            System.out.println("Connecting to Database...");
            getDBConnection();
            System.out.println("Database Connected. ");
            s= conn.createStatement();
            rs= s.executeQuery(query);
            System.out.println("Query Executed. ");
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount= rsmd.getColumnCount();
            if (colCount>0){
                for (int i=1; i<=colCount; i++){
                    columns.add(rsmd.getColumnName(i));
                }
            }
        }catch (SQLException err){
            Log.info("SQLException occured "+ err.getMessage());
            System.err.println("SQL Error");
            err.printStackTrace(System.err);
        }catch (Exception e){
            Log.info("Excetion occured "+ e.getMessage());
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
            }catch (Exception e){

            }
            try {
                s.close();
            }catch (Exception e){

            }
            try {
                conn.close();
            }catch (Exception e){

            }
        }
        return columns;
    }

    /**
     * Get Schemas by Connection
     * @param conn
     * @return
     */

    private static List<String> getSchemas(Connection conn){
        List<String> schemasList = new ArrayList<String>();

        try{
            String tableSchema = null;
            DatabaseMetaData md = conn.getMetaData();
            ResultSet schemas = md.getSchemas();
            while (schemas.next()){
                tableSchema = schemas.getString(1);
                schemasList.add(tableSchema);
            }
        }catch (SQLException e){
            Log.info("SQLException occured "+ e.getMessage());
            String theError = e.getSQLState();
            System.out.println("Can't query DB metadata: "+ theError);
            //System.exit(1);
        }catch (Exception e){
            Log.info("Exception occured "+e.getMessage());
            e.printStackTrace();
        }
            return schemasList;
    }

    /**
     * Get Table of connections and Table Schema
     * @return: List
     */

    private static List<String> getTables(Connection conn, String tableSchema){
        List<String> tableNames = new ArrayList<String>();

        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, tableSchema, "%", null);
            while (rs.next()){
                tableNames.add(rs.getString("TABLE_NAME"));  //or getString(3)
            }
        }catch (SQLException e){
            Log.info(" SQLException occured "+ e.getMessage());
            String theError = e.getSQLState();
            System.out.println(" Can't query DB metadata "+ theError);
            //System.exit(1);
        }catch (Exception e){
            Log.info(" Exception Occured "+ e.getMessage());
            e.printStackTrace();
        }
        return tableNames;
    }
}

