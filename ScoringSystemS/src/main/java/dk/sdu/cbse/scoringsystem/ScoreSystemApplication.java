package dk.sdu.cbse.scoringsystem;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@RestController
@CrossOrigin
public class ScoreSystemApplication {


    private int score = 0;

    public static void main(String[] args) {
        SpringApplication.run(ScoreSystemApplication.class, args);
    }

    @GetMapping("/score/get")
    public int getScore() {
        return this.score;
    }

    @PutMapping("/score/set/{value}")
    public int setScore(@PathVariable("value") int value) {
        this.score = value;
        return this.score;
    }


    @PutMapping("/score/add/{delta}")
    public int addToScore(@PathVariable("delta") int delta) {
        this.score += delta;
        return this.score;
    }
}