package org.hectordam.practicaHector;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hectordam.practicaHector.tareas.Descarga;

public class TablaDescargas extends JTable {

	private ArrayList<Descarga> descargas;
	private DefaultTableModel modelo;
	
	private JPanel panel;
	
	public TablaDescargas(JPanel panel){
		this.panel = panel;
		
		inicializar();
	}
	
	public void inicializar(){
		
		String[] columnas = {"Descargas", "Ruta"};
		
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
		this.descargas.removeAll(descargas);
		
		Descarga descarga;
		try {
			BufferedReader descargado = new BufferedReader (new FileReader("Descargado"));
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
	
	
	public String filaSeleccionada(){
		
		int fila = 0;
		
		if(this.getSelectedRow() == -1){
			return null;
		}
		
		String lista = (String) getValueAt(fila, 1);
		
		return lista;
	}
	
}
