package com.bandido.app.documents.models.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bandido.app.documents.models.entity.Document;

public interface IDocumentRepo extends JpaRepository<Document, Long> {

}
