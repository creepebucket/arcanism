package org.creepebucket.arcanism.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class StoredMana {

    public static final Codec<StoredMana> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Mana.CODEC.fieldOf("current").forGetter(StoredMana::getCurrent),
                    Mana.CODEC.fieldOf("capacity").forGetter(StoredMana::getCapacity)
            ).apply(instance, StoredMana::new)
    );
    public static final StreamCodec<ByteBuf, StoredMana> STREAM_CODEC = StreamCodec.composite(
            Mana.STREAM_CODEC, StoredMana::getCurrent,
            Mana.STREAM_CODEC, StoredMana::getCapacity,
            StoredMana::new
    );

    public Mana current, capacity;

    public StoredMana(Mana current, Mana capacity) {
        this.current = current;
        this.capacity = capacity;
    }

    public Mana getCurrent() {
        return current;
    }

    public Mana getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StoredMana stored && current.equals(stored.current) && capacity.equals(stored.capacity);
    }

    @Override
    public int hashCode() {
        return current.hashCode() * 31 + capacity.hashCode();
    }
}
