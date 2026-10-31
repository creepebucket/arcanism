package org.creepebucket.arcanism.gui.help.contents.casting.spell_syntax;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.creepebucket.arcanism.gui.help.HelpWindowWidgets;
import org.creepebucket.arcanism.gui.lib.api.Animation;
import org.creepebucket.arcanism.gui.lib.api.Coordinate;
import org.creepebucket.arcanism.gui.lib.api.SmoothedValue;
import org.creepebucket.arcanism.gui.lib.api.Widget;
import org.creepebucket.arcanism.gui.lib.api.widgets.Lifecycle;
import org.creepebucket.arcanism.gui.lib.widgets.CullAreaWidget;
import org.creepebucket.arcanism.gui.lib.widgets.ItemTextureWidget;
import org.creepebucket.arcanism.gui.lib.widgets.ScrollbarWidget;
import org.creepebucket.arcanism.gui.lib.widgets.TextureWidget;
import org.creepebucket.arcanism.utils.ModUtils;

import static org.creepebucket.arcanism.Arcanism.MODID;
import static org.creepebucket.arcanism.gui.lib.api.Coordinate.*;

public class SyntaxOverviewWidget extends HelpWindowWidgets.HelpBodyWidget {
    public SyntaxOverviewWidget() {
        super(Component.translatable("gui.arcanism.help.page.syntax_overview.title"));
    }

    @Override
    public void buildContent() {
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.intro"));
        addHeading("copy_to_spell", Component.translatable("gui.arcanism.help.page.syntax_overview.heading.sequence"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequence.simplest"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "break_block"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequence.break_block"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "10explosion"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequence.pattern"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequence.other"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "y_unit_vector,caster_entitygain_velocity"));
        addNote(Component.translatable("gui.arcanism.help.page.syntax_overview.note.comma"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequence.move_up"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequence.complex"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "x_unit_vector+y_unit_vector+z_unit_vector,caster_entitygain_velocity"));
        addHeading("compute_mod", Component.translatable("gui.arcanism.help.page.syntax_overview.heading.compute"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.compute.params"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.compute.familiar"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "1+3*2*(7-4)print"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.compute.result"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.compute.analogy"));
        addHeading("bypass_compile", Component.translatable("gui.arcanism.help.page.syntax_overview.heading.sequential"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequential.single"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequential.chain"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "spell_entity,camera_directiongain_velocitywait_until_touch_ground10explosion"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequential.explain"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequential.parts"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "spell_entity,camera_directiongain_velocity"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "wait_until_touch_ground"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "10explosion"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.sequential.order"));
        addHeading("adjust_spell", Component.translatable("gui.arcanism.help.page.syntax_overview.heading.types"));
        addNote(Component.translatable("gui.arcanism.help.page.syntax_overview.note.tooltip"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.types.intro"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.types.example"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "10explosion"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.types.valid"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "spell_entityexplosion"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.types.invalid"));
        addWarning(Component.translatable("gui.arcanism.help.page.syntax_overview.warning.order"));
        addHeading("control_mod", Component.translatable("gui.arcanism.help.page.syntax_overview.heading.control"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.control.loop"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "loop{1print}"));
        addImportant(Component.translatable("gui.arcanism.help.page.syntax_overview.important.infinite.label"), Component.translatable("gui.arcanism.help.page.syntax_overview.important.infinite.note"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.control.for"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "10for{1print}"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.control.break"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "break"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.control.condition"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "1+1==2if_true_do[trueprint]"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.control.condition_note"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.control.detect_air"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "caster_position-y_unit_vectorblock_ofis_air?if_true_do[1print]"));
        addParagraph(Component.translatable("gui.arcanism.help.page.syntax_overview.control.combined"));
        acceptSegment(new SpellSegmentWidget(fromTopLeft(10, 0), fromTopRight(-20, 20), "camera_direction,spell_entitygain_velocity500for{!(spell_positionblock_ofis_air?)if_true_do[break]}10explosion"));
        addNote(Component.translatable("gui.arcanism.help.page.syntax_overview.note.loop_limit"));
    }

    public static class SpellSegmentWidget extends Widget implements Lifecycle {
        public String spells;

        public SpellSegmentWidget(Coordinate pos, Coordinate size, String spellString) {
            super(pos, size);
            spells = spellString;
        }

        @Override
        public void onInitialize() {
            var cull = addChild(new CullAreaWidget(ZERO, fromTopRight(-20, 12)));
            var cardsDx = new SmoothedValue(0);
            var cards = cull.addChild(new BlankWidget(ZERO, FULL).dx(cardsDx));

            int contentWidth = 0;
            var container = ModUtils.deSerializeSpells(spells);
            for (int i = 0; i < container.getContainerSize(); i++) {
                var stack = container.getItem(i);
                if (stack.isEmpty()) continue;

                cards.addChild(new ItemTextureWidget(fromTopLeft(contentWidth, -2), fromTopLeft(16, 16),
                        BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()).tooltip(stack.getHoverName()));
                contentWidth += 16;
            }

            var copyButton = new TextureWidget(fromTopRight(-16, -2), Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons/copy.png"), fromTopLeft(16, 16))
                    .tooltip(Component.translatable("gui.arcanism.help.copy_spell_sequence"));

            addChild(copyButton).addClickBehavior(() -> {
                        copyButton.addAnimation(new Animation.Transparent(3), 0);
                        Minecraft.getInstance().keyboardHandler.setClipboard(spells);
                        this.addChild(new TextureWidget(fromTopRight(-16, -2), Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons/copied.png"), fromTopLeft(16, 16)))
                                .addAnimation(new Animation.FadeOut.Static(3), 0);});

            var maxScroll = contentWidth - cull.w();
            if (maxScroll <= 0) return;
            var scrollbar = (ScrollbarWidget) addChild(new ScrollbarWidget(fromTopLeft(0, 15), fromTopRight(-20, 4), fromTopLeft(-maxScroll, 0), cardsDx, "x").reverseDirection().bgColor(0));
            scrollbar.contentLength = contentWidth;
        }
    }
}
