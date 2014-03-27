package com.mongodb.jee.webapps.controller;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import com.mongodb.jee.webapps.jaxrs.*;
//import com.mongodb.jee.webapps.jaxrs.JaxrsProductsService;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//
//@ManagedBean
//@SessionScoped
//public class TableBean implements Serializable {
// 
//	private List<Products> products;
//	
//	public TableBean() {
//            super();
//		products = new ArrayList<Products>();
//		
//		populateRandomCars(products,  jrsPS.getSizeCursor());
//	}
//	
//	private void populateRandomCars(List<Products> list, int size) {
//		for(int i = 0 ; i < size ; i++)
//			list.add(new Products(getRef(), getDes(), getQT(), getDep()));
//	}
//	
//	public List<Products> getProducts() {
//		return products;
//	}
//        private String getRef() {
//		return jrsPS.getReference();
//	}
//	private String getDes() {
//		return jrsPS.getDesignation();
//	}
//	
//	private String getQT() {
//		return jrsPS.getQuantite();
//	}
//	
//	private String getDep() {
//		return jrsPS.getDepot();
//	}
//	
//	
//}
//                        

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mongodb.jee.webapps.controller.ProductsBean;
import com.mongodb.jee.webapps.jaxrs.JaxrsproductsserviceBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
@ManagedBean
@SessionScoped
public class TableBean extends JaxrsproductsserviceBean implements Serializable {

	

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

    private List<ProductsBean> filteredCars;

	private List<ProductsBean> carsSmall;

	public TableBean() {
            super();
            
		carsSmall = new ArrayList<ProductsBean>();

		populateRandomCars(carsSmall, super.getSizeCursor());
                populateRandomCars(carsSmall, 9);
        manufacturerOptions = createFilterOptions(manufacturers);
	}

	private void populateRandomCars(List<ProductsBean> list, int size) {
		for(int i = 0 ; i < size ; i++)
//			list.add(new ProductsBean(super.getReference(), super.getDesignation(), super.getQuantite(), super.getDepot()));
                list.add(new ProductsBean(null, null, 0, null));
	}

    public List<ProductsBean> getFilteredCars() {
        return filteredCars;
    }

    public void setFilteredCars(List<ProductsBean> filteredCars) {
        this.filteredCars = filteredCars;
    }

	public List<ProductsBean> getCarsSmall() {
		return carsSmall;
	}

	private int getRandomYear() {
		return (int)(Math.random() * 50 + 1960);
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