package com.pruebas.banco.Entidades;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransaccionDTO {

	private Long cuentaOrigenId;
	private Long cuentaDestinoId;
	private Long bancoId;
	private BigDecimal monto;

	public TransaccionDTO() {
		super();
	}

}
