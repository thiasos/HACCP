package it.lsoft.haccp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@DiscriminatorValue("C")
@Entity
public class RegistroCarico extends Registri implements Serializable{
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
