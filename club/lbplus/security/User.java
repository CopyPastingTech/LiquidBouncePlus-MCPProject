package club.lbplus.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {

    private final String userName;
    private final long userId;
    private final Type userType;

    enum Type {
        RELEASE, PREVIEW, CUTTING_EDGE
    }

}
