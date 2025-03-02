package dev.thource.runelite.dudewheresmystuff.minigames;

import dev.thource.runelite.dudewheresmystuff.DudeWheresMyStuffPlugin;
import dev.thource.runelite.dudewheresmystuff.ItemStack;
import lombok.Getter;
import net.runelite.api.ItemID;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;

/** LastManStanding is responsible for tracking the player's Last Man Standing points. */
@Getter
public class LastManStanding extends MinigamesStorage {

  private final ItemStack points = new ItemStack(ItemID.SKULL, "Points", 0, 0, 0, true);

  private Widget shopWidget = null;

  LastManStanding(DudeWheresMyStuffPlugin plugin) {
    super(MinigamesStorageType.LAST_MAN_STANDING, plugin);

    items.add(points);
  }

  @Override
  public boolean onGameTick() {
    return updateFromWidgets();
  }

  @Override
  public boolean onWidgetLoaded(WidgetLoaded widgetLoaded) {
    if (widgetLoaded.getGroupId() == 645) {
      shopWidget = plugin.getClient().getWidget(645, 0);
    }

    return updateFromWidgets();
  }

  @Override
  public boolean onWidgetClosed(WidgetClosed widgetClosed) {
    if (widgetClosed.getGroupId() == 645) {
      shopWidget = null;
    }

    return false;
  }

  boolean updateFromWidgets() {
    if (shopWidget == null) {
      return false;
    }

    lastUpdated = System.currentTimeMillis();
    int newPoints = plugin.getClient().getVarpValue(261);
    if (newPoints == points.getQuantity()) {
      return !this.getType().isAutomatic();
    }

    points.setQuantity(newPoints);
    return true;
  }
}
