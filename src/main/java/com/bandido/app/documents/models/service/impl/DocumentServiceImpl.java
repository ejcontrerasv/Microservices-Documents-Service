package com.bandido.app.documents.models.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.bandido.app.documents.dto.Status;
import com.bandido.app.documents.exception.ModeloNotFoundException;
import com.bandido.app.documents.models.entity.Document;
import com.bandido.app.documents.models.repo.IDocumentRepo;
import com.bandido.app.documents.models.service.IDocumentService;

@Service("serviceRestTemplate")
public class DocumentServiceImpl implements IDocumentService{
	
	@Autowired
	private IDocumentRepo repo;
	
	@Autowired
	private RestTemplate clienteRest;

	@Override
	public Document registrar(Document obj) {
		Integer statusToCreate = 1;
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", statusToCreate.toString());
		Status status = clienteRest.getForObject("http://status-service/app/status/listar/{id}", Status.class, pathVariables);
		
		if (status.getId() == null) {
			throw new ModeloNotFoundException("NO SE ENCONTRO STATUS CON ID : [" + statusToCreate + "]");
		}
		
		obj.setNameStatus(status.getName());
		return repo.save(obj);
	}
	
	@Override
	public Document registrarFallBack(Document obj) {
		return repo.save(obj);
	}
	
	@Override
	public Document modificar(Document obj) {
		Integer statusToModified = 5;
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", statusToModified.toString());
		Status status = clienteRest.getForObject("http://status-service/app/status/listar/{id}", Status.class, pathVariables);
		
		if (status.getId() == null) {
			throw new ModeloNotFoundException("NO SE ENCONTRO STATUS CON ID : [" + statusToModified + "]");
		}
		
		obj.setNameStatus(status.getName());
		return repo.save(obj);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Document> listar() {
		return (List<Document>) repo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Document leerPorId(Long id) {
		Optional<Document> op = repo.findById(id);
		return op.isPresent() ? op.get() : new Document();
	}

	@Override
	public boolean eliminar(Long id) {
		repo.deleteById(id);
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Document> listarPageable(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	public byte[] leerDocument(Long id) {
		Optional<Document> op = repo.findById(id);
		return op.isPresent() ? op.get().getContentDocument() : new byte[0];
	}

}
