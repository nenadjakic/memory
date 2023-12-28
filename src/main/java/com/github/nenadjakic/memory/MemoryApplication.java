package com.github.nenadjakic.memory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class MemoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoryApplication.class, args);
    }
}
