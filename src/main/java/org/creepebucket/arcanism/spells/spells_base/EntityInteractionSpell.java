package org.creepebucket.arcanism.spells.spells_base;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;
import org.creepebucket.arcanism.ModConfig;
import org.creepebucket.arcanism.entities.SpellEntity;
import org.creepebucket.arcanism.spells.SpellValueType;
import org.creepebucket.arcanism.spells.api.ExecutionResult;
import org.creepebucket.arcanism.spells.api.SpellExceptions;
import org.creepebucket.arcanism.spells.api.SpellItemLogic;
import org.creepebucket.arcanism.spells.api.SpellSequence;
import org.creepebucket.arcanism.utils.Mana;

import java.util.List;

import static org.creepebucket.arcanism.Arcanism.MODID;

public abstract class EntityInteractionSpell extends SpellItemLogic implements SpellItemLogic.BaseSpell {

    public EntityInteractionSpell() {
        subCategory = "spell." + MODID + ".subcategory.entity";
        precedence = -99;
        bypassShunting = true;
    }

    @Override
    public boolean canRun(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
        return true;
    }

    @Override
    public Mana getManaCost(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
        return new Mana();
    }

    public static class TeleportSpell extends EntityInteractionSpell {
        public TeleportSpell() {
            name = "teleport";
            inputTypes = List.of(List.of(SpellValueType.VECTOR3, SpellValueType.ENTITY));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            Entity target = (Entity) paramsList.get(1);
            Vec3 delta = (Vec3) paramsList.get(0);
            target.teleportTo(target.getX() + delta.x, target.getY() + delta.y, target.getZ() + delta.z);
            target.hurtMarked = true;
            return ExecutionResult.SUCCESS(this);
        }

        @Override
        public Mana getManaCost(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            double velocity = ((Vec3) paramsList.get(0)).length() / 0.05;
            double momentum = ModConfig.CONFIG.castMass.get() * velocity * velocity;
            return new Mana(0.0, momentum / 2, momentum, momentum / 2);
        }
    }

    public static class VelocitySpell extends EntityInteractionSpell {
        public VelocitySpell() {
            name = "gain_velocity";
            inputTypes = List.of(List.of(SpellValueType.VECTOR3, SpellValueType.ENTITY));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            Entity target = (Entity) paramsList.get(1);
            Vec3 delta = (Vec3) paramsList.get(0);
            target.addDeltaMovement(delta);
            target.hurtMarked = true;
            return ExecutionResult.SUCCESS(this);
        }

        @Override
        public Mana getManaCost(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            Vec3 delta = (Vec3) paramsList.get(0);
            Vec3 velocity = ((Entity) paramsList.get(1)).getDeltaMovement();
            double before = 0.5 * ModConfig.CONFIG.castMass.get() * velocity.lengthSqr();
            double after = 0.5 * ModConfig.CONFIG.castMass.get() * velocity.add(delta).lengthSqr();
            return new Mana(0.0, 0.0, Math.max(after - before, 0.0), 0.0);
        }
    }

    public static class ApplyPotionSpell extends EntityInteractionSpell {
        public ApplyPotionSpell() {
            name = "apply_potion";
            inputTypes = List.of(List.of(SpellValueType.ITEM, SpellValueType.ENTITY));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            Entity target = (Entity) paramsList.get(1);
            ItemStack potionStack = (ItemStack) paramsList.get(0);

            if (!(target instanceof LivingEntity living)) {
                SpellExceptions.INVALID_INPUT(this, List.of(SpellValueType.fromValue(potionStack), SpellValueType.fromValue(target)), inputTypes).throwIt(caster);
                return ExecutionResult.ERRORED();
            }

            PotionContents potionContents = potionStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potionContents.applyToLivingEntity(living, potionStack.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F));
            return ExecutionResult.SUCCESS(this);
        }

        @Override
        public Mana getManaCost(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            return new Mana(0.0, 0.0, 0.0, 100000.0);
        }
    }

    public static class SendToInventorySpell extends EntityInteractionSpell {
        public SendToInventorySpell() {
            name = "send_to_inventory";
            inputTypes = List.of(List.of(SpellValueType.ITEM));
        }

        @Override
        public ExecutionResult run(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            ItemStack stack = (ItemStack) paramsList.get(0);
            caster.getInventory().placeItemBackInInventory(stack);
            return ExecutionResult.SUCCESS(this);
        }

        @Override
        public Mana getManaCost(Player caster, SpellSequence spellSequence, List<Object> paramsList, SpellEntity spellEntity) {
            int count = ((ItemStack) paramsList.get(0)).getCount();
            double lift = ModConfig.CONFIG.castMass.get() * 9.8 * Math.max(caster.getY() - spellEntity.getY(), 0.0);
            return new Mana(0.0, 0.0, lift * count, 101325.0 * count);
        }
    }
}
