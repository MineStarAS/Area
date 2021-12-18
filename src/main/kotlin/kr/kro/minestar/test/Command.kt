package kr.kro.minestar.test

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

interface Command : CommandExecutor, TabCompleter {
    val argList: List<Arg>

    fun run(player: Player)

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) run(sender).also { return false }
        for (arg in argList) {
            if (arg.toString().lowercase() != args[0].lowercase()) continue
            ArgClass.runArg(sender, arg, args.toMutableList())
            return false
        }
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()
        return list
    }
}