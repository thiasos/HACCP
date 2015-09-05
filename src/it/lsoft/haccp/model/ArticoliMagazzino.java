package it.lsoft.haccp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SqlResultSetMapping(name = "Etichette", entities = { @EntityResult(entityClass = Etichette.class, fields = {
		@FieldResult(name = "descrizione", column = "descrizione"),
		@FieldResult(name = "dataScadenza", column = "dataScadenza"),
		@FieldResult(name = "dataRegistro", column = "dataRegistro") }) })
@NamedNativeQuery(name = "Etichette", resultClass = Etichette.class, resultSetMapping = "Etichette", //
query = "SELECT haccp.articoli.`DESCRIZIONE` as descrizione,"+
	" haccp.articolimagazzino.`LOTTO` as lotto,"+
	" haccp.articolimagazzino.`DATASCADENZA` as dataScadenza,"+
	" haccp.registri.`DATA` as dataRegistro,"+
	" haccp.articolimagazzino.`ID` as id"+
" FROM haccp.articoli"+
	" INNER JOIN haccp.articolimagazzino ON "+
	" haccp.articolimagazzino.`ARTICOLO_ID` = haccp.articoli.`ID` "+
	" INNER JOIN haccp.registri ON "+
	" haccp.registri.`ID` = haccp.articolimagazzino.`REGISTROCARICO_ID` where haccp.articolimagazzino.`REGISTROCARICO_ID`  = :rid")
public class ArticoliMagazzino implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
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

	@Override
	public String toString() {
		return String.format("%s - %s - %s", articolo.toString(), getLotto(), getDataScadenza());
	}
}