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
        ProjectApisApplication.instance.logger.info("Deleted all commands in the stack");
        ProjectApisApplication.instance.getStatusResponseManager().setExecuteCommand(true);
    }

    @DeleteMapping("/api-v1/computercontrol/delete")
    private void deleteCommand(@RequestParam int id){
        if ((Object) stack.findById(id) != Optional.empty()){
            stack.deleteById(id);
            ProjectApisApplication.instance.logger.info("Command deleted by ID: " + id);
            ProjectApisApplication.instance.getStatusResponseManager().setExecuteCommand(true);
        }else{
            throw new CommandNotFoundException(id);
        }
    }

    //controlling

    @GetMapping("/api-v1/computercontrol/ping")
    private StatusResponse controllerPing(){
        StatusResponse statusResponse = ProjectApisApplication.instance.getStatusResponseManager().getResponse();
        ProjectApisApplication.instance.getStatusResponseManager().clear();
        return statusResponse;
    }

    @PostMapping("/api-v1/computercontrol/add")
    private Command addCommand(@RequestBody Command newCommand){
        if (newCommand.getExecCommand().isBlank() || newCommand.getExecCommand().isEmpty())
            throw new EmptyCommandException(newCommand.getId());
        Command command = stack.save(newCommand);
        ProjectApisApplication.instance.logger.info("Command added: " + newCommand.getExecCommand());
        ProjectApisApplication.instance.getStatusResponseManager().setExecuteCommand(true);
        return command;
    }

    //auth
    @PostMapping("/api-v1/computercontrol/auth")
    private boolean isPasswordCorrect(@RequestParam String password){
        return password.equals(ProjectApisApplication.computerControlAuthPassword);
    }
}
