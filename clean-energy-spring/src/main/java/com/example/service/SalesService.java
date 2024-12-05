package com.example.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.SalesModel;
import com.example.repository.SalesModelRepository;

@Service
public class SalesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesService.class);

    @Autowired
    private SalesModelRepository salesRepo;
    
    
    
    public List<SalesModel> getSalesDataByProductCategory(String productCategory){
       
    	return salesRepo.findByProductCategory(productCategory);
    }
    
    public List<SalesModel> getAllSalesData(){
    	
    	return salesRepo.findAll();
    }

	public List<String> getProductCategories() {
	
		
		return salesRepo.getProductCategories();
	}
}
