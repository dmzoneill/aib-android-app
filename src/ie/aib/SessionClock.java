package ie.aib;

import android.app.Activity;

/** Logout countdown shared by every banking screen. */
public final class SessionClock {
  /** Screen that owns this countdown. */
  private final Activity activity;

  /** Title restored when the countdown message clears. */
  private String screenTitle;

  /**
   * Binds the clock to {@code host}.
   *
   * @param host visible screen
   */
  public SessionClock(final Activity host) {
    this.activity = host;
  }

  /**
   * Shows {@code restingTitle} and arms the auto-logout timer.
   *
   * @param restingTitle resting title
   */
  public void start(final String restingTitle) {
    this.screenTitle = restingTitle;
    this.activity.setTitle(restingTitle);
    Timeout.getInstance().attach((onTimedOut) this.activity);
    Timeout.getInstance().autoLogoutTimer();
  }

  /** Closes the screen when the session times out. */
  public void timedOut() {
    try {
      this.activity.finish();
    } catch (final Exception ignored) {
      ignored.getClass();
    }
  }

  /**
   * Shows the countdown, or restores the title when {@code countdown} is null.
   *
   * @param countdown remaining-time title, or null
   */
  public void countDownTitle(final String countdown) {
    if (countdown == null) {
      this.activity.setTitle(this.screenTitle);
    } else {
      this.activity.setTitle(countdown);
    }
  }
}
