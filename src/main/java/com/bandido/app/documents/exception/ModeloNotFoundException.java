package com.bandido.app.documents.exception;

public class ModeloNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -407505003673660990L;

	public ModeloNotFoundException(String mensaje) {
		super(mensaje);
	}

}
