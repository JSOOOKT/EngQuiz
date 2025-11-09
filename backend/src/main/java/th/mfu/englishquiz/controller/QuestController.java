package th.mfu.englishquiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import th.mfu.englishquiz.entity.Quiz;
import th.mfu.englishquiz.entity.Quest;
import th.mfu.englishquiz.repository.QuestRepository;
import th.mfu.englishquiz.repository.QuizRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/quests")
@CrossOrigin(origins = "*")  // Allow frontend connections
public class QuestController {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuizRepository quizRepository;

    // GET all quests
    @GetMapping
    public ResponseEntity<Collection<Quest>> getAllQuests() {
        List<Quest> quests = questRepository.findAll();
        return new ResponseEntity<>(quests, HttpStatus.OK);
    }

    // GET quest by ID
    @GetMapping("/{id}")
    public ResponseEntity<Quest> getQuestById(@PathVariable Long id) {
        Optional<Quest> quest = questRepository.findById(id);
        if (quest.isPresent()) {
            return new ResponseEntity<>(quest.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // CREATE new quest
    @PostMapping
    public ResponseEntity<String> createQuest(@RequestBody Quest newQuest) {
        if (newQuest.getQuestName() == null || newQuest.getQuestName().trim().isEmpty()) {
            return new ResponseEntity<>("Quest name cannot be empty", HttpStatus.BAD_REQUEST);
        }
        questRepository.save(newQuest);
        return new ResponseEntity<>("Quest created successfully", HttpStatus.CREATED);
    }

    // UPDATE existing quest
    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuest(@PathVariable Long id, @RequestBody Quest updatedQuest) {
        Quest quest = questRepository.findById(id).orElse(null);
        if (quest != null) {
            quest.setQuestName(updatedQuest.getQuestName());
            quest.setDescription(updatedQuest.getDescription());
            quest.setDifficultyLevel(updatedQuest.getDifficultyLevel());
            quest.setRewardPoints(updatedQuest.getRewardPoints());
            quest.setLearningFocus(updatedQuest.getLearningFocus());
            questRepository.save(quest);
            return new ResponseEntity<>("Quest updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Quest not found", HttpStatus.NOT_FOUND);
        }
    }

    // DELETE quest
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuest(@PathVariable Long id) {
        if (!questRepository.existsById(id)) {
            return new ResponseEntity<>("Quest not found", HttpStatus.NOT_FOUND);
        }
        questRepository.deleteById(id);
        return new ResponseEntity<>("Quest deleted successfully", HttpStatus.NO_CONTENT);
    }

    // GET all quizzes for a specific quest
    @GetMapping("/{questId}/quizzes")
    public ResponseEntity<Collection<Quiz>> getQuizzesByQuest(@PathVariable Long questId) {
        Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<Quiz> quizzes = quizRepository.findByQuestId(questId);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    // CREATE a new quiz for a specific quest
    @PostMapping("/{questId}/quizzes")
    public ResponseEntity<String> addQuizToQuest(@PathVariable Long questId, @RequestBody Quiz newQuiz) {
        Optional<Quest> quest = questRepository.findById(questId);
        if (quest.isEmpty()) {
            return new ResponseEntity<>("Quest not found", HttpStatus.NOT_FOUND);
        }

        newQuiz.setQuest(quest.get());
        quizRepository.save(newQuiz);
        return new ResponseEntity<>("Quiz added successfully", HttpStatus.CREATED);
    }
}


