package kr.kro.minestar.area

import kr.kro.minestar.area.functions.AreasClass
import kr.kro.minestar.area.functions.evnets.AlwaysEvent
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var pl: Main
        const val prefix = "§f[§9AREA§f]"
    }

    override fun onEnable() {
        pl = this
        logger.info("$prefix §aEnable")
        getCommand("area")?.setExecutor(CMD)
        Bukkit.getPluginManager().registerEvents(AlwaysEvent, this)

        AreasClass.areasEnable()
        for (player in Bukkit.getOnlinePlayers()) if (player.isOp) AreasClass.selectAreaRegister(player)
    }

    override fun onDisable() {
    }
}