package com.mongodb.jee.webapps.jaxrs;

import com.mongodb.BasicDBList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mongodb.jee.MongoHolder;
import com.mongodb.jee.PageResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import dojo.store.PageRangeRequest;
import java.io.File;
import java.io.FileInputStream;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

@Path("/")
public class JaxrsProductsService {

	@GET
	@Path("/init")
	public void initializeData() {
		DB db = MongoHolder.connect().getDB("ecommerce");
		DBCollection products = db.getCollection("products");
		DBObject product = null;
		for (int i = 0; i < 20; i++) {
			product = new BasicDBObject();
			product.put("title", "Product title [" + i + "]");
			product.put("description", "bla bla bla [" + i + "]");
			products.insert(product);
		}
	}

	@GET
	@Path("/products")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String findProducts(@HeaderParam("Range") PageRangeRequest range) throws UnknownHostException {
		
 
        return read();
//        return coll.find().toString();
                
//		return new PageResult(products.find(), range.getFromIndex(),
//				range.getToIndex());
	}
//        DBObject cells=null;
//        private DBObject read(){
//            try
//        {
//            FileInputStream file = new FileInputStream(new File("/home/msi/Bureau/inv-04-02-2014.xls"));
// 
//            //Create Workbook instance holding reference to .xlsx file
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//            //Get first/desired sheet from the workbook
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            JSONArray rows = new JSONArray();
//            //Iterate through each rows one by one
//            Iterator<Row> rowIterator = sheet.iterator();
//            while (rowIterator.hasNext()) 
//            {
//                Row row = rowIterator.next();
//                JSONObject jRow = new JSONObject();
//                cells = new BasicDBObject();
//                //For each row, iterate through all the columns
//                Iterator<Cell> cellIterator = row.cellIterator();
//                 
//                while (cellIterator.hasNext()) 
//                {
//                    Cell cell = cellIterator.next();
//                    //Check the cell type and format accordingly
//                    switch (cell.getCellType()) 
//                    {
//                        case Cell.CELL_TYPE_NUMERIC:
//                            cells.put("Quantite", cell.getNumericCellValue() );
//                            break;
//                        case Cell.CELL_TYPE_STRING:
//                            if (cell.getColumnIndex()==0){
//                            cells.put("Reference",cell.getStringCellValue());
//                            }else if(cell.getColumnIndex()==1)
//                                cells.put("Designation",cell.getStringCellValue() );
//                            else if(cell.getColumnIndex()==3)
//                                cells.put("Depot",cell.getStringCellValue() );
//                            break;
//                                
//                    }
//                }        
//            }
////             MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
//            
//            DB db = MongoHolder.connect().getDB("mydb");
//        DBCollection coll = db.getCollection("Collection1");
//        System.out.println("Count: " + coll.count());
//        
//        coll.insert(cells);
//            System.out.println(cells);
//            file.close();
//        } 
//        catch (Exception e) 
//        {
//            e.printStackTrace();
//        }
//            return cells;
//        }
        JSONObject json;
        JSONArray cells;
        JSONArray rows;
        private String read(){
            try
        {
            FileInputStream file = new FileInputStream(new File("/home/msi/Bureau/inv-04-02-2014.xls"));
 
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            // Start constructing JSON.
            json = new JSONObject();
            DB db = MongoHolder.connect().getDB("mydb");
            DBCollection coll = db.getCollection("Collection1");
            // Iterate through the rows.
            rows = new JSONArray();
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                JSONObject jRow = new JSONObject();
                cells = new JSONArray();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                 
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) 
                    {
//                        case Cell.CELL_TYPE_NUMERIC:
//
//                            cells.put( cell.getNumericCellValue() );
//                            break;
//                        case Cell.CELL_TYPE_STRING:
//
//                            cells.put( cell.getStringCellValue() );
//                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            jRow.put("Quantite", cell.getNumericCellValue() );
                            break;
                        case Cell.CELL_TYPE_STRING:
                            if (cell.getColumnIndex()==0){
                            jRow.put("Reference",cell.getStringCellValue());
                            }else if(cell.getColumnIndex()==1)
                                jRow.put("Designation",cell.getStringCellValue() );
                            else if(cell.getColumnIndex()==3)
                                jRow.put("Depot",cell.getStringCellValue() );
                            break;
                    }
//                    jRow.put( "cell", cells );
                    rows.put(jRow);
                }
                
//                json.put( "rows", rows );
                
            }
//            for()
            System.out.println(rows);
            coll.insert((List<DBObject>) rows);
            file.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
            return rows.toString();
        }
}
