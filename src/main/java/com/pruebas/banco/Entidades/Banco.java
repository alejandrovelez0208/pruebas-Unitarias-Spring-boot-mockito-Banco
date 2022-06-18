package com.pruebas.banco.Entidades;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "bancos")
@Data
public class Banco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	private String nombre;
	
	@Column(name = "total_transferencias")
	private int totalTransferencias;

	public Banco() {

	}

	public Banco(Long id, String nombre, int totalTransferencias) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.totalTransferencias = totalTransferencias;
	}

}
