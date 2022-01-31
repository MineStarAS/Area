package kr.kro.minestar.area.functions.evnets

import kr.kro.minestar.area.functions.AreasClass
import kr.kro.minestar.area.objects.ItemObject
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.EquipmentSlot

object AlwaysEvent : Listener {

    @EventHandler
    fun selectPos(e: PlayerInteractEvent) {
        if (!e.player.isOp) return
        if (e.player.gameMode != GameMode.CREATIVE) return
        if (e.player.inventory.itemInMainHand != ItemObject.positionSelector) return
        val select = AreasClass.SelectAreaMap[e.player] ?: return
        val block = e.clickedBlock ?: return
        when (e.action) {
            Action.LEFT_CLICK_BLOCK -> select.setPos1(block.location)
            Action.RIGHT_CLICK_BLOCK -> {
                if (e.hand == EquipmentSlot.HAND) return
                select.setPos2(block.location)
            }
            else -> return
        }
        e.isCancelled = true
    }

    @EventHandler
    fun join(e: PlayerJoinEvent) {
        if (!e.player.isOp) return
        AreasClass.selectAreaRegister(e.player)
    }
}