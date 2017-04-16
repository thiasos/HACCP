package it.lsoft.haccp.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CarteFedelta implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3498210779842783863L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	private String barcode;
	@OneToMany(mappedBy="carta")
	private List<MovimentiFedelta> movimenti;

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String pin;
	private String email;

	 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public List<MovimentiFedelta> getMovimenti() {
		return movimenti;
	}

	public void setMovimenti(List<MovimentiFedelta> movimenti) {
		this.movimenti = movimenti;
	}

	 

}