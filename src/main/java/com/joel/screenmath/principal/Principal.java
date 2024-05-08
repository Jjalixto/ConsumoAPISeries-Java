package com.joel.screenmath.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.joel.screenmath.model.DatosEpisodio;
import com.joel.screenmath.model.DatosSerie;
import com.joel.screenmath.model.DatosTemporada;
import com.joel.screenmath.service.ConsumoApi;
import com.joel.screenmath.service.ConvierteDatos;

public class Principal {

    private Scanner teclado = new Scanner(System.in);

    private ConsumoApi consumoApi = new ConsumoApi();

    private final String URL_BASE = "https://omdbapi.com/?t=";

    private final String API_KEY = "&apikey=4fc7c187";

    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu(){
        System.out.println("Por favor escribe el nombre de la serie que desees ver");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE +  nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de todas las temporadas
        List<DatosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
			json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY );
			var datosTemporadas = conversor.obtenerDatos(json, DatosTemporada.class);
			temporadas.add(datosTemporadas);
		}
		// temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para las temporadas
        // for (int i = 0; i < datos.totalDeTemporadas(); i++) {
        //     List<DatosEpisodio> episodioTemporada = temporadas.get(i).episodios();
        //     for (int j = 0; j < episodioTemporada.size(); j++) {
        //         System.out.println(episodioTemporada.get(j).titulo());
        //     }
        // }

        //Mejora usando expresiones lambda
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las informaciones a una lista del tipo DatosEpisodio

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //Top 5 episodios
        System.out.println("Top 5 episodios");
        datosEpisodios.stream()
                    .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                    .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                    .limit(5)
                    .forEach(System.out::println);
    }
}
