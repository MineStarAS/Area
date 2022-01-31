package kr.kro.minestar.area.data

import kr.kro.minestar.area.Main.Companion.pl
import kr.kro.minestar.area.Main.Companion.prefix
import kr.kro.minestar.area.functions.AreasClass
import kr.kro.minestar.utility.bool.BooleanScript
import kr.kro.minestar.utility.bool.addScript
import kr.kro.minestar.utility.location.isInside
import kr.kro.minestar.utility.location.toCenter
import kr.kro.minestar.utility.particle.ParticleUtil
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.scheduler.BukkitTask
import java.io.File

class Areas(val name: String) {
    private val file = File(pl.dataFolder, "$name.yml")
    private var data = YamlConfiguration.loadConfiguration(file)

    private val list = mutableListOf<Map<String, Location>>()

    private var task: BukkitTask? = null
    private val displayList = mutableListOf<Int>()

    init {
        init()
    }

    fun init() {
        val values = data.getValues(false)
        if (values.isEmpty()) return
        for (value in values) {
            val v = value.value as MemorySection
            val map = v.getValues(false)
            if (!map.keys.contains("pos1")) continue
            if (!map.keys.contains("pos2")) continue
            if (map["pos1"]!! !is Location) continue
            if (map["pos2"]!! !is Location) continue
            for (key in map.keys) if (key != "pos1" && key != "pos2") map.remove(key)
            list.add(map as Map<String, Location>)
        }
    }

    /**
     * control
     */
    fun save() {
        data = YamlConfiguration()
        for ((int, pair) in list.withIndex()) data["$int"] = pair
        data.save(file)
    }

    fun isInside(loc : Location): Boolean {
        if (list.isEmpty()) return false
        for (map in list) {
            map["pos1"] ?: continue
            map["pos2"] ?: continue
            if (loc.isInside(map["pos1"]!!, map["pos2"]!!)) return true
        }
        return false
    }

    fun addArea(loc1: Location, loc2: Location): BooleanScript {
        if (loc1.world != loc2.world) return false.addScript("$prefix §c두 좌표의 월드가 같지 않습니다.")
        val map = mapOf(Pair("pos1", loc1.toBlockLocation()), Pair("pos2", loc2.toBlockLocation()))
        for (m in list) if (AreasClass.isSameArea(m, map)) return false.addScript("$prefix §c이미 선택한 구역과 일치하는 구역이 존재합니다.")
        list.add(map)
        save()
        return true.addScript("$prefix 선택된 구역이 §e$name §f구역에 §a추가 §f되었습니다.")
    }

    fun removeArea(int: Int): BooleanScript {
        if (list.isEmpty()) return false.addScript("$prefix §c해당 구역은 비어있습니다.")
        if (list.size <= int || int < 0) return false.addScript("$prefix §c해당 구역은 0 번 부터 ${list.size - 1} 번 까지만 존재 합니다.")
        list.removeAt(int)
        save()
        return true.addScript("$prefix 성공적으로 §e$int §f번 구역을 삭제 하였습니다.")
    }

    fun isDisplaying(): Boolean {
        task ?: return false
        return true
    }

