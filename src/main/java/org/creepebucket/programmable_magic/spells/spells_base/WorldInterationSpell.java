package org.creepebucket.programmable_magic.spells.spells_base;

import net.minecraft.world.entity.player.Player;
import org.creepebucket.programmable_magic.ModUtils;
import org.creepebucket.programmable_magic.entities.SpellEntity;
import org.creepebucket.programmable_magic.spells.SpellValueType;
import org.creepebucket.programmable_magic.spells.api.ExecutionResult;
import org.creepebucket.programmable_magic.spells.api.SpellItemLogic;
import org.creepebucket.programmable_magic.spells.api.SpellSequence;

import java.util.List;

import static org.creepebucket.programmable_magic.Programmable_magic.MODID;

public abstract class WorldInterationSpell extends SpellItemLogic implements SpellItemLogic.BaseSpell {

    public WorldInterationSpell() {
        subCategory = "spell." + MODID + ".subcategory.block";
        precedence = -99;
        bypassShunting = true;
    }

    @Override
    public boolean canRun(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
        return true;
    }

    @Override
    public ModUtils.Mana getManaCost(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
        return new ModUtils.Mana();
    }

    public static class BreakBlockSpell extends WorldInterationSpell {
        public BreakBlockSpell() {
            name = "break_block";
            outputTypes = List.of(List.of(SpellValueType.ITEM));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            return ExecutionResult.SUCCESS(this);
        }
    }

    public static class MineBlockSpell extends WorldInterationSpell {
        public MineBlockSpell() {
            name = "mine_block";
            outputTypes = List.of(List.of(SpellValueType.ITEM));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            return ExecutionResult.SUCCESS(this);
        }
    }

    public static class PlaceBlockSpell extends WorldInterationSpell {
        public PlaceBlockSpell() {
            name = "place_block";
            inputTypes = List.of(List.of(SpellValueType.ITEM));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            return ExecutionResult.SUCCESS(this);
        }
    }

    public static class ExplosionSpell extends WorldInterationSpell {
        public ExplosionSpell() {
            name = "explosion";
            inputTypes = List.of(List.of(SpellValueType.NUMBER));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            return ExecutionResult.SUCCESS(this);
        }
    }
}
