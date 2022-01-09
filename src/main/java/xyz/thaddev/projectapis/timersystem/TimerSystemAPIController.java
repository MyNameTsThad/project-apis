package xyz.thaddev.projectapis.timersystem;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.thaddev.projectapis.timersystem.exceptions.InvalidTimerLengthException;
import xyz.thaddev.projectapis.timersystem.exceptions.TimerNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TimerSystemAPIController {
    private final TimerRepository timerRepository;
    private final TimerInstanceRepository timerInstanceRepository;

    public TimerSystemAPIController(TimerRepository timerRepository, TimerInstanceRepository timerInstanceRepository) {
        this.timerRepository = timerRepository;
        this.timerInstanceRepository = timerInstanceRepository;
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
                .orElseThrow(() -> new TimerNotFoundException(id));
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
            throw new TimerNotFoundException(id);
        }
    }

    //instances

    @GetMapping("/api-v1/timer/instances/getall")
    private List<TimerInstance> getAllTimerInstances(){
        return timerInstanceRepository.findAll();
    }

    @PostMapping("/api-v1/timer/instances/new")
    private TimerInstance newTimerInstance(@RequestParam int timerId, @RequestParam(defaultValue = "false") boolean isComputerControl){
        Timer timer = getTimer(timerId);
        return timerInstanceRepository.save(new TimerInstance(timer, isComputerControl));
    }

    @GetMapping("/api-v1/timer/get")
    private TimerInstance getTimerInstance(@RequestParam int id){
        return timerInstanceRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException(id));
    }

    @PatchMapping("/api-v1/timer/instances/pause")
    private TimerInstance pauseTimerInstance(@RequestParam int id){
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    timerInstance.setPaused(true);
                    return timerInstanceRepository.save(timerInstance);
                })
                .orElseThrow(() -> new TimerNotFoundException(id));
    }

    @PatchMapping("/api-v1/timer/instances/start")
    private TimerInstance startTimerInstance(@RequestParam int id){
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    timerInstance.setPaused(false);
                    return timerInstanceRepository.save(timerInstance);
                })
                .orElseThrow(() -> new TimerNotFoundException(id));
    }
}
