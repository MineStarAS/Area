package kr.kro.minestar.area.functions

import kr.kro.minestar.area.Main.Companion.pl
import kr.kro.minestar.area.Main.Companion.prefix
import kr.kro.minestar.area.data.Area
import kr.kro.minestar.utility.string.remove
import kr.kro.minestar.utility.string.toPlayer
import org.bukkit.entity.Player

object AreaClass {

    val areaList = mutableListOf<Area>()
    val SelectAreaMap = hashMapOf<Player, SelectArea>()

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

    fun selectModToggle(player: Player) {
        if (!player.isOp) return
        if (SelectAreaMap[player] == null) {
            SelectAreaMap[player] = SelectArea(player)
            return "$prefix 선택모드 §a활성화".toPlayer(player)
        }
         SelectAreaMap.remove(player)
        return "$prefix 선택모드 §c비활성화".toPlayer(player)
    }
}