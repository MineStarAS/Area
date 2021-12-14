package kr.kro.minestar.area

import kr.kro.minestar.area.functions.AreaClass
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

        AreaClass.areasEnable()
    }

    override fun onDisable() {
    }
}