package edu.columbia.rdf.matcalc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jebtk.bioinformatics.file.BioPathUtils;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.io.ReaderUtils;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.external.microsoft.XLSXMetaData;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.io.RecentFilesService;

import edu.columbia.rdf.matcalc.toolbox.core.io.IOModule;

/**
 * The Class OpenFile.
 */
public class OpenFiles {

  /** The m window. */
  private MainMatCalcWindow mWindow;

  /** The m open mode. */
  private OpenMode mOpenMode = OpenMode.NEW_WINDOW;

  private FileType mFileType = FileType.MIXED;

  /** How many header rows there are. Default assume 1. */
  private int mHeaders = 1;

  /** The m row annotations. */
  private int mIndexes = 0;

  /** The m delimiter. */
  private String mDelimiter = TextUtils.TAB_DELIMITER;

  /** The m skip lines. */
  private Collection<String> mSkipLines = Collections.emptyList();

  /** The m files. */
  //private Collection<Path> mFiles;

  /**
   * Instantiates a new open file.
   *
   * @param window the window
   * @param files the files
   */
  public OpenFiles(MainMatCalcWindow window) {
    mWindow = window;
  }

  /**
   * Instantiates a new open file.
   *
   * @param openFile the open file
   * @param files the files
   */
  public OpenFiles(OpenFiles openFile) {
    mWindow = openFile.mWindow;
    mHeaders = openFile.mHeaders;
    mIndexes = openFile.mIndexes;
    mSkipLines = openFile.mSkipLines;
    mOpenMode = openFile.mOpenMode;
    mFileType = openFile.mFileType;
  }

  public OpenFiles(OpenFiles openFile, MainMatCalcWindow window) {
    this(openFile);

    mWindow = window;
  }

  public boolean open(Path file) throws IOException {
    return open(CollectionUtils.asList(file));
  }
  
  /**
   * Open.
   *
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean open(Collection<Path> files) throws IOException {
    if (CollectionUtils.isNullOrEmpty(files)) {
      return false;
    }
    
    boolean status = false;

    for (Path file : files) {
      
      if (mWindow.mFilesModel.size() > 0 && mOpenMode == OpenMode.NEW_WINDOW) {
        MainMatCalcWindow window = new MainMatCalcWindow(mWindow.getAppInfo());
        window.setVisible(true);

        new OpenFiles(this, window).open(file);

        status |= false;
      } else {
        String ext = PathUtils.getFileExt(file);

        List<IOModule> modules = mWindow.getFileModules(ext);

        for (IOModule module : modules) {
          DataFrame m = module.open(mWindow,
              file,
              mFileType,
              mHeaders,
              mIndexes,
              mDelimiter,
              mSkipLines);

          if (m != null) {
            mWindow.openMatrices().open(file, m);

            RecentFilesService.getInstance().add(file);

            status |= true;

            break;
          }
        }
      }
    }

    return status;
  }
  
  public boolean read(Path file) throws IOException {
    return read(CollectionUtils.asList(file));
  }

  /**
   * Auto open a file without prompting a user to specify parameters.
   *
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean read(Collection<Path> files) throws IOException {
    if (CollectionUtils.isNullOrEmpty(files)) {
      return false;
    }
    
    boolean status = false;

    for (Path file : files) {
      if (mWindow.mFilesModel.size() > 0 && mOpenMode == OpenMode.NEW_WINDOW) {
        MainMatCalcWindow window = new MainMatCalcWindow(mWindow.getAppInfo());
        window.setVisible(true);

        new OpenFiles(this, new MainMatCalcWindow(mWindow.getAppInfo())).read(file);

        status |= false;
      } else {
        String ext = PathUtils.getFileExt(file);

        List<IOModule> modules = mWindow.mOpenFileModuleMap.get(ext);

        for (IOModule module : modules) {
          DataFrame m = module.read(mWindow,
              file,
              mFileType,
              mHeaders,
              mIndexes,
              mDelimiter,
              mSkipLines);

          if (m != null) {
            mWindow.openMatrices().open(file, m);

            status |= true;

            break;
          }
        }
      }
    }

    return status;
  }

  /**
   * Open mode.
   *
   * @param openMode the open mode
   * @return the open file
   */
  public OpenFiles openMode(OpenMode openMode) {
    OpenFiles of = new OpenFiles(this);
    of.mOpenMode = openMode;
    return of;
  }
  
