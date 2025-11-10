package th.mfu.englishquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import th.mfu.englishquiz.entity.Quiz;
import th.mfu.englishquiz.repository.QuizRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
@CrossOrigin(origins = "*") 
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    // --- C: CREATE a new Quiz (POST /quizzes) ---
    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz newQuiz) {
        if (newQuiz.getTitle() == null || newQuiz.getTitle().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        // Note: The Tier relationship must be set correctly in the frontend JSON payload
        Quiz savedQuiz = quizRepository.save(newQuiz);
        return new ResponseEntity<>(savedQuiz, HttpStatus.CREATED);
    }

    // --- R: Retrieve all Quizzes (GET /quizzes) ---
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    // --- R: Retrieve Quiz by ID (GET /quizzes/{id}) ---
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);

        if (quiz.isPresent()) {
            return new ResponseEntity<>(quiz.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // --- U: UPDATE an existing Quiz (PUT /quizzes/{id}) ---
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuizDetails) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);

        if (quizOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Quiz existingQuiz = quizOptional.get();
        
        // Update fields based on the provided entity details
        existingQuiz.setTitle(updatedQuizDetails.getTitle());
        existingQuiz.setTopic(updatedQuizDetails.getTopic());
        existingQuiz.setTotalQuestions(updatedQuizDetails.getTotalQuestions());
        existingQuiz.setQuestionType(updatedQuizDetails.getQuestionType());
        
        // Relationship update: If the new quiz details include a Tier, update it.
        if (updatedQuizDetails.getTier() != null) {
            existingQuiz.setTier(updatedQuizDetails.getTier()); // Uses setTier
        }

        Quiz savedQuiz = quizRepository.save(existingQuiz);
        return new ResponseEntity<>(savedQuiz, HttpStatus.OK);
    }

    // --- D: DELETE a Quiz (DELETE /quizzes/{id}) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        if (!quizRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        quizRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}