package xyz.thaddev.projectapis;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("Permission Denied");
    }
}
