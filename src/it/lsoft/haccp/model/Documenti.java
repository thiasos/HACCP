package it.lsoft.haccp.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Documenti {
	public enum TipoDocEnum {
		C, S
	}

	@Id
	private Integer id;
	private Date data;
	private TipoDocEnum tipoDoc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoDocEnum getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(TipoDocEnum tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

}