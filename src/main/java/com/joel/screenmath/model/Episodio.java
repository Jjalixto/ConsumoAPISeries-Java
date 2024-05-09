package com.joel.screenmath.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Episodio {
    public Episodio(Integer numero, DatosEpisodio d) {
        this.temporada = numero;
        this.titulo = d.titulo();
        this.numeroEpisodio = d.numeroEpisodio();
        try {
            this.evaluacion = Double.valueOf(d.evaluacion());
        } catch (NumberFormatException e) {
            this.evaluacion = 0.0;
        }

        try {
            this.fechaDeLanzamiento = LocalDate.parse(d.fechaDeLanzamiento());
        } catch (DateTimeParseException e) {
            this.fechaDeLanzamiento = null;
        }
        
    }
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double evaluacion;
    private LocalDate fechaDeLanzamiento;

    @Override
    public String toString() {
        return 
            "temporada=" + temporada + 
            ", titulo= '" + titulo + '\'' +
            ", numeroEpisodio=" + numeroEpisodio +
            ", evaluacion=" + evaluacion +
            ", fechaDeLanzamiento=" + fechaDeLanzamiento;
    }
}
