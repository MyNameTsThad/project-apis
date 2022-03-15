package xyz.thaddev.projectapis.timersystem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
public class TimerAPIController {
    private final TimerRepository timerRepository;

    public TimerAPIController(TimerRepository timerRepository) throws IOException {
        this.timerRepository = timerRepository;
        ProjectApisApplication.instance.setTimerAPIController(this);
        new Thread("loadFromFile") {
            @Override
            public void run() {
                try {
                    readFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @GetMapping("/api-v1/timer/getall")
    public List<Timer> getAllTimers() {
        return timerRepository.findAll();
    }

    @PostMapping("/api-v1/timer/new")
    public Timer newTimer(@RequestBody Timer newTimer) {
        if (newTimer.getLengthTime() > 0) {
            Timer saved = timerRepository.save(newTimer);
            ProjectApisApplication.instance.logger.info("Saved new Timer: " + newTimer.getId() + " length: " + newTimer.getLengthTime());
            new Thread("saveToFile") {
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
    public Timer getTimer(@RequestParam int id) {
        return timerRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException(id, false));
    }

    @PatchMapping("/api-v1/timer/set")
    public Timer changeTimer(@RequestBody Timer newTimer, @RequestParam int id) {
        if (newTimer.getLengthTime() > 0) {
            return timerRepository.findById(id)
                    .map(timer -> {
                        timer.setLengthTime(newTimer.getLengthTime());
                        Timer saved = timerRepository.save(timer);
                        new Thread("saveToFile") {
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
                        new Thread("saveToFile") {
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
    public void deleteTimer(@RequestParam int id) {
        if ((Object) timerRepository.findById(id) != Optional.empty()) {
            timerRepository.deleteById(id);
            new Thread("saveToFile") {
                @Override
                public void run() {
                    try {
                        saveToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            throw new TimerNotFoundException(id, false);
        }
    }

    @DeleteMapping("/api-v1/timer/delete/all")
    public void deleteAll(@RequestParam String authPassword) {
        if (authPassword.equals(ProjectApisApplication.authPassword)) {
            timerRepository.deleteAll();
            new Thread("saveToFile") {
                @Override
                public void run() {
                    try {
                        saveToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            throw new PermissionDeniedException();
        }
    }

    public void saveToFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(getAllTimers());
        File path = new File(System.getProperty("user.home") + "/projectapis/db/TimerRepository-current.json");
        FileOutputStream file = FileUtils.openOutputStream(path);
        try {
            file.write(json.getBytes());
            ProjectApisApplication.instance.logger.info("Saved Timer Repository to: " + System.getProperty("user.home") + "/projectapis/db/TimerRepository-current.json");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFromFile() throws IOException {
        try {
            File path = new File(System.getProperty("user.home") + "/projectapis/db/TimerRepository-current.json");
            String json = FileUtils.readFileToString(path);

            List<Timer> result = new Gson().fromJson(json, new TypeToken<List<Timer>>() {}.getType());
            timerRepository.saveAll(result);
            ProjectApisApplication.instance.logger.info("Loaded Timer Repository from: " + System.getProperty("user.home") + "/projectapis/db/TimerRepository-current.json");
        }catch (IOException e){
            if (e.getMessage().equals("File '/home/iwant2tryhard/projectapis/db/TimerRepository-current.json' does not exist")){
                ProjectApisApplication.instance.logger.warn("Storage Files not found; Creating empty files.");
                saveToFile();
                ProjectApisApplication.instance.logger.info("Successfully Created Storage files.");
            }
        }

    }
}
