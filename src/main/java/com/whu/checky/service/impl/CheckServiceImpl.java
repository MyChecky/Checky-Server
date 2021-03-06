package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.whu.checky.domain.Check;
import com.whu.checky.mapper.CheckMapper;
import com.whu.checky.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("CheckService")
public class CheckServiceImpl implements CheckService {
    @Autowired
    private CheckMapper mapper;

    @Override
    public void addCheck(Check check) {
//        mapper.insertNewCheck(check);
        mapper.insert(check);
    }

    @Override
    public void uploadFile() {

    }

    @Override
    public List<Check> queryCheck(String column, String checkId) {
        return mapper.selectList(new EntityWrapper<Check>().eq(column, checkId));
    }

    @Override
    public Check queryCheckById(String checkId) {
        return mapper.selectById(checkId);
    }

    @Override
    public List<Check> queryCheckByUserId(String userId, Page<Check> page) {
        return mapper.selectPage(page,
                new EntityWrapper<Check>()
                        .eq("user_id", userId)
                        .orderBy("check_time", false));
    }


    @Override
    public void getCheckHistory() {

    }

    @Override
    public Integer updateCheck(Check check) {
        return mapper.updateById(check);
    }

    @Override
    public void deleteCheck(String checkId) {
        mapper.delete(new EntityWrapper<Check>().eq("check_id", checkId));
    }

    @Override
    public Check getCheckByTask(String taskId, String date) {
        List<Check> list = mapper.selectList(new EntityWrapper<Check>()
                .like("check_time", date)
                .eq("task_id", taskId)
        );
        if (list.size() == 1) return list.get(0);
        else return null;
    }

    @Override
    public List<Check> getTaskChecks(String taskId) {
        List<Check> list = mapper.selectList(new EntityWrapper<Check>()
                .eq("task_id", taskId)
        );
        return list;
    }

    @Override
    public int[] queryUserCheckNumTimely(String userId) {
        List<Check> checks = mapper.selectList(new EntityWrapper<Check>()
                .eq("user_id", userId));
        int[] chartTime = {0, 0, 0, 0};
        for (Check check : checks) {
            int index = Integer.parseInt(check.getCheckTime().substring(5, 7)) - 1;
            chartTime[index / 3]++;
        }
        return chartTime;
    }

//    @Override
//    public void updatePassSuperviseCheck(String checkId) {
//        Check check = mapper.selectById(checkId);
//        check.setSuperviseNum(check.getSuperviseNum() + 1);
//        check.setPassNum(check.getPassNum() + 1);
//        mapper.updateById(check);
//    }

//    @Override
//    public void updateDenySuperviseCheck(String checkId) {
//        Check check = mapper.selectById(checkId);
//        check.setSuperviseNum(check.getSuperviseNum() + 1);
//        mapper.updateById(check);
//    }


}
