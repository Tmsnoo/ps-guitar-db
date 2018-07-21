package com.guitar.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.Location;
@Repository
public interface LocationJPARepository extends JpaRepository<Location, Long> {

}
