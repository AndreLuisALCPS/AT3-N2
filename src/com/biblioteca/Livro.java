package com.biblioteca;

public class Livro {
    private String autor;
    private String titulo;
    private String genero;
    private int exemplares;

    public Livro(String autor, String titulo, String genero, int exemplares) {
        this.autor = autor;
        this.titulo = titulo;
        this.genero = genero;
        this.exemplares = exemplares;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getExemplares() {
        return exemplares;
    }

    public void setExemplares(int exemplares) {
        this.exemplares = exemplares;
    }

    public String getNome() {
        return getTitulo();
    }

    public int getNumeroExemplares() {
        return getExemplares();
    }

    @Override
    public String toString() {
        return "Livro{" +
                "autor='" + autor + '\'' +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", exemplares=" + exemplares +
                '}';
    }
}
