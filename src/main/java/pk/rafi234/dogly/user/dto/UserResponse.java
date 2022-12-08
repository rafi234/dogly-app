package pk.rafi234.dogly.user.dto;

import lombok.Getter;
import lombok.Setter;
import pk.rafi234.dogly.dog.Dog;
import pk.rafi234.dogly.dog.DogResponse;
import pk.rafi234.dogly.security.role.Group;
import pk.rafi234.dogly.user.User;
import pk.rafi234.dogly.user.address.Address;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
public final class UserResponse {
    private String id;
    private String name;
    private String surname;
    private String email;
    private Address address;
    private List<DogResponse> dogs;
    private Set<String> roles;
    private boolean isActive;

    public UserResponse(User user) {
        this.id = user.getId().toString();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.roles = getSetOfRoles(user.getRoles());
        this.dogs = getDogList(user.getDogs());
        this.isActive = user.isActive();
    }

    private Set<String> getSetOfRoles(Set<Group> rolesToMap) {
        Set<String> retVal = new HashSet<>();
        for (Group group : rolesToMap) {
            retVal.add("ROLE_" + group.getRole().name());
        }
        return retVal;
    }

    private List<DogResponse> getDogList(List<Dog> dogs) {
        return dogs.stream().map(DogResponse::new)
                .collect(Collectors.toList());
    }
}
