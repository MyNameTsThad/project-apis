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

import javax.servlet.http.HttpServletRequest;
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
        List<Command> commands = stack.findAll();
        clearCommandStack();
        return commands;
    }

    @DeleteMapping("/api-v1/computercontrol/clearstack")
    private void clearCommandStack(){
        stack.deleteAll();
        ProjectApisApplication.instance.logger.info("Deleted all commands in the stack");
        //ProjectApisApplication.instance.getStatusResponseManager().setExecuteCommand();
    }

    @DeleteMapping("/api-v1/computercontrol/delete")
    private void deleteCommand(@RequestParam int id) {
        if ((Object) stack.findById(id) != Optional.empty()) {
            stack.deleteById(id);
            ProjectApisApplication.instance.logger.info("Command deleted by ID: " + id);
            ProjectApisApplication.instance.getStatusResponseManager().setExecuteCommand();
        } else {
            throw new CommandNotFoundException(id);
        }
    }

    @GetMapping("/api-v1/computercontrol/ping")
    private StatusResponse controllerPing(HttpServletRequest request) {
        StatusResponse statusResponse = ProjectApisApplication.instance.getStatusResponseManager().getResponse();
        ProjectApisApplication.instance.getStatusResponseManager().clear();
        ProjectApisApplication.instance.getStatusResponseManager().connectedDevicesPing(request.getRemoteAddr());
        return statusResponse;
    }

    //controlling

    @GetMapping("/api-v1/computercontrol/getcontrolled")
    private int getControlledDevices() {
        return ProjectApisApplication.instance.getStatusResponseManager().getConnectedDevicesSize();
    }

    @PostMapping("/api-v1/computercontrol/add")
    private Command addCommand(@RequestBody Command newCommand) {
        if (newCommand.getExecCommand().isBlank() || newCommand.getExecCommand().isEmpty())
            throw new EmptyCommandException(newCommand.getId());
        Command command = stack.save(newCommand);
        ProjectApisApplication.instance.logger.info("Command added: " + newCommand.getExecCommand());
        ProjectApisApplication.instance.getStatusResponseManager().setExecuteCommand();
        return command;
    }

    //auth
    @PostMapping("/api-v1/computercontrol/auth")
    private boolean isPasswordCorrect(@RequestParam String password){
        return password.equals(ProjectApisApplication.computerControlAuthPassword);
    }
}
