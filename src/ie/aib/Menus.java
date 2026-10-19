package ie.aib;

import android.app.Activity;
import android.view.Menu;

/** Small menu helpers so each screen only names its own items. */
public final class Menus {
  private Menus() {
    super();
  }

  /**
   * Inflates {@code menuId} into {@code menu}.
   *
   * @param activity host screen
   * @param menu menu being created
   * @param menuId resource to inflate
   * @return true
   */
  public static boolean inflate(
      final Activity activity, // .
      final Menu menu, // .
      final int menuId) {
    activity.getMenuInflater().inflate(menuId, menu);
    return true;
  }

  /**
   * Disables every listed menu item.
   *
   * @param menu open menu
   * @param ids item ids
   */
  public static void disable(final Menu menu, final int... ids) {
    for (int index = 0; index < ids.length; index++) {
      menu.findItem(ids[index]).setEnabled(false);
    }
  }
}
