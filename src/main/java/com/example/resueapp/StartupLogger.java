package com.example.resueapp;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class StartupLogger implements CommandLineRunner {

    private final Environment environment;

    @Override
    public void run(String... args) {
        System.out.println(
                "Active Profiles: "
                        + Arrays.toString(
                                environment.getActiveProfiles()
                        )
        );
    }
}