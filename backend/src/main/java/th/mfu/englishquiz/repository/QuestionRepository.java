package th.mfu.englishquiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import th.mfu.englishquiz.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Custom repository methods can be added here if needed,
    // e.g., List<Question> findByQuizId(Long quizId);
}