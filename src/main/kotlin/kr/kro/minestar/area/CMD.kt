package kr.kro.minestar.area

import kr.kro.minestar.area.Main.Companion.prefix
import kr.kro.minestar.area.functions.AreaClass
import kr.kro.minestar.utility.string.toPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

object CMD : CommandExecutor, TabCompleter {
    private val args0 = listOf("create", "add", "remove", "select")
    override fun onCommand(player: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (player !is Player) return false
        if (args.isEmpty()) {
            player.sendMessage("null")
            return false
        }
        when (args[0]) {
            "test" -> {
            }
            args0[0] -> {
                if (args.size != 2) return false
                if (AreaClass.createArea(args[1])) "$prefix §a생성 성공".toPlayer(player).also { return false }
                "$prefix §c생성 실패".toPlayer(player).also { return false }
            }
            args0[1] -> {}
            args0[2] -> {}
            args0[3] -> AreaClass.selectModToggle(player)
        }

        return false
    }

    override fun onTabComplete(player: CommandSender, cmd: Command, alias: String, args: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()
        val last = args.size - 1
        if (last == 0) {
            for (s in args0) if (s.contains(args[last])) list.add(s)
            return list
        }
        if (!args0.contains(args[0])) return list
        if (last == 1) {
            if (args[0] == args0[0]) return list
            for (s in AreaClass.nameList()) if (s.contains(args[last])) list.add(s)
            return list
        }
        if (args.size > 1) when (args[0]) {
            args0[0] -> {}
            args0[1] -> {}
            args0[2] -> {}
        }

        return list
    }

}