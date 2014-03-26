package com.mongodb.jee.webapps.jaxrs;

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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;
import dojo.store.PageRangeRequest;
import java.io.File;
import java.io.FileInputStream;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
	}
        
        JSONArray rows;
        private String read(){
            try
        {
            FileInputStream file = new FileInputStream(new File("/home/msi/Bureau/inv-04-02-2014.xls"));
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
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
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) 
                    {

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
                    rows.put(jRow);
                }   
            }
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
        @ManagedBean
        @SessionScoped
        public class fileUploadController {
    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void upload() {
        if(file != null) {
            FacesMessage msg = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
}
