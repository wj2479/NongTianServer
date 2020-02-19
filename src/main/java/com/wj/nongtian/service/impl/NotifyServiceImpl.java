package com.wj.nongtian.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.wj.nongtian.entity.Notify;
import com.wj.nongtian.entity.NotifyReceiver;
import com.wj.nongtian.mapper.NotifyMapper;
import com.wj.nongtian.service.NotifyService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class NotifyServiceImpl implements NotifyService {
    private Logger logger = Logger.getLogger(getClass());

    @Resource
    private NotifyMapper notifyMapper;

    @Override
    public List<Notify> getPublishNotify(int uid) {
        List<Notify> notifyList = null;
        try {
            notifyList = notifyMapper.getPublishNotifysByUserId(uid);

            if (notifyList != null && notifyList.size() > 0) {
                notifyList.forEach(notify -> {
                            // 获取每个通知的接受者ID
                            List<Integer> notifyReceivers = notifyMapper.getNotifyReceivers(notify.getId());
                            StringBuilder sb = new StringBuilder();
                            if (notifyReceivers != null) {
                                for (int i = 0; i < notifyReceivers.size(); i++) {
                                    if (i == notifyReceivers.size() - 1) {
                                        sb.append(notifyReceivers.get(i));
                                    } else {
                                        sb.append(notifyReceivers.get(i));
                                        sb.append(",");
                                    }
                                }
                            }
                            notify.setReceivers(sb.toString());
                        }
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取发布通知列表失败:" + e.toString());
        }

        return notifyList;
    }

    @Override
    public boolean publishNotify(Notify notify) {
        try {
            int count = notifyMapper.addNotify(notify);
            if (count == 1) {
                if (!StringUtils.isEmpty(notify.getReceivers())) {
                    String[] split = notify.getReceivers().split(",");
                    for (String s : split) {
                        try {
                            int uid = Integer.parseInt(s);
                            notifyMapper.addNotifyReceiver(notify.getId(), uid);
                        } catch (Exception e) {
                        }
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加新通知异常：" + e.toString());
        }
        return false;
    }

    @Override
    public List<NotifyReceiver> getReceivedNotify(int uid) {
        List<NotifyReceiver> notifyList = null;

        try {
            notifyList = notifyMapper.getReceiveNotifysByUserId(uid);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取接收到的通知异常：" + e.toString());
        }
        return notifyList;
    }

    @Override
    public boolean setNotifyRead(int uid, int nid) {
        try {
            int count = notifyMapper.setNotifyRead(uid, nid);
            if (count >= 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更改通知已读状态异常：" + e.toString());
        }
        return false;
    }
}
