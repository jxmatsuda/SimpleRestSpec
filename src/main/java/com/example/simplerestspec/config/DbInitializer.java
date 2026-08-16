package com.example.simplerestspec.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class DbInitializer {

    public static void initialize() {
        String homeDir = System.getProperty("user.home");
        Path dbDirPath = Paths.get(homeDir, ".simpleRestSpec");
        Path dbFilePath = dbDirPath.resolve("simplerestspec.db");

        try {
            if (!Files.exists(dbDirPath)) {
                Files.createDirectories(dbDirPath);
                log.info("Created database directory: {}", dbDirPath);
            } else {
                log.info("Database directory already exists: {}", dbDirPath);
            }

            if (!Files.exists(dbFilePath)) {
                Files.createFile(dbFilePath);
                log.info("Created database file: {}", dbFilePath);
            } else {
                log.info("Database file already exists: {}", dbFilePath);
            }
        } catch (IOException e) {
            log.error("Failed to initialize database directory or file: {}", e.getMessage(), e);
            // 初回起動時の致命的なエラーとして例外をスローし、アプリケーションを停止させる
            throw new RuntimeException("Critical failure during database initialization", e);
        }
    }
}
