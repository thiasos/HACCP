package it.lsoft.haccp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class VCaricoStampa implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1328302720070553661L;
	@Id
	@Column(name = "ID")
	private Integer id;
	private String fornitoriNome;
	private String articoliDescrizione;
	private java.lang.Boolean articolimagazzinoConformita;
	private java.sql.Date articolimagazzinoDatascadenza;
	private String articolimagazzinoLotto;
	private String articolimagazzinoTemperatura;
	private java.lang.Boolean articolimagazzinoTrasporto;
	private java.lang.Boolean articolimagazzinoValidita;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFornitoriNome() {
		return fornitoriNome;
	}
	public void setFornitoriNome(String fornitoriNome) {
		this.fornitoriNome = fornitoriNome;
	}
	public String getArticoliDescrizione() {
		return articoliDescrizione;
	}
	public void setArticoliDescrizione(String articoliDescrizione) {
		this.articoliDescrizione = articoliDescrizione;
	}
	public java.lang.Boolean getArticolimagazzinoConformita() {
		return articolimagazzinoConformita;
	}
	public void setArticolimagazzinoConformita(java.lang.Boolean articolimagazzinoConformita) {
		this.articolimagazzinoConformita = articolimagazzinoConformita;
	}
	public java.sql.Date getArticolimagazzinoDatascadenza() {
		return articolimagazzinoDatascadenza;
	}
	public void setArticolimagazzinoDatascadenza(java.sql.Date articolimagazzinoDatascadenza) {
		this.articolimagazzinoDatascadenza = articolimagazzinoDatascadenza;
	}
	public String getArticolimagazzinoLotto() {
		return articolimagazzinoLotto;
	}
	public void setArticolimagazzinoLotto(String articolimagazzinoLotto) {
		this.articolimagazzinoLotto = articolimagazzinoLotto;
	}
	public String getArticolimagazzinoTemperatura() {
		return articolimagazzinoTemperatura;
	}
	public void setArticolimagazzinoTemperatura(String articolimagazzinoTemperatura) {
		this.articolimagazzinoTemperatura = articolimagazzinoTemperatura;
	}
	public java.lang.Boolean getArticolimagazzinoTrasporto() {
		return articolimagazzinoTrasporto;
	}
	public void setArticolimagazzinoTrasporto(java.lang.Boolean articolimagazzinoTrasporto) {
		this.articolimagazzinoTrasporto = articolimagazzinoTrasporto;
	}
	public java.lang.Boolean getArticolimagazzinoValidita() {
		return articolimagazzinoValidita;
	}
	public void setArticolimagazzinoValidita(java.lang.Boolean articolimagazzinoValidita) {
		this.articolimagazzinoValidita = articolimagazzinoValidita;
	}
	public java.sql.Date getRegistriData() {
		return registriData;
	}
	public void setRegistriData(java.sql.Date registriData) {
		this.registriData = registriData;
	}
	private java.sql.Date registriData;

}