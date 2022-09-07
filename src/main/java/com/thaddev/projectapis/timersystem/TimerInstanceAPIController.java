package com.thaddev.projectapis.timersystem;

import com.thaddev.projectapis.PermissionDeniedException;
import com.thaddev.projectapis.ProjectApisApplication;
import com.thaddev.projectapis.timersystem.exceptions.TimerNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TimerInstanceAPIController {
    private final TimerInstanceRepository timerInstanceRepository;

    public TimerInstanceAPIController(TimerInstanceRepository timerInstanceRepository) {
        this.timerInstanceRepository = timerInstanceRepository;
        ProjectApisApplication.instance.setTimerInstanceAPIController(this);
    }

    @GetMapping("/api-v1/timer/instances/getall")
    private List<TimerInstance> getAllTimerInstances() {
        return timerInstanceRepository.findAll();
    }

    @PostMapping("/api-v1/timer/instances/new")
    private TimerInstance newTimerInstance(@RequestParam int timerId, @RequestParam(defaultValue = "false") boolean isComputerControl) {
        Timer timer = ProjectApisApplication.instance.getTimerAPIController().getTimer(timerId);

        if (isComputerControl) disableAllControllingTimers();
        TimerInstance timerInstance = timerInstanceRepository.save(new TimerInstance(timer, isComputerControl));
        ProjectApisApplication.instance.logger.info("TimerInstance created with base timer ID: " + timerId + " with ID: " + timerInstance.getId());
        if (isComputerControl) ProjectApisApplication.instance.getStatusResponseManager().setControllingTimerChanged();
        return timerInstance;
    }

    @GetMapping("/api-v1/timer/instances/get")
    private TimerInstance getTimerInstance(@RequestParam int id) {
        return timerInstanceRepository.findById(id)
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @GetMapping("/api-v1/timer/instances/getcontrolling")
    private TimerInstance getCurrentControllingTimerInstance() {
        List<TimerInstance> instances = getAllTimerInstances();
        for (TimerInstance instance : instances) {
            if (instance.isComputerControl()) {
                return instance;
            }
        }
        return null;
    }

    @PatchMapping("/api-v1/timer/instances/pause")
    private TimerInstance pauseTimerInstance(@RequestParam int id) {
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    timerInstance.setPaused(true);
                    TimerInstance updated = timerInstanceRepository.save(timerInstance);
                    ProjectApisApplication.instance.logger.info("Timer instance ID: " + id + " paused.");
                    if (timerInstance.isComputerControl()) ProjectApisApplication.instance.getStatusResponseManager().setTimerLifeCycleChanged();
                    return updated;
                })
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @PatchMapping("/api-v1/timer/instances/start")
    private TimerInstance startTimerInstance(@RequestParam int id) {
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    timerInstance.setPaused(false);
                    TimerInstance updatedInstance = timerInstanceRepository.save(timerInstance);
                    ProjectApisApplication.instance.logger.info("Timer instance ID: " + id + " started.");
                    if (timerInstance.isComputerControl()) ProjectApisApplication.instance.getStatusResponseManager().setTimerLifeCycleChanged();
                    return updatedInstance;
                })
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @PatchMapping("/api-v1/timer/instances/computercontrol")
    private TimerInstance setTimerInstanceComputerControl(@RequestParam int id, @RequestParam(defaultValue = "true") boolean computerControl) {
        return timerInstanceRepository.findById(id)
                .map(timerInstance -> {
                    if (computerControl) disableAllControllingTimers();
                    timerInstance.setComputerControl(computerControl);
                    TimerInstance saved = timerInstanceRepository.save(timerInstance);
                    ProjectApisApplication.instance.logger.info("Timer instance ID: " + id + " set to be computerControl.");
                    ProjectApisApplication.instance.getStatusResponseManager().setControllingTimerChanged();
                    return saved;
                })
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    @DeleteMapping("/api-v1/timer/instances/delete")
    private void deleteTimerInstance(@RequestParam int id) {
        if ((Object) timerInstanceRepository.findById(id) != Optional.empty()) {
            if (timerInstanceRepository.findById(id).isPresent() && timerInstanceRepository.findById(id).get().isComputerControl())
                ProjectApisApplication.instance.getStatusResponseManager().setControllingTimerChanged();
            timerInstanceRepository.deleteById(id);
            ProjectApisApplication.instance.logger.info("Timer instance ID: " + id + " deleted.");
        } else {
            throw new TimerNotFoundException(id, true);
        }
    }

    @DeleteMapping("/api-v1/timer/instances/delete/all")
    private void deleteAll(@RequestParam String authPassword) {
        if (authPassword.equals(ProjectApisApplication.authPassword)) {
            timerInstanceRepository.deleteAll();
            ProjectApisApplication.instance.logger.info("All timer instances deleted.");
            ProjectApisApplication.instance.getStatusResponseManager().setControllingTimerChanged();
        } else {
            throw new PermissionDeniedException();
        }
    }

    private void disableAllControllingTimers() {
        List<TimerInstance> instances = getAllTimerInstances();
        for (TimerInstance instance : instances) {
            int id = instance.getId();
            timerInstanceRepository.findById(id)
                    .map(timerInstance -> {
                        timerInstance.setComputerControl(false);
                        TimerInstance saved = timerInstanceRepository.save(timerInstance);
                        ProjectApisApplication.instance.logger.info("All TimerInstances set to be not computerControl.");
                        ProjectApisApplication.instance.getStatusResponseManager().setControllingTimerChanged();
                        return saved;
                    });
        }
    }

    public void tickAll(boolean is10thSecond) {
        //ProjectApisApplication.instance.logger.info("Tick all");
        List<TimerInstance> instances = getAllTimerInstances();
        for (TimerInstance instance : instances) {
            if (instance.isPaused()) {
                timerInstanceRepository.findById(instance.getId())
                        .map(timerInstance -> {
                            timerInstance.setTimePaused(timerInstance.getTimePaused() + 1000);
                            timerInstance.setEndTime(timerInstance.getEndTime() + 1000);
                            TimerInstance saved = timerInstanceRepository.save(timerInstance);
                            return saved;
                        });
            }
            if (is10thSecond && getCurrentControllingTimerInstance() != null) ProjectApisApplication.instance.getStatusResponseManager().setTimerLifeCycleChanged();
        }
    }
}
