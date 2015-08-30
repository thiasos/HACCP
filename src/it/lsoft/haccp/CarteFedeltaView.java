package it.lsoft.haccp;

<<<<<<< HEAD
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import it.lsoft.haccp.model.CarteFedelta;
import it.lsoft.haccp.model.MovimentiFedelta;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class CarteFedeltaView extends CarteFedeltaDesign implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5928556157312268110L;
	private static final String MYKEY = "download";
	private final JPAContainer<CarteFedelta> carteDS = JPAContainerFactory.make(CarteFedelta.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final JPAContainer<MovimentiFedelta> movimentiDS = JPAContainerFactory.make(MovimentiFedelta.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final BrowserWindowOpener opener = new BrowserWindowOpener(PrintUI.class);

	public CarteFedeltaView() {
		super();
		saldo.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		saldo.setReadOnly(true);
		table.setContainerDataSource(movimentiDS);
		table.setVisibleColumns(new Object[] { "data", "descrizione", "punti" });
		point.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		movimentiDS.addItemSetChangeListener(new ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				Integer saldoValue = 0;
				for (Object itemId : movimentiDS.getItemIds()) {
					saldoValue += movimentiDS.getItem(itemId).getEntity().getPunti();
				}
				saldo.getPropertyDataSource().setValue(saldoValue);
				btnGift.setEnabled(saldoValue >= 10);
			}
		});
		movimentiDS.addContainerFilter(new IsNull("carta"));
		movimentiDS.applyFilters();
		cardNumber.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				carteDS.removeAllContainerFilters();
				Equal filter;
				try {
					filter = new Equal("id", Integer.valueOf(event.getText()));
				} catch (NumberFormatException e) {
					filter = new Equal("barcode", event.getText());
				}
				carteDS.addContainerFilter(filter);
				carteDS.applyFilters();
				movimentiDS.removeAllContainerFilters();
				if (carteDS.getItemIds().isEmpty()) {
					// email.setReadOnly(true);
					// email.setPropertyDataSource(new
					// ObjectProperty<String>("Carta non associata"));
					movimentiDS.addContainerFilter(new IsNull("carta"));
					opener.setUriFragment(null);
					btnPrint.setEnabled(false);
					btnAddPoint.setEnabled(false);
				} else {
					// email.setReadOnly(false);
					// email.setPropertyDataSource(carteDS.getItem(carteDS.getIdByIndex(0)).getItemProperty("email"));
					movimentiDS.addContainerFilter(
							new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
					opener.setUriFragment(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity().getId().toString());
					btnPrint.setEnabled(true);
					btnAddPoint.setEnabled(true);
				}
				movimentiDS.applyFilters();
			}
		});
		btnAddPoint.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				Integer value = (Integer) point.getPropertyDataSource().getValue();
				if (value != 0) {
					MovimentiFedelta entity2 = new MovimentiFedelta();
					entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
					entity2.setDescrizione("Accredito Punti");
					entity2.setData(new Date());
					entity2.setPunti(value);
					movimentiDS.addEntity(entity2);
					movimentiDS.removeAllContainerFilters();
					movimentiDS.addContainerFilter(
							new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
					movimentiDS.applyFilters();
					point.getPropertyDataSource().setValue(0);
				}

			}
		});

		btnGift.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				MovimentiFedelta entity2 = new MovimentiFedelta();
				entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
				entity2.setDescrizione("Ritiro Omaggio");
				entity2.setData(new Date());
				entity2.setPunti(-10);
				movimentiDS.addEntity(entity2);
				movimentiDS.removeAllContainerFilters();
				movimentiDS
						.addContainerFilter(new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
				movimentiDS.applyFilters();
			}
		});

		opener.setFeatures("height=200,width=400,resizable");
		opener.extend(btnPrint);
		btnPrintCard.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				printNewCards();
			}
		});
	}

	public static class PrintUI extends UI {

		private final JPAContainer<MovimentiFedelta> movimentiDS = JPAContainerFactory.make(MovimentiFedelta.class,
				HaccpUI.PERSISTENCE_UNIT);

		@Override
		protected void init(VaadinRequest request) {
			// Have some content to print

			//

			CarteFedelta value = new CarteFedelta();
			value.setId(Integer.parseInt(getPage().getUriFragment()));
			movimentiDS.addContainerFilter(new Equal("carta", value));
			movimentiDS.applyFilters();
			VerticalLayout content = new VerticalLayout();
			content.setWidth("10cm");
			Label label1 = new Label("Numero Carta");
			Label label2 = new Label(
					movimentiDS.getItem(movimentiDS.getIdByIndex(0)).getEntity().getCarta().getBarcode());
			HorizontalLayout c1 = new HorizontalLayout(label1, label2);
			c1.setSizeFull();
			c1.setComponentAlignment(label1, Alignment.BOTTOM_LEFT);
			c1.setComponentAlignment(label2, Alignment.BOTTOM_RIGHT);
			content.addComponent(c1);
			for (Object itemId : movimentiDS.getItemIds()) {
				content.addComponent(new Label(movimentiDS.getItem(itemId).getItemProperty("data")));
				Label desc = new Label(movimentiDS.getItem(itemId).getItemProperty("descrizione"));
				Label punti = new Label(movimentiDS.getItem(itemId).getItemProperty("punti"));
				HorizontalLayout c = new HorizontalLayout(desc, punti);
				c.setSizeFull();
				c.setComponentAlignment(desc, Alignment.BOTTOM_LEFT);
				c.setComponentAlignment(punti, Alignment.BOTTOM_RIGHT);
				content.addComponent(c);
			}
			setContent(content);

			// Print automatically when the window opens
			JavaScript.getCurrent().execute("setTimeout(function() {" + "  print(); self.close();}, 0);");
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	public void printNewCards() {

		try {
			Random rnd = new Random(System.currentTimeMillis());
			CarteFedelta value = new CarteFedelta();
			SimpleDateFormat sdf = new SimpleDateFormat("2867yyyy");
			String prefix = sdf.format(new Date());
			ArrayList<CarteFedelta> carte = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				Integer nextInt;
				while ((nextInt = rnd.nextInt(10000)) < 1000)
					;
				value.setPin(nextInt.toString());
				EntityItem<CarteFedelta> item = carteDS.getItem(carteDS.addEntity(value));
				CarteFedelta entity = item.getEntity();
				carte.add(entity);
				String leftPad = StringUtils.leftPad(entity.getId().toString(), 4, '0');
				entity.setBarcode(prefix + leftPad);
				item.getItemProperty("barcode").setValue(prefix + leftPad);
			}
			final List<InputStream> list = new ArrayList<InputStream>();
			list.add(getClass().getClassLoader().getResourceAsStream("Tessere.pdf"));
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(
					getClass().getClassLoader().getResourceAsStream("Tessere.jasper"), new HashMap<>(),
					new JRBeanArrayDataSource(carte.toArray(new CarteFedelta[0]), false));
			JasperExportManager.exportReportToPdfStream(jprint, outputStream);
			outputStream.flush();
			outputStream.close();
			//list.add(new ByteArrayInputStream(outputStream.toByteArray()));
			final ByteArrayOutputStream oStream = new ByteArrayOutputStream();

		//	doMerge(list, outputStream);

			StreamResource res = new StreamResource(new StreamResource.StreamSource() {

				public InputStream getStream() {
					return new ByteArrayInputStream(outputStream.toByteArray());
				}
			}, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
			setResource(MYKEY, res);
			ResourceReference rr = ResourceReference.create(res, this, MYKEY);
			Page.getCurrent().open(rr.getURL(), "new");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static void doMerge(List<InputStream> list, OutputStream outputStream)
			throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		PdfContentByte cb = writer.getDirectContent();

		for (InputStream in : list) {
			PdfReader reader = new PdfReader(in);
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				document.newPage();
				// import the page from source pdf
				PdfImportedPage page = writer.getImportedPage(reader, i);
				// add the page to the destination pdf
				cb.addTemplate(page, 0, 0);
			}
		}

		outputStream.flush();
		document.close();
		outputStream.close();
	}

	public static void main(String[] args) {
		InputStream resourceAsStream = CarteFedeltaView.class.getClassLoader().getResourceAsStream("Tessere.pdf");
		System.err.println(resourceAsStream);
	}
=======
import java.util.Date;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import it.lsoft.haccp.model.CarteFedelta;
import it.lsoft.haccp.model.MovimentiFedelta;

public class CarteFedeltaView extends CarteFedeltaDesign implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5928556157312268110L;
	private final JPAContainer<CarteFedelta> carteDS = JPAContainerFactory.make(CarteFedelta.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final JPAContainer<MovimentiFedelta> movimentiDS = JPAContainerFactory.make(MovimentiFedelta.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final FieldGroup fg = new FieldGroup();
	private final BrowserWindowOpener opener = new BrowserWindowOpener(PrintUI.class);

	public CarteFedeltaView() {
		super();

		saldo.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		saldo.setReadOnly(true);
		table.setContainerDataSource(movimentiDS);
		table.setVisibleColumns(new Object[] { "data", "descrizione", "punti" });
		point.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		movimentiDS.addItemSetChangeListener(new ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				Integer saldoValue = 0;
				for (Object itemId : movimentiDS.getItemIds()) {
					saldoValue += movimentiDS.getItem(itemId).getEntity().getPunti();
				}
				saldo.getPropertyDataSource().setValue(saldoValue);
				btnGift.setEnabled(saldoValue >= 10);
			}
		});
		movimentiDS.addContainerFilter(new IsNull("carta"));
		movimentiDS.applyFilters();
		cardNumber.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				carteDS.removeAllContainerFilters();
				carteDS.addContainerFilter("barCode", event.getText(), false, false);
				carteDS.applyFilters();
				movimentiDS.removeAllContainerFilters();
				if (carteDS.getItemIds().isEmpty()) {
					// email.setReadOnly(true);
					// email.setPropertyDataSource(new
					// ObjectProperty<String>("Carta non associata"));
					movimentiDS.addContainerFilter(new IsNull("carta"));
					opener.setUriFragment(null);
					btnPrint.setEnabled(false);
				} else {
					// email.setReadOnly(false);
					// email.setPropertyDataSource(carteDS.getItem(carteDS.getIdByIndex(0)).getItemProperty("email"));
					movimentiDS.addContainerFilter(
							new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
					opener.setUriFragment( carteDS.getItem(carteDS.getIdByIndex(0)).getEntity().getId().toString());
					btnPrint.setEnabled(true);

				}
				movimentiDS.applyFilters();
			}
		});
		btnAddPoint.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (carteDS.getItemIds().isEmpty()) {
					CarteFedelta entity = new CarteFedelta();
					entity.setBarCode(cardNumber.getValue());
					Object addEntity = carteDS.addEntity(entity);
					carteDS.applyFilters();
					// email.setValue(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity().getEmail());

				}
				Integer value = (Integer) point.getPropertyDataSource().getValue();
				if (value != 0) {
					MovimentiFedelta entity2 = new MovimentiFedelta();
					entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
					entity2.setDescrizione("Accredito Punti");
					entity2.setData(new Date());
					entity2.setPunti(value);
					movimentiDS.addEntity(entity2);
					movimentiDS.removeAllContainerFilters();
					movimentiDS.addContainerFilter(
							new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
					movimentiDS.applyFilters();
					point.getPropertyDataSource().setValue(0);
				}

			}
		});

		btnGift.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				MovimentiFedelta entity2 = new MovimentiFedelta();
				entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
				entity2.setDescrizione("Ritiro Omaggio");
				entity2.setData(new Date());
				entity2.setPunti(-10);
				movimentiDS.addEntity(entity2);
				movimentiDS.removeAllContainerFilters();
				movimentiDS
						.addContainerFilter(new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
				movimentiDS.applyFilters();
			}
		});

		opener.setFeatures("height=200,width=400,resizable");
		opener.extend(btnPrint);

	}

	public static class PrintUI extends UI {

		private final JPAContainer<MovimentiFedelta> movimentiDS = JPAContainerFactory.make(MovimentiFedelta.class,
				HaccpUI.PERSISTENCE_UNIT);

		@Override
		protected void init(VaadinRequest request) {
			// Have some content to print

			// 
			
			
			CarteFedelta value = new CarteFedelta();
			value.setId(Integer.parseInt(getPage().getUriFragment()));
			movimentiDS.addContainerFilter(new Equal("carta", value));
			movimentiDS.applyFilters();
			VerticalLayout content = new VerticalLayout();
			content.setWidth("10cm");
			Label label1 = new Label("Numero Carta");
			Label label2 = new Label(movimentiDS.getItem(movimentiDS.getIdByIndex(0)).getEntity().getCarta().getBarCode());
			HorizontalLayout c1 = new HorizontalLayout(label1, label2);
			c1.setSizeFull();
			c1.setComponentAlignment(label1, Alignment.BOTTOM_LEFT);
			c1.setComponentAlignment(label2, Alignment.BOTTOM_RIGHT);
			content.addComponent(c1);
			for (Object itemId : movimentiDS.getItemIds()) {
				content.addComponent(new Label(movimentiDS.getItem(itemId).getItemProperty("data")));
				Label desc = new Label(movimentiDS.getItem(itemId).getItemProperty("descrizione"));
				Label punti = new Label(movimentiDS.getItem(itemId).getItemProperty("punti"));
				HorizontalLayout c = new HorizontalLayout(desc, punti);
				c.setSizeFull();
				c.setComponentAlignment(desc, Alignment.BOTTOM_LEFT);
				c.setComponentAlignment(punti, Alignment.BOTTOM_RIGHT);
				content.addComponent(c);
			}
			setContent(content);

			// Print automatically when the window opens
			JavaScript.getCurrent().execute("setTimeout(function() {" + "  print(); self.close();}, 0);");
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

>>>>>>> branch 'develop' of https://github.com/thiasos/HACCP.git
}
