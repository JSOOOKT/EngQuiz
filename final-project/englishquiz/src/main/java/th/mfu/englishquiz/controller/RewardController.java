package th.mfu.englishquiz.controller;

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
import th.mfu.englishquiz.entity.Tier; 
import th.mfu.englishquiz.entity.Reward;
import th.mfu.englishquiz.repository.RewardRepository;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardRepository rewardRepository;

   @GetMapping // Retrieve all rewards (Uncommented and fixed)
    public ResponseEntity<List<Reward>> getAllRewards() {
        List<Reward> rewards = rewardRepository.findAll();
        // Returns the list of rewards with a 200 OK status
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reward> createReward(@RequestBody Reward newreward) {
        // Save the new entity and return the created object with 201 CREATED status
        Reward savedReward = rewardRepository.save(newreward);
        return new ResponseEntity<>(savedReward, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reward> getRewardById(@PathVariable Long id){
        // 1. Find the reward using Optional
        Optional<Reward> rewardOptional = rewardRepository.findById(id);

        if(!rewardOptional.isPresent()){
            // 2. If not found, return 404 NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // 3. If found, return the reward object with 200 OK
        return new ResponseEntity<>(rewardOptional.get(), HttpStatus.OK);
    }
    
   @PutMapping("/{id}")
    public ResponseEntity<Reward> updateReward(@PathVariable Long id, @RequestBody Reward updatedRewardDetails) {
        // 1. Check if the target reward exists
        if (!rewardRepository.existsById(id)) {
            // If it doesn't exist, return 404 NOT_FOUND
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 2. Ensure the ID of the object being saved matches the path variable
        updatedRewardDetails.setId(id);
        
        // 3. Save the updated details; Spring Data JPA handles the update
        Reward savedReward = rewardRepository.save(updatedRewardDetails);
        
        // Returns the updated reward object with 200 OK status
        return new ResponseEntity<>(savedReward, HttpStatus.OK);
    }


   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        if(!rewardRepository.existsById(id)){
            // If the reward doesn't exist, we can't delete it
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        rewardRepository.deleteById(id);
        // Returns 204 NO_CONTENT to indicate successful deletion with no response body
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
