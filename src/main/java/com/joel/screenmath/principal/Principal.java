package com.joel.screenmath.principal;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import com.joel.screenmath.model.DatosEpisodio;
import com.joel.screenmath.model.DatosSerie;
import com.joel.screenmath.model.DatosTemporada;
import com.joel.screenmath.model.Episodio;
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
                    .peek(e -> System.out.println("Primer filtro (N/A)" + e))
                    .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                    .peek(e -> System.out.println("Segundo ordenacion (M>m)" + e))
                    .map(e -> e.titulo().toUpperCase())
                    .peek(e -> System.out.println("Tercer filtro Mayusula (m>M)" + e))
                    .limit(5)
                    .forEach(System.out::println);

        //Convirtiendo los datos a una lista del tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                            .flatMap(t -> t.episodios().stream()
                            .map(d -> new Episodio(t.numero(),d)))
                            .collect(Collectors.toList());
        // episodios.forEach(System.out::println);
        
        //busqueda de episodios a partir de x año
        // System.out.println("Por favor indica el año desde donde quieres ver");
        // var fecha = teclado.nextInt();
        // teclado.nextLine();

        // LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // episodios.stream()
        //         .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
        //         .forEach(e -> System.out.println(
        //             "Temporada "+ e.getTemporada() + 
        //             "Episodio "+ e.getTitulo() + 
        //             "Fecha de Lanzamiento " + e.getFechaDeLanzamiento().format(dtf)
        //         ));

        //busca episodios por pedazo del titulo
        // System.out.println("Por favor escriba el titulo del episodio que desea ver");
        // var pedazoTitulo = teclado.nextLine();
        // Optional<Episodio> episodioBuscado = episodios.stream()
        //         .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
        //         .findFirst();
        // if(episodioBuscado.isPresent()){
        //     System.out.println("Episodio Encontrado");
        //     System.out.println("Los datos son: "+ episodioBuscado.get().getTitulo());
        // }else{
        //     System.out.println("Episodio no encontrado");
        // }

        Map<Integer , Double> evaluacionPorTemporada = episodios.stream()
                        .filter(e -> e.getEvaluacion() > 0.0)
                        .collect(Collectors.groupingBy(Episodio::getTemporada,
                                Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                        .filter(e -> e.getEvaluacion() > 0.0)
                        .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println(est);
        System.out.println("Media de las evaluaciones: "+ est.getAverage());
        System.out.println("Media de las evaluaciones: "+ est.getCount());
        System.out.println("Media de las evaluaciones: "+ est.getMax());
        System.out.println("Media de las evaluaciones: "+ est.getMin());
        System.out.println("Media de las evaluaciones: "+ est.getSum());

    }
}
