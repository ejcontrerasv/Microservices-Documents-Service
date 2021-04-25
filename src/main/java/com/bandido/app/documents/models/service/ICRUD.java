package com.bandido.app.documents.models.service;

import java.util.List;

public interface ICRUD<T> {

	T registrar(T obj);
	T registrarFallBack(T obj);
	T modificar(T obj);
	List<T> listar();
	T leerPorId(Long id);
	boolean eliminar(Long id);
}
