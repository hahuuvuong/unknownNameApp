package com.example.sach.model;

import java.util.ArrayList;

public class chapter {
    private ArrayList<String> tenChapter;
    private String ChapterName;
    private String ChapterContext;
    private int idChapter;
    public chapter(String chapterName, String chapterContext) {
        ChapterName = chapterName;
        ChapterContext = chapterContext;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getChapterContext() {
        return ChapterContext;
    }

    public void setChapterContext(String chapterContext) {
        ChapterContext = chapterContext;
    }

    public chapter() {
    }

    public ArrayList<String> getTenChapter() {
        return tenChapter;
    }

    public void setTenChapter(ArrayList tenChapter) {
        this.tenChapter = tenChapter;
    }

    public chapter(String chapterName, String chapterContext, int idChapter) {
        ChapterName = chapterName;
        ChapterContext = chapterContext;
        this.idChapter = idChapter;
    }

    public int getIdChapter() {
        return idChapter;
    }

    public void setIdChapter(int idChapter) {
        this.idChapter = idChapter;
    }
}
