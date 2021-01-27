package io.github.aluria.region.selector;

import lombok.Getter;
import org.bukkit.Location;

import java.util.Objects;

@Getter
public class PlayerSelector {

    private Location start;
    private Location end;

    public boolean setStart(Location start) {
        if (checkState(start, end)) {
            this.end = null;
        }
        this.start = start;
        return true;
    }

    public boolean setEnd(Location end) {
        if (checkState(start, end)) return false;
        this.end = end;
        return true;
    }

    public boolean allCornersSet() {
        return start != null && end != null;
    }

    public void expand() {
        if (start != null) start.setY(0);
        if (end != null) end.setY(255);
    }

    public boolean checkState(Location cornerOne, Location cornerTwo) {
        if (cornerOne == null || cornerTwo == null) return false;
        return !Objects.equals(cornerOne.getWorld(), cornerTwo.getWorld());
    }
}
