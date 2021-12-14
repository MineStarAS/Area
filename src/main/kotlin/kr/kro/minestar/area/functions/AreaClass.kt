package kr.kro.minestar.area.functions

import kr.kro.minestar.area.Main.Companion.pl
import kr.kro.minestar.area.data.Area
import kr.kro.minestar.utility.string.remove
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import java.util.*

object AreaClass {

    val areaList = mutableListOf<Area>()

    fun areasEnable() {
        if (!pl.dataFolder.exists()) pl.dataFolder.mkdir()
        val files = pl.dataFolder.listFiles()
        if (files.isEmpty()) return
        for (f in files) {
            if (!f.name.contains(".yml")) continue
            areaList.add(Area(f.name.remove(".yml")))
        }
    }

    fun nameList(): List<String> {
        val list = mutableListOf<String>()
        for (area in areaList) list.add(area.name)
        return list
    }

    fun createArea(name: String): Boolean {
        if (nameList().contains(name)) return false
        val area = Area(name)
        areaList.add(area)
        area.save()
        return true
    }

//    fun test(){
//        Particle
//        pTick.put(p.getUniqueId(), Bukkit.getScheduler().runTaskTimer(pl, Runnable {
//            val x = intArrayOf(pos1.get(p.getUniqueId()).getBlockX(), pos2.get(p.getUniqueId()).getBlockX())
//            val y = intArrayOf(pos1.get(p.getUniqueId()).getBlockY(), pos2.get(p.getUniqueId()).getBlockY())
//            val z = intArrayOf(pos1.get(p.getUniqueId()).getBlockZ(), pos2.get(p.getUniqueId()).getBlockZ())
//            Arrays.sort(x)
//            Arrays.sort(y)
//            Arrays.sort(z)
//            for (ix in x[0]..x[1]) {
//                for (iy in y[0]..y[1]) {
//                    for (iz in z[0]..z[1]) {
//                        var count = 0
//                        var unCount = true
//                        if (ix == x[0]) ++count
//                        if (ix == x[1]) ++count
//                        if (iy == y[0]) ++count
//                        if (iy == y[1]) ++count
//                        if (iz == z[0]) ++count
//                        if (iz == z[1]) ++count
//                        val loc = Location(pos1.get(p.getUniqueId()).getWorld(), ix.toDouble(), iy.toDouble(), iz.toDouble()).block.location
//                        if (ix == x[0] && iy == y[0] && iz == z[0]) {
//                            loc.world.spawnParticle(Particle.REDSTONE, loc.clone().add(0.5, 0.5, 0.5), 1, 0.0, 0.0, 0.0, 0.0, DustOptions(Color.AQUA, 1))
//                            unCount = false
//                        }
//                        if (ix == x[1] && iy == y[1] && iz == z[1]) {
//                            loc.world.spawnParticle(Particle.REDSTONE, loc.clone().add(0.5, 0.5, 0.5), 1, 0.0, 0.0, 0.0, 0.0, DustOptions(Color.AQUA, 1))
//                            unCount = false
//                        }
//                        if (count == 2) loc.world.spawnParticle(Particle.REDSTONE, loc.clone().add(0.5, 0.5, 0.5), 1, 0.0, 0.0, 0.0, 0.0, DustOptions(Color.RED, 1))
//                        if (count == 3 && unCount) loc.world.spawnParticle(Particle.REDSTONE, loc.clone().add(0.5, 0.5, 0.5), 1, 0.0, 0.0, 0.0, 0.0, DustOptions(Color.BLUE, 1))
//                    }
//                }
//            }
//        }, 1, 3))
//    }
}