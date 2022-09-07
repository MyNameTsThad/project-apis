package com.thaddev.projectapis.chatthreadsystem;

import com.thaddev.projectapis.ProjectApisApplication;
import com.thaddev.projectapis.chatthreadsystem.exceptions.ThreadNotFoundException;
import com.thaddev.projectapis.timersystem.exceptions.TimerNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ThreadAPIController {
    //Thread repository
    private final ThreadRepository threadRepository;

    //constructor
    public ThreadAPIController(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
        ProjectApisApplication.instance.setThreadAPIController(this);
    }

    //GET request that returns all threads (option to sort by created date ascending or descending)
    //Usage: /api-v1/chat/threads/getall?sort=asc/dsc
    @GetMapping("/api-v1/chat/threads/getall")
    public List<Thread> getAllThreads(@RequestParam(required = false) String sort) {
        List<Thread> threads = new ArrayList<>();
        //if the sort parameter is not null
        if (sort != null) {
            List<Thread> t = threadRepository.findAll();
            for (int i = 0; i < t.size(); i++) {
                for (int j = i + 1; j < t.size(); j++) {
                    if (sort.equals("asc")) {
                        if (t.get(i).getCreated() > t.get(j).getCreated()) {
                            Thread temp = t.get(i);
                            t.set(i, t.get(j));
                            t.set(j, temp);
                        }
                    } else if (sort.equals("dsc")) {
                        if (t.get(i).getCreated() < t.get(j).getCreated()) {
                            Thread temp = t.get(i);
                            t.set(i, t.get(j));
                            t.set(j, temp);
                        }
                    }
                }
            }
            threads = t;
        }
        //if the sort parameter is null
        else {
            //return all threads
            threads = threadRepository.findAll();
        }
        return threads;
    }

    //GET request that returns all unarchived threads
    //Usage: /api-v1/chat/threads/getunarchived
    @GetMapping("/api-v1/chat/threads/getunarchived")
    public List<Thread> getUnarchivedThreads() {
        //get all threads and store them in a List variable
        List<Thread> threads = threadRepository.findAll();

        //create a new List variable to store unarchived threads
        List<Thread> unarchivedThreads = new java.util.ArrayList<>();

        //loop through all threads
        for (Thread thread : threads) {
            //if the thread is not archived
            if (!thread.isArchived()) {
                //add it to the unarchived threads list
                unarchivedThreads.add(thread);
            }
        }

        //return the unarchived threads list
        return unarchivedThreads;
    }

    //GET request that returns all archived threads
    //Usage: /api-v1/chat/threads/getarchived
    @GetMapping("/api-v1/chat/threads/getarchived")
    public List<Thread> getArchivedThreads() {
        //get all threads and store them in a List variable
        List<Thread> threads = threadRepository.findAll();

        //create a new List variable to store archived threads
        List<Thread> archivedThreads = new java.util.ArrayList<>();

        //loop through all threads
        for (Thread thread : threads) {
            //if the thread is archived
            if (thread.isArchived()) {
                //add it to the archived threads list
                archivedThreads.add(thread);
            }
        }

        //return the archived threads list
        return archivedThreads;
    }

    //GET request that returns the thread of a specified ID
    //Usage: /api-v1/chat/threads/get?id=<id>
    @GetMapping("/api-v1/chat/threads/get")
    public Thread getThread(@RequestParam long id) {
        return threadRepository.findById(id).orElseThrow(() -> new ThreadNotFoundException(id));
    }

    //POST request that creates a new thread
    //Usage: /api-v1/chat/threads/create
    @PostMapping("/api-v1/chat/threads/create")
    public Thread createThread(@RequestBody Thread thread) {
        return threadRepository.save(thread);
    }

    //PATCH request that archives a thread
    //Usage: /api-v1/chat/threads/archive?id=<id>
    @PatchMapping("/api-v1/chat/threads/archive")
    public Thread archiveThread(@RequestParam long id) {
        return threadRepository.findById(id)
                .map(thread -> {
                    thread.setArchived(true);
                    return threadRepository.save(thread);
                })
                .orElseThrow(() -> new TimerNotFoundException(id, true));
    }

    //
}
