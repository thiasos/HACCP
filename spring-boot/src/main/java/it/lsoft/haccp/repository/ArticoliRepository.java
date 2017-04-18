package it.lsoft.haccp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lsoft.haccp.data.Articoli;

public interface ArticoliRepository  extends JpaRepository<Articoli, Integer>{
	 
}