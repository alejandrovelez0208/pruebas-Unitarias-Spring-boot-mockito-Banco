package com.pruebas.banco.Servicio;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pruebas.banco.Entidades.Banco;
import com.pruebas.banco.Entidades.Cuenta;
import com.pruebas.banco.Repositorios.BancoRepositorios;
import com.pruebas.banco.Repositorios.CuentaRepositorio;

@Service
public class CuentaServicioImpl implements CuentaServicio {

	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private BancoRepositorios bancoRepositorio;

	@Override
	@Transactional(readOnly = true)
	public List<Cuenta> listAll() {
	
		return cuentaRepositorio.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cuenta findById(Long id) {
		
		return cuentaRepositorio.findById(id).orElseThrow();
	}

	@Override
	@Transactional
	public Cuenta save(Cuenta cuenta) {
		
		return cuentaRepositorio.save(cuenta);
	}

	@Override
	@Transactional(readOnly = true)
	public int revisarTotalDeTransferencias(Long bancoId) {
		
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow();
		return banco.getTotalTransferencias();
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal revisarSaldo(Long cuentaId) {
		
		Cuenta cuenta = cuentaRepositorio.findById(cuentaId).orElseThrow();
		return cuenta.getSaldo();
	}

	@Override
	public void transferirDinero(Long numeroCuentaOrigen, Long numeroCuentaDestino, BigDecimal monto, Long bancoId) {
		
		Cuenta cuentaOrigen = cuentaRepositorio.findById(numeroCuentaOrigen).orElseThrow();
		cuentaOrigen.realizarDebito(monto);
		cuentaRepositorio.save(cuentaOrigen);
		
		Cuenta cuentasDestino = cuentaRepositorio.findById(numeroCuentaDestino).orElseThrow();
		cuentasDestino.realizarCredito(monto);
		cuentaRepositorio.save(cuentasDestino);
		
		Banco banco = bancoRepositorio.findById(bancoId).orElseThrow();
		int totalTransferencias = banco.getTotalTransferencias();
		banco.setTotalTransferencias(++totalTransferencias);
		bancoRepositorio.save(banco);
	}
}
