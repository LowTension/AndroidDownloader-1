package com.httpionichina.androiddownloader.db.dao;

import com.httpionichina.androiddownloader.entity.ThreadInfo;

import java.util.List;

/**
 * 线程数据表的处理接口
 * Created by Martin on 2015/7/17.
 */
public interface ThreadDAO {
    /**
     * 插入线程数据
     *
     * @param threadInfo 线程对象
     */
    public void insertThread(ThreadInfo threadInfo);

    /**
     * 删除线程数据
     *
     * @param url       线程绑定的下载地址
     * @param thread_id 线程id
     */
    public void deleteThread(String url, int thread_id);

    /**
     * 更新线程数据,下载进度
     *
     * @param url       线程绑定的下载地址
     * @param thread_id 线程id
     * @param finished  县城下载的完成进度
     */
    public void updateThread(String url, int thread_id, int finished);

    /**
     * 查询文件对应的所有的线程信息
     *
     * @param url 目标文件地址
     * @return
     */
    public List<ThreadInfo> getThreads(String url);

    /**
     * 判断线程信息是否存在
     *
     * @param url       线程绑定的下载地址
     * @param thread_id 线程id
     * @return
     */
    public boolean isExist(String url, int thread_id);
}
