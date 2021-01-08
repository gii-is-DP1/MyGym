package org.springframework.samples.petclinic.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@SuppressWarnings("serial")
	private static final Map<String, Collection<String>> PERMISSIONS = new HashMap<String, Collection<String>>() {{

		put("assign-workouts", Collections.unmodifiableCollection(Arrays.asList("admin")));

		put("view-users-workouts", Collections.unmodifiableCollection(Arrays.asList("admin")));
		
	}};
	
	public static boolean isAllowedTo(String action, Collection<SimpleGrantedAuthority> authorities) {
		if (!PERMISSIONS.containsKey(action)) {
			return false;
		}
		Collection<String> permissions = PERMISSIONS.get(action);
		
		return authorities.stream().anyMatch(authority -> permissions.contains(authority.getAuthority()));
	}

	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**","/webjars/**","/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/","/oups").permitAll()
				.antMatchers("/users/new").permitAll()
				.antMatchers("/usuarios").permitAll()
				// exercises
				.antMatchers("/exercises").hasAnyAuthority("admin")
				.antMatchers("/exercises/**").hasAnyAuthority("admin")
				// trainings
				.antMatchers("/trainings").hasAnyAuthority("admin")
				.antMatchers("/trainings/**").hasAnyAuthority("admin")
				// workouts
				.antMatchers("/workouts").authenticated()
				.antMatchers("/workouts/assign").hasAnyAuthority("admin")
				.antMatchers("/workouts/**").authenticated()
				
				.antMatchers("/products").hasAnyAuthority("admin")
				.antMatchers("/products/**").hasAnyAuthority("admin")
				.antMatchers("/usuarios/**").permitAll()
				.antMatchers("/salas").permitAll()
				.antMatchers("/salas/**").permitAll()
				.antMatchers("/actividades").permitAll()
				.antMatchers("/actividades/**").permitAll()
				.antMatchers("/admin/**").hasAnyAuthority("admin")
				.antMatchers("/vets/**").authenticated()
				.anyRequest().denyAll()
				.and()
				 	.formLogin()
				 	/*.loginPage("/login")*/
				 	.failureUrl("/login-error")
				.and()
					.logout()
						.logoutSuccessUrl("/"); 
                // Configuración para que funcione la consola de administración 
                // de la BD H2 (deshabilitar las cabeceras de protección contra
                // ataques de tipo csrf y habilitar los framesets si su contenido
                // se sirve desde esta misma página.
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
	      .dataSource(dataSource)
	      .usersByUsernameQuery(
	       "select username,password,enabled "
	        + "from users "
	        + "where username = ?")
	      .authoritiesByUsernameQuery(
	       "select username,type "
	        + "from users "
	        + "where username = ?")	      	      
	      .passwordEncoder(passwordEncoder());	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {	    
		PasswordEncoder encoder =  NoOpPasswordEncoder.getInstance();
	    return encoder;
	}
	
}


