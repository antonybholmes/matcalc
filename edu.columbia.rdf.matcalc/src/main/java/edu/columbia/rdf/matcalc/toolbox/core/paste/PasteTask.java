package edu.columbia.rdf.matcalc.toolbox.core.paste;

import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingWorker;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.collections.ArrayListCreator;
import org.jebtk.core.collections.CountMap;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashSetCreator;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Io;
import org.jebtk.core.io.TmpService;
import org.jebtk.core.stream.Stream;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MixedWorksheetParser;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.status.StatusService;
import org.xml.sax.SAXException;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

/**
 * Overlap segments.
 * 
 * @author Antony Holmes
 */
public class PasteTask extends SwingWorker<Void, Void> {

  private List<Path> mFiles;

  private MainMatCalcWindow mWindow;

  private boolean mHeaderMode;

  private String mDelimiter;

  public PasteTask(MainMatCalcWindow window, List<Path> files, String delimiter, boolean indexMode) {
    mWindow = window;
    mFiles = files;
    mDelimiter = delimiter;
    mHeaderMode = indexMode;
  }

  @Override
  public Void doInBackground() {
    try {
      paste();
    } catch (InvalidFormatException | ClassNotFoundException | InstantiationException | IllegalAccessException
        | IOException | SAXException | ParserConfigurationException | FontFormatException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }

    return null;
  }

  private void paste() throws IOException, InvalidFormatException, ClassNotFoundException, InstantiationException,
      IllegalAccessException, SAXException, ParserConfigurationException, FontFormatException,
      UnsupportedLookAndFeelException {
    // First get all the regions to search into one sorted map

    Path pwd = RecentFilesService.getInstance().getPwd();

    Path file = pasteByIndex();

    // mWindow.openFile(file);

    DataFrame m = new MixedWorksheetParser(1, 0, TextUtils.TAB_DELIMITER).parse(file);

    mWindow.history().addToHistory("Paste", m);

    // Set the pwd again so that we don't default to the tmp folder
    // after opening the tmp file
    RecentFilesService.getInstance().setPwd(pwd);
  }

  private Path pasteByIndex() throws IOException {
    String line;
    BufferedReader reader;
    BufferedWriter writer;
    List<String> tokens;
    String key;

    StatusService.getInstance().setStatus("Working...");

    // CountMap<String> keyMap = new CountMap<String>();

    Map<String, Set<Path>> fileMap = DefaultHashMap.create(new HashSetCreator<Path>());

    List<String> keyList = new ArrayList<String>();

    /*
     * List<String> header = new UniqueArrayList<String>();
     * 
     * Splitter split = Splitter.onTab();
     * 
     * if (mHeaderMode) { for (Path file : mFiles) { reader =
     * FileUtils.newBufferedReader(file);
     * 
     * try { header.addAll(split.text(reader.readLine())); } finally {
     * reader.close(); } } }
     */

    int c = 0;

    for (int i = 0; i < mFiles.size(); ++i) {
      Path file = mFiles.get(i);

      reader = FileUtils.newBufferedReader(file);

      try {
        if (mHeaderMode) {
          // skip header
          reader.readLine();
        }

        while ((line = reader.readLine()) != null) {
          key = TextUtils.firstSplit(line, TextUtils.TAB_DELIMITER);

          if (!fileMap.containsKey(key)) {
            // Base the key list on the first file
            if (i == 0) {
              keyList.add(key);
            }
          }

          fileMap.get(key).add(file);

          if (c % 100000 == 0) {
            PasteModule.LOG.info("Processed {} records", c);
          }

          ++c;
        }
      } finally {
        reader.close();
      }
    }

    PasteModule.LOG.info("Removing unused keys...");

    int minSize = mFiles.size();

    int totalKeys = keyList.size();

    for (int i = 0; i < keyList.size(); ++i) {
      if (fileMap.get(keyList.get(i)).size() < minSize) {
        keyList.set(i, TextUtils.EMPTY_STRING);
        --totalKeys;
      }

      if (i % 100000 == 0) {
        PasteModule.LOG.info("{} keys to go...", i);
      }
    }

    PasteModule.LOG.info("Total keys {}", totalKeys);

    fileMap.clear();

    int index = 0;

    CountMap<String> keyMap = new CountMap<String>();

    for (int i = 0; i < keyList.size(); ++i) {
      key = keyList.get(i);

      if (!TextUtils.isNullOrEmpty(key)) {
        keyMap.put(key, index++);
      }
    }

    // free the memory
    keyList.clear();

    Path tmp1 = TmpService.getInstance().newTmpFile("t1", "txt");
    Path tmp2 = TmpService.getInstance().newTmpFile("t2", "txt");

    // write out the first file intact with just the core keys
    reader = FileUtils.newBufferedReader(mFiles.get(0));
    writer = FileUtils.newBufferedWriter(tmp1);

    c = 0;

    try {
      while ((line = reader.readLine()) != null) {
        if (Io.isEmptyLine(line)) {
          continue;
        }

        tokens = TextUtils.tabSplit(line);

        key = tokens.get(0);

        if (c == 0 || keyMap.containsKey(key)) {
          writer.write(line);
          writer.newLine();
        }

        ++c;
      }
    } finally {
      writer.close();
      reader.close();
    }

    // Stores the entries for a column
    Map<String, List<String>> columnMap = DefaultHashMap.create(new ArrayListCreator<String>());

    String header = TextUtils.EMPTY_STRING;

    for (int i = 1; i < mFiles.size(); ++i) {
      // load the file into memory
      // order the probes then append to the
      // temp file
      // rewrite the temp file with the new
      // data then repeat for each file

      reader = FileUtils.newBufferedReader(mFiles.get(i));

      c = 0;

      try {
        while ((line = reader.readLine()) != null) {
          if (Io.isEmptyLine(line)) {
            continue;
          }

          tokens = TextUtils.tabSplit(line);

          line = Stream.of(tokens).skip(1).asString().tabJoin();

          if (c == 0) {
            header = line;
          } else {
            key = tokens.get(0);

            columnMap.get(key).add(line);
          }

          ++c;
        }
      } finally {
        reader.close();
      }

      reader = FileUtils.newBufferedReader(tmp1);

      writer = FileUtils.newBufferedWriter(tmp2);

      try {
        c = 0;

        // write out the original temp file plus the
        // extension of each line
        while ((line = reader.readLine()) != null) {
          if (Io.isEmptyLine(line)) {
            continue;
          }

          key = TextUtils.firstSplit(line, TextUtils.TAB_DELIMITER);

          writer.write(line);
          writer.write(mDelimiter);

          if (c == 0) {
            writer.write(line);
            writer.write(mDelimiter);
            writer.write(header);
            writer.newLine();
          } else {
            if (!columnMap.containsKey(key)) {
              System.err.println("c" + key + " " + columnMap.size() + " " + line);
            }

            for (String l2 : columnMap.get(key)) {
              writer.write(line);
              writer.write(mDelimiter);
              writer.write(l2);
              writer.newLine();
            }
          }

          ++c;
        }
      } finally {
        reader.close();
        writer.close();
      }

      FileUtils.mv(tmp2, tmp1);
    }

    return tmp1;
  }
}
