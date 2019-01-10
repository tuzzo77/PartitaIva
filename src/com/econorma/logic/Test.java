package com.econorma.logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Test {


	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36"; 
	public static final String LOGIN_FORM_URL = "https://telematici.agenziaentrate.gov.it/VerificaPIVA/IVerificaPiva.jsp";
	public static final String PARTITA_IVA = "02061630261";  


	public static void main(String[] args) throws IOException {


		Connection conn = Jsoup.connect(LOGIN_FORM_URL).userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)").timeout(5000);
		Document d = conn.get();

		Element partitaIva = d.select("#partIva").first();
		partitaIva.text(PARTITA_IVA);

		Element captcha = d.select("#imgCaptcha").first();
		if (captcha == null) {
			throw new RuntimeException("Unable to find captcha...");
		}

		// Fetch the captcha image
		Connection.Response response = Jsoup //
				.connect(captcha.absUrl("src")) // Extract image absolute URL
				.cookies(conn.response().cookies()) // Grab cookies
				.ignoreContentType(true) // Needed for fetching image
				.execute();
		 

		Element captchaChars = d.select("#inCaptchaChars").first();
		partitaIva.text(PARTITA_IVA);


		// Load image from Jsoup response

		ByteArrayInputStream bytes = new ByteArrayInputStream(response.bodyAsBytes());
		ImageIcon imageIcon = new ImageIcon(ImageIO.read(bytes));

		//		File out = new File("captcha.jpg");
		//		ImageIO.write(ImageIO.read(bytes), "jpg", out);
		//		getImgText(out.getAbsolutePath());


		// Show image
		JOptionPane.showMessageDialog(null, imageIcon, "Captcha image", JOptionPane.PLAIN_MESSAGE);

	 
		System.out.println("Captcha: ");
	    Scanner scanner = new Scanner(System.in);
	    String input = scanner.nextLine();
	 

		if (input.length()>0){
			captchaChars.text(input);
			
			 Document doc = Jsoup.connect(LOGIN_FORM_URL)
			            .data("piva", PARTITA_IVA)
			            .data("inCaptchaChars", input)
			            .cookies(conn.response().cookies()) 
			            .post();

 			    System.out.println(doc);

			    String newURL = doc.location().toString();
			    System.out.println(newURL);
			 
		}

	}

	//	 public static String getImgText(String imageLocation) {
	//		 
	//	      ITesseract instance = new Tesseract();
	//	      try     {
	//	         String imgText = instance.doOCR(new File(imageLocation));
	//	         return imgText;
	//	      } 
	//	      catch (TesseractException e) {
	//	         e.getMessage();
	//	         return "Error while reading image";
	//	      }
	//	   }



}
