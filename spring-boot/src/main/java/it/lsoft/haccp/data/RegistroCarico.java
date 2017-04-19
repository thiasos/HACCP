package it.lsoft.haccp.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="IDFORNITORE")
	private Fornitori fornitore;
	@Column(name="DOCUMENT_NUMBER")
	private String documentNumber;
	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Fornitori getFornitore() {
		return fornitore;
	}

	public void setFornitore(Fornitori fornitore) {
		this.fornitore = fornitore;
	}

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
