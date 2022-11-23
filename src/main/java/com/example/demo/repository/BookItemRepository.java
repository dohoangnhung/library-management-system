package com.example.demo.repository;

import com.example.demo.entity.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, String> {

    @Query(
            nativeQuery = true,
            value = "select * from book_item where isbn = ?1 and status = 'AVAILABLE'"
    )
    List<BookItem> selectAvailableBookItems(String isbn);

}
