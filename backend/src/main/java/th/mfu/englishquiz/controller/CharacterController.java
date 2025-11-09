package th.mfu.englishquiz.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import th.mfu.englishquiz.entity.GameCharacter;
import th.mfu.englishquiz.repository.CharacterRepository;
import th.mfu.englishquiz.entity.CharacterStatsUpdate;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterRepository characterRepository;

    @GetMapping   //Retrive all characteers
    public ResponseEntity<Collection<GameCharacter>> listGameCharacters(){
        List<GameCharacter> characters = characterRepository.findAll();
        return new ResponseEntity<>(characters,HttpStatus.OK);
    }
    

   @PostMapping // Creates a new character
    public ResponseEntity<GameCharacter> createCharacters(@RequestBody GameCharacter character){
        // Saves the new character and returns the saved object with 201 CREATED status
        GameCharacter savedCharacter = characterRepository.save(character);
        return new ResponseEntity<>(savedCharacter, HttpStatus.CREATED);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<GameCharacter> getCharacterById(@PathVariable Long id){
        // Use Optional to handle the case where the character might not exist
        Optional<GameCharacter> characterOptional = characterRepository.findById(id);

        if(!characterOptional.isPresent()){
            // If character is not found, return 404 NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // If found, return the character and 200 OK
        return new ResponseEntity<>(characterOptional.get(), HttpStatus.OK);
    }
    
    @PutMapping("/{id}") // Updates an existing character
    public ResponseEntity<GameCharacter> updateCharacter(@PathVariable Long id, @RequestBody GameCharacter updatedCharacterDetails) {
        // 1. Check if the character exists
        if (!characterRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 2. Get the existing character, update fields, and save
        GameCharacter characterToUpdate = characterRepository.findById(id).get();
        
        // Ensure the ID of the character being saved matches the path variable
        updatedCharacterDetails.setId(id);
        
        // Save the updated details; Spring Data JPA handles the update due to the matching ID
        GameCharacter savedCharacter = characterRepository.save(updatedCharacterDetails);
        
        // Returns the updated character with 200 OK status
        return new ResponseEntity<>(savedCharacter, HttpStatus.OK);
    }

     @DeleteMapping("/{id}") // Deletes a character
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        if (!characterRepository.existsById(id)) {
            // If the character doesn't exist, we can't delete it
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        
        characterRepository.deleteById(id);
        // Returns 204 NO_CONTENT to indicate successful deletion with no body
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }


    @PutMapping("/{id}/add-xp/{points}")
    public ResponseEntity<GameCharacter> addExperiencePoints(
            @PathVariable Long id, 
            @PathVariable int points) {

        Optional<GameCharacter> characterOptional = characterRepository.findById(id);

        if (!characterOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GameCharacter character = characterOptional.get();
        
        GameCharacter savedCharacter = characterRepository.save(character);
        
        return new ResponseEntity<>(savedCharacter, HttpStatus.OK);
    }

    
    @PostMapping("/{id}/reset")
    public ResponseEntity<GameCharacter> resetCharacterState(@PathVariable Long id) {
        Optional<GameCharacter> characterOptional = characterRepository.findById(id);

        if (!characterOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GameCharacter character = characterOptional.get();
        
        // Reset key character attributes
        character.setHealthPoints(100); // Set to default health
        character.setAttackPower(10);   // Set to default attack
        character.setDefense(5);        // Set to default defense
        // character.setLevel(1);       // Maybe reset level or score through the related User entity
        
        GameCharacter savedCharacter = characterRepository.save(character);
        
        return new ResponseEntity<>(savedCharacter, HttpStatus.OK);
    }
    
    
    @PutMapping("/{id}/stats")
    public ResponseEntity<GameCharacter> updateCharacterStats(
            @PathVariable Long id, 
            @RequestBody CharacterStatsUpdate statsUpdate) { // Need a simple DTO class called 'CharacterStatsUpdate'

        if (!characterRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GameCharacter character = characterRepository.findById(id).get();
        
        // Apply updates from the request body DTO
        character.setAttackPower(statsUpdate.getAttack());
        character.setDefense(statsUpdate.getDefense());
        character.setHealthPoints(statsUpdate.getHealth());

        GameCharacter savedCharacter = characterRepository.save(character);
        
        return new ResponseEntity<>(savedCharacter, HttpStatus.OK);
    }
}


