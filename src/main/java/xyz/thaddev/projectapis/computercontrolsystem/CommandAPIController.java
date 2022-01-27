package xyz.thaddev.projectapis.computercontrolsystem;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.thaddev.projectapis.ProjectApisApplication;
import xyz.thaddev.projectapis.computercontrolsystem.exceptions.CommandNotFoundException;
import xyz.thaddev.projectapis.computercontrolsystem.exceptions.EmptyCommandException;

import java.util.List;
import java.util.Optional;

@RestController
public class CommandAPIController {
    private final CommandStack stack;

    public CommandAPIController(CommandStack stack) {
        this.stack = stack;
        ProjectApisApplication.instance.setCommandAPIController(this);
    }

    //controlled

    @GetMapping("/api-v1/computercontrol/getstack")
    private List<Command> getCommandStack(){
        return stack.findAll();
    }

    @DeleteMapping("/api-v1/computercontrol/clearstack")
    private void clearCommandStack(){
        stack.deleteAll();
    }

    @DeleteMapping("/api-v1/computercontrol/delete")
    private void deleteCommand(@RequestParam int id){
        if ((Object) stack.findById(id) != Optional.empty()){
            stack.deleteById(id);
        }else{
            throw new CommandNotFoundException(id);
        }
    }

    //controlling

    @GetMapping("/api-v1/computercontrol/ping")
    private int controllerPing(){
        return 0;
    }

    @PostMapping("/api-v1/computercontrol/add")
    private Command addCommand(@RequestBody Command newCommand){
        if (newCommand.getExecCommand().isBlank() || newCommand.getExecCommand().isEmpty()){
            throw new EmptyCommandException(newCommand.getId());
        }
        return stack.save(newCommand);
    }

    //auth
    @PostMapping("/api-v1/computercontrol/auth")
    private boolean isPasswordCorrect(@RequestParam String password){
        return password.equals(ProjectApisApplication.computerControlAuthPassword);
    }
}
