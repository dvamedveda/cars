package ru.job4j.cars.controller;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Вспомогательный класс, позволяющий подменить поток ввода сервлета.
 * https://stackoverflow.com/a/38855151/5939454
 */
public class MockServletInputStream extends ServletInputStream {

    private final InputStream delegate;

    public MockServletInputStream(InputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public int read() throws IOException {
        return delegate.read();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }
}