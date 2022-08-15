package python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class PythonHandler {

    public String runPythonScript(String path) throws IOException {
        String command = "python3 " + path;
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader output = getOutput(p);
        return readAllLines(output);
    }
    private BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private String readAllLines(BufferedReader reader) {
        return reader.lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
