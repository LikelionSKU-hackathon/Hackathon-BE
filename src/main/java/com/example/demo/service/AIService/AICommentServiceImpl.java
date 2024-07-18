package com.example.demo.service.AIService;

import com.example.demo.domain.AIComment;
import com.example.demo.domain.Diary;
import com.example.demo.repository.AICommentRepository;
import com.example.demo.repository.DiaryRepository;
import com.example.demo.web.dto.ChatDTO.ChatRequestDTO;
import com.example.demo.web.dto.ChatDTO.ChatResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class AICommentServiceImpl implements AICommentService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final AICommentRepository aiCommentRepository;
    private final DiaryRepository diaryRepository;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    @Override
    public Diary generateAIComment(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow();
        String content = diary.getContent();
        String title = diary.getTitle();
        String mood = diary.getMood().toString();
        String username= diary.getMember().getUsername();

        String prompt = "You are an AI text generator that creates Korean answers for psychotherapy and modern people's mental health based on the mood, diary topic, and diary content of input information. " +
                "Your response should always be polite. " +
                "You have to say \"username\" at the beginning of a sentence and start a sentence."+
                "You should write your answers based on the input information (feel, diary topic, diary content) into upward, empathy, solution, and support, and you can't leave any of them out. " +
                "Each part (consolation, empathy, solution, support) should be written in 2 lines and the context of the 4 parts should be connected. " +
                "You have to combine each part in order to make it a single answer and return it, but when you make it a single answer, there should be no line change between each part.\n\n" +
                "username: "+username+ "\n"+
                "feel: " + mood + "\n" +
                "diary topic: " + title + "\n" +
                "diary content: " + content;

        ChatRequestDTO chatRequest = new ChatRequestDTO(model, prompt);
        HttpEntity<ChatRequestDTO> requestEntity = new HttpEntity<>(chatRequest, httpHeaders);

        ChatResponseDTO chatResponseDTO = restTemplate.postForObject(apiUrl, requestEntity, ChatResponseDTO.class);

        if (chatResponseDTO == null || chatResponseDTO.getChoices() == null || chatResponseDTO.getChoices().isEmpty()) {
            throw new RuntimeException("AI response is null or empty");
        }

        String aiComment = chatResponseDTO.getChoices().get(0).getMessage().getContent();

        AIComment newComment = AIComment.builder()
                .content(aiComment)
                .diary(diary)
                .build();

        aiCommentRepository.save(newComment);


        return diary;
    }
}
