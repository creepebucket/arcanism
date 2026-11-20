package org.creepebucket.arcanism.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.creepebucket.arcanism.mananet.connectors.AbstractManaConnector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.creepebucket.arcanism.Arcanism.MODID;

/**
 * 为所有魔力连接器生成绑定到方块模型的 item 模型与 client items
 */
public class ManaConnectorItemModelProvider implements DataProvider {

    private final PackOutput.PathProvider modelsProvider;
    private final PackOutput.PathProvider itemsProvider;

    public ManaConnectorItemModelProvider(PackOutput output) {
        this.modelsProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
        this.itemsProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Block block : BuiltInRegistries.BLOCK) {
            if (!(block instanceof AbstractManaConnector)) continue;
            Identifier id = BuiltInRegistries.BLOCK.getKey(block);

            // models/item/<name>.json
            JsonObject model = new JsonObject();
            model.addProperty("parent", MODID + ":block/" + id.getPath());
            model.add("display", JsonParser.parseString("""
                    {
                      "thirdperson_righthand": {"rotation": [75, 45, 0], "translation": [0, 2.5, 0], "scale": [0.375, 0.375, 0.375]},
                      "thirdperson_lefthand": {"rotation": [75, 45, 0], "translation": [0, 2.5, 0], "scale": [0.375, 0.375, 0.375]},
                      "firstperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.4, 0.4, 0.4]},
                      "firstperson_lefthand": {"rotation": [0, 225, 0], "translation": [0, 0, 0], "scale": [0.4, 0.4, 0.4]},
                      "ground": {"rotation": [0, 0, 0], "translation": [0, 3, 0], "scale": [0.25, 0.25, 0.25]},
                      "gui": {"rotation": [30, 225, 0], "translation": [0, 0, 0], "scale": [0.75, 0.75, 0.75]},
                      "fixed": {"rotation": [0, 0, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]}
                    }
                    """));
            Path modelPath = modelsProvider.file(id, "json");
            futures.add(DataProvider.saveStable(cache, model, modelPath));

            // items/<name>.json
            JsonObject clientItem = new JsonObject();
            JsonObject modelRef = new JsonObject();
            modelRef.addProperty("type", "minecraft:model");
            modelRef.addProperty("model", MODID + ":item/" + id.getPath());
            clientItem.add("model", modelRef);
            Path itemPath = itemsProvider.file(id, "json");
            futures.add(DataProvider.saveStable(cache, clientItem, itemPath));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Mana Connector Item Models";
    }
}
