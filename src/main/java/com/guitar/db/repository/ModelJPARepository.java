package com.guitar.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.Model;
@Repository
public interface ModelJPARepository extends JpaRepository<Model, Long> {

}
