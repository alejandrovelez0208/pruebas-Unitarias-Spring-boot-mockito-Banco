package com.pruebas.banco.Excepciones;

public class DineroInsuficienteExcepcion extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public DineroInsuficienteExcepcion(String mensaje){
		super(mensaje);
	}
}
