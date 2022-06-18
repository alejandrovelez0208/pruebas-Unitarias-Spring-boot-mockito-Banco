package com.pruebas.banco;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.banco.Controlador.CuentaControlador;
import com.pruebas.banco.Entidades.Cuenta;
import com.pruebas.banco.Entidades.TransaccionDTO;
import com.pruebas.banco.Servicio.CuentaServicio;

@WebMvcTest(CuentaControlador.class)
public class CuentaControladorTest {

	@Autowired
	private MockMvc mockMVC;

	@MockBean
	private CuentaServicio cuentaServicio;

	ObjectMapper objectMapper;

	@BeforeEach
	public void configurar() {
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testVerDetalles() throws Exception {
		when(cuentaServicio.findById(1L)).thenReturn(Datos.crearCuenta001().orElseThrow());

		mockMVC.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpectAll(jsonPath("$.persona").value("Christian"))
				.andExpect(jsonPath("$.saldo").value("1000"));

		verify(cuentaServicio).findById(1L);
	}

	@Test
	public void testTransferirDinero() throws Exception {
		TransaccionDTO transaccionDTO = new TransaccionDTO();
		transaccionDTO.setCuentaOrigenId(1L);
		transaccionDTO.setCuentaDestinoId(2L);
		transaccionDTO.setMonto(new BigDecimal("100.00"));
		transaccionDTO.setBancoId(1L);

		System.out.println(objectMapper.writeValueAsString(transaccionDTO));

		Map<String, Object> respuesta = new HashMap<>();
		respuesta.put("date", LocalDate.now().toString());
		respuesta.put("status", "OK");
		respuesta.put("mensaje", "Transferencias realizada con exito");
		respuesta.put("transaccionDTO", transaccionDTO);

		mockMVC.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transaccionDTO)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
				.andExpect(jsonPath("$.mensaje").value("Transferencias realizada con exito"))
				.andExpect(jsonPath("$.transaccionDTO.cuentaOrigenId").value(transaccionDTO.getCuentaOrigenId()))
				.andExpect(content().json(objectMapper.writeValueAsString(respuesta)));
	}

	@Test
	public void testListarCuentas() throws JsonProcessingException, Exception {
		List<Cuenta> cuentas = Arrays.asList(Datos.crearCuenta001().orElseThrow(),
				Datos.crearCuenta002().orElseThrow());

		when(cuentaServicio.listAll()).thenReturn(cuentas);

		mockMVC.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].persona").value("Christian"))
				.andExpect(jsonPath("$[1].persona").value("Julen")).andExpect(jsonPath("$[0].saldo").value("1000"))
				.andExpect(jsonPath("$[1].saldo").value("2000")).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().json(objectMapper.writeValueAsString(cuentas)));

		verify(cuentaServicio).listAll();
	}

	@Test
	public void testguardarCuenta() throws JsonProcessingException, Exception{
		Cuenta cuentas = new Cuenta(null, "Biaggio", new BigDecimal("2000"));
		when(cuentaServicio.save(any())).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});
		
		mockMVC.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cuentas)))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id",is(3)))
				.andExpect(jsonPath("$.persona",is("Biaggio")))
				.andExpect(jsonPath("$.saldo",is(2000)));
		
		verify(cuentaServicio).save(any());
	}
}
