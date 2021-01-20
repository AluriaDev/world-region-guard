package io.github.aluria.region.bus.event;

import io.github.aluria.region.entity.RegionObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public abstract class RegionEvent extends PlayerEvent implements Cancellable {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final RegionObject from;
    private final RegionObject to;
    private final RegionType type;
    @Setter
    private boolean cancelled = false;

    public RegionEvent(Player who, RegionObject from, RegionObject to, RegionType type) {
        super(who);
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public synchronized void makeInternal(RegionEvent... events) {
        synchronized (this) {
            for (RegionEvent event : events) {
                Bukkit.getPluginManager().callEvent(event);
            }
        }
    }

    public synchronized void perform() {
        synchronized (this) {
            makeInternal(this);
        }
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public enum RegionType {
        NORMAL, CHANGED_REGION
    }
}
