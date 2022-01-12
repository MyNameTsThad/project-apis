package xyz.thaddev.projectapis.timersystem;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xyz.thaddev.projectapis.timersystem.exceptions.TimerNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
public class TimerInstanceAPIController {
    private final TimerInstanceRepository timerInstanceRepository;

    public TimerInstanceAPIController(TimerInstanceRepository timerInstanceRepository) {
        this.timerInstanceRepository = timerInstanceRepository;
    }

    @GetMapping("/api-v1/timer/instances/getall")
    private List<TimerInstance> getAllTimerInstances(){
        return timerInstanceRepository.findAll();
    }

    @PostMapping("/api-v1/timer/instances/new")
    private TimerInstance newTimerInstance(@RequestParam int timerId, @RequestParam(defaultValue = "false") boolean isComputerControl){
        String uri = "http://localhost:8080/api-v1/timer/get?id=" + timerId;
        RestTemplate template = new RestTemplate();
        Timer timer = template.getForObject(uri, Timer.class);
        if (timer != null){
            if (isComputerControl) disableAllControllingTimers();
            return timerInstanceRepository.save(new TimerInstance(timer, isComputerControl));
        }
        throw new TimerNotFoundException(timerId, false);
    }

    @GetMapping("/api-v1/timer/instances/get")
    private TimerInstance getTimerInstance(@RequestParam int id){
        return timerInstanceRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @GetMapping("/api-v1/timer/instances/getcontrolling")
    private TimerInstance getCurrentControllingTimerInstance(){
        List<TimerInstance> instances = getAllTimerInstances();
        for (TimerInstance instance : instances) {
            if (instance.isComputerControl()){
                return instance;
            }
        }
        return null;
    }

    @PatchMapping("/api-v1/timer/instances/pause")
    private TimerInstance pauseTimerInstance(@RequestParam int id){
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    timerInstance.setPaused(true);
                    return timerInstanceRepository.save(timerInstance);
                })
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @PatchMapping("/api-v1/timer/instances/start")
    private TimerInstance startTimerInstance(@RequestParam int id){
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    timerInstance.setPaused(false);
                    return timerInstanceRepository.save(timerInstance);
                })
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @PatchMapping("/api-v1/timer/instances/computercontrol")
    private TimerInstance setTimerInstanceComputerControl(@RequestParam int id){
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    disableAllControllingTimers();
                    timerInstance.setComputerControl(true);
                    return timerInstanceRepository.save(timerInstance);
                })
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @DeleteMapping("/api-v1/timer/instances/delete")
    private void deleteTimerInstance(@RequestParam int id){
        if ((Object) timerInstanceRepository.findById(id) != Optional.empty()){
            timerInstanceRepository.deleteById(id);
        }else{
            throw new TimerNotFoundException(id, true);
        }
    }

    private void disableAllControllingTimers(){
        List<TimerInstance> instances = getAllTimerInstances();
        for (TimerInstance instance : instances) {
            int id = instance.getId();
            timerInstanceRepository.findById(id)
                    .map(timerInstance -> {
                        timerInstance.setComputerControl(false);
                        return timerInstanceRepository.save(timerInstance);
                    });
        }
    }
}
