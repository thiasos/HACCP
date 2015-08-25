package it.lsoft.haccp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Articoli {
	@Id
	private Integer id;
	private String ean;

	private Integer idFornitore;
	private String descrizione;
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public Integer getIdFornitore() {
		return idFornitore;
	}
	public void setIdFornitore(Integer idFornitore) {
		this.idFornitore = idFornitore;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	@Override
	public String toString() {
		return "Articoli [ean=" + ean + ", idFornitore=" + idFornitore + ", descrizione=" + descrizione + "]";
	}

}