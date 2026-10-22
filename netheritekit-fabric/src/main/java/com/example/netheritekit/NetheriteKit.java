package com.example.netheritekit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemContainerContents;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NetheriteKit implements ModInitializer {
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(RequestKitPayload.ID, RequestKitPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RequestKitPayload.ID, (payload, context) -> {
            context.server().execute(() -> giveKit(context.player()));
        });
    }

    private static void giveKit(ServerPlayerEntity player) {
        var lookup = player.getWorld().getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT);

        List<ItemStack> items = new ArrayList<>();
        items.add(enchanted(lookup, Items.NETHERITE_HELMET,
                Enchantments.PROTECTION, 4, Enchantments.FIRE_PROTECTION, 4,
                Enchantments.UNBREAKING, 3));
        items.add(enchanted(lookup, Items.NETHERITE_CHESTPLATE,
                Enchantments.PROTECTION, 4, Enchantments.FIRE_PROTECTION, 4,
                Enchantments.UNBREAKING, 3));
        items.add(enchanted(lookup, Items.NETHERITE_LEGGINGS,
                Enchantments.PROTECTION, 4, Enchantments.FIRE_PROTECTION, 4,
                Enchantments.UNBREAKING, 3));
        items.add(enchanted(lookup, Items.NETHERITE_BOOTS,
                Enchantments.PROTECTION, 4, Enchantments.FIRE_PROTECTION, 4,
                Enchantments.FEATHER_FALLING, 4, Enchantments.UNBREAKING, 3));
        items.add(enchanted(lookup, Items.NETHERITE_SWORD,
                Enchantments.SHARPNESS, 5, Enchantments.FIRE_ASPECT, 2,
                Enchantments.UNBREAKING, 3));
        items.add(new ItemStack(Items.SHIELD));
        items.add(enchanted(lookup, Items.NETHERITE_PICKAXE,
                Enchantments.EFFICIENCY, 5, Enchantments.UNBREAKING, 3));

        ItemStack box = new ItemStack(Items.SHULKER_BOX);
        box.set(DataComponentTypes.CONTAINER, ItemContainerContents.fromItems(items));

        player.giveItemStack(box);
        player.sendMessage(Text.literal("已获得下界合金套装潜影盒！"), true);
    }

    private static ItemStack enchanted(RegistryWrapper.RegistryLookup<Enchantment> lookup, Item item, Object... enchantments) {
        ItemStack stack = new ItemStack(item);
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(stack.getEnchantments());
        for (int i = 0; i + 1 < enchantments.length; i += 2) {
            @SuppressWarnings("unchecked")
            RegistryEntry.Reference<Enchantment> key = (RegistryEntry.Reference<Enchantment>) enchantments[i];
            builder.add(lookup.getOrThrow(key), (Integer) enchantments[i + 1]);
        }
        stack.set(DataComponentTypes.ENCHANTMENTS, builder.build());
        return stack;
    }
}
