package ie.aib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/** Progress dialog with the same cancel button used by the banking screens. */
public final class CancelDialog {
  private CancelDialog() {
    super();
  }

  /**
   * Shows a cancellable progress dialog.
   *
   * @param context host screen
   * @param title dialog title
   * @param message dialog message
   * @param onCancel runs when the user taps Cancel
   * @return the showing dialog
   */
  public static ProgressDialog show(
      final Context context, // .
      final String title, // .
      final String message, // .
      final Runnable onCancel) {
    final ProgressDialog dialog = new ProgressDialog(context);
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setButton(
        "Cancel",
        new DialogInterface.OnClickListener() {
          public void onClick(final DialogInterface source, final int which) {
            try {
              onCancel.run();
            } catch (final Exception ignored) {
              ignored.getClass();
            }
            return;
          }
        });
    dialog.show();
    return dialog;
  }
}
