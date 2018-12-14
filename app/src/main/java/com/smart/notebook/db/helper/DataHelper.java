package com.smart.notebook.db.helper;

import com.smart.notebook.db.control.BaseDao;
import com.smart.notebook.db.entity.DataEntity;
import com.smart.notebook.db.entity.DataEntityDao;

import java.util.List;

public class DataHelper extends BaseDao<DataEntity> {
    private volatile static DataHelper instance;

    private DataHelper() {
        super();
    }

    public static DataHelper getInstance() {
        if (instance == null) {
            synchronized (DataHelper.class) {
                if (instance == null) {
                    instance = new DataHelper();
                }
            }
        }
        return instance;
    }

    public boolean insert(DataEntity message) {
        try {
            daoSession.insert(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(DataEntity message) {
        super.deleteObject(message);
    }

    public boolean update(DataEntity user) {
        try {
            daoSession.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DataEntity> queryList() {
        DataEntityDao messDao = daoSession.getDataEntityDao();
        return messDao.queryBuilder().orderAsc(DataEntityDao.Properties.Data_lasttime).list();
    }

    public DataEntity queryListById(long videoId) {
        DataEntityDao messDao = daoSession.getDataEntityDao();
        DataEntity mess = messDao.queryBuilder().where(DataEntityDao.Properties.Data_id.eq(videoId)).unique();
        if (mess == null) {
            return null;
        } else {
            return mess;
        }
    }
}
