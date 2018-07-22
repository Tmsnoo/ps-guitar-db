package com.guitar.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.Location;

@Repository
public interface LocationJPARepository extends JpaRepository<Location, Long> {
	List<Location> findByStateLike(String stateName);
	
	List<Location> findByStateOrCountry(String state, String Country);
	
	List<Location> findByStateAndCountry(String state , String country);
}
