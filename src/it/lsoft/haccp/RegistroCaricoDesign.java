package it.lsoft.haccp;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.declarative.Design;

/** 
 * !! DO NOT EDIT THIS FILE !!
 * 
 * This class is generated by Vaadin Designer and will be overwritten.
 * 
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { � }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class RegistroCaricoDesign extends VerticalLayout {
	protected ComboBox idArticolo;
	protected TextField ean;
	protected DateField dataScadenza;
	protected TextField lotto;
	protected TextField temperatura;
	protected CheckBox conformita;
	protected CheckBox trasporto;
	protected CheckBox validita;
	protected Button btnAdd;
	protected Button btnPrint;
	protected Table tableMagazzino;

	public RegistroCaricoDesign() {
		Design.read(this);
	}
}
