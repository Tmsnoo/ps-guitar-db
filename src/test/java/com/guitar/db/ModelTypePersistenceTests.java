package com.guitar.db;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.ModelType;
import com.guitar.db.repository.ModelTypeJPARepository;
import com.guitar.db.repository.ModelTypeRepository;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelTypePersistenceTests {
	@Autowired
	private ModelTypeRepository modelTypeRepository;

	@Autowired
	private ModelTypeJPARepository modelTypeJPARepository;
	
	
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void testJPARepository() {
		//given
		List<ModelType> models = modelTypeJPARepository.findAll();
		//expected
		assertThat(models, notNullValue());
	}
	
	@Test
	public void testSaveAndGetAndDelete() throws Exception {
		ModelType mt = new ModelType();
		mt.setName("Test Model Type");
		mt = modelTypeJPARepository.save(mt);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		ModelType otherModelType = modelTypeJPARepository.findOne(mt.getId());
		assertThat(otherModelType.getName(),is("Test Model Type" ));
		
		modelTypeJPARepository.delete(otherModelType);
	}

	@Test
	public void testFind() throws Exception {
		ModelType mt = modelTypeRepository.find(1L);
		assertEquals("Dreadnought Acoustic", mt.getName());
	}
}
