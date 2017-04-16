package it.lsoft.haccp.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@DiscriminatorValue("C")
@Entity
public class RegistroCarico extends Registri implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5009601868638395871L;
	@OneToMany(mappedBy="registroCarico")
	private List<ArticoliMagazzino> movimenti;
	
	@Override
	public TipoRegistroEnum getTipoRegistro() {
		return TipoRegistroEnum.C;
	}

	public List<ArticoliMagazzino> getMovimenti() {
		return movimenti;
	}

	public void setMovimenti(List<ArticoliMagazzino> movimenti) {
		this.movimenti = movimenti;
	}

}
