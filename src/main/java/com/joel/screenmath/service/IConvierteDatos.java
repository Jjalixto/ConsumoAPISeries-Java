package com.joel.screenmath.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
