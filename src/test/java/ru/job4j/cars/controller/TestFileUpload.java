package ru.job4j.cars.controller;

/**
 * Вспомогательный класс, представляющий загружаемый в сервлет файл.
 * https://stackoverflow.com/a/38855151/5939454
 */
public class TestFileUpload {
    private final String filename;
    private final String mimeType;
    private final byte[] contents;

    public TestFileUpload(String filename, String mimeType, byte[] contents) {
        this.filename = filename;
        this.mimeType = mimeType;
        this.contents = contents;
    }

    public String getFilename() {
        return filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public byte[] getContents() {
        return contents;
    }
}