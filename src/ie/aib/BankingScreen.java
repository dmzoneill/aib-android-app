package ie.aib;

import android.app.Activity;

/** Activity with the shared logout countdown. */
public abstract class BankingScreen extends Activity implements onTimedOut {
  /** Countdown shown in the window title. */
  private SessionClock session;

  /**
   * Shows {@code restingTitle} and arms auto-logout.
   *
   * @param restingTitle resting title
   */
  protected final void arm(final String restingTitle) {
    this.session = new SessionClock(this);
    this.session.start(restingTitle);
  }

  /** Closes a non-list banking screen. */
  @Override
  public final void timedOut() {
    this.session.timedOut();
  }

  /**
   * @param countdown remaining time for a non-list screen, or null
   */
  @Override
  public final void countDownTitle(final String countdown) {
    this.session.countDownTitle(countdown);
  }
}
