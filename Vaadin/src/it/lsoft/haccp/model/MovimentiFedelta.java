package it.lsoft.haccp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MovimentiFedelta implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	@ManyToOne(fetch = FetchType.LAZY)
	private CarteFedelta carta;
	private String descrizione;
	private Integer punti;
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	 
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public CarteFedelta getCarta() {
		return carta;
	}
	public void setCarta(CarteFedelta carta) {
		this.carta = carta;
	}
	public Integer getPunti() {
		return punti;
	}
	public void setPunti(Integer punti) {
		this.punti = punti;
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
	 
	 
}