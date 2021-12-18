package kr.kro.minestar.test.commands.test

import kr.kro.minestar.test.Arg
import kr.kro.minestar.test.Command
import kr.kro.minestar.utility.string.toPlayer
import org.bukkit.entity.Player

object Test : Command {
    override val argList: List<Arg> = listOf()
    val map = hashMapOf<String, Unit>()
    override fun run(player: Player) {
        "Command run Test String".toPlayer(player)
    }
}