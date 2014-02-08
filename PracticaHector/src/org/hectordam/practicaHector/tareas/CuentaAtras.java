package org.hectordam.practicaHector.tareas;

import javax.swing.SwingWorker;

public class CuentaAtras extends SwingWorker<Void, Void>{

	private int hora;
	private int minuto;
	
	public CuentaAtras(){
		
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		
		while(hora > 0 || minuto > 0){
			Thread.sleep(1000*60);
			if(hora > 0){
				
				minuto = minuto - 1;
				if(minuto == 0){
					hora = hora -1;
					minuto = 60;
				}
			}
			else{
				minuto = minuto - 1;
			}
			
		}
		
		this.firePropertyChange("tiempo", 0, 1);
		
		return null;
	}

	public int getHora() {
		return hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	public int getMinuto() {
		return minuto;
	}

	public void setMinuto(int minuto) {
		this.minuto = minuto;
	}
	
}
