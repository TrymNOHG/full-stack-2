package edu.ntnu.idatt2105.backend.dto.quiz.collaborator;

import lombok.Builder;
import lombok.NonNull;

/**
 * This record provides a data transfer object for a collaborator on a quiz.
 *
 * @param quizId    The id of the quiz.
 * @param userId    The id of the collaborator.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Builder
public record QuizAuthorLoadDTO(@NonNull Long quizId, @NonNull Long userId) {
}
