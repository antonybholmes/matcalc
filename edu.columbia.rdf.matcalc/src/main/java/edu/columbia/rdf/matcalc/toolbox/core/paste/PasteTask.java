package edu.columbia.rdf.matcalc.toolbox.core.paste;

import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.collections.CountMap;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Io;
import org.jebtk.core.io.Temp;
import org.jebtk.core.stream.Stream;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.status.StatusService;
import org.xml.sax.SAXException;

import edu.columbia.rdf.matcalc.MainMatCalcWindow;

/**
 * Overlap segments.
 * 
 * @author Antony Holmes Holmes
 */
public class PasteTask extends SwingWorker<Void, Void> {

	private List<Path> mFiles;

	private MainMatCalcWindow mWindow;

	private boolean mIndexMode;

	private String mDelimiter;

	public PasteTask(MainMatCalcWindow window, 
			List<Path> files,
			String delimiter,
			boolean indexMode) {
		mWindow = window;
		mFiles = files;
		mDelimiter = delimiter;
		mIndexMode = indexMode;
	}

	@Override
	public Void doInBackground() {
		try {
			paste();
		} catch (InvalidFormatException | ClassNotFoundException | InstantiationException | IllegalAccessException
				| IOException | SAXException | ParserConfigurationException | ParseException | FontFormatException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		return null;
	}


	private void paste() throws IOException, InvalidFormatException, ClassNotFoundException, InstantiationException, IllegalAccessException, SAXException, ParserConfigurationException, ParseException, FontFormatException, UnsupportedLookAndFeelException {
		// First get all the regions to search into one sorted map

		Path pwd = RecentFilesService.getInstance().getPwd();

		Path file = pasteByIndex();

		mWindow.openFile(file);

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

		CountMap<String> keyMap = new CountMap<String>();
		List<String> keyList = new ArrayList<String>();

		int c = 0;


		for (int i = 0; i < mFiles.size(); ++i) {
			reader = FileUtils.newBufferedReader(mFiles.get(i));

			try {
				while ((line = reader.readLine()) != null) {
					key = TextUtils.firstSplit(line, TextUtils.TAB_DELIMITER);

					if (!keyMap.containsKey(key)) {
						// Base the key list on the first file
						if (i == 0) {
							keyList.add(key);
						}
					}

					keyMap.inc(key);

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

		for (int i = keyList.size() - 1; i >= 0; --i) {
			if (keyMap.get(keyList.get(i)) < minSize) {
				keyList.set(i, TextUtils.EMPTY_STRING);
				--totalKeys;
			}

			if (i % 100000 == 0) {
				PasteModule.LOG.info("{} keys to go...", i);
			}
		}

		PasteModule.LOG.info("Total keys {}", totalKeys);

		keyMap.clear();

		int index = 0;

		for (int i = 0; i < keyList.size(); ++i) {
			key = keyList.get(i);

			if (!TextUtils.isNullOrEmpty(key)) {
				keyMap.put(key, index++);
			}
		}

		// free the memory
		keyList.clear();
		

		Path tmp1 = Temp.generateTempFile("txt");
		Path tmp2 = Temp.generateTempFile("txt");


		// write out the first file largely intact

		writer = FileUtils.newBufferedWriter(tmp1);
		reader = FileUtils.newBufferedReader(mFiles.get(0));

		try {
			while ((line = reader.readLine()) != null) {
				if (Io.isEmptyLine(line)) {
					continue;
				}

				tokens = TextUtils.tabSplit(line);

				key = tokens.get(0);

				if (keyMap.containsKey(key)) {
					writer.write(line);
					writer.newLine();
				}
			}
		} finally {
			writer.close();
			reader.close();
		}

		// Stores the entries for a column
		Map<Integer, String> columnMap = new HashMap<Integer, String>();

		for (int i = 1; i < mFiles.size(); ++i) {
			// load the file into memory
			// order the probes then append to the
			// temp file
			// rewrite the temp file with the new
			// data then repeat for each file

			reader = FileUtils.newBufferedReader(mFiles.get(i));

			try {
				while ((line = reader.readLine()) != null) {
					if (Io.isEmptyLine(line)) {
						continue;
					}

					tokens = TextUtils.tabSplit(line);

					key = tokens.get(0);

					if (keyMap.containsKey(key)) {
						if (mIndexMode) {
							line = Stream.stream(tokens)
									.skip(1)
									.mapToString()
									.tabJoin();
						}


						// remove the first column probe id on subsequent files
						columnMap.put(keyMap.get(key), line);
					}
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

					writer.write(line);
					writer.write(mDelimiter);
					writer.write(columnMap.get(c));
					writer.newLine();

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
