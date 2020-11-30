package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.service.UsuarioService;
import org.springframework.stereotype.Component;

@Component
public class UserTypeFormatter implements Formatter<UserType> {

	private final UsuarioService usuarioService;

	@Autowired
	public UserTypeFormatter(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public String print(UserType usuarioService, Locale locale) {
		return usuarioService.getName();
	}

	@Override
	public UserType parse(String text, Locale locale) throws ParseException {
		Collection<UserType> findUserTypes = this.usuarioService.findUserTypes();
		for (UserType userType : findUserTypes) {
			if (userType.getName().equals(text)) {
				return userType;
			}
		}
		throw new ParseException("usertype not found: " + text, 0);
	}

}