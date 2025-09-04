package web.com.springweb.notification;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/notify")
public class NotifyAdminController {
    private final NotificationHandler handler;
    public NotifyAdminController(NotificationHandler h){ this.handler = h; }

    @PostMapping("/broadcast")
    public void broadcast(@RequestParam String title, @RequestParam String message){
        String json = String.format(
          "{\"type\":\"BROADCAST\",\"title\":%s,\"message\":%s,\"level\":\"danger\"}",
          toJson(title), toJson(message)
        );
        handler.sendToAll(json);
    }
    private String toJson(String s){ return "\"" + s.replace("\"","\\\"") + "\""; }
}