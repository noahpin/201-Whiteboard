package whiteboard201;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

@WebServlet("/saveWhiteboard")
public class SaveWhiteboardServlet extends HttpServlet {

	public static final long serialVersionUID = 1L;

	private static final Path SAVE_DIR = Paths.get("/opt/whiteboards"); // on directory work i referenced this:
																		// https://docs.oracle.com/javase/tutorial/essential/io/pathOps.html

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get ID input from parameter input then check to make sure it works
		String whiteboardIdInput = request.getParameter("id");

		if (whiteboardIdInput == null || whiteboardIdInput.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing whiteboard ID");
			return;
		}

		int whiteboardId;

		try {
			whiteboardId = Integer.parseInt(whiteboardIdInput);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid whiteboard ID");
			return;
		}

		//read raw JSON from request, use StringBuilder to get into a single string: https://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();

		try (BufferedReader reader = request.getReader()) {
			String line;
			while (true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				sb.append(line);
			}
		}

		// check if json is valid by trying to parse
		try {
			gson.fromJson(sb.toString(), JsonElement.class);
		} catch (JsonSyntaxException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
			return;
		}

		// write to file if all worked
		try {
			writeJsonFile(whiteboardId, sb.toString());
		} catch (IOException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to write file");
			return;
		}

		// note that everything worked
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter out = response.getWriter()) {
			gson.toJson(Map.of("message", "Whiteboard saved successfully!"), out);
		}

	}

	// helper function for writing the json file
	private void writeJsonFile(int id, String json) throws IOException {

		Files.createDirectories(SAVE_DIR);

		Path filePath = SAVE_DIR.resolve(id + ".json");

		
		//use buffered bc large 
		// also need to specify the charset since some of team has windows some has mac, just going to do to be safe
		// found here and clicked through ;
		// https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#newBufferedWriter-java.nio.file.Path-java.nio.charset.Charset-java.nio.file.OpenOption...-
		try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
			writer.write(json);
		}
	}
}
