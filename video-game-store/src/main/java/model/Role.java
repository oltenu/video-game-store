package model;

import model.annotations.Id;

import java.util.List;
import java.util.Objects;

public class Role {
    @Id
    private Long id;
    private String role;
    private List<Right> rights;

    public Role(Long id, String role, List<Right> rights) {
        this.id = id;
        this.role = role;
        this.rights = rights;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Right> getRights() {
        return rights;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(id, role1.id) && Objects.equals(role, role1.role) && Objects.equals(rights, role1.rights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, rights);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", rights=" + rights +
                '}';
    }
}
