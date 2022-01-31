package kr.kro.minestar.area.objects

import kr.kro.minestar.utility.item.display
import kr.kro.minestar.utility.item.flagAll
import kr.kro.minestar.utility.material.item
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

object ItemObject {
    val positionSelector = Material.WOODEN_PICKAXE.item().display("Position Selector").also {
        it.itemMeta = it.itemMeta.also { meta ->
            meta.isUnbreakable = true
            meta.addEnchant(Enchantment.LUCK, 1, false)
        }
        it.flagAll()
    }
}
