package ie.aib;

import android.app.ListActivity;
import android.view.Menu;

/** List screen with the shared logout countdown. */
public abstract class BankingList extends ListActivity implements onTimedOut {
  /** Countdown shown above the list. */
  private SessionClock session;

  /**
   * @return options menu for this list
   */
  protected abstract int optionsMenu();

  /**
   * Shows {@code listTitle} and arms auto-logout for the list.
   *
   * @param listTitle resting title
   */
  protected final void arm(final String listTitle) {
    final SessionClock clock = new SessionClock(this);
    clock.start(listTitle);
    this.session = clock;
  }

  /**
   * @param menu list menu being created @return true
   */
  @Override
  public final boolean onCreateOptionsMenu(final Menu menu) {
    return Menus.inflate(this, menu, this.optionsMenu());
  }

  /** Closes a banking list. */
  @Override
  public final void timedOut() {
    this.session.timedOut();
  }

  /**
   * @param countdown remaining time for a list screen, or null
   */
  @Override
  public final void countDownTitle(final String countdown) {
    this.session.countDownTitle(countdown);
  }
}
