package edu.ntnu.idatt2105.backend.dto.quiz;

import lombok.Builder;
import lombok.NonNull;


/**
 * This Data Transfer Object contains the information for changing a quiz.
 *
 * @param quizId            The id of the quiz.
 * @param newName           The changed name of the quiz.
 * @param newDescription    The changed description of the quiz.
 *
 * @author Trym Hamer Gudvangen
 * @version 1.0 24.03.2024
 */
@Builder
public record QuizUpdateDTO(@NonNull Long quizId, String newName, String newDescription) {
}