  public OpenFiles newWindow() {
    return openMode(OpenMode.NEW_WINDOW);
  }

  public OpenFiles type(FileType type) {
    OpenFiles of = new OpenFiles(this);
    of.mFileType = type;
    return of;
  }

  /**
   * Headers.
   *
   * @param headers the headers
   * @return the open file
   */
  public OpenFiles headers(int headers) {
    OpenFiles of = new OpenFiles(this);
    of.mHeaders = headers;
    return of;
  }

  /**
   * No header.
   *
   * @return the open file
   */
  public OpenFiles noHeader() {
    return headers(0);
  }

  /**
   * Specify the number of row annotations.
   *
   * @param indexes the number of row annotations.
   * 
   * @return
   */
  public OpenFiles indexes(int indexes) {
    OpenFiles of = new OpenFiles(this);
    of.mIndexes = indexes;
    return of;
  }

  /**
   * Specify the delimiter. The default is the tab character.
   *
   * @param delimiter the delimiter
   * @return the open file
   */
  public OpenFiles delimiter(String delimiter) {
    OpenFiles of = new OpenFiles(this);
    of.mDelimiter = delimiter;
    return of;
  }

  public OpenFiles skipLines(String skip) {
    return skipLines(CollectionUtils.asList(skip));
  }

  /**
   * Specify lines that can be skipped at the beginning of the file e.g. for
   * example # or %.
   * 
   * @param skipLines the characters that can be skipped.
   * @return the open file
   */
  public OpenFiles skipLines(Collection<String> skipLines) {
    OpenFiles of = new OpenFiles(this);
    of.mSkipLines = skipLines;
    return of;
  }

  /**
   * Guess the file delimiter.
   *
   * @param file the file
   * @return the string
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String guessDelimiter(Path file, Collection<String> skipMatches)
      throws IOException {
    //
    // Work out if we need to skip annotation rows that should be
    // ignored

    int skipLines = ReaderUtils.countHeaderLines(file, skipMatches);

    BufferedReader reader = FileUtils.newBufferedReader(file);

    String ret = TextUtils.TAB_DELIMITER;

    try {
      // Skip past any header
      ReaderUtils.skipLines(reader, skipLines);

      char[] chars = reader.readLine().toCharArray();

      for (char c : chars) {
        // System.err.println("test " + c + " " + (c == '\t'));

        boolean found = false;

        switch (c) {
        case '\t':
          ret = TextUtils.TAB_DELIMITER;
          found = true;
          break;
        case ';':
          ret = TextUtils.SEMI_COLON_DELIMITER;
          found = true;
          break;
        case ',':
          ret = TextUtils.COMMA_DELIMITER;
          found = true;
          break;
        default:
          found = false;
          break;
        }

        if (found) {
          break;
        }
      }
    } finally {
      reader.close();
    }

    return ret;
  }

  /**
   * Guess if matrix contains only numbers since we get a speed up for
   * using numerical matrices.
   * 
   * @param file
   * @return
   * @throws IOException
   */
  public static boolean guessNumerical(Path file,
      int headers,
      String delimiter,
      int rowAnnotations,
      Collection<String> skipMatches) throws IOException {
    //
    // Work out if we need to skip annotation rows that should be
    // ignored

    int skipLines = ReaderUtils.countHeaderLines(file, skipMatches);

    BufferedReader reader = FileUtils.newBufferedReader(file);

    List<String> tokens;

    try {
      // Skip past any header
      ReaderUtils.skipLines(reader, skipLines);

      ReaderUtils.skipLines(reader, headers);

      tokens = Splitter.on(delimiter).text(reader.readLine());
    } finally {
      reader.close();
    }

    return TextUtils.areNumbers(CollectionUtils.tail(tokens, rowAnnotations));
  }

  public static int estimateRowAnnotations(Path file,
      int headers,
      Collection<String> skipLines) throws IOException {
    if (BioPathUtils.ext().xlsx().test(file)
        || BioPathUtils.ext().xls().test(file)) {
      return 0;
    }

    BufferedReader reader = FileUtils.newBufferedReader(file);

    int ret = 0;

    try {
      ReaderUtils.skipLines(reader, headers);

      List<String> tokens = Splitter.onTab().text(reader.readLine());

      ret = XLSXMetaData.estimateRowAnnotations(tokens);
    } finally {
      reader.close();
    }

    return ret;
  }
}
