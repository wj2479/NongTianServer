package com.wj.nongtian.service;


import com.wj.nongtian.entity.Notify;

import java.util.List;

public interface NotifyService {

    List<Notify> getPublishNotify(int uid);

    boolean publishNotify(Notify notify);

    List<Notify> getReceivedNotify(int uid);

    boolean setNotifyRead(int uid, int nid);

}
