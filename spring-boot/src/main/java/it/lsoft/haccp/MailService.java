package it.lsoft.haccp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.URLName;
import javax.mail.internet.MimeMultipart;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.MimeTypeUtils;

import email.com.gmail.ttsai0509.escpos.EscPosBuilder;
import email.com.gmail.ttsai0509.escpos.command.Align;
import email.com.gmail.ttsai0509.escpos.command.Cut;
import email.com.gmail.ttsai0509.escpos.command.Font;

@EnableIntegration
public class MailService {
	private final class AbstractMailMessageTransformerExtension extends AbstractMailMessageTransformer<String> {
		@Override
		protected AbstractIntegrationMessageBuilder<String> doTransform(javax.mail.Message mailMessage) throws Exception {
			Object content = mailMessage.getContent();
			if (content instanceof String) {
				return this.getMessageBuilderFactory().withPayload((String) content);
			}
			if (content instanceof MimeMultipart) {
				AbstractIntegrationMessageBuilder<String> withPayload = null;
				int count = ((Multipart) content).getCount();
				for (int i = 0; i < count; i++) {
					Part bodyPart = ((MimeMultipart) content).getBodyPart(i);
					if (MimeTypeUtils.TEXT_PLAIN_VALUE.equalsIgnoreCase(bodyPart.getContentType())) {
						return parsePart(bodyPart);
					} else if (withPayload == null) {
						withPayload = parsePart(bodyPart);
					}

				}
				if (withPayload != null) {
					return withPayload;
				}
			} else if (content instanceof Part) {
				return parsePart((Part) content);
			}
			throw new IllegalArgumentException("failed to transform contentType [" + mailMessage.getContentType() + "] to String.");
		}

		private AbstractIntegrationMessageBuilder<String> parsePart(Part content) throws IOException, MessagingException {
			Part part = (Part) content;
			if (part.getContent() instanceof String) {
				return this.getMessageBuilderFactory().withPayload((String) part.getContent());
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			part.writeTo(outputStream);
			return this.getMessageBuilderFactory().withPayload(new String(outputStream.toByteArray()));
		}
	}

	@Autowired
	private ImapIdleChannelAdapter adapter;
	@Autowired
	@Qualifier("MailTransformer")
	private Transformer mts;

	@Bean
	@Qualifier("MailTransformer")
	public Transformer builMesageTransformer() {
		return new AbstractMailMessageTransformerExtension();
	}

	@Bean
	public ImapMailReceiver buildReceiver() {
		String string = new URLName("imap", "imap.lapiadaloca.it", 143, "INBOX", "orders@lapiadaloca.it", "Roberto.2867").toString();
		ImapMailReceiver mailReceiver = new ImapMailReceiver(string);
		return mailReceiver;
	}

	@Bean("InMailChannel")
	public ImapIdleChannelAdapter buildAdapter(ImapMailReceiver mailReceiver, @Qualifier("MailChannel") MessageChannel messageChannel) {
		ImapIdleChannelAdapter channelAdapter = new ImapIdleChannelAdapter(mailReceiver);
		channelAdapter.setOutputChannel(messageChannel);
		channelAdapter.setAutoStartup(true);
		return channelAdapter;
	}

	@Bean("MailChannel")
	@Qualifier("MailChannel")
	public MessageChannel buildMessageChannel() {
		return new MessageChannel() {

			@Override
			public boolean send(Message<?> message, long timeout) {
				processMessage((String) mts.transform(message).getPayload());
				return true;
			}

			private void processMessage(String message) {
				System.err.println(message);
				try {
					print(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public boolean send(Message<?> message) {
				processMessage((String) mts.transform(message).getPayload());
				return true;
			}
		};
	}

	@PostConstruct
	public void post() {
		adapter.start();
	}
public static void main(String[] args) throws PrintException, IOException {
	print("Hello World!");
}
	private static void print(String str)   {
//		try {
//			String printerName = "AL-M8000-49A3FF";
//			PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
//			printServiceAttributeSet.add(new PrinterName(printerName, null));
//			PrintService printService = PrintServiceLookup.lookupPrintServices(null, printServiceAttributeSet)[0];
//			DocPrintJob job = printService.createPrintJob();
//			Doc doc = new SimpleDoc(new ByteArrayInputStream(str.getBytes()), DocFlavor.INPUT_STREAM.AUTOSENSE, null);
//			job.print(doc, null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			EscPosBuilder escPos = new EscPosBuilder();
			byte[] data = escPos.initialize()
			        .font(Font.EMPHASIZED)
			        .align(Align.LEFT)
			        .text(str)
			        .feed(5)
			        .cut(Cut.FULL)
			        .getBytes();
			 Socket echoSocket = new Socket("172.24.2.35", 515);
			 echoSocket.getOutputStream().write(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
