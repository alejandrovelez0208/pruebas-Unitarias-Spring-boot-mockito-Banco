package com.pruebas.banco.Entidades;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pruebas.banco.Excepciones.DineroInsuficienteExcepcion;

import lombok.Data;

@Entity
@Table(name = "cuentas")
@Data
public class Cuenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String persona;
	private BigDecimal saldo;

	public Cuenta() {
		super();
	}

	public Cuenta(Long id, String persona, BigDecimal saldo) {
		super();
		this.id = id;
		this.persona = persona;
		this.saldo = saldo;
	}

	public void realizarDebito(BigDecimal monto) {

		BigDecimal nuevoSaldo = this.saldo.subtract(monto);
		if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
			throw new DineroInsuficienteExcepcion("Dinero Insuficiente en la cuenta");
		}
		this.saldo = nuevoSaldo;
	}

	public void realizarCredito(BigDecimal monto) {

		this.saldo = saldo.add(monto);
	}

}
