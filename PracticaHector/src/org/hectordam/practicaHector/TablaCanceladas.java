package org.hectordam.practicaHector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hectordam.practicaHector.tareas.Descarga;

public class TablaCanceladas extends JTable{

	private ArrayList<Descarga> descargas;
	private DefaultTableModel modelo;
	
	private JPanel panel;
	private TablaDescargas tablaDescarga;
	
	public TablaCanceladas(JPanel panel, TablaDescargas tablaDescarga){
		this.panel = panel;
		this.tablaDescarga = tablaDescarga;
		
		inicializar();
	}
	
	public void inicializar(){
		
		String[] columnas = {"Canceladas", "Ruta"};
		
		modelo = new DefaultTableModel(columnas, 0){
			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}			
		};
		this.setModel(modelo);
		
		descargas = new ArrayList<Descarga>();
	}
	
	public void listar(){
		
		modelo.setNumRows(0);
		this.descargas = new ArrayList<Descarga>();
		
		Descarga descarga;
		try {
			BufferedReader descargado = new BufferedReader (new FileReader("Cancelado"));
			String frase = descargado.readLine();
			String ruta;
			while(frase != null){
				ruta = descargado.readLine();
				
				String[] fila = {frase, ruta};
				modelo.addRow(fila);
				
				descarga = new Descarga();
				descarga.setUrl(frase);
				descarga.setRuta(ruta);
				this.descargas.add(descarga);
				
				frase = descargado.readLine();
			}
			
			descargado.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void reanudar(){
		String ruta = filaSeleccionada();
		
		for(int i=0; i<this.descargas.size(); i++){
			
			if(descargas.get(i).getRuta().equalsIgnoreCase(ruta)){
				this.descargas.get(i).inicializar(panel);
				descargas.get(i).execute();
				
				descargas.get(i).addPropertyChangeListener(new PropertyChangeListener(){
	
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					
					if(event.getPropertyName().equalsIgnoreCase("descargado")){
						tablaDescarga.listar();
					}
					if(event.getPropertyName().equalsIgnoreCase("cancelado")){
						File f = new File(filaSeleccionada());
						f.delete();
						listar();
					}
				}
			});
				
				break;
			}
		}
		
		
	}
	
	public String filaSeleccionada(){
		
		int fila = this.getSelectedRow();
		
		if(fila == -1){
			return null;
		}
		
		String lista = (String) getValueAt(fila, 1);
		return lista;
	}
	
}