    fun onDisplay() {
        val yellowParticle = ParticleUtil().also {
            it.particle = Particle.REDSTONE
            it.data = Particle.DustOptions(Color.YELLOW, 2F)
            it.force = true
        }
        val orangeParticle = ParticleUtil().also {
            it.particle = Particle.REDSTONE
            it.data = Particle.DustOptions(Color.ORANGE, 2F)
            it.force = true
        }
        task?.cancel()
        task = Bukkit.getScheduler().runTaskTimer(pl, Runnable {
            for (int in displayList) {
                if (displayList.isEmpty()) {
                    task?.cancel()
                    task = null
                }
                if (list.size <= int || int < 0) {
                    displayList.remove(int)
                    continue
                }
                val pos1 = list[int]["pos1"]!!
                val pos2 = list[int]["pos2"]!!
                val world = pos1.world
                val xL: Int
                val xH: Int
                val yL: Int
                val yH: Int
                val zL: Int
                val zH: Int

                if (pos1.blockX <= pos2.blockX) {
                    xL = pos1.blockX
                    xH = pos2.blockX
                } else {
                    xL = pos2.blockX
                    xH = pos1.blockX
                }
                if (pos1.blockY <= pos2.blockY) {
                    yL = pos1.blockY
                    yH = pos2.blockY
                } else {
                    yL = pos2.blockY
                    yH = pos1.blockY
                }
                if (pos1.blockZ <= pos2.blockZ) {
                    zL = pos1.blockZ
                    zH = pos2.blockZ
                } else {
                    zL = pos2.blockZ
                    zH = pos1.blockZ
                }

                for (x in xL..xH) {
                    val list = listOf(
                        Location(world, x.toDouble(), yL.toDouble(), zL.toDouble()).toCenter(),
                        Location(world, x.toDouble(), yH.toDouble(), zH.toDouble()).toCenter(),
                        Location(world, x.toDouble(), yL.toDouble(), zH.toDouble()).toCenter(),
                        Location(world, x.toDouble(), yH.toDouble(), zL.toDouble()).toCenter()
                    )
                    if (x == xL || x == xH) for (loc in list) orangeParticle.play(loc)
                    else for (loc in list) yellowParticle.play(loc)
                }
                for (y in yL..yH) {
                    if (y == yL || y == yH) continue
                    val list = listOf(
                        Location(world, xL.toDouble(), y.toDouble(), zL.toDouble()).toCenter(),
                        Location(world, xH.toDouble(), y.toDouble(), zH.toDouble()).toCenter(),
                        Location(world, xL.toDouble(), y.toDouble(), zH.toDouble()).toCenter(),
                        Location(world, xH.toDouble(), y.toDouble(), zL.toDouble()).toCenter()
                    )
                    for (loc in list) yellowParticle.play(loc)
                }
                for (z in zL..zH) {
                    if (z == zL || z == zH) continue
                    val list = listOf(
                        Location(world, xL.toDouble(), yL.toDouble(), z.toDouble()).toCenter(),
                        Location(world, xH.toDouble(), yH.toDouble(), z.toDouble()).toCenter(),
                        Location(world, xL.toDouble(), yH.toDouble(), z.toDouble()).toCenter(),
                        Location(world, xH.toDouble(), yL.toDouble(), z.toDouble()).toCenter()
                    )
                    for (loc in list) yellowParticle.play(loc)
                }
            }
        }, 0, 10)
    }

    fun offDisplay() {
        task?.cancel()
        task = null
    }

    fun addDisplay(int: Int): BooleanScript {
        if (list.size <= int || int < 0) return false.addScript("$prefix §c해당 구역은 0 번 부터 ${list.size - 1} 번 까지만 존재 합니다.")
        if (displayList.contains(int)) return false.addScript("$prefix §c$int 번 구역은 이미 추가되어 있습니다.")
        displayList.add(int)
        if (!isDisplaying()) onDisplay()
        return true.addScript("$prefix §e$int §a번 구역을 표시 리스트에 추가 하였습니다.")
    }

    fun allDisplay(): BooleanScript {
        if (list.isEmpty()) return false.addScript("$prefix §c해당 구역은 비어 있습니다.")
        clearDisplay()
        for (int in 0 until list.size) displayList.add(int)
        if (!isDisplaying()) onDisplay()
        return true.addScript("$prefix §a해당 구역의 모든 구역을 표시 리스트에 추가 하였습니다.")
    }

    fun removeDisplay(int: Int): BooleanScript {
        if (list.size <= int || int < 0) return false.addScript("$prefix §c해당 구역은 0 번 부터 ${list.size - 1} 번 까지만 존재 합니다.")
        if (!displayList.contains(int)) return false.addScript("$prefix §c$int 번 구역은 표시 리스트에 추가되어 있지 않습니다.")
        displayList.remove(int)
        return true.addScript("$prefix §e$int §f번 구역을 표시 리스트에서 제거 하였습니다.")
    }

    fun clearDisplay(): BooleanScript {
        if (displayList.isEmpty()) return false.addScript("$prefix §c해당 구역의 표시 리스트는 비어 있습니다.")
        displayList.clear()
        offDisplay()
        return true.addScript("$prefix §a해당 구역의 표시 리스트를 초기화 하였습니다.")
    }
}