package org.dsf.imagemirador.Viewer;

import org.dsf.imagemirador.Dto.MediaItem;
import java.util.List;

public class NavigationViewer {

    private List<MediaItem> playlist;
    private int currentIndex = 0;

    public void load(List<MediaItem> items, int startingIndex) {
        this.playlist = items;
        this.currentIndex = startingIndex;
    }

    public MediaItem next() {
        if (isEmpty()) return null;
        currentIndex++;
        if (currentIndex >= playlist.size()) currentIndex = 0;
        return playlist.get(currentIndex);
    }

    public MediaItem previous() {
        if (isEmpty()) return null;
        currentIndex--;
        if (currentIndex < 0) currentIndex = playlist.size() - 1;
        return playlist.get(currentIndex);
    }

    public MediaItem getCurrent() {
        if (isEmpty()) return null;
        return playlist.get(currentIndex);
    }

    // Méetodo necesario para saltar a una imagen específica
    public void setIndex(int index) {
        if (playlist != null && index >= 0 && index < playlist.size()) {
            this.currentIndex = index;
        }
    }

    public void clear() {
        if (playlist != null) playlist.clear();
        currentIndex = 0;
    }

    public boolean isEmpty() {
        return playlist == null || playlist.isEmpty();
    }

    public int getIndex() {
        return currentIndex;
    }

    public int getTotal() {
        return playlist == null ? 0 : playlist.size();
    }
}