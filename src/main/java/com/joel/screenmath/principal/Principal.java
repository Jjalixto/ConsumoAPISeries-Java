package com.joel.screenmath.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.joel.screenmath.model.DatosSerie;
import com.joel.screenmath.model.DatosTemporada;
import com.joel.screenmath.model.Serie;
import com.joel.screenmath.service.ConsumoApi;
import com.joel.screenmath.service.ConvierteDatos;

public class Principal {

    private Scanner teclado = new Scanner(System.in);

    private ConsumoApi consumoApi = new ConsumoApi();

    private final String URL_BASE = "https://omdbapi.com/?t=";

    private final String API_KEY = "&apikey=4fc7c187";

    private ConvierteDatos conversor = new ConvierteDatos();

    private List<DatosSerie> datosSeries = new ArrayList<>();

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Mostrar series buscadas

                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                break;
                case 0:
                    System.out.println("Cerrando la aplicacion");
                    break;
                default:
                    System.out.println("Opcion invalida");;
            }
        }
    }

    private DatosSerie getDatosSerie(){
        System.out.println("Escribe el nombre de la serie que desees buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json,DatosSerie.class);
        return datos;
    }
    
    private void buscarEpisodioPorSerie(){
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporada> temporadas = new ArrayList<>();
        
        for(int i = 1; i<=datosSerie.totalDeTemporadas();i++){
            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+")+ "&season=" + i + API_KEY);
            DatosTemporada DatosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(DatosTemporada);
        }
        temporadas.forEach(System.out::println);
    }
    
    private void buscarSerieWeb(){
        DatosSerie datos = getDatosSerie();
        datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        List<Serie> series = new ArrayList<>();
        series = datosSeries.stream()
                .map(d -> new Serie(d))
                .collect(Collectors.toList());
        series.stream()
            .sorted(Comparator.comparing(Serie::getGenero))
            .forEach(System.out::println);
    }
}
