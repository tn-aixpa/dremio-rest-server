// SPDX-FileCopyrightText: © 2025 DSLab - Fondazione Bruno Kessler
//
// SPDX-License-Identifier: Apache-2.0

package it.digitalhub.dremiorestserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DremioRestServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DremioRestServerApplication.class, args);
	}

}
