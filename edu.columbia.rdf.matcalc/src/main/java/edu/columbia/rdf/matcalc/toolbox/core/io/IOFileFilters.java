package edu.columbia.rdf.matcalc.toolbox.core.io;

import java.util.Iterator;
import java.util.Map.Entry;

import org.jebtk.core.collections.IterMap;
import org.jebtk.core.collections.IterTreeMap;
import org.jebtk.modern.io.GuiFileExtFilter;

public class IOFileFilters implements Iterable<Entry<GuiFileExtFilter, Boolean>> {
  private IterMap<GuiFileExtFilter, Boolean> mFileFilters = new IterTreeMap<GuiFileExtFilter, Boolean>();

  public void add(GuiFileExtFilter filter) {
    add(filter, true);
  }

  public void add(GuiFileExtFilter filter, boolean showInUI) {
    mFileFilters.put(filter, showInUI);
  }

  @Override
  public Iterator<Entry<GuiFileExtFilter, Boolean>> iterator() {
    return mFileFilters.iterator();
  }

}
