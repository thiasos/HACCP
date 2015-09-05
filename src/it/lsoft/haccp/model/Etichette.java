package it.lsoft.haccp.model;

import java.sql.Date;

public class Etichette {
	private String descrizione;
	private String lotto;
	private Date dataScadenza;
	private Date dataRegistro;
	private Integer id;
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getLotto() {
		return lotto;
	}
	public void setLotto(String lotto) {
		this.lotto = lotto;
	}
	public Date getDataScadenza() {
		return dataScadenza;
	}
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	public Date getDataRegistro() {
		return dataRegistro;
	}
	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
