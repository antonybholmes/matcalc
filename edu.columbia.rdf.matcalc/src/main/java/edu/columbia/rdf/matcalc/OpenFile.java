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
import org.jebtk.core.text.RegexUtils;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.external.microsoft.XLSXMetaData;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.io.RecentFilesService;

import edu.columbia.rdf.matcalc.toolbox.FileModule;

/**
 * The Class OpenFile.
 */
public class OpenFile {

	/** The m window. */
	private MainMatCalcWindow mWindow;

	/** The m open mode. */
	private OpenMode mOpenMode = OpenMode.NEW_WINDOW;

	private FileType mFileType = FileType.MIXED;

	/** How many header rows there are. Default assume 1. */
	private int mHeaders = 1;

	/** The m row annotations. */
	private int mRowAnnotations = 0;

	/** The m delimiter. */
	private String mDelimiter = TextUtils.TAB_DELIMITER;

	/** The m skip lines. */
	private Collection<String> mSkipLines = Collections.emptyList();

	/** The m files. */
	private Collection<Path> mFiles;

	/**
	 * Instantiates a new open file.
	 *
	 * @param window the window
	 * @param file the file
	 */
	public OpenFile(MainMatCalcWindow window, Path file) {
		this(window, CollectionUtils.asList(file));
	}

	/**
	 * Instantiates a new open file.
	 *
	 * @param window the window
	 * @param files the files
	 */
	public OpenFile(MainMatCalcWindow window, Collection<Path> files) {
		mWindow = window;
		mFiles = files;
	}

	/**
	 * Instantiates a new open file.
	 *
	 * @param openFile the open file
	 * @param file the file
	 */
	public OpenFile(OpenFile openFile, Path file) {
		this(openFile, CollectionUtils.asList(file));
	}

	/**
	 * Instantiates a new open file.
	 *
	 * @param openFile the open file
	 */
	public OpenFile(OpenFile openFile) {
		this(openFile, openFile.mFiles);
	}

	/**
	 * Instantiates a new open file.
	 *
	 * @param openFile the open file
	 * @param files the files
	 */
	public OpenFile(OpenFile openFile, Collection<Path> files) {
		mWindow = openFile.mWindow;
		mFiles = files;
		mHeaders = openFile.mHeaders;
		mRowAnnotations = openFile.mRowAnnotations;
		mSkipLines = openFile.mSkipLines;
		mOpenMode = openFile.mOpenMode;
		mFileType = openFile.mFileType;
	}

	public OpenFile(OpenFile openFile, Path file, MainMatCalcWindow window) {
		this(openFile, file);
		
		mWindow = window;
	}

	/**
	 * Open.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean open() throws IOException {
		boolean status = false;

		for (Path file : mFiles) {
			if (mWindow.mFilesModel.size() > 0 && mOpenMode == OpenMode.NEW_WINDOW) {
				MainMatCalcWindow window = new MainMatCalcWindow(mWindow.getAppInfo());
				window.setVisible(true);

				OpenFile of = new OpenFile(this, file, window);

				of.open();

				status |= false;
			} else {
				String ext = PathUtils.getFileExt(file);

				FileModule module = mWindow.getFileModule(ext);

				if (module != null) {
					DataFrame m = module.openFile(mWindow, 
							file,
							mFileType,
							mHeaders,
							mRowAnnotations,
							mDelimiter,
							mSkipLines);

					mWindow.openMatrix(file, m);

					RecentFilesService.getInstance().add(file);

					status |= true;
				} else {
					status |= false;
				}
			}
		}
		
		return status;
	}

	/**
	 * Auto open.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean autoOpen() throws IOException {
		boolean status = false;

		for (Path file : mFiles) {
			if (mWindow.mFilesModel.size() > 0 && mOpenMode == OpenMode.NEW_WINDOW) {
				MainMatCalcWindow window = new MainMatCalcWindow(mWindow.getAppInfo());
				window.setVisible(true);

				OpenFile of = new OpenFile(this, file);
				of.mWindow = new MainMatCalcWindow(mWindow.getAppInfo());

				of.autoOpen();

				status |= false;
			} else {
				String ext = PathUtils.getFileExt(file);

				FileModule module = mWindow.mOpenFileModuleMap.get(ext);

				if (module != null) {
					DataFrame m = module.autoOpenFile(mWindow, 
							file,
							mFileType,
							mHeaders,
							mRowAnnotations,
							mDelimiter,
							mSkipLines);

					mWindow.openMatrix(file, m);

					status |= true;
				} else {
					status |= false;
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
	public OpenFile openMode(OpenMode openMode) {
		OpenFile of = new OpenFile(this);
		of.mOpenMode = openMode;
		return of;
	}

	public OpenFile type(FileType type) {
		OpenFile of = new OpenFile(this);
		of.mFileType = type;
		return of;
	}

	/**
	 * Headers.
	 *
	 * @param headers the headers
	 * @return the open file
	 */
	public OpenFile headers(int headers) {
		OpenFile of = new OpenFile(this);
		of.mHeaders = headers;
		return of;
	}

	/**
	 * No header.
	 *
	 * @return the open file
	 */
	public OpenFile noHeader() {
		return headers(0);
	}

	/**
	 * Specify the number of row annotations.
	 *
	 * @param headers 	the number of row annotations.
	 * 
	 * @return 			
	 */
	public OpenFile rowAnnotations(int headers) {
		OpenFile of = new OpenFile(this);
		of.mRowAnnotations = headers;
		return of;
	}

	/**
	 * Specify the delimiter. The default is the tab character.
	 *
	 * @param delimiter the delimiter
	 * @return the open file
	 */
	public OpenFile delimiter(String delimiter) {
		OpenFile of = new OpenFile(this);
		of.mDelimiter = delimiter;
		return of;
	}

	public OpenFile skipLines(String skip) {
		return skipLines(CollectionUtils.asList(skip));
	}

	/**
	 * Specify lines that can be skipped at the beginning of the file
	 * e.g. for example # or %.
	 * 
	 * @param skipLines 	the characters that can be skipped.
	 * @return 				the open file
	 */
	public OpenFile skipLines(Collection<String> skipLines) {
		OpenFile of = new OpenFile(this);
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
	public static String guessDelimiter(Path file, 
			Collection<String> skipMatches) throws IOException {
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
				//System.err.println("test " + c + " " + (c == '\t'));

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
	 * Guess if file contains only numbers.
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
		if (BioPathUtils.ext().xlsx().test(file) || 
				BioPathUtils.ext().xls().test(file)) {
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
