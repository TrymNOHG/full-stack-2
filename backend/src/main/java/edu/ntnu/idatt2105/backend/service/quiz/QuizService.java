package edu.ntnu.idatt2105.backend.service.quiz;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadAllDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.collaborator.QuizAuthorDTO;
import edu.ntnu.idatt2105.backend.model.quiz.Difficulty;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizLoadDTO;
import edu.ntnu.idatt2105.backend.dto.quiz.QuizUpdateDTO;
import edu.ntnu.idatt2105.backend.exception.notfound.QuizNotFoundException;
import edu.ntnu.idatt2105.backend.mapper.quiz.QuizMapper;
import edu.ntnu.idatt2105.backend.model.quiz.Quiz;
import edu.ntnu.idatt2105.backend.model.quiz.QuizAuthor;
import edu.ntnu.idatt2105.backend.model.users.User;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizAuthorRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.QuizRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.MultipleChoiceRepository;
import edu.ntnu.idatt2105.backend.repo.quiz.question.QuestionRepository;
import edu.ntnu.idatt2105.backend.repo.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This service provides the logic for the Quiz entity.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 01.04.2024
 */
@Service
@RequiredArgsConstructor
public class QuizService {

    private final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizMapper quizMapper;
    private final MultipleChoiceRepository multipleChoiceRepository;
    private final QuizAuthorRepository quizAuthorRepository;

    public Quiz getQuizById(long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz with id " + quizId + " not found"));
    }

    @Transactional
    public QuizLoadDTO createQuiz(String quizName, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + adminEmail + " not found"));
        Quiz quiz = Quiz.builder()
                .quizName(quizName)
                .admin(admin)
                .difficulty(Difficulty.EASY)
                .build();
        quiz = quizRepository.save(quiz);
        return QuizLoadDTO.builder()
                .quizId(quiz.getQuizId())
                .quizName(quiz.getQuizName())
                .adminId(admin.getUserId())
                .difficulty(quiz.getDifficulty())
                .build();
    }

    /**
     * This method loads all quizzes available.
     * @return  All quizzes as QuizLoadAllDTO
     */
    public QuizLoadAllDTO loadAllQuiz(){
        List<Quiz> allQuizzes = quizRepository.findAll();
        return QuizLoadAllDTO
                .builder()
                .quizzes(allQuizzes.stream().map(quizMapper::quizToQuizLoadDTO).collect(Collectors.toSet()))
                .build();
    }

    @Transactional
    public QuizLoadDTO updateQuiz(QuizUpdateDTO quizUpdateDTO) {
        //TODO: check that user trying to change is owner or collaborator
        LOGGER.info("Attempting to retrieve quiz with id: " + quizUpdateDTO.quizId());
        Quiz quiz = quizRepository.findById(quizUpdateDTO.quizId())
                .orElseThrow(() -> new QuizNotFoundException("Id: " + quizUpdateDTO.quizId()));

        if (quizUpdateDTO.newName() != null) {
            LOGGER.info("Updating quiz name");
            quiz.setQuizName(quizUpdateDTO.newName());
        }

        if (quizUpdateDTO.newDescription() != null) {
            LOGGER.info("Updating quiz description");
            quiz.setQuizDescription(quizUpdateDTO.newDescription());
        }

        if (quizUpdateDTO.difficulty() != null) {
            LOGGER.info("Updating quiz difficulty");
            quiz.setDifficulty(quizUpdateDTO.difficulty());
        }

        Quiz savedQuiz = quizRepository.save(quiz);

        return quizMapper.quizToQuizLoadDTO(savedQuiz);
    }

    /**
     * This method deletes a quiz based on its id.
     * @param quizId    The id of the quiz.
     */
    public void deleteQuiz(Long quizId) {
        //TODO: check if owner
        LOGGER.info("Attempting to find quiz to delete.");
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz with id: " + quizId));
        LOGGER.info("Attempting to delete quiz.");
        quizRepository.delete(quiz);
        LOGGER.info("Quiz successfully deleted.");
    }

    /**
     * This method adds a new collaborator to a quiz.
     * @param newCollaborator   The new collaborator.
     */
    public void addCollaborator(QuizAuthorDTO newCollaborator) {
        //TODO: check if person adding collaborator can do so.
        //TODO: check that newCollaborator is not owner.
        LOGGER.info("Finding User");
        User user = userRepository.findById(newCollaborator.userId())
                .orElseThrow(() -> new UsernameNotFoundException("Id " + newCollaborator.userId()));
        LOGGER.info("User found. Looking for Quiz.");
        Quiz quiz = quizRepository.findById(newCollaborator.quizId())
                .orElseThrow(() -> new QuizNotFoundException(newCollaborator.quizId().toString()));
        LOGGER.info("Quiz found. Creating quiz author object.");
        QuizAuthor quizAuthor = QuizAuthor
                .builder()
                .quiz(quiz)
                .user(user)
                .build();

        LOGGER.info("Saving quiz author object.");
        quizAuthorRepository.save(quizAuthor);
        LOGGER.info("Quiz author saved.");
    }

    /**
     * This method removes a collaborator from a quiz.
     * @param newCollaborator   The collaborator to remove and the quiz.
     */
    public void removeCollaborator(QuizAuthorDTO newCollaborator) {
        //TODO: check that the user trying to remove the collaborator can.
        LOGGER.info("Looking for collaborator.");
        QuizAuthor quizAuthor = quizAuthorRepository
                .findQuizAuthorByQuizQuizIdAndUserUserId(newCollaborator.quizId(), newCollaborator.userId())
                .orElseThrow(() -> new UsernameNotFoundException("Quiz Id: " + newCollaborator.quizId()
                        + ". User Id: " + newCollaborator.userId()));
        LOGGER.info("Collaborator found.");
        quizAuthorRepository.delete(quizAuthor);
        LOGGER.info("Collaborator removed.");
    }


}
