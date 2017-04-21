package it.lsoft.haccp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import it.lsoft.haccp.data.RegistroCarico;

public interface RegistroCaricoRepository extends JpaRepository<RegistroCarico, Integer> {
	public int countByData(@Param("data") Date data);
	public List<RegistroCarico> findByData(@Param("data") Date data);
}
