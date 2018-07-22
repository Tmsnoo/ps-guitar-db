package com.guitar.db;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Location;
import com.guitar.db.repository.LocationJPARepository;
import com.guitar.db.repository.LocationRepository;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationPersistenceTests {
	
	@Autowired
	LocationJPARepository locationJPARepository;

	@PersistenceContext
	private EntityManager entityManager;

	
	@Test
	public void testJpaRepositoryFind() {
		List<Location> location = locationJPARepository.findAll();
		assertThat(location, notNullValue()); 
	}
	
	@Test
	public void testJpaRepositoryFindByStateOrCountry() {
		//given
		List<Location> locations = locationJPARepository.findByStateOrCountry("Utah", "Utah");
		//when
		List <String>location = locations.stream().map(l -> l.getState()).collect(Collectors.toList()); 
		//then
		assertThat(location.get(0), is("Utah"));
	}
	@Test
	public void shouldFindByStateAndCountry() {
		//given
		List<Location> locations = locationJPARepository.findByStateAndCountry("Utah", "United States");
		//expected
		assertThat(locations.get(0).getCountry(), is("United States"));
	}
	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Location location = new Location();
		location.setCountry("Canada");
		location.setState("British Columbia");
		location = locationJPARepository.saveAndFlush(location);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		Location otherLocation = locationJPARepository.findOne(location.getId());
		assertThat(otherLocation.getCountry(),is("Canada"));
		assertEquals("British Columbia", otherLocation.getState());
		
		//delete BC location now
		locationJPARepository.delete(otherLocation);
	}

	@Test
	public void testFindWithLike() throws Exception {
		List<Location> locs = locationJPARepository.findByStateLike("New%");
		assertThat(locs, hasSize(4));
	}
	@Test
	public void testFindStartingWith() throws Exception {
		List<Location> locs = locationJPARepository.findByStateStartingWith("New");
		assertThat(locs, hasSize(4));
	}

	@Test
	@Transactional  //note this is needed because we will get a lazy load exception unless we are in a tx
	public void testFindWithChildren() throws Exception {
		Location arizona = locationJPARepository.findOne(3L);
		assertEquals("United States", arizona.getCountry());
		assertEquals("Arizona", arizona.getState());
		
		assertEquals(1, arizona.getManufacturers().size());
		
		assertEquals("Fender Musical Instruments Corporation", arizona.getManufacturers().get(0).getName());
	}
}
