package com.example.demo;

import com.example.demo.entity.BoardEntity;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class BoardDataInitializer {

    private final BoardRepository boardRepository;

    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            IntStream.rangeClosed(1, 55).forEach(i -> {
                BoardEntity board = BoardEntity.builder()
                        .title("게시글 제목" + i)
                        .contents("게시글 내용" + i)
                        .author("작성자" + i)
                        .createdAt(LocalDateTime.now())
                        .build();
                boardRepository.save(board);
            });
        };
    }
}
