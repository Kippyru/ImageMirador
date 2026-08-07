package org.dsf.imagemirador.Dto;

//agregue un enum, asi el dto soporta otras media y no estamos pasando un string como uga uga
public record MediaItem(String path, String name, MediaType type) {
    public enum MediaType { IMAGE, VIDEO, AUDIO } //el enum deberia ser un archivo aparte peroo.... anidar anidar
}