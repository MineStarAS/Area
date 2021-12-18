package kr.kro.minestar.area

import kr.kro.minestar.area.Main.Companion.prefix
import kr.kro.minestar.area.functions.AreasClass
import kr.kro.minestar.utility.string.toPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

object CMD : CommandExecutor, TabCompleter {
    private enum class Args0 { CREATE, ADD, REMOVE, CLEAR, DISPLAY }

    override fun onCommand(player: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (player !is Player) return false
        if (!player.isOp) return false
        if (args.isEmpty()) {
            "$prefix §eCommands List".toPlayer(player)
            for (s in Args0.values()) "- ${s.toString().lowercase()}".toPlayer(player)
            return false
        }
        val ex = "$prefix §c/area ${args[0]}"
        when (Args0.valueOf(args[0])) {
            Args0.CREATE -> {
                if (args.size != 2) "$ex <AreasName>".toPlayer(player).also { return false }
                AreasClass.createArea(args[1]).script.toPlayer(player).also { return false }
            }
            Args0.ADD -> {
                if (args.size != 2) "$ex <AreasName>".toPlayer(player).also { return false }
                AreasClass.addSelectAreaToAreas(args[1], player).script.toPlayer(player).also { return false }
            }
            Args0.REMOVE -> {
                if (args.size != 3) "$ex <AreasName> <Number>".toPlayer(player).also { return false }
                args[2].toIntOrNull() ?: "$prefix §c'${args[2]}' 은/는 정수가 아닙니다.".toPlayer(player).also { return false }
                AreasClass.removeArea(args[1], args[2].toInt()).script.toPlayer(player).also { return false }
            }
            Args0.CLEAR -> AreasClass.selectAreaPosClear(player).script.toPlayer(player).also { return false }
            Args0.DISPLAY -> {
                val displayArgs = listOf("view", "add", "all", "remove", "clear")
                if (args.size == 1) "$prefix §c구역 이름을 작성 해주시기 바랍니다.".toPlayer(player).also { return false }
                if (args.size == 2) {
                    "$prefix §eDisplay Commands List".toPlayer(player)
                    for (s in displayArgs) "- /area ${args[0]} $s".toPlayer(player)
                    return false
                }
                when (args[2]) {
                    displayArgs[0] -> {
                        if (args.size != 3) "$ex <AreasName> ${args[2]}".toPlayer(player).also { return false }
                        AreasClass.areasDisplayOnOff(args[1]).also { return false }
                    }
                    displayArgs[1] -> {
                        if (args.size != 4) "$ex <AreasName> ${args[2]} <Number>".toPlayer(player).also { return false }
                        args[3].toIntOrNull() ?: "$prefix §c'${args[3]}' 은/는 정수가 아닙니다.".toPlayer(player).also { return false }
                        AreasClass.areasDisplayAdd(args[1], args[3].toInt()).also { return false }
                    }
                    displayArgs[2] -> {
                        if (args.size != 3) "$ex <AreasName> ${args[2]}".toPlayer(player).also { return false }
                        AreasClass.areasDisplayAddAll(args[1]).also { return false }
                    }
                    displayArgs[3] -> {
                        if (args.size != 4) "$ex <AreasName> ${args[2]} <Number>".toPlayer(player).also { return false }
                        args[3].toIntOrNull() ?: "$prefix §c'${args[3]}' 은/는 정수가 아닙니다.".toPlayer(player).also { return false }
                        AreasClass.areasDisplayRemove(args[1], args[3].toInt()).also { return false }
                    }
                    displayArgs[4] -> {
                        if (args.size != 3) "$ex <AreasName> ${args[2]}".toPlayer(player).also { return false }
                        AreasClass.areasDisplayClear(args[1]).also { return false }
                    }
                }
            }
        }

        return false
    }

    override fun onTabComplete(player: CommandSender, cmd: Command, alias: String, args: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()
        val last = args.size - 1
        if (!player.isOp) return list
        if (last == 0) {
            for (s in Args0.values().tos) if (s.contains(args[last])) list.add(s)
            return list
        }
        if (!args0.contains(args[0])) return list
        if (last == 1) {
            if (args[0] == args0[0]) return list
            if (args[0] == args0[3]) return list
            for (s in AreasClass.nameList()) if (s.contains(args[last])) list.add(s)
            return list
        }
        if (args.size > 1) when (args[0]) {
            args0[0] -> {}
            args0[1] -> {}
            args0[2] -> {}
            args0[3] -> {}
            args0[4] -> {
                if (args.size == 3) {
                    val displayArgs = listOf("view", "add", "all", "remove", "clear")
                    for (s in displayArgs) if (s.contains(args[last])) list.add(s)
                }
            }
        }

        return list
    }

}