package th.mfu.englishquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import th.mfu.englishquiz.entity.Quiz;
import th.mfu.englishquiz.entity.Tier; // Uses the new Tier entity
import th.mfu.englishquiz.repository.TierRepository; // Uses the new TierRepository
import th.mfu.englishquiz.repository.QuizRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/tiers") // Renamed base path
@CrossOrigin(origins = "*") 
public class TierController {

    @Autowired
    private TierRepository tierRepository; 

    @Autowired
    private QuizRepository quizRepository;

    // GET all tiers
    @GetMapping
    public ResponseEntity<Collection<Tier>> getAllTiers() { 
        List<Tier> tiers = tierRepository.findAll();
        return new ResponseEntity<>(tiers, HttpStatus.OK);
    }

    // GET tier by ID
    @GetMapping("/{id}")
    public ResponseEntity<Tier> getTierById(@PathVariable Long id) { 
        Optional<Tier> tier = tierRepository.findById(id);
        if (tier.isPresent()) {
            return new ResponseEntity<>(tier.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // CREATE new tier
    @PostMapping
    public ResponseEntity<String> createTier(@RequestBody Tier newTier) { 
        if (newTier.getTierName() == null || newTier.getTierName().trim().isEmpty()) { 
            return new ResponseEntity<>("Tier name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        tierRepository.save(newTier);
        return new ResponseEntity<>("Tier created successfully", HttpStatus.CREATED);
    }

    // UPDATE existing tier
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTier(@PathVariable Long id, @RequestBody Tier updatedTier) { 
        Tier tier = tierRepository.findById(id).orElse(null);
        if (tier != null) {
            tier.setTierName(updatedTier.getTierName()); 
            tier.setDescription(updatedTier.getDescription());
            tier.setDifficultyLevel(updatedTier.getDifficultyLevel());
            tier.setRewardPoints(updatedTier.getRewardPoints());
            tier.setLearningFocus(updatedTier.getLearningFocus());
            tierRepository.save(tier);
            return new ResponseEntity<>("Tier updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Tier not found", HttpStatus.NOT_FOUND);
        }
    }

    // DELETE tier
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTier(@PathVariable Long id) { 
        if (!tierRepository.existsById(id)) {
            return new ResponseEntity<>("Tier not found", HttpStatus.NOT_FOUND);
        }
        tierRepository.deleteById(id);
        return new ResponseEntity<>("Tier deleted successfully", HttpStatus.NO_CONTENT);
    }

    // GET all quizzes for a specific tier
    @GetMapping("/{tierId}/quizzes") 
    public ResponseEntity<Collection<Quiz>> getQuizzesByTier(@PathVariable Long tierId) { 
        Optional<Tier> tier = tierRepository.findById(tierId);
        if (tier.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Collection<Quiz> quizzes = quizRepository.findByTierId(tierId); // Calls the new repository method
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    // CREATE a new quiz for a specific tier
    @PostMapping("/{tierId}/quizzes") 
    public ResponseEntity<String> addQuizToTier(@PathVariable Long tierId, @RequestBody Quiz newQuiz) { 
        Optional<Tier> tier = tierRepository.findById(tierId);
        if (tier.isEmpty()) {
            return new ResponseEntity<>("Tier not found", HttpStatus.NOT_FOUND);
        }

        newQuiz.setTier(tier.get()); 
        quizRepository.save(newQuiz);
        return new ResponseEntity<>("Quiz added successfully", HttpStatus.CREATED);
    }
}