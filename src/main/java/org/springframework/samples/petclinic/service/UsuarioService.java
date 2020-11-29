package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Rate;
import org.springframework.samples.petclinic.model.UserType;
import org.springframework.samples.petclinic.model.Usuario;
import org.springframework.samples.petclinic.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Iterable<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
	
	@Transactional
	public void save(Usuario user) {
		usuarioRepository.save(user);
	}
	
	@Transactional
	public void delete(Usuario user) {
		usuarioRepository.delete(user);
	}
	
	@Transactional
	public Optional<Usuario> findUserById(int userId) {
		return usuarioRepository.findById(userId);
	}
	
	@Transactional(readOnly = true)
	public Collection<UserType> findUserTypes() throws DataAccessException {
		return usuarioRepository.findUserTypes();
	}
	
	@Transactional(readOnly = true)
	public Collection<Rate> findRates() throws DataAccessException {
		return usuarioRepository.findRates();
	}
}
