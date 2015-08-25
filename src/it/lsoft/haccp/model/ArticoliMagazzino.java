package it.lsoft.haccp.model;

import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ArticoliMagazzino {

	@Id
	private Integer id;
	private Integer idDocCarico;
	private Integer idDocScarico;
	private String eanArticolo;
	private String lotto;
	private Date dataScadenza;
	private Boolean conformita;
	private Boolean validita;
	private Double temperatura;
	private Boolean trasporto;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdDocCarico() {
		return idDocCarico;
	}

	public void setIdDocCarico(Integer idDocCarico) {
		this.idDocCarico = idDocCarico;
	}

	public Integer getIdDocScarico() {
		return idDocScarico;
	}

	public void setIdDocScarico(Integer idDocScarico) {
		this.idDocScarico = idDocScarico;
	}

	public String getEanArticolo() {
		return eanArticolo;
	}

	public void setEanArticolo(String eanArticolo) {
		this.eanArticolo = eanArticolo;
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

	public Boolean getConformita() {
		return conformita;
	}

	public void setConformita(Boolean conformita) {
		this.conformita = conformita;
	}

	public Boolean getValidita() {
		return validita;
	}

	public void setValidita(Boolean validita) {
		this.validita = validita;
	}

	public Double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}

	public Boolean getTrasporto() {
		return trasporto;
	}

	public void setTrasporto(Boolean trasporto) {
		this.trasporto = trasporto;
	}

}