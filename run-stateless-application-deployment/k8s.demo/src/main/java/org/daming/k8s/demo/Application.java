package org.daming.k8s.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/k8s-test")
@SpringBootApplication
public class Application {

	/**
     * get timestamp
     * @return
     */
    @GetMapping(value = "/timestamp")
    public ResponseEntity<?> getTimestamp() {
        return ResponseEntity.ok(System.currentTimeMillis() + "\n");
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
