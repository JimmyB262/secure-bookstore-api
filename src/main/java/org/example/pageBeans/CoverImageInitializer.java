package org.example.pageBeans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.example.repository.BookRepository;

import java.io.File;
import java.util.Arrays;

@Singleton
@Startup
public class CoverImageInitializer {

    @Inject
    private BookRepository bookRepository;

    @PostConstruct
    public void loadCoverImages() {
        try {
            bookRepository.setBookCoverImage(1, null,
                    new File(getClass().getClassLoader().getResource("book-covers/1.jpg").toURI()).getAbsolutePath(),
                    "image/jpg");
            bookRepository.setBookCoverImage(2, null,
                    new File(getClass().getClassLoader().getResource("book-covers/2.jpg").toURI()).getAbsolutePath(),
                    "image/jpg");
            bookRepository.setBookCoverImage(3, null,
                    new File(getClass().getClassLoader().getResource("book-covers/3.jpg").toURI()).getAbsolutePath(),
                    "image/jpg");
            bookRepository.setBookCoverImage(4, null,
                    new File(getClass().getClassLoader().getResource("book-covers/4.jpg").toURI()).getAbsolutePath(),
                    "image/jpg");
            bookRepository.setBookCoverImage(5, null,
                    new File(getClass().getClassLoader().getResource("book-covers/5.jpg").toURI()).getAbsolutePath(),
                    "image/jpg");

            System.out.println(" Cover images successfully loaded");

        } catch (Exception e) {
            System.err.println(" Error loading cover images: " + e.getMessage());
            e.printStackTrace();
        }
    }
}