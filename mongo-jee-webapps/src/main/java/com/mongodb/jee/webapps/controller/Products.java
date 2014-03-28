package com.mongodb.jee.webapps.controller;

import java.util.Date;

public class Products {

        private String REFERENCE;
        private String DESIGNATION;
        private String QUANTITE;
        private String DEPOT;
        
        public Products(String REFERENCE, String DESIGNATION, String QUANTITE, String DEPOT) {
                this.REFERENCE = REFERENCE;
                this.DESIGNATION = DESIGNATION;
                this.QUANTITE = QUANTITE;
                this.DEPOT = DEPOT;
        }

        public String getREFERENCE() {
                return REFERENCE;
        }

        public void setREFERENCE(String REFERENCE) {
                this.REFERENCE = REFERENCE;
        }

        public String getQuantite() {
                return QUANTITE;
        }

        public void setQuantite(String qauntite) {
                this.QUANTITE = qauntite;
        }

        public String getDESIGNATION() {
                return DESIGNATION;
        }

        public void setDESIGNATION(String DESIGNATION) {
                this.DESIGNATION = DESIGNATION;
        }

        public String getDepot() {
                return DEPOT;
        }

        public void setColor(String depot) {
                this.DEPOT = depot;
        }
}