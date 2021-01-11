package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Fee;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class FeeTest {

	@Autowired
	protected UserService userService;
    
   /* @Test
	void shouldFindExerciseWithCorrectId() {
    	Exercise exercise = this.workoutService.findExerciseById(1);
    	
    	assertThat(exercise.getName()).startsWith("Abdominales");
    	assertThat(exercise.getType().getName()).isEqualTo("repetitive");
	}
    
    @Test
	void shouldFindAllExerciseTypes() {
    	Collection<ExerciseType> exerciseTypes = this.workoutService.findExerciseTypes();
    	
    	ExerciseType type1 = EntityUtils.getById(exerciseTypes, ExerciseType.class, 1);
    	ExerciseType type2 = EntityUtils.getById(exerciseTypes, ExerciseType.class, 2);
    	
		assertThat(type1.getName()).isEqualTo("temporary");
		assertThat(type2.getName()).isEqualTo("repetitive");
	}*/
    
    /*
     * H4+E1 Caso positivo de alta de usuario con cuota
     */
    @Test
	@Transactional
	public void shouldInsertUserIntoDatabaseWithFee() {
    	Collection<UserType> userTypes = this.userService.findUserTypes();
    	Collection<Rate> userRates = this.userService.findRates();
    	Fee fee = new Fee();
    	fee.setAmount(28.0);
    	fee.setStart_date(LocalDate.of(2020, 11, 29));
    	fee.setEnd_date(LocalDate.of(2020, 12, 29));
    	fee.setRate(userRates.stream().filter(ur -> ur.getId() == 2).findFirst().get());
    	User user = new User();
    	user.setNombre("Pepe");
    	user.setApellidos("Pérez");
    	user.setEmail("pepe@gmail.com");
    	user.setDni("12345678D");
    	user.setFecha_nacimiento(LocalDate.of(1990, 10, 10));
    	user.setFee(fee);
    	user.setUsername("pepeperez");
    	user.setType(userTypes.stream().filter(ut -> ut.getName().equals("client")).findFirst().get());
		
        try {
            this.userService.save(user);
        } catch (Exception ex) {
            Logger.getLogger(FeeTest.class.getName()).log(Level.SEVERE, null, ex);
        }

		Optional<User> searchResultUser = this.userService.findUser("pepeperez");
		assertThat(searchResultUser.orElse(null)).isNotNull();
	}
    
    /*
     * H4+E1 Caso negativo de alta de usuario con cuota
     */
    @Test
	@Transactional
	public void shouldInsertUserIntoDatabaseWithBadDateInFee() {
    	Collection<UserType> userTypes = this.userService.findUserTypes();
    	Collection<Rate> userRates = this.userService.findRates();
    	Fee fee = new Fee();
    	fee.setAmount(28.0);
    	fee.setEnd_date(LocalDate.of(2020, 11, 29));
    	fee.setStart_date(LocalDate.of(2020, 12, 29));
    	fee.setRate(userRates.stream().filter(ur -> ur.getId() == 2).findFirst().get());
    	User user = new User();
    	user.setNombre("Pepe");
    	user.setApellidos("Pérez");
    	user.setEmail("pepe@gmail.com");
    	user.setDni("12345678D");
    	user.setFecha_nacimiento(LocalDate.of(1990, 10, 10));
    	user.setFee(fee);
    	user.setUsername("pepeperez");
    	user.setType(userTypes.stream().filter(ut -> ut.getName().equals("client")).findFirst().get());
		
        try {
            this.userService.save(user);
        } catch (Exception ex) {
            Logger.getLogger(WorkoutServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }

		Optional<User> searchResultUser = this.userService.findUser("pepeperez");
		assertThat(searchResultUser.orElse(null)).isNotNull();
	}
    
   
}
