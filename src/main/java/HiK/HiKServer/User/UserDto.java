package HiK.HiKServer.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private String id;
    private String email;
    private String name;
    private int age;
    private String gender;
    private String nation;
    private String language;
}
