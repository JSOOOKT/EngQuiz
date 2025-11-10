package th.mfu.englishquiz.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import th.mfu.englishquiz.entity.GameCharacter;
import th.mfu.englishquiz.entity.CharacterStatsUpdate;
import th.mfu.englishquiz.repository.CharacterRepository;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterRepository characterRepository;

    // --- Get all characters ---
    @GetMapping
    public ResponseEntity<List<GameCharacter>> getAllCharacters() {
        return ResponseEntity.ok(characterRepository.findAll());
    }

    // --- Get by ID ---
    @GetMapping("/{id}")
    public ResponseEntity<GameCharacter> getCharacterById(@PathVariable Long id) {
        Optional<GameCharacter> character = characterRepository.findById(id);
        return character.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- Create ---
    @PostMapping
    public ResponseEntity<GameCharacter> createCharacter(@RequestBody GameCharacter character) {
        return new ResponseEntity<>(characterRepository.save(character), HttpStatus.CREATED);
    }

    // --- Update ---
    @PutMapping("/{id}")
    public ResponseEntity<GameCharacter> updateCharacter(@PathVariable Long id, @RequestBody GameCharacter updatedCharacter) {
        if (!characterRepository.existsById(id)) return ResponseEntity.notFound().build();

        updatedCharacter.setId(id);
        return ResponseEntity.ok(characterRepository.save(updatedCharacter));
    }

    // --- Delete ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        if (!characterRepository.existsById(id)) return ResponseEntity.notFound().build();
        characterRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // --- Add XP ---
    @PutMapping("/{id}/add-xp/{points}")
    public ResponseEntity<GameCharacter> addExperience(@PathVariable Long id, @PathVariable int points) {
        Optional<GameCharacter> characterOpt = characterRepository.findById(id);
        if (characterOpt.isEmpty()) return ResponseEntity.notFound().build();

        GameCharacter character = characterOpt.get();
        character.setHealthPoints(character.getHealthPoints() + points); // Example: XP adds to HP
        // TODO: Add proper XP/Leveling logic here
        return ResponseEntity.ok(characterRepository.save(character));
    }

    // --- Reset Stats ---
    @PostMapping("/{id}/reset")
    public ResponseEntity<GameCharacter> resetCharacter(@PathVariable Long id) {
        Optional<GameCharacter> characterOpt = characterRepository.findById(id);
        if (characterOpt.isEmpty()) return ResponseEntity.notFound().build();

        GameCharacter character = characterOpt.get();
        character.setHealthPoints(100);
        character.setAttackPower(10);
        character.setDefense(5);
        // TODO: Reset level or other stats if needed
        return ResponseEntity.ok(characterRepository.save(character));
    }

    // --- Update Stats ---
    @PutMapping("/{id}/stats")
    public ResponseEntity<GameCharacter> updateStats(@PathVariable Long id, @RequestBody CharacterStatsUpdate statsUpdate) {
        Optional<GameCharacter> characterOpt = characterRepository.findById(id);
        if (characterOpt.isEmpty()) return ResponseEntity.notFound().build();

        GameCharacter character = characterOpt.get();
        character.setAttackPower(statsUpdate.getAttack());
        character.setDefense(statsUpdate.getDefense());
        character.setHealthPoints(statsUpdate.getHealth());

        return ResponseEntity.ok(characterRepository.save(character));
    }
}
