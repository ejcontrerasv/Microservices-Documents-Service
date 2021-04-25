package com.bandido.app.documents.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bandido.app.documents.exception.ModeloNotFoundException;
import com.bandido.app.documents.models.entity.Document;
import com.bandido.app.documents.models.service.IDocumentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
@RequestMapping("/app/documents")
public class DocumentController {
	
	@Autowired
	@Qualifier("serviceFeign")
	private IDocumentService service;
	
	@HystrixCommand(fallbackMethod = "registrarFallBack", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10") })
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Document document){
		Document doc = service.registrar(document);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(doc.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	
	public ResponseEntity<Object> registrarFallBack(Document obj) {
		Document doc = service.registrarFallBack(obj);
		return new ResponseEntity<Object>(doc, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Document> modificar(@Valid @RequestBody Document docuemnt){
		Document doc = service.modificar(docuemnt);
		return new ResponseEntity<Document>(doc, HttpStatus.OK);
	}
	
	
	@GetMapping("/listarPageable")
	public ResponseEntity<Page<Document>> listarPageable(Pageable pageable){
		Page<Document> docs = service.listarPageable(pageable);
		return new ResponseEntity<Page<Document>>(docs, HttpStatus.OK);
	}
	
	@GetMapping("/listar/{id}")
	public ResponseEntity<Document> ListarPorId(@PathVariable("id") Long id) throws Exception {
		Document doc = service.leerPorId(id);
		
		return new ResponseEntity<Document>(doc, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Long id){
		Document doc = service.leerPorId(id);
		if (doc.getId() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO: [" + id +"]");
		}
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	
}
