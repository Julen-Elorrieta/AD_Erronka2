package com.elorserv.elorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ElorapiApplication {

	public static void main(String[] args) {
		//AQUI HAY QUE INICIALIZAR SPRINGBOOT, SOCKET, HILOS, HIBERNATE Y TODO
		SpringApplication.run(ElorapiApplication.class, args);
	}

}
