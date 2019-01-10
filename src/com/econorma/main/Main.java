package com.econorma.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.econorma.logic.PartitaIva;

public class Main {

	
	public static void main(String[] args)  {
		
	 		JFrame f=new JFrame("Partita Iva checker"); 
	 		f.setLocationRelativeTo(null); 
	 		f.setResizable(false);
						 
			JButton b=new JButton("Invia");    
			b.setBounds(100,100, 140, 30);    
					 
			JLabel label = new JLabel();		
			label.setText("Partita Iva:");
			label.setBounds(10, 10, 100, 100);
						 
			JLabel label1 = new JLabel();
			label1.setBounds(10, 110, 200, 100);
						 
			JTextField textfield= new JTextField();
			textfield.setBounds(110, 50, 130, 30);
						 
			f.add(label1);
			f.add(textfield);
			f.add(label);
			f.add(b);    
			f.setSize(300, 180);    
			f.setLayout(null);    
			f.setVisible(true);    
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
			
								 
			b.addActionListener(new ActionListener() {
		        
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						PartitaIva piva = new PartitaIva(textfield.getText());
						piva.run();
					} catch (Exception e) {
						 
					}
					
				}          
		      });
			}         
	
}
