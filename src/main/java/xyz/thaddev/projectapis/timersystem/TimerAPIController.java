package xyz.thaddev.projectapis.timersystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.thaddev.projectapis.PermissionDeniedException;
import xyz.thaddev.projectapis.ProjectApisApplication;
import xyz.thaddev.projectapis.timersystem.exceptions.InvalidTimerLengthException;
import xyz.thaddev.projectapis.timersystem.exceptions.TimerNotFoundException;

import java.io.FileWriter;
import java.io.IOException;
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
            Timer saved = timerRepository.save(newTimer);
            new Thread("saveToFile"){
                @Override
                public void run() {
                    try {
                        saveToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            return saved;
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
                        Timer saved = timerRepository.save(timer);
                        new Thread("saveToFile"){
                            @Override
                            public void run() {
                                try {
                                    saveToFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        return saved;
                    })
                    .orElseGet(() -> {
                        newTimer.setId(id);
                        Timer saved = timerRepository.save(newTimer);
                        new Thread("saveToFile"){
                            @Override
                            public void run() {
                                try {
                                    saveToFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        return saved;
                    });
        }
        throw new InvalidTimerLengthException(id, newTimer.getLengthTime());
    }

    @DeleteMapping("/api-v1/timer/delete")
    private void deleteTimer(@RequestParam int id){
        if ((Object) timerRepository.findById(id) != Optional.empty()){
            timerRepository.deleteById(id);
            new Thread("saveToFile"){
                @Override
                public void run() {
                    try {
                        saveToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }else{
            throw new TimerNotFoundException(id, false);
        }
    }

    @DeleteMapping("/api-v1/timer/delete/all")
    private void deleteAll(@RequestParam String authPassword){
        if (authPassword.equals(ProjectApisApplication.authPassword)){
            timerRepository.deleteAll();
            new Thread("saveToFile"){
                @Override
                public void run() {
                    try {
                        saveToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }else{
            throw new PermissionDeniedException();
        }
    }

    public void saveToFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(getAllTimers());
        FileWriter file = new FileWriter("/var/lib/projectapis/db/current.json");;
        try {
            file.write(json);
            ProjectApisApplication.instance.logger.info("Saved Timer Repository to: /var/lib/projectapis/db/timerRepository-current.json");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
