package com.example.demo.service.AIService;

import com.example.demo.apiPayload.code.status.ErrorStatus;
import com.example.demo.apiPayload.exception.handler.AiHandler;
import com.example.demo.apiPayload.exception.handler.MemberHandler;
import com.example.demo.domain.*;
import com.example.demo.domain.mapping.MemberQuestion;
import com.example.demo.repository.*;
import com.example.demo.web.dto.ChatDTO.ChatRequestDTO;
import com.example.demo.web.dto.ChatDTO.ChatResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AICommentServiceImpl implements AICommentService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final SpicyAICommentRepository spicyAICommentRepository;
    private final AICommentRepository aiCommentRepository;
    private final AIQuestionRepository aiQuestionRepository;
    private final MemberQuestionRepository memberQuestionRepository;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
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
                "The entire response must be within 300 characters.\n\n"+
                "username: "+username+ "\n"+
                "feel: " + mood + "\n" +
                "diary topic: " + title + "\n" +
                "diary content: " + content;

        ChatRequestDTO chatRequest = new ChatRequestDTO(model, prompt);
        HttpEntity<ChatRequestDTO> requestEntity = new HttpEntity<>(chatRequest, httpHeaders);

        ChatResponseDTO chatResponseDTO = restTemplate.postForObject(apiUrl, requestEntity, ChatResponseDTO.class);

        if (chatResponseDTO == null || chatResponseDTO.getChoices() == null || chatResponseDTO.getChoices().isEmpty()) {
            throw new AiHandler(ErrorStatus.AI_RESPONSE_NULL_OR_EMPTY);
        }

        String aiComment = chatResponseDTO.getChoices().get(0).getMessage().getContent();

        AIComment newComment = AIComment.builder()
                .content(aiComment)
                .diary(diary)
                .build();

        aiCommentRepository.save(newComment);
        return diary;
    }
    @Override
    public AIQuestion generateAIQuestion(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        List<String> keywordList = member.getMemberKeywordList() != null ?
                member.getMemberKeywordList().stream()
                        .map(memberKeyword -> memberKeyword.getKeyword().getCategory())
                        .collect(Collectors.toList()) :
                new ArrayList<>();
        String ageGroup = member.getAgeGroup();
        String keyword1 = keywordList.size() > 0 ? keywordList.get(0) : "";
        String keyword2 = keywordList.size() > 1 ? keywordList.get(1) : "";
        String keyword3 = keywordList.size() > 2 ? keywordList.get(2) : "";

        String prompt = "You are an AI diary subject question generator that generates Korean answers based on your three keywords and age group. " +
                "Based on the user's current three keywords and age group you provide, you should create a diary theme that can reflect on your day. " +
                "Your answer should always be polite and answer in Korean " +
                "Considering that the three keywords I currently provide are the user's current situation, I set one of the user's current keywords as the main keyword and create a diary topic related to this. " +
                "You have to write a diary topic in one line. " +
                "You must return the selected main keyword and generated diary theme in the form (selected main keyword, diary topic).\n\n" +
                "This form should not contain anything else, but only the result value will be returned.\n\n"+
                "keywords: " + keyword1 + ", " + keyword2 + ", " + keyword3 + "\n" +
                "age group: " + ageGroup;

        String aiResponse;
        boolean validResponse = false;
        do {
            ChatRequestDTO chatRequest = new ChatRequestDTO(model, prompt);
            HttpEntity<ChatRequestDTO> requestEntity = new HttpEntity<>(chatRequest, httpHeaders);

            ChatResponseDTO chatResponseDTO = restTemplate.postForObject(apiUrl, requestEntity, ChatResponseDTO.class);

            if (chatResponseDTO == null || chatResponseDTO.getChoices() == null || chatResponseDTO.getChoices().isEmpty()) {
                throw new AiHandler(ErrorStatus.AI_RESPONSE_NULL_OR_EMPTY);
            }

            aiResponse = chatResponseDTO.getChoices().get(0).getMessage().getContent();

            // Check if the response matches the expected format
            if (aiResponse.matches("\\(.*\\,.*\\)")) {
                validResponse = true;
            } else {
                // Regenerate prompt if the response format is incorrect
                prompt = "Your previous response did not match the required format. Please return the selected main keyword and generated diary theme in the form (main keyword, diary topic).\n\n" +
                        "keywords: " + keyword1 + ", " + keyword2 + ", " + keyword3 + "\n" +
                        "age group: " + ageGroup;
            }
        } while (!validResponse);


        String mainKeyword = aiResponse.substring(aiResponse.indexOf('(') + 1, aiResponse.indexOf(',')).trim();
        String diaryTopic = aiResponse.substring(aiResponse.indexOf(',') + 1, aiResponse.indexOf(')')).trim();

        AIQuestion aiQuestion1 = AIQuestion.builder()
                .category(mainKeyword)
                .content(diaryTopic)
                .build();

        aiQuestionRepository.save(aiQuestion1);

        MemberQuestion memberQuestion = MemberQuestion.builder()
                .member(member)
                .aiQuestion(aiQuestion1)
                .build();

        memberQuestionRepository.save(memberQuestion);

        return aiQuestion1;
    }
    @Override
    public Diary generateSpicyAIComment(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow();
        String content = diary.getContent();
        String title = diary.getTitle();
        String mood = diary.getMood().toString();
        String username = diary.getMember().getUsername();

        String prompt = "You are an AI text generator that creates harsh and realistic comments for a diary entry. " +
                "Your response should be critical and provide realistic advice, even if it may hurt the user's feelings.\n\n" +
                "Your answer should be answer in Korean "+
                "The entire response must be within 300 characters.\n\n"+
                "username: " + username + "\n" +
                "feel: " + mood + "\n" +
                "diary topic: " + title + "\n" +
                "diary content: " + content + "\n" +
                "Please respond in Korean.";

        ChatRequestDTO chatRequest = new ChatRequestDTO(model, prompt);
        HttpEntity<ChatRequestDTO> requestEntity = new HttpEntity<>(chatRequest, httpHeaders);

        ChatResponseDTO chatResponseDTO = restTemplate.postForObject(apiUrl, requestEntity, ChatResponseDTO.class);

        if (chatResponseDTO == null || chatResponseDTO.getChoices() == null || chatResponseDTO.getChoices().isEmpty()) {
            throw new AiHandler(ErrorStatus.AI_RESPONSE_NULL_OR_EMPTY);
        }

        String spicyAiComment = chatResponseDTO.getChoices().get(0).getMessage().getContent();

        SpicyAIComment newSpicyComment = SpicyAIComment.builder()
                .content(spicyAiComment)
                .diary(diary)
                .build();

        spicyAICommentRepository.save(newSpicyComment);
        return diary;
    }

    @Override
    public Diary getDiaryWithSpicyComment(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow();
        return diary;
    }
}






