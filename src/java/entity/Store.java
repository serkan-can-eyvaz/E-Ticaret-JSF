/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.sql.Timestamp;

/**
 *
 * @author Demirr
 */

public class Store extends BaseEntity{
    private String name;
    private Seller seller;

    public Store(String name, Seller seller, Long id, Timestamp createdDate, Timestamp lastModifiedDate) {
        super(id, createdDate, lastModifiedDate);
        this.name = name;
        this.seller = seller;
    }

    public Store() {
    }

    public Store(String name, Seller seller) {
        this.name = name;
        this.seller = seller;
    }
    
}
