package ie.aib;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ie.aib.entities.Person;

/** Short view lookups so call sites stay within the line limit. */
public final class Forms {
  /** Spinner dropdown layout. */
  public static final int DROP = android.R.layout.simple_spinner_dropdown_item;

  private Forms() {
    super();
  }

  /**
   * Text currently in an edit field.
   *
   * @param activity host
   * @param id field id
   * @return field text
   */
  public static String text(final Activity activity, final int id) {
    final EditText field = (EditText) activity.findViewById(id);
    return field.getText().toString();
  }

  /**
   * Edit field on an activity.
   *
   * @param activity host
   * @param id field id
   * @return edit field
   */
  public static EditText edit(final Activity activity, final int id) {
    return (EditText) activity.findViewById(id);
  }

  /**
   * Spinner on the activity.
   *
   * @param activity host
   * @param id spinner id
   * @return spinner
   */
  public static Spinner spinner(final Activity activity, final int id) {
    return (Spinner) activity.findViewById(id);
  }

  /**
   * Parses a numeric web field.
   *
   * @param raw web value
   * @return integer
   */
  public static int number(final String raw) {
    return Integer.parseInt(raw);
  }

  /**
   * Sets a text view.
   *
   * @param view text view
   * @param value text
   */
  public static void show(final TextView view, final String value) {
    view.setText(value);
  }

  /** Loads the statement for the account chosen on the overview. */
  public static void fetchStatement() {
    final Person person = Person.getInstance();
    final int account = StatementActivity.getAccountNumber();
    person.getAccounts().get(account).getStatement();
  }
}
