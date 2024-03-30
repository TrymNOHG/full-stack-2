package edu.ntnu.idatt2105.backend.dto.quiz.question;

import edu.ntnu.idatt2105.backend.model.quiz.question.Difficulty;
import edu.ntnu.idatt2105.backend.model.quiz.question.Type;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

public record QuestionCreateDTO(@NonNull Long quizId, @NonNull String question, @NonNull String answer,
                                @NonNull Type type, @NonNull Difficulty difficulty,
                                @Nullable Set<MultipleChoiceDTO> choices) {
}
