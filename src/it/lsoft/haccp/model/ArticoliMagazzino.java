package it.lsoft.haccp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
public class ArticoliMagazzino implements Serializable{

	@Id
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	private RegistroCarico registroCarico;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private RegistroScarico registroScarico;
	@ManyToOne(fetch = FetchType.LAZY)
	private Articoli articolo;
	private String lotto;
	@Temporal(TemporalType.DATE)
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
	public RegistroCarico getRegistroCarico() {
		return registroCarico;
	}

	public void setRegistroCarico(RegistroCarico registroCarico) {
		this.registroCarico = registroCarico;
	}

	public RegistroScarico getRegistroScarico() {
		return registroScarico;
	}

	public void setRegistroScarico(RegistroScarico registroScarico) {
		this.registroScarico = registroScarico;
	}

	public Articoli getArticolo() {
		return articolo;
	}

	public void setArticolo(Articoli articolo) {
		this.articolo = articolo;
	}

}