package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.example.repository.BookRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Singleton
@Startup
public class CoverImageInitializer {

    @EJB
    private BookRepository bookRepository;

    @PostConstruct
    public void loadCoverImages() {
        try {
            loadCoverImage(1, "book-covers/1.jpg", "image/jpg");
            loadCoverImage(2, "book-covers/2.jpg", "image/jpg");
            loadCoverImage(3, "book-covers/3.jpg", "image/jpg");
            loadCoverImage(4, "book-covers/4.jpg", "image/jpg");
            loadCoverImage(5, "book-covers/5.jpg", "image/jpg");

            System.out.println("Cover images successfully loaded");

        } catch (Exception e) {
            System.err.println("Error loading cover images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to load a single cover image from classpath
     */
    private void loadCoverImage(int bookId, String resourcePath, String contentType) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            byte[] imageBytes = is.readAllBytes();
            bookRepository.setBookCoverImage(bookId, imageBytes, contentType, null);
        }
    }

}