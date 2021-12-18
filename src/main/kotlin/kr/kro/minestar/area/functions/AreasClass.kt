package kr.kro.minestar.area.functions

import kr.kro.minestar.area.Main.Companion.pl
import kr.kro.minestar.area.Main.Companion.prefix
import kr.kro.minestar.area.data.Areas
import kr.kro.minestar.area.data.SelectArea
import kr.kro.minestar.utility.bool.BooleanScript
import kr.kro.minestar.utility.bool.addScript
import kr.kro.minestar.utility.string.remove
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object AreasClass {

    val areasList = mutableListOf<Areas>()
    val SelectAreaMap = hashMapOf<Player, SelectArea>()

    val nullArea = false.addScript("$prefix §c존재하지 않는 구역입니다.")

    /**
     * 데이터 폴더 내에 있는 모든 Areas 파일을 활성화 합니다.
     */
    fun areasEnable() {
        if (!pl.dataFolder.exists()) pl.dataFolder.mkdir()
        val files = pl.dataFolder.listFiles()
        if (files.isEmpty()) return
        for (f in files) {
            if (!f.name.contains(".yml")) continue
            areasList.add(Areas(f.name.remove(".yml")))
        }
    }

    /**
     * 활성화된 Areas 클래스 이름 리스트를 가져옵니다.
     */
    fun nameList(): List<String> {
        val list = mutableListOf<String>()
        for (area in areasList) list.add(area.name)
        return list
    }

    /**
     * 새로운 Areas 파일을 만든 후 활성화 합니다.
     */
    fun createArea(name: String): BooleanScript {
        if (nameList().contains(name)) return false.addScript("$prefix §c이미 같은 이름의 구역이 존재합니다.")
        val areas = Areas(name)
        areasList.add(areas)
        areas.save()
        return false.addScript("$prefix §e$name §a구역을 성공적으로 생성 하였습니다.")
    }

    /**
     * 다른 위치에 있는 Areas 파일을 가져와 복사 후 활성화 합니다.
     */
    fun loadAreaFile(file: File): BooleanScript {
        if (!file.name.contains(".yml")) return false.addScript("$prefix §cYaml 형식의 파일이 아닙니다.")
        if (nameList().contains(file.name.remove(".yml"))) return BooleanScript(false, "$prefix §c이미 같은 이름의 구역이 존재합니다.")
        val data = YamlConfiguration.loadConfiguration(file)
        val copy = File(pl.dataFolder, file.name)
        data.save(copy)
        val areas = Areas(file.name.remove(".yml"))
        areasList.add(areas)
        return false.addScript("$prefix §e${file.name.remove(".yml")} §a구역을 성공적으로 복사 하였습니다.")
    }

    /**
     * 이름이 name 과 같은 구역 안에 있는지 확인 합니다.
     */
    fun isInside(areasName: String, loc: Location): Boolean {
        val areas = getArea(areasName) ?: return false
        return areas.isInside(loc)
    }

    /**
     * name 과 같은 이름의 Areas 클래스를 가져옵니다.
     */
    fun getArea(name: String): Areas? {
        for (area in areasList) if (area.name == name) return area
        return null
    }

    /**
     * 플레이어에게 SelectArea 클래스를 등록합니다.
     */
    fun selectAreaRegister(player: Player) {
        if (!player.isOp) return
        if (SelectAreaMap[player] == null) SelectAreaMap[player] = SelectArea(player)
    }

    /**
     * SelectArea 클래스의 Pos를 초기화 합니다.
     */
    fun selectAreaPosClear(player: Player): BooleanScript {
        val sel = SelectAreaMap[player] ?: return false.addScript("$prefix §c플레이어의 선택모드를 찾을 수 없습니다.")
        sel.posClear()
        return return true.addScript("$prefix §d선택 구역을 초기화 하였습니다.")
    }

    /**
     * SelectArea 클래스로 선택된 구역을 Areas 클래스에 추가합니다.
     */
    fun addSelectAreaToAreas(areasName: String, player: Player): BooleanScript {
        val areas = getArea(areasName) ?: return nullArea
        val selArea = SelectAreaMap[player] ?: return false.addScript("$prefix §c플레이어의 선택모드를 찾을 수 없습니다.")
        val pos1 = selArea.getPos1() ?: return false.addScript("$prefix §cPos1 이/가 선택되지 않았습니다.")
        val pos2 = selArea.getPos2() ?: return false.addScript("$prefix §cPos2 이/가 선택되지 않았습니다.")

        return areas.addArea(pos1, pos2)
    }

    /**
     * Areas의 int 번 구역을 삭제 합니다.
     */
    fun removeArea(areasName: String, int: Int): BooleanScript {
        val areas = getArea(areasName) ?: return nullArea
        return areas.removeArea(int)
    }

    /**
     * 두 구역이 서로 일치 하는 구역인지 확인합니다.
     */
    fun isSameArea(map1: Map<String, Location>, map2: Map<String, Location>): Boolean {
        if (!isAreaMap(map1)) return false
        if (!isAreaMap(map2)) return false
        if (map1["pos1"]!!.world != map2["pos1"]!!.world) return false
        val sortMap1 = areaMapSort(map1)
        val sortMap2 = areaMapSort(map2)
        return (sortMap1 == sortMap2)
    }

    /**
     * Map 이 옳바른 구역 Map 인지 구분합니다.
     */
    fun isAreaMap(map: Map<String, Location>): Boolean {
        if (map.keys.size != 2) return false
        if (!map.keys.contains("pos1")) return false
        if (!map.keys.contains("pos2")) return false
        if (map["pos1"]!!.world != map["pos2"]!!.world) return false
        return true
    }

    /**
     * isSameArea 를 사용 하기 위한 구역 Map 의 좌표들을 정렬한 Map 을 출력합니다.
     */
    fun areaMapSort(map: Map<String, Location>): Map<String, Int>? {
        if (!isAreaMap(map)) return null
        val pos1 = map["pos1"]!!
        val pos2 = map["pos2"]!!

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

        return mapOf(
            Pair("xL", xL),
            Pair("xH", xH),
            Pair("yL", yL),
            Pair("yH", yH),
            Pair("zL", zL),
            Pair("zH", zH),
        )
    }

    /**
     * Areas 의 Display 기능을 꺼져 있으면 켜고, 켜져 있으면 끕니다.
     */
    fun areasDisplayOnOff(areasName: String): BooleanScript {
        val areas = getArea(areasName) ?: return nullArea
        if (areas.isDisplaying()) {
            areas.offDisplay()
            return true.addScript("$prefix §c구역 표시 기능을 끕니다.")
        }
        areas.onDisplay()
        return true.addScript("$prefix §a구역 표시 기능을 켭니다.")
    }

    /**
     * Areas 의 Display List 에 Display 할 Area 를 추가 합니다.
     */
    fun areasDisplayAdd(areasName: String, int: Int): BooleanScript {
        val areas = getArea(areasName) ?: return nullArea
        return areas.addDisplay(int)
    }

    /**
     * Areas 의 Display List 에 모든 Area 를 추가 합니다.
     */
    fun areasDisplayAddAll(areasName: String): BooleanScript {
        val areas = getArea(areasName) ?: return nullArea
        return areas.allDisplay()
    }

    /**
     *  Areas 의 Display List 에서 int 에 해당 하는 Area 를 제거 합니다.
     */
    fun areasDisplayRemove(areasName: String, int: Int): BooleanScript {
        val areas = getArea(areasName) ?: return nullArea
        return areas.removeDisplay(int)
    }

    /**
     *  Areas 의 Display List 를 초기화 합니다.
     */
    fun areasDisplayClear(areasName: String): BooleanScript {
        val areas = getArea(areasName) ?: return nullArea
        return areas.clearDisplay()
    }
}