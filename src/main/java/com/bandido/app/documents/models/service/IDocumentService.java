package com.bandido.app.documents.models.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bandido.app.documents.models.entity.Document;

public interface IDocumentService extends ICRUD<Document> {
	
	Page<Document> listarPageable(Pageable pageable);
	byte[] leerDocument(Long id);
}
