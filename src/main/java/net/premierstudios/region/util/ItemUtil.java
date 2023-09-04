package net.premierstudios.region.util;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemUtil {

    public static ItemStack setNBT(ItemStack itemStack, String key, String value) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItemStack == null) {
            return new ItemStack(Material.AIR);
        } else {
            CompoundTag compoundTag = nmsItemStack.getOrCreateTag();
            compoundTag.putString(key, value);
            nmsItemStack.setTag(compoundTag);
            return CraftItemStack.asBukkitCopy(nmsItemStack);
        }
    }

    public static ItemStack removeNBT(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItemStack == null) {
            return new ItemStack(Material.AIR);
        } else {
            CompoundTag compoundTag = nmsItemStack.getOrCreateTag();
            compoundTag.remove(key);
            nmsItemStack.setTag(compoundTag);
            return CraftItemStack.asBukkitCopy(nmsItemStack);
        }
    }

    public static String getNBT(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItemStack == null) {
            return "";
        } else {
            CompoundTag compoundTag = nmsItemStack.getOrCreateTag();
            return compoundTag.getString(key);
        }
    }

    public static boolean hasNBT(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsItemStack == null) {
            return false;
        } else {
            CompoundTag compoundTag = nmsItemStack.getOrCreateTag();
            return compoundTag.contains(key);
        }
    }

    public static String toBase64(ItemStack item) {
        if (item == null) return "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ItemStack fromBase64(String string) {
        if (string == null) return new ItemStack(Material.AIR);
        ItemStack item = new ItemStack(Material.AIR);
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(string));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public static String arrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to serialize item stacks.", e);
        }
    }

    public static ItemStack[] arrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to deserialize class type.", e);
        }
    }

}
