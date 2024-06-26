package edu.ntnu.idatt2105.backend.unit.service;

import edu.ntnu.idatt2105.backend.dto.quiz.QuestionDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.MultipleChoiceCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionCreateDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.question.QuestionEditDTO;
import edu.ntnu.idatt2105.backend.model.quiz.question.QuestionType;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import edu.ntnu.idatt2105.backend.service.images.ImageService;
import edu.ntnu.idatt2105.backend.service.quiz.QuestionService;
import edu.ntnu.idatt2105.backend.service.quiz.QuizService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class QuestionServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuestionService questionService;

    @MockBean
    private ImageService imageService;


    @BeforeEach
    public void setUp() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("test@test.test", null, Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        userRepository.save(
                User.builder()
                        .email("test@test.test")
                        .password("password")
                        .username("test")
                        .build()
        );
    }

    @Test
    @Transactional
    void Create_question_test() throws IOException {
        QuizLoadDTO quizLoadDTO = quizService.createQuiz("test quiz", "test@test.test");
        questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.SHORT_ANSWER)
                .choices(null)
                .quizId(quizLoadDTO.quizId())
                .build(), "test@test.test");
        assertEquals(1, quizService.getQuizById(quizLoadDTO.quizId()).getQuestions().size());
    }

    @Test
    void Get_question_dto_test() throws IOException {
        QuizLoadDTO quizLoadDTO = quizService.createQuiz("test quiz", "test@test.test");
        quizLoadDTO = questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.SHORT_ANSWER)
                .quizId(quizLoadDTO.quizId())
                .build(), "test@test.test");
        long firstQuestionId = quizLoadDTO.questions().stream().findFirst().get().questionId();
        assertEquals("test question", questionService.getQuestionDTO(firstQuestionId, UUID.randomUUID()).question());
        assertEquals("test answer", questionService.getQuestionDTO(firstQuestionId, UUID.randomUUID()).answer());
        assertEquals(QuestionType.SHORT_ANSWER.name(), questionService.getQuestionDTO(firstQuestionId, UUID.randomUUID()).questionType());
    }

    @Test
    void Edit_question_test() throws IOException {
        QuizLoadDTO quizLoadDTO = quizService.createQuiz("test quiz", "test@test.test");
        quizLoadDTO = questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.SHORT_ANSWER)
                .quizId(quizLoadDTO.quizId())
                .build(), "test@test.test");
        questionService.editQuestion(QuestionEditDTO.builder()
                .question("edited question")
                .answer("edited answer")
                .type(QuestionType.MULTIPLE_CHOICE)
                .questionId(1L)
                .quizId(quizLoadDTO.quizId())
                .build(), "test@test.test");

        QuestionDTO dto = questionService.getQuestionDTO(quizLoadDTO.quizId(), UUID.randomUUID());
        assertEquals("edited question", dto.question());
        assertEquals("edited answer", dto.answer());
        assertEquals(QuestionType.MULTIPLE_CHOICE.name(), dto.questionType());
    }

    @Test
    void Delete_question_test() throws IOException {
        quizService.createQuiz("test quiz", "test@test.test");
        questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.SHORT_ANSWER)
                .quizId(1L)
                .build(), "test@test.test");
        questionService.deleteQuestion(1L, "test@test.test");
        assertThrows(NoSuchElementException.class, () -> questionService.getQuestionDTO(1L, UUID.randomUUID()));
    }

    @Test
    void Add_multiple_choice_question_test() throws IOException {
        quizService.createQuiz("test quiz", "test@test.test");
        questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.MULTIPLE_CHOICE)
                .quizId(1L)
                .build(), "test@test.test");
        questionService.addQuestion(QuestionCreateDTO.builder()
                .question("test question")
                .answer("test answer")
                .type(QuestionType.MULTIPLE_CHOICE)
                .quizId(1L)
                .choices(Collections.singleton(MultipleChoiceCreateDTO.builder()
                        .alternative("choice")
                        .isCorrect(true)
                        .build()))
                .build(), "test@test.test");
    }
}
