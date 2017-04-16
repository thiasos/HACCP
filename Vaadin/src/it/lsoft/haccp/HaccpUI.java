package it.lsoft.haccp;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.lang3.StringUtils;

import com.ejt.vaadin.loginform.DefaultVerticalLoginForm;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import it.lsoft.haccp.model.Utenti;

@SuppressWarnings("serial")
@Theme("haccp")
public class HaccpUI extends UI {
	public static final String PERSISTENCE_UNIT = "haccp";
	private final EntityManager utentiDS = JPAContainerFactory
			.createEntityManagerForPersistenceUnit(HaccpUI.PERSISTENCE_UNIT);

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = HaccpUI.class, widgetset = "it.lsoft.haccp.widgetset.HaccpWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		Utenti find2 = utentiDS.find(Utenti.class,"roberto");
		if (find2==null) {
			find2 = new Utenti();
			find2.setLogin("roberto");
			find2.setFirma("Ambrogio Roberto Alfonso");
			find2.setPassword("bruno.2867");
			utentiDS.persist(find2);
		}
		DefaultVerticalLoginForm loginForm = new DefaultVerticalLoginForm();
		setContent(loginForm);
		loginForm.addLoginListener(event -> {
			Utenti find = utentiDS.find(Utenti.class, event.getUserName());
			if (find!=null && StringUtils.equals(find.getPassword(), event.getPassword())) {
				setContent(new AppRootView(find));
				return;
			}
			loginForm.setComponentError(new UserError("Login Fallito")  );
			 
		});

	}

}