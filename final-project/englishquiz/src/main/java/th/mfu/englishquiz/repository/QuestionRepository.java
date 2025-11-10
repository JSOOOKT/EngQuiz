package th.mfu.englishquiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import th.mfu.englishquiz.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // This interface inherits all standard CRUD (Create, Read, Update, Delete) methods
    // for the Question entity. No custom methods are required based on the provided controllers.
}
