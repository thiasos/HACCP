package it.lsoft.haccp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lsoft.haccp.data.ArticoliMagazzino;

public interface ArticoliMagazzinoRepository extends JpaRepository<ArticoliMagazzino, Integer> {
	
}