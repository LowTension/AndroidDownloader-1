package com.httpionichina.androiddownloader.entity;

/**
 * 下载的文件的信息实体
 * Created by Martin on 2015/7/16.
 */
public class FileInfo extends BaseEntity {

    private int id;
    //文件的网络地址
    private String fileUrl;
    //文件的名称
    private String fileName;
    //文件的大小
    private int fileSize;
    //当前完成的进度
    private int finishedSize;

    public FileInfo() {
    }

    public FileInfo(int id, String fileUrl, String fileName, int fileSize, int finishedSize) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFinishedSize() {
        return finishedSize;
    }

    public void setFinishedSize(int finishedSize) {
        this.finishedSize = finishedSize;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", finishedSize=" + finishedSize +
                '}';
    }
}
