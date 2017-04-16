package it.lsoft.haccp.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Articoli implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -953634458114916933L;
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	private String ean;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="IDFORNITORE")
	private Fornitori fornitore;
	private String descrizione;
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	 
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Fornitori getFornitore() {
		return fornitore;
	}
	public void setFornitore(Fornitori fornitore) {
		this.fornitore = fornitore;
	}
	@Override
	public String toString() {
		return String.format("%s",getDescrizione());
	}
}