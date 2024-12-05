package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.SalesModel;

@Repository
public interface SalesModelRepository extends JpaRepository<SalesModel, Long> {

	public List<SalesModel> findByProductCategory(String productCategory);
	
	@Query("SELECT DISTINCT productCategory from SalesModel")
	public List<String> getProductCategories();
}
