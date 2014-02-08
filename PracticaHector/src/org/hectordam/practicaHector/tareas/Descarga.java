package org.hectordam.practicaHector.tareas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;

import org.hectordam.practicaHector.PracticaHector;
import org.hectordam.practicaHector.TablaCanceladas;
import org.hectordam.practicaHector.TablaDescargas;

public class Descarga extends SwingWorker<Void, Void>{

	private String ruta;
	private String Url;
	
	private JProgressBar pbPorcentaje;
	private JButton btCancelar;
	private JButton btDescargar;
	private JLabel etiqueta;
	
	private URL enlace;
	private URLConnection conexion;
	private InputStream is;
	private long tamano;
	private FileOutputStream fos;
	
	
	public Descarga(){
		
	}
	
	public void inicializar(JPanel panel){
		
		pbPorcentaje = new JProgressBar();
		etiqueta = new JLabel("0%");
		
		btCancelar = new JButton();
		btCancelar.setIcon(new ImageIcon("stop.icon.jpeg"));
		btCancelar.setBackground(Color.white);
		btCancelar.setPreferredSize(new Dimension(30,30));
		
		panel.add(pbPorcentaje);
		panel.add(etiqueta);
		panel.add(btCancelar);
		
		btCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelar();
			}
		});
		
	}
	
	private void conectar() throws IOException{
		
		enlace = new URL(this.Url);
		conexion = enlace.openConnection();
		
		tamano = conexion.getContentLengthLong();
		is = enlace.openStream();
		fos = new FileOutputStream(this.ruta);
	}
	
	public void cancelar(){
		
		cancel(true);
		eliminar();
	}
	
	
	
	@Override
	protected Void doInBackground(){
		
		try {
			conectar();
			byte[] bytes = new byte[2048];
			int longitud = 0, progresoDescarga = 0;
			
			while ((longitud = is.read(bytes)) != -1) {
				fos.write(bytes, 0, longitud);
				
				progresoDescarga += longitud;			
				setProgress((int)(progresoDescarga * 100 / tamano));
				
				pbPorcentaje.setValue((int)(progresoDescarga * 100 / tamano));
				etiqueta.setText(Integer.toString((int)(progresoDescarga * 100 / tamano))+"%");
				
				if (isCancelled()){
					
					PrintWriter cancelado = new PrintWriter (new BufferedWriter(new FileWriter("Cancelado", true)));
					cancelado.println(this.Url);
					cancelado.println(this.ruta);
					cancelado.close();
					
					this.firePropertyChange("cancelado", 0, 1);
					
					eliminar();
					break;
				}
					
			}
			
			is.close();
			fos.close();
			
			if (isCancelled() == false){
				PrintWriter descargado = new PrintWriter (new BufferedWriter(new FileWriter("Descargado", true)));
				descargado.println(this.Url);
				descargado.println(this.ruta);
				descargado.close();
				
				this.firePropertyChange("descargado", 0, 1);
				
				eliminar();
				
				setProgress(100);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void eliminar(){
		
		this.btCancelar.setVisible(false);
		this.pbPorcentaje.setVisible(false);
		this.etiqueta.setVisible(false);
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
	
}
