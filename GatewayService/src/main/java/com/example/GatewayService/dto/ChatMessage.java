package  com.example.GatewayService.dto;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data               // generates getter/setter, toString, equals, hashCode
@NoArgsConstructor  // default constructor
@AllArgsConstructor // all-args constructor
@Builder            // builder pattern (optional)
public class ChatMessage {
    private String type;       // e.g., message.send
    private String tempId;     // client temp id
    private Map<String, Object> payload = new HashMap<>();
}
