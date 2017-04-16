package it.lsoft.haccp.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@DiscriminatorColumn(name = "TIPOREGISTRO", discriminatorType=DiscriminatorType.STRING)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Registri implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5504306354633271926L;
	public enum TipoRegistroEnum {
		C("Carico"), S("Scarico");
		private String desc;

		private TipoRegistroEnum(String desc) {
			this.desc = desc;
		}

		@Override
		public String toString() {
			return desc;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	@Temporal(TemporalType.DATE)
	private Date data;
 
 
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

	public abstract TipoRegistroEnum getTipoRegistro() ;
	public abstract List<ArticoliMagazzino> getMovimenti();

}