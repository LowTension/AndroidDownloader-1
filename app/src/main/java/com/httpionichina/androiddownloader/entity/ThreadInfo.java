package com.httpionichina.androiddownloader.entity;

/**
 * 断点续传的线程信息
 * Created by Martin on 2015/7/16.
 */
public class ThreadInfo extends BaseEntity {
    private int id;
    //文件的网络地址
    private String fileUrl;
    //线程开始的位置
    private int start;
    //线程结束的位置
    private int end;
    //线程当前所处的位置
    private int finishedSize;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String fileUrl, int start, int end, int finishedSize) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.start = start;
        this.end = end;
        this.finishedSize = finishedSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinishedSize() {
        return finishedSize;
    }

    public void setFinishedSize(int finishedSize) {
        this.finishedSize = finishedSize;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", fileUrl='" + fileUrl + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finishedSize=" + finishedSize +
                '}';
    }
}
