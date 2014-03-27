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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.jee.webapps.controller.ProductsBean;
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
import com.mongodb.jee.webapps.controller.TableBean;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean
@SessionScoped
@Path("/")
public class JaxrsproductsserviceBean implements Serializable{
    DBObject jRow;
        JSONArray rows;
        DBCollection coll;
        private String f;
        DB db;
        private List<ProductsBean> products;
        
//    private File file;
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
        public JaxrsproductsserviceBean(){
            super();
            products = new ArrayList<ProductsBean>();
            populateRandomCars(products,  getSizeCursor());
        }
        public List<ProductsBean> getProducts() {
            
		return products;
	}
        
        private void populateRandomCars(List<ProductsBean> list, int size) {
		for(int i = 0 ; i < size ; i++)
			list.add(new ProductsBean(getReference(), getDesignation(), getQuantite(), getDepot()));
	}
        
	@GET
	@Path("/products")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PageResult findProducts(@HeaderParam("Range") PageRangeRequest range) throws UnknownHostException {
 try
        {
            FileInputStream file = new FileInputStream(new File("/home/msi/Bureau/inv-04-02-2014.xls"));
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            db = MongoHolder.connect().getDB("mydb");
            coll = db.getCollection("Collection1");
            // Iterate through the rows.
            rows = new JSONArray();
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) 
            {
                Row row = rowIterator.next();
                jRow = new BasicDBObject();
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
                coll.insert(jRow);
            }
            System.out.println(rows);

            file.close();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
return new PageResult(coll.find(), range.getFromIndex(),
				range.getToIndex());
//            return getFile();
	}
        public String getReference(){
            DBCursor cursorRef= coll.find();
            cursorRef.next().get("Reference");
            return cursorRef.toString();
        }
        public String getDesignation(){
            DBCursor cursorDes= coll.find();
            cursorDes.next().get("Designation");
            return cursorDes.toString();
        }
        public int getQuantite(){
            DBCursor cursorQ= coll.find();
            cursorQ.next().get("Quantite");
            int i;
            i = Integer.parseInt(cursorQ.toString());
            return i;
        }
        public String getDepot(){
            DBCursor cursorDep= coll.find();
            cursorDep.next().get("Depot");
            return cursorDep.toString();
        }
        public int getSizeCursor(){
            
            return coll.find().count();
        }
}
        
//    public String getFile() {
//           
//        return "";
//    
//    }

