package kr.kro.minestar.test

import kr.kro.minestar.utility.string.toPlayer
import org.bukkit.entity.Player

object ArgClass {
    fun runArg(player: Player, arg: Arg, args: MutableList<String>) {
        if (arg.argList.size <= 1) {
            arg.run(player)
            return
        }
        for (arg in arg.argList) {
            if (arg.toString().lowercase() != args[1].lowercase()) continue
            args.removeAt(0)
            args.size.toString().toPlayer(player)
            runArg(player, arg, args)
            return
        }
        "out".toPlayer(player)
        arg.run(player)
        return
    }
}