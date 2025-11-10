package th.mfu.englishquiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import th.mfu.englishquiz.entity.Quiz;
import java.util.List;
import java.util.Collection;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    // Custom method to find all quizzes belonging to a specific Tier ID
    // Spring Data JPA automatically implements this based on the Quiz entity's 'tier' field.
    Collection<Quiz> findByTierId(Long tierId); 
}