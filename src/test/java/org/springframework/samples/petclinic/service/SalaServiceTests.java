/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Sala;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * SalaServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class SalaServiceTests {                
        @Autowired
	protected SalaService salaService;
        
    public static <T> Collection<T> iterableToCollection(Iterable<T> iterable) {
        	  Collection<T> collection = new ArrayList<>();
        	  iterable.forEach(collection::add);
        	  return collection;
      }
        
   @Test
   void shouldFindAll() {
	   Iterable<Sala> salasIt = this.salaService.findAll();
	   Collection<Sala> salas = iterableToCollection(salasIt);
	   
	   assertThat(salas.size()).isEqualTo(2);
   }
        
   
   	@Test
	@Transactional
	void shouldUpdateSala() {
   		Sala sala = this.salaService.findSalaById(2);
		
		String old = sala.getNombre();
		String newN = old + "X";
		
		sala.setNombre(newN);
		this.salaService.save(sala);
		
		sala = this.salaService.findSalaById(2);
		assertThat(sala.getNombre()).isEqualTo(newN);

		
	}
   	
   	@Test
	@Transactional
	void shouldDeleteSala() {
   		Iterable<Sala> salasIt = this.salaService.findAll();
   		Collection<Sala> salas = iterableToCollection(salasIt);
   		
   		Sala sala = EntityUtils.getById(salas, Sala.class, 1);
   		this.salaService.delete(sala);
   		
   		Iterable<Sala> resultIt = this.salaService.findAll();
   		Collection<Sala> result = iterableToCollection(resultIt);

   		assertThat(salas.size()).isEqualTo(result.size()+1);
		
	}
   	
   	@Test
   	void shouldFindSalaById() {
   		Sala sala = this.salaService.findSalaById(2);
   		assertThat(sala.getNombre()).isEqualTo("Calistenia");
   		
   		
   	}

 
   
  
   

	

}
