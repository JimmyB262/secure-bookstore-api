package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.example.repository.BookRepository;

@Singleton
@Startup
public class CoverImageInitializer {

    @Inject
    private BookRepository bookService;

    @PostConstruct
    public void loadCoverImages() {
        try {
            bookService.setBookCoverImage(1, "C:/Users/d_mpallas/Desktop/wildfly-32.0.1.Final/standalone/data/book-covers/1.jpg", "image/jpeg");
            bookService.setBookCoverImage(2, "C:/Users/d_mpallas/Desktop/wildfly-32.0.1.Final/standalone/data/book-covers/2.jpg", "image/jpeg");
            bookService.setBookCoverImage(3, "C:/Users/d_mpallas/Desktop/wildfly-32.0.1.Final/standalone/data/book-covers/3.jpg", "image/jpeg");
            bookService.setBookCoverImage(4, "C:/Users/d_mpallas/Desktop/wildfly-32.0.1.Final/standalone/data/book-covers/4.jpg", "image/jpeg");
            bookService.setBookCoverImage(5, "C:/Users/d_mpallas/Desktop/wildfly-32.0.1.Final/standalone/data/book-covers/5.jpg", "image/jpeg");

            System.out.println("✅ Cover images successfully loaded");

        } catch (Exception e) {
            System.err.println("❌ Error loading cover images: " + e.getMessage());
            e.printStackTrace();
        }
    }
}