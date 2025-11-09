package th.mfu.englishquiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import th.mfu.englishquiz.entity.Quiz;


public interface QuizRepository extends JpaRepository<Quiz, Long> {
        // List<Quiz> findByCategory(String category);
        List<Quiz> findByQuestId(Long questId);

}

