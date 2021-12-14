package kr.kro.minestar.area.data

import kr.kro.minestar.area.Main.Companion.pl
import kr.kro.minestar.utility.location.isInside
import kr.kro.minestar.utility.string.remove
import kr.kro.minestar.utility.yaml.clear
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Entity
import java.io.File

class Area(val name: String) {
    val file = File(pl.dataFolder, "$name.yml")
    val data = YamlConfiguration.loadConfiguration(file)

    val list = mutableListOf<Pair<Location, Location>>()

    init {
        init()
    }

    fun init() {
        val keys = data.getKeys(false)
        if (keys.isEmpty()) return
        for (key in keys) {
            if (!key.contains('+')) continue
            if (key.contains('-')) continue
            val k = key.remove('+').toIntOrNull() ?: continue
            if (!keys.contains("$k-")) continue
            val loc1 = data.getLocation(key) ?: continue
            val loc2 = data.getLocation("$k-") ?: continue
            list.add(Pair(loc1, loc2))
        }
    }

    /**
     * control
     */
    fun save() {
        data.clear()
        for ((int, pair) in list.withIndex()) {
            data["$int+"] = pair.first
            data["$int-"] = pair.second
        }
        data.save(file)
    }

    fun isInside(entity: Entity): Boolean {
        if (list.isEmpty()) return false
        val loc = entity.location
        for (pair in list) if (loc.isInside(pair.first, pair.second)) return true
        return false
    }

    fun addArea(loc1: Location, loc2: Location): Boolean {
        if (loc1.world != loc2.world) return false
        val pair = Pair(loc1.block.location, loc2.block.location)
        if (list.contains(pair)) return false
        list.add(Pair(loc1, loc2))
        save()
        return true
    }

    fun removeArea(int: Int): Boolean {
        if (list.size <= int) return false
        if (int < 0) return false
        list.removeAt(int)
        save()
        return true
    }
}