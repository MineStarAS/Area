package kr.kro.minestar.area.data

import kr.kro.minestar.area.Main.Companion.pl
import kr.kro.minestar.area.Main.Companion.prefix
import kr.kro.minestar.utility.location.toCenter
import kr.kro.minestar.utility.particle.ParticleUtil
import kr.kro.minestar.utility.string.toPlayer
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask

class SelectArea(val player: Player) {
    private var pos1: Location? = null
    private var pos2: Location? = null

    private var sortPos1: Location? = null
    private var sortPos2: Location? = null

    private val aquaParticle = ParticleUtil().also {
        it.particle = Particle.REDSTONE
        it.data = Particle.DustOptions(Color.AQUA, 2F)
        it.force = true
        it.players.add(player)
    }
    private val blueParticle = ParticleUtil().also {
        it.particle = Particle.REDSTONE
        it.data = Particle.DustOptions(Color.BLUE, 2F)
        it.force = true
        it.players.add(player)
    }

    private var task: BukkitTask? = null

    fun getPos1(): Location? {
        return pos1
    }

    fun getPos2(): Location? {
        return pos2
    }

    fun setPos1(loc: Location): Boolean {
        if (pos1 == loc) return false
        if (pos2 != null && pos2!!.world != loc.world) return false
        pos1 = loc.toBlockLocation()
        contourEnable()
        "$prefix §dPos1 이/가 [${pos1!!.blockX}, ${pos1!!.blockY}, ${pos1!!.blockZ}] 로 설정되었습니다.".toPlayer(player)
        return true
    }

    fun setPos2(loc: Location): Boolean {
        if (pos2 == loc) return false
        if (pos1 != null && pos1!!.world != loc.world) return false
        pos2 = loc.toBlockLocation()
        contourEnable()
        "$prefix §dPos2 이/가 [${pos2!!.blockX}, ${pos2!!.blockY}, ${pos2!!.blockZ}] 로 설정되었습니다.".toPlayer(player)
        return true
    }

    fun posClear() {
        pos1 = null
        pos2 = null
        task?.cancel()
        task = null
    }

    fun contourEnable() {
        pos1 ?: return
        pos2 ?: return
        task?.cancel()

        val xL: Int
        val xH: Int
        val yL: Int
        val yH: Int
        val zL: Int
        val zH: Int

        if (pos1!!.blockX <= pos2!!.blockX) {
            xL = pos1!!.blockX
            xH = pos2!!.blockX
        } else {
            xL = pos2!!.blockX
            xH = pos1!!.blockX
        }
        if (pos1!!.blockY <= pos2!!.blockY) {
            yL = pos1!!.blockY
            yH = pos2!!.blockY
        } else {
            yL = pos2!!.blockY
            yH = pos1!!.blockY
        }
        if (pos1!!.blockZ <= pos2!!.blockZ) {
            zL = pos1!!.blockZ
            zH = pos2!!.blockZ
        } else {
            zL = pos2!!.blockZ
            zH = pos1!!.blockZ
        }

        sortPos1 = Location(pos1!!.world, xL.toDouble(), yL.toDouble(), zL.toDouble())
        sortPos2 = Location(pos2!!.world, xH.toDouble(), yH.toDouble(), zH.toDouble())

        task = Bukkit.getScheduler().runTaskTimer(pl, Runnable {
            val world = sortPos1!!.world
            val x1 = sortPos1!!.blockX
            val x2 = sortPos2!!.blockX
            val y1 = sortPos1!!.blockY
            val y2 = sortPos2!!.blockY
            val z1 = sortPos1!!.blockZ
            val z2 = sortPos2!!.blockZ

            for (x in x1..x2) {
                val list = listOf(
                    Location(world, x.toDouble(), y1.toDouble(), z1.toDouble()).toCenter(),
                    Location(world, x.toDouble(), y2.toDouble(), z2.toDouble()).toCenter(),
                    Location(world, x.toDouble(), y1.toDouble(), z2.toDouble()).toCenter(),
                    Location(world, x.toDouble(), y2.toDouble(), z1.toDouble()).toCenter()
                )
                if (x == x1 || x == x2) for (loc in list) blueParticle.play(loc)
                else for (loc in list) aquaParticle.play(loc)
            }
            for (y in y1..y2) {
                if (y == y1 || y == y2) continue
                val list = listOf(
                    Location(world, x1.toDouble(), y.toDouble(), z1.toDouble()).toCenter(),
                    Location(world, x2.toDouble(), y.toDouble(), z2.toDouble()).toCenter(),
                    Location(world, x1.toDouble(), y.toDouble(), z2.toDouble()).toCenter(),
                    Location(world, x2.toDouble(), y.toDouble(), z1.toDouble()).toCenter()
                )
                for (loc in list) aquaParticle.play(loc)
            }
            for (z in z1..z2) {
                if (z == z1 || z == z2) continue
                val list = listOf(
                    Location(world, x1.toDouble(), y1.toDouble(), z.toDouble()).toCenter(),
                    Location(world, x2.toDouble(), y2.toDouble(), z.toDouble()).toCenter(),
                    Location(world, x1.toDouble(), y2.toDouble(), z.toDouble()).toCenter(),
                    Location(world, x2.toDouble(), y1.toDouble(), z.toDouble()).toCenter()
                )
                for (loc in list) aquaParticle.play(loc)
            }
        }, 0, 10)
    }
}