package org.hectordam.practicaHector.tareas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;

public class Splash extends JFrame {

	private JPanel contentPane;
	private JLabel lblIcono;


	/**
	 * Create the frame.
	 */
	public Splash() {
		setResizable(false);
		setUndecorated(true);
		//setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 325, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		lblIcono = new JLabel("");
		lblIcono.setBorder(null);
		lblIcono.setBackground(Color.WHITE);
		lblIcono.setIcon(new ImageIcon("C:\\Users\\nose\\Desktop\\2-DAM\\programacion de servicios\\proyectos\\PracticaHector\\descarga.png"));
		contentPane.add(lblIcono);
		setLocationRelativeTo(null);
	}

}
