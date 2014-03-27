/*
 * Copyright 2014 msi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.jee.webapps.controller;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class ProductsBean {

        private String Reference;
        private String Designation;
        private int Quantite;
        private String Depot;
        
        public ProductsBean(String Reference, String Designation, int Quantite, String Depot) {
                this.Reference = Reference;
                this.Designation=Designation;
                this.Quantite = Quantite;
                this.Depot = Depot;
        }
        public ProductsBean(){
            
        }

        public String getReference() {
                return Reference;
        }

        public void setReference(String Reference) {
                this.Reference = Reference;
        }
        
        public String getDesignation() {
                return Designation;
        }

        public void setDesignation(String Designation) {
                this.Designation = Designation;
        }
        
         public int getQuantite() {
                return Quantite;
        }
        
         public void setQuantite(int Quantite) {
                this.Quantite = Quantite;
        }

        public String getDepot() {
                return Depot;
        }

        public void setDepot(String Depot) {
                this.Depot = Depot;
        }
}