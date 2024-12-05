package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.APIMessageResponse;
import com.example.model.SalesModel;
import com.example.service.SalesService;

@RestController
public class SalesController {

	@Autowired
	private SalesService salesService;
	
	
	@GetMapping(value="/sales")
	public ResponseEntity getAllSalesData(){
	
		List<SalesModel> salesData = salesService.getAllSalesData();
		if(salesData==null || salesData.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new APIMessageResponse("No Sales Data!!"));
		else
			return ResponseEntity.status(HttpStatus.OK).body(salesData);
	}
	
	@GetMapping(value="/product_categories")
	public ResponseEntity getProductsCategories() {
		
		List<String> productCategories = salesService.getProductCategories();
		
		if(productCategories==null || productCategories.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new APIMessageResponse("No Product Categories"));
		else
			return ResponseEntity.status(HttpStatus.OK).body(productCategories);
		
	}
	
	@GetMapping(value="/sales_by_product")
	public ResponseEntity getSalesDataByProductCategory(@RequestParam String productCategory) {
		List<SalesModel> salesData = salesService.getSalesDataByProductCategory(productCategory);
		
		if(salesData==null || salesData.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new APIMessageResponse("No Sales Data!"));
		else
			return ResponseEntity.status(HttpStatus.OK).body(salesData);
		
	}
}
