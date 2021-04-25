package com.bandido.app.documents.models.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "Informacion asociada a los Documentos")
@Entity
@Data
@Table(name= "documents")
public class Document implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(notes = "El nombre del documento debe tener minimo 3 caracteres", required =true)
	@Size(min = 2 , message = "El nombre del documento debe tener minimo 3 caracteres")
	@Column(name = "nombre")
	private String name;
	
	@Column(name = "create_at")
	private LocalDateTime createAt;
	
	@Column(name = "contenido_documento")
	private  byte[] contentDocument;
	
	@ApiModelProperty(notes = "La extension debe tener al menos 2 caracteres", required = false)
	@Size(min = 2, message ="La extension debe tener al menos 2 caracteres")
	@Column(name = "extension")
	private String extensionFile; 
	
	@ApiModelProperty(notes = "id del tipo de documento documents-type-service", required = true)
	@Column(name = "type_document_id")
	private Integer idTypeDocument;
	
	@JsonIgnoreProperties
	@ApiModelProperty(notes = "nombre del estado del documento status-service", required = true)
	@Column(name = "status_name", nullable = false, updatable = true)
	private String nameStatus;
	
	private static final long serialVersionUID = 7344364126529500873L;
}
