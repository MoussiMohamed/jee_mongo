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
import com.mongodb.jee.webapps.controller.Products;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.faces.model.SelectItem;
import org.codehaus.jettison.json.JSONException;

@ManagedBean
@SessionScoped
@Path("/")
public class JaxrsproductsserviceBean implements Serializable{
    DBObject jRow;
        JSONArray rows;
        DBCollection coll;
        private String f;
        DB db;
        private List<Products> products;
        
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
        public JaxrsproductsserviceBean() throws JSONException{
		carsSmall = new ArrayList<Products>();
		populateRandomCars(carsSmall,9);
//                System.out.println(getDesignation());
        manufacturerOptions = createFilterOptions(manufacturers);
        }
        public List<Products> getProducts() {
            
		return products;
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
        List<String> lstRef;
        String strRef;
             public List<String> getReference() throws JSONException{
            Map<DBObject, DBObject> map = new HashMap<DBObject, DBObject>();
            lstRef = new ArrayList<String>();
            db = MongoHolder.connect().getDB("mydb");
            coll = db.getCollection("Collection1");            
            BasicDBObject query = new BasicDBObject(); 
            // configure fields to be returned (true/1 or false/0 will work)
            // YOU MUST EXPLICITLY CONFIGURE _id TO NOT SHOW
            BasicDBObject fields = new BasicDBObject("Reference",true).append("_id",false);

            // do a query specifying the fields (and print results)
            DBCursor curs = coll.find(query, fields);
    
    while(curs.hasNext()) {
    DBObject o = curs.next();
    strRef = (String) o.get("Reference");
    lstRef.add(strRef);  
    }
            return lstRef;
  }
        String desString = null;
        List<String> list=null;
        public List<String> getDesignation() throws JSONException{
            Map<DBObject, DBObject> map = new HashMap<DBObject, DBObject>();
            list = new ArrayList<String>();
            db = MongoHolder.connect().getDB("mydb");
            coll = db.getCollection("Collection1");            
            BasicDBObject query = new BasicDBObject(); 
            // configure fields to be returned (true/1 or false/0 will work)
            // YOU MUST EXPLICITLY CONFIGURE _id TO NOT SHOW
            BasicDBObject fields = new BasicDBObject("Designation",true).append("_id",false);

            // do a query specifying the fields (and print results)
            DBCursor curs = coll.find(query, fields);
    
    while(curs.hasNext()) {
    DBObject o = curs.next();
    desString = (String) o.get("Designation");
    list.add(desString);  
    }
            return list;
  }
            
        Double doubleQt;
        List<Double> lstQt;
            public List<Double> getQuantite() throws JSONException{
            Map<DBObject, DBObject> map = new HashMap<DBObject, DBObject>();
            lstQt = new ArrayList<Double>();
            db = MongoHolder.connect().getDB("mydb");
            coll = db.getCollection("Collection1");            
            BasicDBObject query = new BasicDBObject(); 
            // configure fields to be returned (true/1 or false/0 will work)
            // YOU MUST EXPLICITLY CONFIGURE _id TO NOT SHOW
            BasicDBObject fields = new BasicDBObject("Quantite",true).append("_id",false);

            // do a query specifying the fields (and print results)
            DBCursor curs = coll.find(query, fields);
    
    while(curs.hasNext()) {
    DBObject o = curs.next();
    doubleQt = (Double) o.get("Quantite");
    lstQt.add(doubleQt);  
    }
            return lstQt;
  }
             String strDep;
             List<String> lstDep;
            public List<String> getDepot() throws JSONException{
            Map<DBObject, DBObject> map = new HashMap<DBObject, DBObject>();
            lstDep = new ArrayList<String>();
            db = MongoHolder.connect().getDB("mydb");
            coll = db.getCollection("Collection1");            
            BasicDBObject query = new BasicDBObject(); 
            // configure fields to be returned (true/1 or false/0 will work)
            // YOU MUST EXPLICITLY CONFIGURE _id TO NOT SHOW
            BasicDBObject fields = new BasicDBObject("Depot",true).append("_id",false);

            // do a query specifying the fields (and print results)
            DBCursor curs = coll.find(query, fields);
    
    while(curs.hasNext()) {
    DBObject o = curs.next();
    strDep = (String) o.get("Depot");
    lstDep.add(strDep);  
    }
            return lstDep;
  }
                          
        public int getSizeCursor(){
            db = MongoHolder.connect().getDB("mydb");
            coll = db.getCollection("Collection1");
            return coll.find().count();
        }
        
        private final static String[] colors;

	private final static String[] manufacturers;
        static {
		colors = new String[10];
		colors[0] = "Black";
		colors[1] = "White";
		colors[2] = "Green";
		colors[3] = "Red";
		colors[4] = "Blue";
		colors[5] = "Orange";
		colors[6] = "Silver";
		colors[7] = "Yellow";
		colors[8] = "Brown";
		colors[9] = "Maroon";

		manufacturers = new String[10];
		manufacturers[0] = "Mercedes";
		manufacturers[1] = "BMW";
		manufacturers[2] = "Volvo";
		manufacturers[3] = "Audi";
		manufacturers[4] = "Renault";
		manufacturers[5] = "Opel";
		manufacturers[6] = "Volkswagen";
		manufacturers[7] = "Chrysler";
		manufacturers[8] = "Ferrari";
		manufacturers[9] = "Ford";
	}
    private SelectItem[] manufacturerOptions;

    private List<Products> filteredCars;

	private List<Products> carsSmall;

	private void populateRandomCars(List<Products> list, int size) throws JSONException {
	           
            for(int i = 0 ; i < size ; i++)
			list.add(new Products(getReference().get(i), getDesignation().get(i), getQuantite().get(i).toString(), getDepot().get(i)));
	}

    public List<Products> getFilteredCars() {
        return filteredCars;
    }

    public void setFilteredCars(List<Products> filteredCars) {
        this.filteredCars = filteredCars;
    }

	public List<Products> getCarsSmall() {
		return carsSmall;
	}

	private int getRandomYear() {
		return (int) (Math.random() * 50 + 1960);
	}

	private String getRandomColor() {
		return colors[(int) (Math.random() * 10)];
	}

	private String getRandomManufacturer() {
		return manufacturers[(int) (Math.random() * 10)];
	}

	private String getRandomModel() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

    private SelectItem[] createFilterOptions(String[] data)  {
        SelectItem[] options = new SelectItem[data.length + 1];

        options[0] = new SelectItem("", "Select");
        for(int i = 0; i < data.length; i++) {
            options[i + 1] = new SelectItem(data[i], data[i]);
        }

        return options;
    }

    public SelectItem[] getManufacturerOptions() {
        return manufacturerOptions;
    }
        
}
        
//    public String getFile() {
//           
//        return "";
//    
//    }

