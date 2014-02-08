package org.hectordam.practicaHector;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.hectordam.practicaHector.tareas.CuentaAtras;
import org.hectordam.practicaHector.tareas.Descarga;
import org.hectordam.practicaHector.tareas.Splash;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class PracticaHector {

	private JFrame frame;
	private JTextField txtRuta;
	private JButton btnDescargar;
	private JPanel panel;
	
	private Descarga descarga;
	private JLabel lblProgamarDescarga;
	private JButton btActivar;
	private JLabel lblLimiteDescarga;
	private JTextField txtLimite;
	private JButton btnAceptarLimite;
	private JLabel lblHora;
	private JLabel lblMin;
	private JButton btDeshactivar;
	private JButton btnCancelarLimite;
	private JComboBox cbHora;
	private JComboBox cbMinuto;
	
	private boolean esProgramada;
	private boolean esLimite;
	private int limite;
	private int contador = 1;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private TablaDescargas tablaDescargas;
	private TablaCanceladas tablaCanceladas;
	
	private ArrayList<Descarga> lista;
	private JButton btnReanudar;
	
	private JFileChooser fileChooser;
	private File rutaFichero;
	
	
	private void limitar(){
		
		if(this.esLimite == true){
			
			if(contador <= this.limite){
				cargar();
				
				this.contador++;
			}
			else{
				JOptionPane.showMessageDialog(null, "Has alcanzado el cupo de descargas, candela el limite de descargas si deseas seguir descargando", "Limite", JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			cargar();
		}
	}
	
	private void cargar(){
		fileChooser = new JFileChooser();
		fileChooser.showSaveDialog(null);
		rutaFichero = fileChooser.getSelectedFile();
			
		if(esProgramada == true){
			
			CuentaAtras cuenta = new CuentaAtras();
			cuenta.setHora((Integer)this.cbHora.getSelectedItem());
			cuenta.setMinuto((Integer)this.cbMinuto.getSelectedItem());
			
			cuenta.addPropertyChangeListener(new PropertyChangeListener(){
				@Override
				public void propertyChange(PropertyChangeEvent event) {
					
					if(event.getPropertyName().equalsIgnoreCase("tiempo")){
						descargar();
					}
					
				}
			});
			
			cuenta.execute();
			
		}
		else{
			
			descargar();
		}
		
	}
	
	private void descargar(){
		
		descarga = new Descarga();
		
		descarga.setUrl(this.txtRuta.getText());
		descarga.setRuta(rutaFichero.getAbsolutePath());
		
		descarga.inicializar(panel);
		
		this.frame.revalidate();
		this.frame.repaint();
		
		descarga.addPropertyChangeListener(new PropertyChangeListener(){

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				
				if(event.getPropertyName().equalsIgnoreCase("descargado")){
					tablaDescargas.listar();
				}
				if(event.getPropertyName().equalsIgnoreCase("cancelado")){
					File f = new File(descarga.getRuta());
					f.delete();
					tablaCanceladas.listar();
				}
			}
		});
		
		descarga.execute();
		
	}
	
	
	private void aceptarProgramada(){
		
		this.esProgramada = true;
		this.btActivar.setEnabled(false);
		this.btDeshactivar.setEnabled(true);
		this.cbHora.setEnabled(false);
		this.cbMinuto.setEnabled(false);
	}
	
	private void cancelarProgramada(){
		
		this.esProgramada = false;
		this.btActivar.setEnabled(true);
		this.btDeshactivar.setEnabled(false);
		this.cbHora.setEnabled(true);
		this.cbMinuto.setEnabled(true);
	}
	
	private void aceptarLimite(){
		
		if(this.txtLimite.getText().equalsIgnoreCase("") || this.txtLimite.getText().matches("[0-9]*") == false || Integer.parseInt(this.txtLimite.getText()) < 1){
			JOptionPane.showMessageDialog(null, "No has introducido un valor correcto", "Limite", JOptionPane.ERROR_MESSAGE);
		}
		else{
			this.limite = Integer.parseInt(this.txtLimite.getText());
			
			this.btnAceptarLimite.setEnabled(false);
			this.btnCancelarLimite.setEnabled(true);
			this.esLimite = true;
		}
	}
	
	private void cancelarLimite(){
		this.limite = 1;
		this.contador = 1;
		this.btnAceptarLimite.setEnabled(true);
		this.btnCancelarLimite.setEnabled(false);
		this.btnDescargar.setEnabled(true);
		this.esLimite = false;
	}
	
	private void inicializar(){		
		Thread hilo = new Thread (new Runnable(){
			public void run(){
				Splash splash=new Splash();
				splash.setVisible(true);
				try {
					
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				splash.setVisible(false);
				frame.setVisible(true);
			}
		});
		hilo.start();
		
		this.lista = new ArrayList<Descarga>();
		
		this.tablaDescargas.listar();
		this.tablaCanceladas.listar();
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PracticaHector window = new PracticaHector();
					window.frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PracticaHector() {
		initialize();
		inicializar();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 570, 637);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtRuta = new JTextField();
		txtRuta.setBounds(10, 11, 534, 20);
		frame.getContentPane().add(txtRuta);
		txtRuta.setColumns(10);
		
		btnDescargar = new JButton("Descargar");
		btnDescargar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limitar();
			}
		});
		btnDescargar.setBounds(375, 42, 102, 23);
		frame.getContentPane().add(btnDescargar);
		
		lblProgamarDescarga = new JLabel("Progamar Descarga");
		lblProgamarDescarga.setBounds(317, 103, 115, 14);
		frame.getContentPane().add(lblProgamarDescarga);
		
		btActivar = new JButton("Activar");
		btActivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aceptarProgramada();
			}
		});
		btActivar.setBounds(324, 176, 89, 23);
		frame.getContentPane().add(btActivar);
		
		lblLimiteDescarga = new JLabel("Limite Descarga");
		lblLimiteDescarga.setBounds(442, 103, 110, 14);
		frame.getContentPane().add(lblLimiteDescarga);
		
		txtLimite = new JTextField();
		txtLimite.setBounds(470, 127, 25, 20);
		frame.getContentPane().add(txtLimite);
		txtLimite.setColumns(10);
		
		btnAceptarLimite = new JButton("Aceptar");
		btnAceptarLimite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceptarLimite();
			}
		});
		btnAceptarLimite.setBounds(442, 158, 91, 23);
		frame.getContentPane().add(btnAceptarLimite);
		
		lblHora = new JLabel("Hora");
		lblHora.setBounds(328, 128, 33, 14);
		frame.getContentPane().add(lblHora);
		
		lblMin = new JLabel("Min");
		lblMin.setBounds(388, 128, 25, 14);
		frame.getContentPane().add(lblMin);
		
		btDeshactivar = new JButton("Desactivar");
		btDeshactivar.setEnabled(false);
		btDeshactivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelarProgramada();
			}
		});
		btDeshactivar.setBounds(326, 210, 87, 23);
		frame.getContentPane().add(btDeshactivar);
		
		btnCancelarLimite = new JButton("Cancelar");
		btnCancelarLimite.setEnabled(false);
		btnCancelarLimite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelarLimite();
			}
		});
		btnCancelarLimite.setBounds(442, 188, 91, 23);
		frame.getContentPane().add(btnCancelarLimite);
		
		cbHora = new JComboBox();
		cbHora.setBounds(322, 144, 43, 22);
		for(int i = 0; i <= 23; i++){
			this.cbHora.addItem((Integer) i);
		}
		frame.getContentPane().add(cbHora);
		
		cbMinuto = new JComboBox();
		cbMinuto.setBounds(375, 143, 43, 22);
		for(int i = 0; i <= 59; i++){
			this.cbMinuto.addItem((Integer) i);
		}
		frame.getContentPane().add(cbMinuto);
		
		panel = new JPanel();
		panel.setBounds(10, 42, 262, 282);
		frame.getContentPane().add(panel);
		panel.setBackground(Color.WHITE);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 466, 534, 122);
		frame.getContentPane().add(scrollPane);
		
		tablaDescargas = new TablaDescargas(this.panel);
		tablaDescargas.setEnabled(false);
		scrollPane.setViewportView(tablaDescargas);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 335, 534, 120);
		frame.getContentPane().add(scrollPane_1);
		
		tablaCanceladas = new TablaCanceladas(this.panel, this.tablaDescargas);
		scrollPane_1.setViewportView(tablaCanceladas);
		
		btnReanudar = new JButton("Reanudar");
		btnReanudar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tablaCanceladas.reanudar();
			}
		});
		btnReanudar.setBounds(388, 301, 89, 23);
		frame.getContentPane().add(btnReanudar);
		
		this.frame.setLocationRelativeTo(null);
	}
}
