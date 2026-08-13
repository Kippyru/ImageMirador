package org.dsf.imagemirador.Dto;

public record MediaItem(String path, String name, MediaType type) {
    public enum MediaType { IMAGE, VIDEO, AUDIO } //el enum deberia ser un archivo aparte peroo.... anidar anidar
}