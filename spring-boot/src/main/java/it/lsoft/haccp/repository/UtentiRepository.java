package it.lsoft.haccp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lsoft.haccp.data.Utenti;

public interface UtentiRepository   extends JpaRepository<Utenti, String>  {

 

}