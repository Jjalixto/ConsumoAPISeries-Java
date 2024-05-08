package com.joel.screenmath;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.joel.screenmath.principal.EjemploStreams;
import com.joel.screenmath.principal.Principal;

@SpringBootApplication
public class ScreenmathApplication {

	public static void main(String[] args) {
		
		Principal principal = new Principal();
		principal.muestraElMenu();

		EjemploStreams ejemploStreams = new EjemploStreams();
		ejemploStreams.muestraEjemplo();
	}

}
