package com.bandido.app.documents.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.core.env.Environment;
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

import com.bandido.app.documents.config.RibbonConfigurator;
import com.bandido.app.documents.exception.ModeloNotFoundException;
import com.bandido.app.documents.models.entity.Document;
import com.bandido.app.documents.models.service.IDocumentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RefreshScope
@RestController
@RibbonClient(name = "documents-service", configuration = RibbonConfigurator.class)
@RequestMapping("/app/documents")
public class DocumentController {
	
	@Autowired
	private Environment env;
	
	@Value("${configuracion.description}")
	private String texto;
	
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
	
	@GetMapping("/get-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
		
		log.info(texto);
		
		Map<String, String> json = new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		
		if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor,email"));
		}
		
		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
		
	}
	
	
	
}
