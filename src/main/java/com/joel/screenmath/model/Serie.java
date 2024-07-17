package com.joel.screenmath.model;

import java.util.OptionalDouble;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Serie {
    private String titulo;
    private Integer totalDeTemporadas; 
    private Double evaluacion;
    private Categoria genero;
    private String actores;
    private String poster;
    private String sinopsis;

    public Serie(DatosSerie datosSerie){
        this.titulo = datosSerie.titulo();
        this.totalDeTemporadas = datosSerie.totalDeTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0);
        this.poster = datosSerie.poster();
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.actores = datosSerie.actores();
        this.sinopsis = datosSerie.sinopsis();
    }
    @Override
    public String toString() {
        return "genero"+ genero;
    }
}
