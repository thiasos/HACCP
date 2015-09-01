package it.lsoft.haccp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.GsonBuilder;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Page;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import it.lsoft.haccp.model.CarteFedelta;
import it.lsoft.haccp.model.MovimentiFedelta;
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

	@SuppressWarnings("unchecked")
	public CarteFedeltaView() {
		super();

		saldo.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		saldo.setReadOnly(true);
		table.setContainerDataSource(movimentiDS);
		table.setVisibleColumns(new Object[] { "data", "descrizione", "punti" });
		point.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		movimentiDS.addItemSetChangeListener(event -> {
			Integer saldoValue = 0;
			for (Object itemId : movimentiDS.getItemIds()) {
				saldoValue += movimentiDS.getItem(itemId).getEntity().getPunti();
			}
			saldo.getPropertyDataSource().setValue(saldoValue);
			btnGift.setEnabled(saldoValue >= 10);
		});
		movimentiDS.addContainerFilter(new IsNull("carta"));
		movimentiDS.applyFilters();
		cardNumber.addTextChangeListener(event -> {
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
				movimentiDS
						.addContainerFilter(new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
				opener.setUriFragment(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity().getId().toString());
				btnPrint.setEnabled(true);
				btnAddPoint.setEnabled(true);
			}
			movimentiDS.applyFilters();
		});
		final Export export = new Export();
		btnAddPoint.addClickListener(event -> {

			Integer value = (Integer) point.getPropertyDataSource().getValue();
			if (value != 0) {
				MovimentiFedelta entity2 = new MovimentiFedelta();
				entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
				entity2.setDescrizione("Accredito Punti");
				entity2.setData(new Date());
				entity2.setPunti(value);
				movimentiDS.addEntity(entity2);
				movimentiDS.removeAllContainerFilters();
				movimentiDS
						.addContainerFilter(new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
				movimentiDS.applyFilters();
				point.getPropertyDataSource().setValue(0);
			}
			export.doExport();
		});

		btnGift.addClickListener(event -> {
			MovimentiFedelta entity2 = new MovimentiFedelta();
			entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
			entity2.setDescrizione("Ritiro Omaggio");
			entity2.setData(new Date());
			entity2.setPunti(-10);
			movimentiDS.addEntity(entity2);
			movimentiDS.removeAllContainerFilters();
			movimentiDS.addContainerFilter(new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
			movimentiDS.applyFilters();
		});

		opener.setFeatures("height=200,width=400,resizable");
		opener.extend(btnPrint);
		btnPrintCard.addClickListener(event -> printNewCards());

	}

	public static class PrintUI extends UI {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6474363374321739896L;
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
			// list.add(new ByteArrayInputStream(outputStream.toByteArray()));
			final ByteArrayOutputStream oStream = new ByteArrayOutputStream();

			// doMerge(list, outputStream);

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

	public static class Export {
		private final JPAContainer<CarteFedelta> carteDS = JPAContainerFactory.make(CarteFedelta.class,
				HaccpUI.PERSISTENCE_UNIT);

		public void doExport() {
			{
				ArrayList<CarteFedelta> cf = new ArrayList<>();
				for (Object object : carteDS.getItemIds()) {
					CarteFedelta entity = carteDS.getItem(object).getEntity();
					for (MovimentiFedelta movimentiFedelta : entity.getMovimenti()) {
						movimentiFedelta.setCarta(null);
					}

					cf.add(entity);
				}
				try {
					MultipartUtility mpu = new MultipartUtility("http://www.tientiinpie.it/upload.php", "ISO-8859-1");
					mpu.addFilePart("fileToUpload", "cards.json",
							new ByteArrayInputStream(new GsonBuilder().create().toJson(cf).getBytes()));
					mpu.finish();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * This utility class provides an abstraction layer for sending multipart
	 * HTTP POST requests to a web server.
	 * 
	 * @author www.codejava.net
	 *
	 */
	public static class MultipartUtility {
		private final String boundary;
		private static final String LINE_FEED = "\r\n";
		private HttpURLConnection httpConn;
		private String charset;
		private OutputStream outputStream;
		private PrintWriter writer;

		/**
		 * This constructor initializes a new HTTP POST request with content
		 * type is set to multipart/form-data
		 * 
		 * @param requestURL
		 * @param charset
		 * @throws IOException
		 */
		public MultipartUtility(String requestURL, String charset) throws IOException {
			this.charset = charset;

			// creates a unique boundary based on time stamp
			boundary = "===" + System.currentTimeMillis() + "===";

			URL url = new URL(requestURL);
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setUseCaches(false);
			httpConn.setDoOutput(true); // indicates POST method
			httpConn.setDoInput(true);
			httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
			httpConn.setRequestProperty("Test", "Bonjour");
			outputStream = httpConn.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
		}

		/**
		 * Adds a form field to the request
		 * 
		 * @param name
		 *            field name
		 * @param value
		 *            field value
		 */
		public void addFormField(String name, String value) {
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
			writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.append(value).append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Adds a upload file section to the request
		 * 
		 * @param fieldName
		 *            name attribute in <input type="file" name="..." />
		 * @param uploadFile
		 *            a File to be uploaded
		 * @throws IOException
		 */
		public void addFilePart(String fieldName, File uploadFile) throws IOException {
			String fileName = uploadFile.getName();
			InputStream inputStream = new FileInputStream(uploadFile);
			addFilePart(fieldName, fileName, inputStream);
		}

		public void addFilePart(String fieldName, String fileName, InputStream inputStream) throws IOException {
			writer.append("--" + boundary).append(LINE_FEED);
			writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
					.append(LINE_FEED);
			writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
			writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
			writer.append(LINE_FEED);
			writer.flush();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
			inputStream.close();

			writer.append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Adds a header field to the request.
		 * 
		 * @param name
		 *            - name of the header field
		 * @param value
		 *            - value of the header field
		 */
		public void addHeaderField(String name, String value) {
			writer.append(name + ": " + value).append(LINE_FEED);
			writer.flush();
		}

		/**
		 * Completes the request and receives response from the server.
		 * 
		 * @return a list of Strings as response in case the server returned
		 *         status OK, otherwise an exception is thrown.
		 * @throws IOException
		 */
		public List<String> finish() throws IOException {
			List<String> response = new ArrayList<String>();

			writer.append(LINE_FEED).flush();
			writer.append("--" + boundary + "--").append(LINE_FEED);
			writer.close();

			// checks server's status code first
			int status = httpConn.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					response.add(line);
				}
				reader.close();
				httpConn.disconnect();
			} else {
				throw new IOException("Server returned non-OK status: " + status);
			}

			return response;
		}
	}
}
