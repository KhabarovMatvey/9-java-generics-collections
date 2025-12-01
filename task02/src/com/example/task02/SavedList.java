package com.example.task02;

import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {
    private final File file;
    private final List<E> list;

    @SuppressWarnings("unchecked")
    public SavedList(File file) {
        this.file = file;
        this.list = new ArrayList<>();

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                list.addAll((List<E>) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("Failed to load list from file", e);
            }
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(list));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save list to file", e);
        }
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        E oldElement = list.set(index, element);
        saveToFile();
        return oldElement;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
        saveToFile();
    }

    @Override
    public E remove(int index) {
        E removedElement = list.remove(index);
        saveToFile();
        return removedElement;
    }

    @Override
    public boolean add(E element) {
        boolean result = list.add(element);
        saveToFile();
        return result;
    }

    @Override
    public void clear() {
        list.clear();
        saveToFile();
    }
}