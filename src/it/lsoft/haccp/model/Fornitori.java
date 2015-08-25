package it.lsoft.haccp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Fornitori {
	@Id
	private Integer id;

	private String nome;
	private String indirizzo;
	private String telefono;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Fornitori [id=" + id + ", nome=" + nome + ", indirizzo=" + indirizzo + ", telefono=" + telefono + "]";
	}
 
}