package wyxmttk.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSystemResource implements Resource {
    private String path;
    private File file;

    public FileSystemResource(String path) {
        this.path = path;
        this.file = new File(path);
    }
    public FileSystemResource(String path, File file) {
        this.path = path;
        this.file = file;
    }
    public FileSystemResource(File file) {
        this.path = file.getPath();
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
    public String getPath() {
        return path;
    }
}
