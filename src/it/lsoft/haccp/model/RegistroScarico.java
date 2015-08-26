package it.lsoft.haccp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@DiscriminatorValue("S")
@Entity
public class RegistroScarico extends Registri implements Serializable {

	@OneToMany(mappedBy = "registroScarico")
	private List<ArticoliMagazzino> movimenti;

	@Override
	public TipoRegistroEnum getTipoRegistro() {
		return TipoRegistroEnum.S;
	}

	public List<ArticoliMagazzino> getMovimenti() {
		return movimenti;
	}

	public void setMovimenti(List<ArticoliMagazzino> movimenti) {
		this.movimenti = movimenti;
	}
}
