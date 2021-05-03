package qna.domain.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.Question;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
  List<Answer> findByQuestionAndDeletedFalse(Question question);

  Optional<Answer> findByIdAndDeletedFalse(Long id);
}