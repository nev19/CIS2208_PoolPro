package com.example.poolproapp.backend;

import com.example.poolproapp.backend.EmailService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailExecutor {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void sendEmailAsync(String to, String subject, String body) {
        executor.execute(() -> {
            try {
                EmailService.sendEmail(to, subject, body);
                // Handle success
            } catch (Exception e) {
                // Handle failure
            }
        });
    }
}