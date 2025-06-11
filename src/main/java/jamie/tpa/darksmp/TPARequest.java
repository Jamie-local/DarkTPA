package jamie.tpa.darksmp;

import java.util.UUID;

public class TPARequest {

    public enum Type {
        TPA, TPAHERE, TPASWAP
    }

    private final UUID sender;
    private final Type type;
    private final long timestamp;

    public TPARequest(UUID sender, Type type) {
        this.sender = sender;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getSender() {
        return sender;
    }

    public Type getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
