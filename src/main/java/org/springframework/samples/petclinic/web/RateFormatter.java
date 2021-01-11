package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class RateFormatter implements Formatter<Rate> {

	private final UserService usuarioService;

	@Autowired
	public RateFormatter(UserService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public String print(Rate usuarioService, Locale locale) {
		return usuarioService.getName();
	}

	@Override
	public Rate parse(String text, Locale locale) throws ParseException {
		Collection<Rate> findRates = this.usuarioService.findRates();
		for (Rate rate : findRates) {
			if (rate.getName().equals(text)) {
				return rate;
			}
		}
		throw new ParseException("rate not found: " + text, 0);
	}
}
