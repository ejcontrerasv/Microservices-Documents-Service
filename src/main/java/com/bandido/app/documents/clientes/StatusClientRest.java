package com.bandido.app.documents.clientes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bandido.app.documents.dto.Status;


@FeignClient(name = "status-service/app/status")
public interface StatusClientRest {
	
	@GetMapping("/listar/{id}")
	public Status listarPorId(@PathVariable("id") Integer id);

}
