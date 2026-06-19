package by.shakhau.dictionary.logic.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileLoader {

	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	public static String readTextFile(String path) {
        return readTextFile(new File(path));
    }

	public static String readTextFile(File file) {
		if (!file.exists()) {
			return null;
		}

		String fileContent;
		try {
			fileContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return fileContent;
	}

	public static void copyBinaryFile(File sourceFile, File targetFile) {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(sourceFile);
			output = new FileOutputStream(targetFile);
			byte[] buff = new byte[BUFFER_SIZE];
			int read;
			while ((read = input.read(buff)) > 0) {
				output.write(buff, 0, read);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public static void copyBinaryFile(InputStream sourceInput, File targetFile) {
		OutputStream writer = null;
		try {
			writer = new FileOutputStream(targetFile);
			byte[] buff = new byte[BUFFER_SIZE];
			int read;
			while ((read = sourceInput.read(buff)) > 0) {
				writer.write(buff, 0, read);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public static void serialize(File file, Object object) {
		ObjectOutputStream writer = null;
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			writer = new ObjectOutputStream(os);
			writer.writeObject(object);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public static <T> T deserialize(File file) {
		if (!file.exists()) {
			return null;
		}

		ObjectInputStream reader = null;
		try {
			reader = new ObjectInputStream(new FileInputStream(file));
			return (T) reader.readObject();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public static String readInputStream(InputStream stream) {
		StringBuilder fileContent = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			String line = reader.readLine();
			while (line != null) {
				fileContent.append(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
		return fileContent.toString();
	}

	public static void writeFile(String path, String data) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		Exception ex = null;
		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(data);
		} catch (IOException e) {
			ex = e;
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
				if (ex != null) {
					throw ex;
				}
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public static void writeFile(File file, byte[] content) {
		OutputStream writer = null;
		try {
			writer = new FileOutputStream(file);
			writer.write(content);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
}