package xyz.thaddev.projectapis.timersystem;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.thaddev.projectapis.timersystem.exceptions.InvalidTimerLengthException;
import xyz.thaddev.projectapis.timersystem.exceptions.TimerNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
public class TimerAPIController {
    private final TimerRepository timerRepository;

    public TimerAPIController(TimerRepository timerRepository) {
        this.timerRepository = timerRepository;
    }

    @GetMapping("/api-v1/timer/getall")
    private List<Timer> getAllTimers(){
        return timerRepository.findAll();
    }

    @PostMapping("/api-v1/timer/new")
    private Timer newTimer(@RequestBody Timer newTimer){
        if (newTimer.getLengthTime() > 0){
            return timerRepository.save(newTimer);
        }
        throw new InvalidTimerLengthException(newTimer.getId(), newTimer.getLengthTime());
    }

    @GetMapping("/api-v1/timer/get")
    private Timer getTimer(@RequestParam int id){
        return timerRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException(id, false));
    }

    @PatchMapping("/api-v1/timer/set")
    private Timer changeTimer(@RequestBody Timer newTimer, @RequestParam int id){
        if (newTimer.getLengthTime() > 0){
            return timerRepository.findById(id)
                    .map(timer -> {
                        timer.setLengthTime(newTimer.getLengthTime());
                        return timerRepository.save(timer);
                    })
                    .orElseGet(() -> {
                        newTimer.setId(id);
                        return timerRepository.save(newTimer);
                    });
        }
        throw new InvalidTimerLengthException(id, newTimer.getLengthTime());
    }

    @DeleteMapping("/api-v1/timer/delete")
    private void deleteTimer(@RequestParam int id){
        if ((Object) timerRepository.findById(id) != Optional.empty()){
            timerRepository.deleteById(id);
        }else{
            throw new TimerNotFoundException(id, false);
        }
    }


}
