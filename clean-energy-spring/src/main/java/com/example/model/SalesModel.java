package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sales_data")
public class SalesModel {

	@Id
	@Column(name ="product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long proudctId;
	
	private String productCategory;
	
	private String productName;
	
	private int productPrice;
	
	

	public SalesModel() {
		super();
	}

	public SalesModel(Long proudctId,String productCategory, String productName, int productPrice) {
		super();
		this.proudctId = proudctId;
		this.productCategory =productCategory;
		this.productName = productName;
		this.productPrice = productPrice;
	}

	public Long getProudctId() {
		return proudctId;
	}

	public void setProudctId(Long proudctId) {
		this.proudctId = proudctId;
	}
	
	public String getProductCategory() {
		return productCategory;
	}
	
	public void setProductCategory(String productCategory) {
		this.productCategory =productCategory;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	
	
	
}
