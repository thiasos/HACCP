package it.lsoft.haccp.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "v_scarico")
public class VScaricoStampa implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1328302720070553661L;
	@Id
	@Column(name = "ID")
	private Integer id;
	private Integer cnt;
	private String articoliDescrizione;
	private String articolimagazzinoLotto;
	private java.sql.Date articolimagazzinoDatascadenza;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getArticoliDescrizione() {
		return articoliDescrizione;
	}

	public void setArticoliDescrizione(String articoliDescrizione) {
		this.articoliDescrizione = articoliDescrizione;
	}

	public String getArticolimagazzinoLotto() {
		return articolimagazzinoLotto;
	}

	public void setArticolimagazzinoLotto(String articolimagazzinoLotto) {
		this.articolimagazzinoLotto = articolimagazzinoLotto;
	}

	public java.sql.Date getRegistriData() {
		return registriData;
	}

	public void setRegistriData(java.sql.Date registriData) {
		this.registriData = registriData;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public java.sql.Date getArticolimagazzinoDatascadenza() {
		return articolimagazzinoDatascadenza;
	}

	public void setArticolimagazzinoDatascadenza(java.sql.Date articolimagazzinoDatascadenza) {
		this.articolimagazzinoDatascadenza = articolimagazzinoDatascadenza;
	}

	private java.sql.Date registriData;

}