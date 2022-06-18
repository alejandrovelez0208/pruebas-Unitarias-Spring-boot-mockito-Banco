package com.pruebas.banco.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pruebas.banco.Entidades.Banco;

public interface BancoRepositorios extends JpaRepository<Banco, Long> {

	
}
