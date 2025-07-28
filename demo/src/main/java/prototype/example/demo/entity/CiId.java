package prototype.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

public class CiId implements Serializable {
    private String email2;
    private int installDay;

    public CiId() {}

    public CiId(String email2, int installDay) {
        this.email2 = email2;
        this.installDay = installDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CiId)) return false;
        CiId that = (CiId) o;
        return installDay == that.installDay && Objects.equals(email2, that.email2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email2, installDay);
    }
}
