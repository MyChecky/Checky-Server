package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Check;
import com.whu.checky.domain.Task;
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
        mapper.insertNewCheck(check);
        //mapper.insert(check);
    }

    @Override
    public void uploadFile() {

    }

    @Override
    public List<Check> queryCheck(String column, String checkId) {
        return mapper.selectList(new EntityWrapper<Check>().eq(column,checkId));
    }

    @Override
    public void getCheckHistory() {

    }

    @Override
    public void updateCheck(Check check) {
        mapper.updateById(check);
    }

    @Override
    public void deleteCheck(String checkId) {
        mapper.delete(new EntityWrapper<Check>().eq("check_id",checkId));
    }

    @Override
    public Check getCheckByTask(String taskId,String date) {
        List<Check> list = mapper.selectList(new EntityWrapper<Check>()
                .like("check_time",date)
                .eq("task_id",taskId)
        );
        if(list.size()==1) return list.get(0);
        else return null;
    }


}
