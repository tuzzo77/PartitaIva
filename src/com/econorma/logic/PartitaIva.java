package com.econorma.logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

public class PartitaIva {

	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"; 
	private final String LOGIN_FORM_URL = "https://telematici.agenziaentrate.gov.it/VerificaPIVA/IVerificaPiva.jsp";
  
	 
	private String partitaIva;
	
	public PartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public void run() throws IOException {
		// # Go to login page
		Connection.Response loginFormResponse = Jsoup.connect(LOGIN_FORM_URL)
				.method(Connection.Method.GET)
				.userAgent(USER_AGENT)
				.execute(); 


		FormElement loginForm = (FormElement)loginFormResponse.parse().select("#vcf").first();
		checkElement("Login Form", loginForm);

		Element imgCaptcha = loginForm.select("#imgCaptcha").first();
		checkElement("Image Captcha", imgCaptcha);

		Element ivaField = loginForm.select("#partIva").first();
		checkElement("Partita iva", ivaField);
		ivaField.val(partitaIva);


		Element securityField = loginForm.select("#inCaptchaChars").first();
		checkElement("Security", securityField);



		// Fetch the captcha image
		Connection.Response response = Jsoup //
				.connect(imgCaptcha.absUrl("src")) // Extract image absolute URL
				.cookies(loginFormResponse.cookies()) // Grab cookies
				.ignoreContentType(true) // Needed for fetching image
				.execute();

		ByteArrayInputStream bytes = new ByteArrayInputStream(response.bodyAsBytes());
		ImageIcon imageIcon = new ImageIcon(ImageIO.read(bytes));
		
		int selection = JOptionPane.showConfirmDialog(null, imageIcon, "Title", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
 
		
		if (selection == JOptionPane.OK_OPTION) {
			
			
			JFrame frame = new JFrame();
		    Object result = JOptionPane.showInputDialog(frame, "Enter captcha text:");
			
		    String input = result.toString();
 

			if (input.length()>0){
				System.out.println(input);
				securityField.val(input);


				// # Now send the form for login
				Connection.Response loginActionResponse = loginForm.submit()
						.cookies(loginFormResponse.cookies())
						.userAgent(USER_AGENT)  
						.execute();

				Document doc = loginActionResponse.parse();

				Elements elements = doc.select("div[id=vcfcontenitore]");
				StringBuilder messagge = new StringBuilder();

				for (Element element : elements) {

					Elements paragraphs = element.getElementsByTag("p");

					for (Element paragraph : paragraphs) {
						messagge.append(paragraph.text());
						messagge.append("\r\n");
					}


				}

				JOptionPane.showMessageDialog(null, messagge.toString(), "Risultato", JOptionPane.PLAIN_MESSAGE);
				System.exit(0);
			}
			
			
			
			
		} else {
			System.exit(0);	
		}
        



	}

	public void checkElement(String name, Element elem) {
		if (elem == null) {
			throw new RuntimeException("Unable to find " + name);
		}
	}

}
