package kr.kro.minestar.test

import org.bukkit.entity.Player

abstract class Arg {
    abstract val argList: List<Arg>

    abstract fun run(player: Player)

    override fun toString(): String = javaClass.simpleName.lowercase()
}