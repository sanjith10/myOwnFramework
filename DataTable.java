package com.core.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.*;
import java.security.KeyStore;
import java.util.*;

@SuppressWarnings("unused")
public class DataTable extends PublicVariables{
    public static String File = null;
    public String xlDataFileName;
    public String xlSheetName;
    public static Map<String, String> RowData = new HashMap<String, String>();
    private String path;
    public FileInputStream inputStream = null;
    public FileOutputStream outputStream= null;
    public  String hname, value;
    public int i, j;
    public File file;
    org.apache.poi.ss.usermodel.Workbook workbook;
    org.apache.poi.ss.usermodel.Sheet sheet;
    private static final Logger Log = LogManager.getLogger(DriverFactory.class);


    public DataTable() throws FileNotFoundException {

    }
    /**
     * Description: Constructor which has Excel file for next operations
     */
    public DataTable(String xlPath) throws FileNotFoundException {
        this.path= xlPath;
        this.file= new File(path);

        try {
            inputStream = new FileInputStream(path);
            if (path.split("\\.")[1].equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook(inputStream);
            }else if (path.split("\\.")[1].equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }
            sheet = workbook.getSheetAt(0);
            inputStream.close();
        }catch (IOException e){
            Log.info("IOException occured "+e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Constructor which has Excel file for next operations
     */
    public DataTable(String xlPath, String sheetName) throws IOException{
        this.path= xlPath;
        this.file= new File(path);
        try {
            inputStream = new FileInputStream(path);
            if(path.split("\\.")[1].equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook(inputStream);

            }else if (path.split("\\.")[1].equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }
            sheet = workbook.getSheet(sheetName);
            inputStream.close();
        }catch (FileNotFoundException e){
            Log.info("FileNotFoundException occured "+ e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Excel Data Management
     */
public DataTable(File file, Workbook workbook, Sheet sheet) throws IOException {
    inputStream = new FileInputStream(file);
    this.path= file.toString();
    String fileExtensionName = path.split(".")[1];
    if (fileExtensionName.equalsIgnoreCase("xlsx")) {
    //if (path.split("\\.")[1].equalsIgnoreCase("xlsx")){
       workbook = new XSSFWorkbook(inputStream);
        }else if(fileExtensionName.equalsIgnoreCase("xls")) {
            //(path.split("\\.")[1].equalsIgnoreCase("xls")){
        workbook = new HSSFWorkbook(inputStream);
    }
    this.file=file;
    this.workbook= workbook;
    this.sheet = sheet;
    if (inputStream!=null){
        inputStream.close();
    }
    }

    /**
     * This method implements exporting of HTML to excel functionality
     */
@SuppressWarnings({"resource", "unused"})
    public void exportHtmlToExcel(String htmlFilePath, String excelFilePath, String... sheetName) throws IOException {
    File file = new File(excelFilePath);
    if (file.exists()) {
        file.delete();
    }

    DataTable dataTable = new DataTable();
    dataTable.createWorkBook(excelFilePath);

    if (sheetName.length > 0) {
        dataTable.setFileAndSheet(excelFilePath, sheetName[0]);
    } else {
        dataTable.setFileAndSheet(excelFilePath);
    }
    file = new File(htmlFilePath);
    FileReader fileReader = new FileReader(file);
    BufferedReader br = new BufferedReader(fileReader);
    StringBuilder sb = new StringBuilder();

    String line = br.readLine();
    while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
    }

    String everything = sb.toString();
    System.out.println(everything);

    Document html = Jsoup.parse(everything);
    Elements elmnts = html.getElementsByTag("table");

    int rowNum = 0;
    int colNum = 0;
    String cellValue = null;
    for (Element elmnt : elmnts) {
        Elements tbodyElements = elmnt.getElementsByTag("tbody");
        for (Element tbodyElmnt : tbodyElements) {

            Elements rowElements = tbodyElmnt.getElementsByTag("tr");
            for (Element rowElmnt : rowElements) {
                rowNum = rowNum + 1;
                colNum = 1;
                Elements colElements = rowElmnt.getElementsByTag("td");
                for (Element colElmnt : colElements) {
                    cellValue = colElmnt.ownText();
                    //System.out.println("Row, Column, CellValue"+rowNum+", "+colNum+", "+cellValue);
                    if (cellValue != null) {
                        dataTable.setCellData(rowNum, colNum, cellValue);
                        //System.out.println(cellValue);
                        colNum = colNum + 1;
                        cellValue = null;
                    }
                }
            }
        }
    }

}

    /**
     * This method implements the functionality of getting Column Data
     * @param ColumnName
     * @param startRow
     * @return
     */

    public List<String> getColumnData (String ColumnName,int...startRow){
        Row row = null;
        int colNo = getColumnNumber(ColumnName, startRow);
        List<String> actVals = new ArrayList<String>();


        if (startRow.length>0){
            row = sheet.getRow(startRow[0]-1);
        }else {
            row= sheet.getRow(0);
        }

        boolean runFlag = false;
        if (colNo != -1){
            for (Row rows: sheet){
                if (!runFlag){
                    if (rows != row){
                        continue;
                    }
                }
                row = rows;
                Cell c = rows.getCell(colNo-1);

                if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK){
                    //Nothing in the cell in this row, skip it
                }else {
                    actVals.add(c.toString());
                }
                runFlag = true;
            }
        }
        return actVals;

    }

    /**
     * Getting Column Data by Column Name
     * @param ColumnName
     * @return
     */

    public List<String> getColumnData(String ColumnName){
        Row row = null;
        int colNo = getColumnNumber(ColumnName, 1);
        List<String> actVals = new ArrayList<String>();

        row = sheet.getRow(0);
        boolean runFlag = false;
        if (colNo != -1){
            for (Row rows: sheet){
                if (!runFlag){
                    if (rows != row){
                        continue;
                    }
                }
                row = rows;
                Cell c= rows.getCell(colNo-1);

                if (c == null || c.getCellType() == Cell.CELL_TYPE_BLANK){
                    //Nothing in this row, skip it
                }else {
                    actVals.add(c.toString());
                }
                runFlag = true;
            }
        }
        return actVals;
    }

    /**
     * Getting Row Data by using Filepath, Sheet Name and TCID
     * @param xlPath
     * @param SheetName
     * @param TCID
     * @throws InvalidFormatException
     * @throws IOException
     */
    public static void getRowData(String xlPath, String SheetName, String TCID) throws InvalidFormatException, IOException{
        FileInputStream inputStream = null;
        String hname;
        String value;
        File file;
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        int rowNum, colNum, i, j;
        tcRowData.clear();

        try{
            file = new File(xlPath);
            inputStream = new FileInputStream(file);

        }catch (FileNotFoundException e){
            System.out.println("File Not Found in the specified path");
            Log.info("FileNotFoundException occured "+ e.getMessage());
            e.printStackTrace();
        }


            try{
            int rownum = 0;
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheet(SheetName);
            rowNum = sheet.getLastRowNum()+1;
            colNum = sheet.getRow(0).getLastCellNum();
            for (i=1; i<rowNum; i++){
                XSSFRow row = sheet.getRow(i);
                j=0;

                    XSSFCell cell = row.getCell(j);
                    value= cell.toString();
                    if (value.equalsIgnoreCase("TCID")){
                        int rowTestCase= row.getRowNum();
                        rownum = rowTestCase;
                        break;

                    }


            }
            XSSFCell cell = null;
            for (i=0; i<colNum; i++){
                cell = sheet.getRow(rownum).getCell(i);
                if (cell!= null){
                    value = cell.toString();
                    hname= sheet.getRow(0).getCell(i).toString();
                    tcRowData.put(hname, value);

                }else {
                    value = "";
                    hname = sheet.getRow(0).getCell(i).toString();
                    tcRowData.put(hname, value);
                }
            }

            }catch (IOException e){
            Log.info("IOException Occured "+e.getMessage());
            e.printStackTrace();
            }

    }

    /**
     * Getting Row Count by using Sheet Name
     * @param sheetName
     * @return
     */

public int getRowCount(String sheetName){
        int index = workbook.getSheetIndex(sheetName);
        if (index== -1){
            return -1;
        }else {
            sheet= workbook.getSheetAt(index);
            int number = sheet.getLastRowNum()+1;
            return number;
        }
}

    /**
     * Setting File from given filePath
     * @param filePath
     * @throws IOException
     */
    public void setFile(String filePath) throws IOException{
        inputStream = null;
        workbook = null;
        file = null;
        this.path = filePath;


        try{
            file = new File(filePath);
            inputStream = new FileInputStream(file);
        }catch (FileNotFoundException e){
            System.out.println("File Not found in the specified path");
            Log.info("FileNotFoundException occured "+ e.getMessage());
            e.printStackTrace();

        }

        try {
            if (path.split("\\.")[1].equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook(inputStream);
            }else if (path.split("\\.")[1].equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }

            inputStream.close();

        }catch (IOException e){
            Log.info("IOException occured "+ e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * This method is used for creating workbook from given filepath
     * @param filePath
     * @return
     * @throws IOException
     */


    public Workbook createWorkBook(String filePath) throws IOException{
        inputStream = null;
        workbook = null;
        sheet = null;
        file = null;
        this.path= filePath;
        file = new File(filePath);
        String fileExtensionName = path.substring(path.indexOf("."));
        System.out.println("\n"+fileExtensionName);
        if (file.exists()){
            inputStream = new FileInputStream(file);

        }else{
            if (fileExtensionName.equalsIgnoreCase(".xlsx")){
            //if (path.split("\\.")[1].equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook();
            }else if (fileExtensionName.equalsIgnoreCase(".xls")){
                //if (path.split("\\.")[1].equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook();
            }
           // inputStream.close();
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream = new FileInputStream(file);
        }

        if (fileExtensionName.equalsIgnoreCase(".xlsx")){//if(path.split("\\")[1].equalsIgnoreCase("xlsx")){
            workbook = new XSSFWorkbook(inputStream);
        }else if (fileExtensionName.equalsIgnoreCase(".xls")){//if (path.split("\\.")[1].equalsIgnoreCase("xls")){

            workbook = new HSSFWorkbook(inputStream);
        }
        if (workbook.getNumberOfSheets()==0)

            sheet = workbook.createSheet();
        inputStream.close();
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        if (file.exists()){
            return workbook;
        }else {
            return null;
        }

    }

    /**
     * This method Creates WorkBook from filepath & sheetName
     * @param filePath
     * @param sheetName
     * @return
     * @throws IOException
     */
    public Workbook createWorkBook(String filePath, String sheetName) throws IOException, InvalidFormatException {


        inputStream = null;
        workbook = null;
        //sheet = null;
        file = new File(filePath);
        this.path= file.toString();
        String fileExtensionName = filePath.substring(filePath.indexOf("."));

        if (file.exists()){
            inputStream = new FileInputStream(file);
        }else  {
            if (fileExtensionName.equalsIgnoreCase(".xlsx")) {
            //if (path.split(".")[1].equalsIgnoreCase("xlsx")){
                //workbook= WorkbookFactory.create(file);
                workbook = new XSSFWorkbook();

            }else if (fileExtensionName.equalsIgnoreCase(".xls")){ //(path.split(".")[1].equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook();
            }
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream = new FileInputStream(file);

        }
        if (fileExtensionName.equalsIgnoreCase(".xlsx")){//if(path.split("\\")[1].equalsIgnoreCase("xlsx")){
            //workbook= WorkbookFactory.create(file);
            workbook = new XSSFWorkbook(inputStream);
        }else if (fileExtensionName.equalsIgnoreCase(".xls")){//if (path.split("\\.")[1].equalsIgnoreCase("xls")){

            //workbook= WorkbookFactory.create(file); //
            workbook = new HSSFWorkbook(inputStream);
        }

        /*if (path.split(".")[1].equalsIgnoreCase("xlsx")){
            workbook = new XSSFWorkbook(inputStream);
        }else if(path.split(".")[1].equalsIgnoreCase("xls")){
            workbook= new HSSFWorkbook(inputStream);
        }*/
        DataTable dts = new DataTable(file, workbook, sheet);
        dts.createSheet(sheetName);

        inputStream.close();
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        if(file.exists()){
            return workbook;
        }else {
            return null;
        }

    }

    /**
     * This method is for creating excel Sheet from given sheetName
     * @param sheetName
     * @return
     * @throws IOException
     */
    public Sheet createSheet(String sheetName) throws IOException, NullPointerException{
        int shtCnt = workbook.getNumberOfSheets();
        Boolean blnFlag;
        if(shtCnt>0){
            for (int i=0; i<shtCnt; i++){
                blnFlag = true;
                for (int j=0; j< shtCnt; j++){
                    if (workbook.getSheetName(j).equalsIgnoreCase(sheetName)){
                        blnFlag = false;
                        break;
                    }
                }
                if (blnFlag){
                    sheet = workbook.createSheet(sheetName);
                    break;
                }else {
                    break;
                }
            }
        }else if (shtCnt==0){
            sheet = workbook.createSheet(sheetName);
        }
        sheet = workbook.getSheet(sheetName);
        Row row = sheet.createRow(0);
        row.createCell(0);

        try{
            if (inputStream!=null){
                inputStream.close();
            }
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

        }catch (IOException e){
            Log.info("IOException occured "+e.getMessage());
            e.printStackTrace();
        }
        return sheet;

    }

    /**
     * This Method saves the File & Closes the outputStream
     * @param filePath
     * @throws IOException
     */

    public void saveAs(String filePath) throws IOException {
        try {
            outputStream = new FileOutputStream(new File(filePath));
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

        }catch (FileNotFoundException e){
            Log.info("FileNotFoundException occured "+e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Closing WorkBook
     */

    public void closeWorkBook(){
        try {
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            Log.info("IOException occured "+ e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * Setting Sheet by using Sheet Name
     * @param sheetName
     * @return
     */


    public Sheet setSheet(String sheetName){
        sheet = workbook.getSheet(sheetName);
        return sheet;

    }

    /**
     * Getting Sheet Name
     * @return
     */

    public String getSheetName(){
        return sheet.getSheetName();

    }

    /**
     * this method is for setting file and sheet from filepath & sheetName
     * @param filePath
     * @param sheetName
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public void setFileAndSheet(String filePath, String... sheetName) throws IOException {
        try {
            inputStream = new FileInputStream(filePath);
            this.path= filePath;
            if (path.split("\\.")[1].equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook(inputStream);
            }else if (path.split("\\.")[1].equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }
            if(sheetName.length>0){
                sheet = workbook.getSheet(sheetName[0]);
            }else {
                for (int i=0; i<workbook.getNumberOfSheets(); i++){
                    sheet = workbook.getSheet(workbook.getSheetName(i));
                    break;
                }
            }
            File file = new File(filePath);
            DataTable dts = new DataTable(file, workbook, sheet);
            inputStream.close();
        }catch (FileNotFoundException e){
            Log.info("FileNotFoundException occured "+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Getting Value from given Row Number and Column Number
     * @param rowNum
     * @param colNum
     * @return
     */
    public String value(int rowNum, int colNum) {
        int rownum = rowNum -1;
        int colnum = colNum -1;
        //Row row = sheet.getRow(rownum);
        //Cell cellValue = row.getCell(colnum);
        Cell cellValue = sheet.getRow(rownum).getCell(colnum);
        if (cellValue != null) {
            return cellValue.toString();
        } else {
            System.out.println("No Value at this position (" + rownum + ", " + colnum + ")");
            return null;
        }
    }

    //Below  4 methods are for driver script Execution
    private static XSSFWorkbook excelWorkbook;
    private static XSSFSheet excelSheet;
    private static XSSFCell cell;



    public static void setExcelFile(String path) throws IOException {
        FileInputStream excelFile = new FileInputStream(path);
        excelWorkbook = new XSSFWorkbook(excelFile);
    }



    public static String getCellData(int rowNum, int colNum, String sheetName){
         excelSheet= excelWorkbook.getSheet(sheetName);
         try{
             cell = excelSheet.getRow(rowNum).getCell(colNum);
             String cellData = cell.getStringCellValue();
             return cellData;
         }catch (Exception e){
             return "";
         }
    }

    public static int getRowCountOfTestExcel(String sheetName){
        excelSheet = excelWorkbook.getSheet(sheetName);
        int number = excelSheet.getLastRowNum();
                number = number+1;
        return number;

    }


    public void setCellDataToExcel(int rowNum, int colNum, String cellValue, String filePath ) throws IOException {
        int row = rowNum-1;
        int column = colNum-1;
        File file = new File(filePath);
        DataTable dts = new DataTable();
        //FileInputStream  inputStream;// =new FileInputStream(filePath);
        FileOutputStream outputStream;



        Row actRow = excelSheet.getRow(row);
        if (actRow==null){
            actRow= dts.addNewRow(row+1);
        }
        Cell actCell = actRow.getCell(column);

        if(actCell == null){
            try {
                actCell = actRow.createCell(column);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                Log.info("IllegalArgumentException occured "+e.getMessage());
                System.out.println("Column doesn't exists, exiting method ");
                return;
            }
        }
        actCell.setCellType(Cell.CELL_TYPE_STRING);
        actCell.setCellValue(cellValue);

        /**if (inputStream != null){
         inputStream.close();
         }*/


        outputStream = new FileOutputStream(file);
        excelWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return;

    }





    public static int getRowContains(String sTestCaseName, int colNum,String SheetName) throws Exception{
        int i;
        excelSheet = excelWorkbook.getSheet(SheetName);
        int rowCount = dataTable.getRowCount(SheetName);
        for (i=0 ; i<rowCount; i++){
            if  (dataTable.getCellData(i,colNum,SheetName).equalsIgnoreCase(sTestCaseName)){
                break;
            }
        }
        return i;
    }

    
    //This method is to get the count of the test steps of test case
    //This method takes three arguments (Sheet name, Test Case Id & Test case row number)
    public static int getTestStepsCount(String SheetName, String sTestCaseName, int iTestCaseStart) throws Exception{
        for(int i=iTestCaseStart;i<=dataTable.getRowCount(SheetName);i++){
            if(!sTestCaseName.equals(dataTable.getCellData(i, PublicVariables.col_TestCaseName, SheetName))){
                int number = i;
                return number;
            }
        }
        excelSheet = excelWorkbook.getSheet(SheetName);
        int number=excelSheet.getLastRowNum()+1;
        return number;
    }
    
    
    
    
    
    /**
     * Getting Row Data from given Row Number
     * @param rowNum
     * @return
     */

    public Map<String, String> getRowData(int rowNum){
        HashMap<String, String> obMap = new HashMap<String, String>();
        Row actRow = getRow(rowNum);
        if (actRow == null){
            return null;
        }
        int i = 0;
        Iterator<Cell> cellIterator = actRow.iterator();
        while (cellIterator.hasNext()){
            i= i+1;
            Cell cell = cellIterator.next();
            obMap.put("Cell value"+i, cell.getStringCellValue().toString());
        }
        return obMap;

    }

    /**
     * Getting Row Number by using CellValue and Column
     * @param cellValue
     * @param column
     * @return
     */

    public int getRowNumber(String cellValue, int column){
        int i=0;
        column = column-1;
        Iterator<Row> rowIterator= sheet.iterator();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            Cell cell = row.getCell(column);
            if (cell!= null){
                if (cell.toString().equalsIgnoreCase(cellValue)){
                    return i+1;
                }
            }
            i=i+1;
        }
        return  -1;

    }

    /**
     * Getting Row by given Cell Value and Column
     * @param cellValue
     * @param column
     * @return
     */

    public Row getRow(String cellValue, int column){

        column = column-1;
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            Cell cell = row.getCell(column);
            if (cell!= null){
                if(cell.toString().equalsIgnoreCase(cellValue)){
                    return row;
                }
            }
        }
        return null;

    }

    /**
     * Getting Row from Row Number
     * @param rowNum
     * @return
     */
    public Row getRow(int rowNum){
        int lastRowNum =sheet.getLastRowNum();
        if (rowNum-1 <= lastRowNum){
            return sheet.getRow(rowNum-1);
        }else {
            return null;
        }
    }

    /**
     * Getting Row by given cellValue
     * @param rowCellValue
     * @return
     * @throws IOException
     */
    public Row getRow(String rowCellValue) throws IOException{
        DataTable dts = new DataTable(file, workbook, sheet);
        int rowNumber = dts.getRowNumber(rowCellValue);
        return dts.getRow(rowNumber);
    }


    /**
     * Getting Row Number by rowCellValue
     * @param tCID
     * @return
     */

    public int getRowNumber(String tCID){
        int i = 0;
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()){
            Row row = rowIterator.next();
            Cell cell = row.getCell(0);

                if (cell.toString().equalsIgnoreCase(tCID)){
                    return i+1;
                }
                i=i+1;
            }


        return  -1;

    }

    /**
     * Get Column Number by cellValue and rowNumber
     * @param cellValue
     * @param rowNumber
     * @return
     */
    public int getColumnNumber(String cellValue, int rowNumber){
        int i = 0;
        rowNumber = rowNumber-1;
        Iterator<Cell> colIterator = sheet.getRow(rowNumber).cellIterator();
        while (colIterator.hasNext()){
            Cell cell = colIterator.next();
            if (cell.getStringCellValue().toString().equalsIgnoreCase(cellValue)){
                return  i+1;

            }
            i=i+1;
        }
        return -1;
    }


    /**
     * get Column by cellValue & rowNumber
     * @param cellValue
     * @param rowNumber
     * @return
     */

    public Cell getColumn(String cellValue, int rowNumber){
       int i= 0;
       rowNumber = rowNumber-1;
       Iterator<Cell> colIterator = sheet.getRow(rowNumber).cellIterator();
       while (colIterator.hasNext()){
           Cell cell = colIterator.next();
           if (cell.getStringCellValue().toString().equalsIgnoreCase(cellValue)){
               return  cell;
           }
           i = i+1;
       }
       return null;
    }


    /**
     * Getting Row Count of the Sheet
     * @return
     */
    public int getRowCount(){
        return sheet.getLastRowNum()+1;
    }


    /**
     * Getting Column Count by using Row Number and Sheet Name
     * @param rowNum
     * @param sheetName
     * @return
     * @throws IOException
     */
    public  int getColumnCount(int rowNum, String sheetName) throws IOException{
        DataTable dts = new DataTable(file, workbook, sheet);
        XSSFSheet sheet = (XSSFSheet)dts.setSheet(sheetName);
        return sheet.getRow(rowNum-1).getLastCellNum();
    }


    /**
     * Getting Column Count by using Sheet Name
     * @param sheetName
     * @return
     * @throws IOException
     */
    public int getColumnCount(String sheetName) throws IOException{
        DataTable dts = new DataTable(file, workbook, sheet);
        XSSFSheet sheet = (XSSFSheet)dts.setSheet(sheetName);
        return sheet.getRow(0).getLastCellNum();
}

    /**
     * Getting Column Count by using Row Number
     * @param row
     * @return
     */
    public int getColumnCount(int row){
        return sheet.getRow(row-1).getLastCellNum();

}

    /**
     * Getting Column Count
     * @return
     */
    public  int getColumnCount(){
        return sheet.getRow(0).getLastCellNum();
}

    /**
     * Getting Number of sheets by using workbook
     * @return
     */
    public int getSheetCount(){
        return workbook.getNumberOfSheets();
}

    /**
     * Getting Sheet Number by using Sheet Name
     * @param sheetName
     * @return
     */
    public int getSheetNumber(String sheetName){
        int i, shtCnt;
        shtCnt = getSheetCount();
        for (i=1; i<shtCnt; i++){
            if(workbook.getSheetAt(i-1).getSheetName().toString().equalsIgnoreCase(sheetName)){
                return  i;
            }
        }
        return -1;
}

    /**
     * Getting Column Number by Column Name
     * @param columnName
     * @return
     */

    public int getColumnNumber(String columnName){
        int i=0;
        Row row= sheet.getRow(0);
        Iterator<Cell> colIterator = row.cellIterator();
        while (colIterator.hasNext()){
            Cell cell = colIterator.next();
            if (cell.getStringCellValue().toString().equalsIgnoreCase(columnName)){
                return  i+1;
            }
            i=i+1;
        }
        return -1;
    }



    /**
     * Getting Column Number by Column Name & Row
     * @param columnName
     * @param row
     * @return
     */

    public int getColumnNumber(String columnName, int...row){
        int i=0;

        Row actRow = null;
        if(row.length>0){
            actRow = sheet.getRow(row[0]-1);
        }else {
            actRow = sheet.getRow(0);
        }

        Iterator<Cell> colIterator = actRow.cellIterator();
        while (colIterator.hasNext()){
            Cell cell = colIterator.next();
            if (cell.getStringCellValue().toString().equalsIgnoreCase(columnName)){
                return i+1;
            }
            i=i+1;
        }
        return -1;
    }


    /**
     * Adding New Row by using Row Number
     * @param rowNo
     * @return
     * @throws IOException
     */
    public Row addNewRow(int rowNo) throws IOException{
        boolean op = false;
        Row row = excelSheet.createRow(rowNo-1);
        Cell cell = row.createCell(0);
        cell.setCellValue("New Row");
        if(row!=null){
            op =true;
        }
        ;
        if (op) {
            return row;
        }else {
            return null;
        }
    }


    /**
     * Adding New Column to the sheet
     * @return
     * @throws IOException
     */
    public Cell addNewColumn() throws IOException{
        boolean op = false;
        DataTable dts = new DataTable(file, workbook, sheet);
        int cc = dts.getColumnCount();
        Cell cell = null;
        if (cc != -1){
            cell = sheet.getRow(0).createCell(cc);
        }
        if (cell != null){
            op = true;
        }
        if (inputStream != null){
            inputStream.close();

        }
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        if (op){
            return cell;
        }else {
            return null;
        }
    }


    /**
     * Adding New Column by using Row
     * @param row
     * @return
     * @throws IOException
     */
    public Cell addNewColumn(int row) throws IOException{
        boolean op = false;
        DataTable dts = new DataTable(file, workbook, sheet);
        int cc = dts.getColumnCount();
        Cell cell = null;

        if (cc!= -1){
            cell = sheet.getRow(row).createCell(cc+1);
        }else {
            cell = sheet.getRow(row).createCell(cc);
        }
        if (cell!= null){
            op= true;
        }
        if (inputStream != null){
            inputStream.close();
        }
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        if (op){
            return cell;
        }else {
            return null;
        }
    }



    /**
     *Setting Cell Data by using Row Number, Column Number and Cell Value
     * @param rowNum
     * @param colNum
     * @param cellValue
     */

    public void setCellData(int rowNum, int colNum, String cellValue ) throws IOException {
        int row = rowNum-1;
        int column = colNum-1;
        DataTable dts = new DataTable(file, workbook, sheet);


        Row actRow = excelSheet.getRow(row);
        if (actRow==null){
            actRow= dts.addNewRow(row+1);
        }
        Cell actCell = actRow.getCell(column);

        if(actCell == null){
            try {
                actCell = actRow.createCell(column);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                Log.info("IllegalArgumentException occured "+e.getMessage());
                System.out.println("Column doesn't exists, exiting method ");
                return;
            }
        }
        actCell.setCellType(Cell.CELL_TYPE_STRING);
        actCell.setCellValue(cellValue);

        if (inputStream != null){
            inputStream.close();
        }


        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return;

    }



    /**
     * Setting Cell Data by using Row, Column Name and Cell Value
     * @param row
     * @param column
     * @param cellValue
     * @return
     * @throws IOException
     */
    public boolean setCellData(int row, String column, String cellValue) throws IOException{
        row = row-1;
        DataTable dts = new DataTable(file, workbook, sheet);
        int colNum = dts.getColumnNumber(column);
        if (colNum != -1){
            colNum = colNum-1;
        }else {
            System.out.println("Column '" +column+ "' doesn't exists.");
            return false;
        }
        Row actRow = sheet.getRow(row);
        if (actRow == null){
            actRow = dts.addNewRow(row+1);
        }
        Cell actCell = actRow.getCell(colNum);
        if (actCell== null){
            try {
                actCell = actRow.createCell(colNum);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                Log.info("IllegalArgumentException occured "+e.getMessage());
                System.out.println("Column doesn't exists, exiting method ");
                return false;
            }

        }

        actCell.setCellType(Cell.CELL_TYPE_STRING);
        actCell.setCellValue(cellValue);

        if (inputStream != null){
            inputStream.close();
        }

        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return true;

}


    /**
     * Setting Cell Data by using RowCellText, Column, trgtColumn & CellValue
     * @param rowCellText
     * @param column
     * @param trgtColumn
     * @param cellValue
     * @throws IOException
     */
    public void setCellData(String rowCellText, int column, int trgtColumn, String cellValue) throws IOException{

        DataTable dts = new DataTable(file, workbook, sheet);
        Row actRow = dts.getRow(rowCellText, column);

        if (actRow == null){
            System.out.println("No such value "+rowCellText+" exists in column "+column);
            return;
        }
        Cell actCell = actRow.getCell(column-1);

        if(actCell == null){
            System.out.println("No such value "+rowCellText+" exists in column "+column);
            return;
        }

        Cell trgtCell = actRow.getCell(trgtColumn-1);

        if (trgtCell == null){
            try {
                trgtCell = actRow.createCell(trgtColumn-1);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                Log.info("IllegalArgumentException occured "+e.getMessage());
                System.out.println("Column doesn't exists, exiting method  ");
                return;
            }

            trgtCell.setCellValue(cellValue);

        }else {
            trgtCell.setCellType(Cell.CELL_TYPE_STRING);
            trgtCell.setCellValue(cellValue);
        }

        if (inputStream != null){
            inputStream.close();
        }

        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();



}


    /**
     * Setting Row Data by using Row and Row Data of Key Value pairs
     * @param row
     * @param rowData
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
public void setRowData(int row, Map<String, String> rowData) throws IOException{
        DataTable dts = new DataTable(file, workbook, sheet);
        Row actRow = dts.getRow(row);
        Cell cell = null;
        Iterator entries = rowData.entrySet().iterator();
        while (entries.hasNext()){

            Map.Entry thisEntry = (Map.Entry) entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            int colNum = dts.getColumnNumber(key.toString());

            if (colNum != -1) {
                cell = actRow.getCell(colNum);
                if (cell == null) {
                    cell = actRow.createCell(colNum);
                    cell.setCellValue(value.toString());

                } else {
                    cell.setCellValue(value.toString());

                }
            }else {
                System.out.println("No such Column exists in table ");
            }

        }
        if (inputStream != null){
            inputStream.close();
        }
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return;

}

    /**
     * Setting Row Data by using RowCellText, Row Data of Key & Value
     * @param rowCellText
     * @param rowData
     * @throws IOException
     */
    public void setRowData(String rowCellText, Map<String, String> rowData) throws IOException{
        DataTable dts = new DataTable(file, workbook, sheet);
        Row actRow = dts.getRow(rowCellText);
        Cell cell = null;

        Iterator entries = rowData.entrySet().iterator();

        while (entries.hasNext()){
            Map.Entry thisEntry = (Map.Entry)entries.next();
            Object key = thisEntry.getKey();
            Object value = thisEntry.getValue();
            int colNum = dts.getColumnNumber(key.toString());

            if (colNum != -1){
                dts.setCellData(rowCellText, 1, colNum, value.toString());
                cell = actRow.getCell(colNum);

               /* if (cell == null){
                    cell = actRow.createCell(colNum);
                    cell.setCellValue(value.toString());
                }else {
                    cell.setCellValue(value.toString());
                }*/
               cell = null;
            }else {
                System.out.println("No such Column exists in table ");
            }
        }

        if (inputStream != null){
            inputStream.close();
        }
        outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();


}


    /**
     * Setting File by using File Path
     * @param filePath
     */
    public void setFileAndSheet(String filePath) {
        inputStream = null;
        workbook = null;
        file = null;
        this.path = filePath;


        try{
            file = new File(filePath);
            inputStream = new FileInputStream(file);
        }catch (FileNotFoundException e){
            System.out.println("File Not found in the specified path");
            Log.info("FileNotFoundException occured "+ e.getMessage());
            e.printStackTrace();

        }

        try {
            if (path.split("\\.")[1].equalsIgnoreCase("xlsx")){
                workbook = new XSSFWorkbook(inputStream);
            }else if (path.split("\\.")[1].equalsIgnoreCase("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }
            sheet = workbook.getSheet(sheet.getSheetName());

            inputStream.close();

        }catch (IOException e){
            Log.info("IOException occured "+ e.getMessage());
            e.printStackTrace();
        }

    }




}
